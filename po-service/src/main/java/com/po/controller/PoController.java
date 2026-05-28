package com.po.controller;

import com.po.dto.ApiResponse;
import com.po.dto.PoData;
import com.po.dto.PoLineData;
import com.po.entity.PoHeader;
import com.po.entity.PoLine;
import com.po.kafka.KafkaProducer;
import com.po.repository.PoHeaderRepository;
import com.po.repository.PoLineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/po")
public class PoController {

    private static final Logger logger = LoggerFactory.getLogger(PoController.class);

    private final KafkaProducer kafkaProducer;
    private final PoHeaderRepository poHeaderRepository;
    private final PoLineRepository poLineRepository;
    private final com.po.service.PoService poService;

    public PoController(KafkaProducer kafkaProducer,
                        PoHeaderRepository poHeaderRepository,
                        PoLineRepository poLineRepository,
                        com.po.service.PoService poService) {
        this.kafkaProducer = kafkaProducer;
        this.poHeaderRepository = poHeaderRepository;
        this.poLineRepository = poLineRepository;
        this.poService = poService;
    }

    // Get all Purchase Orders
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PoData>>> getAllPurchaseOrders() {
        logger.info("Fetching all Purchase Orders");

        try {
            List<PoHeader> headers = poHeaderRepository.findAll();
            List<PoData> poList = new ArrayList<>();
            for (PoHeader h : headers) {
                PoData p = new PoData();
                p.setId(h.getId() == null ? null : String.valueOf(h.getId()));
                p.setPoNumber(h.getPoNumber());
                p.setVendor(h.getVendorId());
                p.setAmount(h.getAmount() == null ? 0.0 : h.getAmount());
                p.setStatus(h.getStatus());
                p.setDescription(h.getPoDescription());
                poList.add(p);
            }

            logger.info("Successfully fetched {} purchase orders", poList.size());
            return ResponseEntity.ok(ApiResponse.success(poList, "Purchase orders retrieved successfully"));

        } catch (Exception e) {
            logger.error("Error fetching purchase orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error fetching purchase orders"));
        }
    }

    // Get Purchase Order by PO number
    @GetMapping("/{poNumber}")
    public ResponseEntity<ApiResponse<PoData>> getPurchaseOrderByPoNumber(@PathVariable String poNumber) {
        logger.info("Fetching Purchase Order with PO number: {}", poNumber);

        try {
            if (poNumber == null || poNumber.isEmpty()) {
                logger.warn("Purchase Order PO number is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("Purchase Order PO number is required"));
            }

            Optional<PoHeader> opt = poHeaderRepository.findByPoNumber(poNumber);
            if (!opt.isPresent()) {
                logger.warn("Purchase Order not found with PO number: {}", poNumber);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound("Purchase Order not found"));
            }

            PoHeader h = opt.get();
            PoData p = new PoData();
            p.setId(h.getId() == null ? null : String.valueOf(h.getId()));
            p.setPoNumber(h.getPoNumber());
            p.setVendor(h.getVendorId());
            p.setAmount(h.getAmount() == null ? 0.0 : h.getAmount());
            p.setStatus(h.getStatus());
            p.setDescription(h.getPoDescription());

            logger.info("Successfully fetched purchase order: {}", poNumber);
            return ResponseEntity.ok(ApiResponse.success(p, "Purchase order retrieved successfully"));

        } catch (Exception e) {
            logger.error("Error fetching purchase order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error fetching purchase order"));
        }
    }

    // Create Purchase Order
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PoData>> createPurchaseOrder(@RequestBody PoData poData) {
        logger.info("Creating new Purchase Order: {}", poData.getPoNumber());

        try {
            if (poData.getPoNumber() == null || poData.getPoNumber().isEmpty()) {
                logger.warn("PO Number is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("PO Number is required"));
            }

            PoHeader header = new PoHeader();
            header.setPoNumber(poData.getPoNumber());
            header.setPoDescription(poData.getDescription());
            header.setVendorId(poData.getVendor());
            header.setAmount(poData.getAmount());
            header.setStatus("PENDING");
            header.setCreatedTime(LocalDateTime.now());
            header = poHeaderRepository.save(header);

            poData.setId(header.getId() == null ? null : String.valueOf(header.getId()));
            poData.setStatus(header.getStatus());

            kafkaProducer.send("po-created", poData);

            logger.info("Purchase Order created successfully with ID: {}", poData.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created(poData, "Purchase order created successfully"));

        } catch (Exception e) {
            logger.error("Error creating purchase order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error creating purchase order"));
        }
    }

    @PostMapping("/create-from-pr/{prNumber}")
    public ResponseEntity<ApiResponse<PoData>> createPurchaseOrderFromPr(@PathVariable String prNumber,
                                                                        @RequestBody PoData poData) {
        ApiResponse<PoData> resp = poService.createPurchaseOrderFromPr(prNumber, poData);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    // Fallback for circuit breaker when PR service is unavailable
    public ResponseEntity<ApiResponse<PoData>> createPurchaseOrderFromPrFallback(String prNumber, PoData poData, Throwable t) {
        logger.warn("pr-service unavailable or failing, returning Service Unavailable for PR {}: {}", prNumber, t == null ? "" : t.toString());

        // Do not create PO when PR details cannot be fetched. Inform caller that PR service is unavailable.
        ApiResponse<PoData> resp = new ApiResponse<>(503, "PR service unavailable", null);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(resp);
    }

    // Get PO line details
    @GetMapping("/{poNumber}/lines")
    public ResponseEntity<ApiResponse<List<PoLineData>>> getPoLines(@PathVariable String poNumber) {
        logger.info("Fetching PO lines for {}", poNumber);
        Optional<PoHeader> opt = poHeaderRepository.findByPoNumber(poNumber);
        if (!opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("PO not found"));
        }
        PoHeader header = opt.get();
        List<PoLine> lines = poLineRepository.findByPoHeader(header);
        List<PoLineData> dto = new ArrayList<>();
        for (PoLine l : lines) {
            PoLineData d = new PoLineData();
            d.setId(l.getId() == null ? null : String.valueOf(l.getId()));
            d.setPoLineId(l.getPoLineId());
            d.setItem(l.getItem());
            d.setUom(l.getUom());
            d.setQty(l.getQty() == null ? 0 : l.getQty());
            d.setUnitCost(l.getUnitCost() == null ? 0.0 : l.getUnitCost());
            d.setTaxPercentage(l.getTaxPercentage() == null ? 0.0 : l.getTaxPercentage());
            d.setTotalCost(l.getTotalCost() == null ? 0.0 : l.getTotalCost());
            d.setCategory(l.getCategory());
            d.setPoNumber(header.getPoNumber());
            d.setReceivedQty(l.getReceivedQty() == null ? 0 : l.getReceivedQty());
            dto.add(d);
        }
        return ResponseEntity.ok(ApiResponse.success(dto, "PO line items retrieved successfully"));
    }

    @PostMapping("/{poNumber}/lines")
    public ResponseEntity<ApiResponse<PoLineData>> addPoLine(@PathVariable String poNumber,
                                                             @RequestBody PoLineData lineData) {
        logger.info("Adding PO line for {}", poNumber);
        if (lineData.getItem() == null || lineData.getItem().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.badRequest("Item name is required"));
        }
        Optional<PoHeader> opt = poHeaderRepository.findByPoNumber(poNumber);
        if (!opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("PO not found"));
        }
        PoHeader header = opt.get();
        PoLine line = new PoLine();
        line.setPoLineId(lineData.getPoLineId());
        line.setItem(lineData.getItem());
        line.setUom(lineData.getUom());
        line.setQty(lineData.getQty());
        line.setUnitCost(lineData.getUnitCost());
        line.setTaxPercentage(lineData.getTaxPercentage());
        line.setTotalCost(lineData.getTotalCost());
        line.setCategory(lineData.getCategory());
        line.setReceivedQty(lineData.getReceivedQty());
        line.setCreatedTime(LocalDateTime.now());
        line.setCreatedBy(lineData.getCreatedBy());
        line.setPoHeader(header);

        line = poLineRepository.save(line);

        // update header totals
        Double headerTotal = header.getTotalAmount() == null ? 0.0 : header.getTotalAmount();
        headerTotal += line.getTotalCost() == null ? 0.0 : line.getTotalCost();
        header.setTotalAmount(headerTotal);
        poHeaderRepository.save(header);

        lineData.setId(line.getId() == null ? null : String.valueOf(line.getId()));
        lineData.setPoNumber(poNumber);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(lineData, "PO line created successfully"));
    }

    // Update Purchase Order Status
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PoData>> updatePoStatus(
            @PathVariable String id,
            @RequestParam String status) {
        logger.info("Updating Purchase Order status for ID: {} to {}", id, status);

        try {
            if (id == null || id.isEmpty()) {
                logger.warn("Purchase Order ID is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("Purchase Order ID is required"));
            }

            Optional<PoHeader> opt = poHeaderRepository.findById(Long.valueOf(id));
            if (!opt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound("PO not found"));
            }
            PoHeader header = opt.get();
            header.setStatus(status);
            poHeaderRepository.save(header);

            PoData poData = new PoData();
            poData.setId(String.valueOf(header.getId()));
            poData.setPoNumber(header.getPoNumber());
            poData.setStatus(header.getStatus());

            logger.info("Purchase Order status updated successfully for ID: {}", id);
            return ResponseEntity.ok(ApiResponse.success(poData, "Purchase order status updated successfully"));

        } catch (Exception e) {
            logger.error("Error updating purchase order status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error updating purchase order status"));
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        logger.info("PO Service health check");
        return ResponseEntity.ok(ApiResponse.success("PO Service is running", "Health check successful"));
    }
    
    @GetMapping("/healths")
    public ResponseEntity<ApiResponse<String>> healths() {
        logger.info("PO Service health check");
        return ResponseEntity.ok(ApiResponse.success("PO Service is running", "Health check successful"));
    }
}

