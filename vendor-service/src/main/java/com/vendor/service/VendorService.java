package com.vendor.service;

import com.vendor.dto.ApiResponse;
import com.vendor.dto.VendorDetails;
import com.vendor.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VendorService {

    private static final Logger logger = LoggerFactory.getLogger(VendorService.class);

    private final KafkaProducer kafkaProducer;

    public VendorService(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
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
                return ApiResponse.badRequest("Vendor id is required");
            }
            vendorDetails.setId(id);
            return ApiResponse.success(vendorDetails, "Vendor updated successfully");
        } catch (Exception e) {
            logger.error("Error updating vendor", e);
            return ApiResponse.internalError("Error updating vendor");
        }
    }
