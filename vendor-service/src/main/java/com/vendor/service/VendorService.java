package com.vendor.service;

import com.vendor.dto.ApiResponse;
import com.vendor.dto.ReceiptDTO;
import com.vendor.dto.VendorDetails;
import com.vendor.entity.ReceiptHeader;
import com.vendor.entity.ReceiptLine;
import com.vendor.kafka.KafkaProducer;
import com.vendor.repository.ReceiptLineRepo;
import com.vendor.repository.ReciptHeaderRepo;
import org.apache.coyote.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendorService {

    private static final Logger logger = LoggerFactory.getLogger(VendorService.class);

    private final KafkaProducer kafkaProducer;
    private final ReceiptLineRepo receiptLineRepo;
    private final ReciptHeaderRepo reciptHeaderRepo;

    public VendorService(KafkaProducer kafkaProducer, ReceiptLineRepo receiptLineRepo, ReciptHeaderRepo reciptHeaderRepo) {
        this.receiptLineRepo = receiptLineRepo;
        this.kafkaProducer = kafkaProducer;
        this.reciptHeaderRepo = reciptHeaderRepo;
    }

    public ApiResponse<List<VendorDetails>> getAllVendors() {
        try {
            List<VendorDetails> vendors = new ArrayList<>();
            vendors.add(new VendorDetails("VEND_001", "Supplier A", "supplierA@example.com", null, "123 Vendor St",
                    "PAN001", "TAN001", "2025-01-01", "supplier_admin", null,
                    "2026-05-01T08:00:00", "system", "2026-05-10T08:00:00", "system"));
            return ApiResponse.success(vendors, "Vendors retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving vendors", e);
            return ApiResponse.internalError("Error retrieving vendors");
        }
    }

    public ApiResponse<VendorDetails> getVendorById(String id) {
        try {
            if (id == null || id.isEmpty()) {
                return ApiResponse.badRequest("Vendor id is required");
            }
            VendorDetails vendor = new VendorDetails(id, "Supplier A", "supplierA@example.com", null, "123 Vendor St",
                    "PAN001", "TAN001", "2025-01-01", "supplier_admin", null,
                    "2026-05-01T08:00:00", "system", "2026-05-10T08:00:00", "system");
            return ApiResponse.success(vendor, "Vendor retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving vendor", e);
            return ApiResponse.internalError("Error retrieving vendor");
        }
    }

    public ApiResponse<VendorDetails> signup(VendorDetails vendorDetails) {
        try {
            if (vendorDetails.getCompanyName() == null || vendorDetails.getCompanyName().isEmpty()) {
                return ApiResponse.badRequest("Company name is required");
            }
            vendorDetails.setId("VEND_" + System.currentTimeMillis());
            kafkaProducer.send("vendor-signedup", vendorDetails);
            return ApiResponse.created(vendorDetails, "Vendor signup successful");
        } catch (Exception e) {
            logger.error("Error signing up vendor", e);
            return ApiResponse.internalError("Error signing up vendor");
        }
    }

    public ApiResponse<VendorDetails> updateVendor(String id, VendorDetails vendorDetails) {
        try {
            if (id == null || id.isEmpty()) {
            	System.out.println("Hello hyd");
                return ApiResponse.badRequest("Vendor id is required");
            }
            vendorDetails.setId(id);
            return ApiResponse.success(vendorDetails, "Vendor updated successfully");
        } catch (Exception e) {
            logger.error("Error updating vendor", e);
            return ApiResponse.internalError("Error updating vendor");
        }
    }


    public ApiResponse<ReceiptDTO> createReceiptbyDelivery(ReceiptHeader header) {

        try {
            // Validate input
            if (header == null || header.getDeliveryNoteId() == null) {
                return ApiResponse.badRequest("Delivery note ID is required");
            }


            // Create receipt header
            ReceiptHeader receiptHeader = new ReceiptHeader();
            receiptHeader.setReceiptNumber("REC-" + LocalDateTime.now().getYear()+ "-" +(System.currentTimeMillis()%10000));
            receiptHeader.setReceiptDate(LocalDate.now());
            receiptHeader.setDeliveryNoteId(header.getDeliveryNoteId());
            receiptHeader.setStatus("CREATED");
            receiptHeader.setCreatedBy("system");
            receiptHeader.setCreatedDate(LocalDateTime.now());

            // Store warehouse
            String warehouseInfo = String.format("Warehouse: %s - %s",
                    header.getWarehouseCode(),
                    header.getWarehouseName());
            receiptHeader.setRemarks(warehouseInfo);

            // Save receipt header
            ReceiptHeader savedReceiptHeader = reciptHeaderRepo.save(receiptHeader);
            logger.info("Receipt header saved with ID: {}", savedReceiptHeader.getId());

            //  receipt lines
            List<ReceiptLine> savedLines = new ArrayList<>();
            for (ReceiptLine lineRequest : header.getReceiptLines()) {
                ReceiptLine receiptLine = new ReceiptLine();
                receiptLine.setReceiptHeader(savedReceiptHeader);
                receiptLine.setDeliveryNoteLineId(lineRequest.getDeliveryNoteLineId());
                receiptLine.setReceivedQuantity(lineRequest.getReceivedQuantity());
                receiptLine.setAcceptedQuantity(lineRequest.getAcceptedQuantity());
                receiptLine.setRejectedQuantity(lineRequest.getRejectedQuantity());
                receiptLine.setRejectionReason(lineRequest.getRejectionReason());
                receiptLine.setStatus("PROCESSED");

                ReceiptLine savedLine = receiptLineRepo.save(receiptLine);
                savedLines.add(savedLine);
            }

            // response DTO
            ReceiptDTO receiptDTO = new ReceiptDTO(
                    savedReceiptHeader.getId(),
                    savedReceiptHeader.getReceiptNumber(),
                    savedReceiptHeader.getStatus()
            );

            return ApiResponse.created(receiptDTO, "Receipt created successfully");

        } catch (Exception e) {
            logger.error("Error creating receipt request", e);
            return ApiResponse.internalError("Error creating receipt request: " + e.getMessage());
        }
    }

}
