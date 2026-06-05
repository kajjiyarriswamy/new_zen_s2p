package com.po.service;

import java.time.LocalDateTime;

import org.apache.el.stream.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.po.dto.ApiResponse;
import com.po.dto.PoData;
import com.po.entity.PoHeader;
import com.po.kafka.KafkaProducer;
import com.po.repository.PoHeaderRepository;
import com.po.repository.PoLineRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class PoService {

    private static final Logger logger = LoggerFactory.getLogger(PoService.class);

    private final KafkaProducer kafkaProducer;
    private final PoHeaderRepository poHeaderRepository;
    private final PoLineRepository poLineRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public PoService(KafkaProducer kafkaProducer,
                     PoHeaderRepository poHeaderRepository,
                     PoLineRepository poLineRepository) {
        this.kafkaProducer = kafkaProducer;
        this.poHeaderRepository = poHeaderRepository;
        this.poLineRepository = poLineRepository;
    }

    @CircuitBreaker(name = "prService", fallbackMethod = "createPurchaseOrderFromPrFallback")
    public ApiResponse<PoData> createPurchaseOrderFromPr(String prNumber, PoData poData) {
        logger.info("Service: Creating PO from PR {}", prNumber);

        if (prNumber == null || prNumber.isEmpty()) {
            return ApiResponse.badRequest("PR number is required");
        }
        if (poData.getPoNumber() == null || poData.getPoNumber().isEmpty()) {
            return ApiResponse.badRequest("PO number is required");
        }

        try {
            String prServiceUrl = "http://localhost:8081/pr/" + prNumber;
            ApiResponse prResp = restTemplate.getForObject(prServiceUrl, ApiResponse.class);
            if (prResp == null || prResp.getStatusCode() >= 400) {
                logger.warn("Failed to fetch PR from pr-service, status: {}", prResp == null ? "null" : prResp.getStatusCode());
                return new ApiResponse<>(HttpStatus.BAD_GATEWAY.value(), "Failed to fetch PR from pr-service", null);
            }

            PoHeader header = new PoHeader();
            header.setPoNumber(poData.getPoNumber());
            header.setPoDescription(poData.getPoDescription());
            header.setPrNumber(prNumber);
            header.setStatus("PENDING");
            header.setCreatedTime(LocalDateTime.now());
            header = poHeaderRepository.save(header);

            // attempt to fetch PR lines (non-blocking mapping)
            try {
                String prLinesUrl = "http://localhost:8081/pr/" + prNumber + "/lines";
                ApiResponse prLinesResp = restTemplate.getForObject(prLinesUrl, ApiResponse.class);
                // we don't auto-create PO lines here; client may post lines after creation
                if (prLinesResp != null && prLinesResp.getStatusCode() == 200) {
                    logger.debug("Fetched PR lines for {}", prNumber);
                }
            } catch (Exception ex) {
                logger.debug("Unable to fetch PR lines (non-fatal): {}", ex.toString());
            }

            poData.setId(header.getId() == null ? null : header.getId());
            poData.setStatus(header.getStatus());
            kafkaProducer.send("po-created", poData);

            return ApiResponse.created(poData, "Purchase order created from PR successfully");

        } catch (Exception e) {
            logger.error("Error creating PO from PR in service", e);
            return ApiResponse.internalError("Error creating PO from PR");
        }
    }

    // Fallback returns PR service unavailable (no PO creation)
    public ApiResponse<PoData> createPurchaseOrderFromPrFallback(String prNumber, PoData poData, Throwable t) {
        logger.warn("Service fallback: pr-service unavailable for PR {}: {}", prNumber, t == null ? "" : t.toString());
        return new ApiResponse<>(HttpStatus.SERVICE_UNAVAILABLE.value(), "PR service unavailable", null);
    }

	public ResponseEntity<ApiResponse<PoData>> getPoByPrNumber(String prNumber) {
    	logger.info("Fetching Purchase Order with PO number: {}", prNumber);

    	try {

    	    if (prNumber == null || prNumber.trim().isEmpty()) {
    	        logger.warn("Purchase Order PO number is empty");

    	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    	                .body(ApiResponse.badRequest("Purchase Order PO number is required"));
    	    }

    	    java.util.Optional<PoHeader> opt = poHeaderRepository.findByPrNumber(prNumber);

    	    if (!opt.isPresent()) {
    	        logger.warn("Purchase Order not found with PR number: {}", prNumber);

    	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
    	                .body(ApiResponse.notFound("Purchase Order not found"));
    	    }

    	    PoHeader h = opt.get();

    	    PoData p = new PoData();

    	    p.setId(h.getId());
    	    p.setPoNumber(h.getPoNumber());
    	    p.setPoDescription(h.getPoDescription());
    	    p.setRequestor(h.getRequestor());
    	    p.setOwnerBuyer(h.getOwnerBuyer());
    	    p.setVendorId(h.getVendorId());
    	    p.setAmount(h.getAmount());
    	    p.setTaxAmount(h.getTaxAmount());
    	    p.setTotalAmount(h.getTotalAmount());
    	    p.setApproverList(h.getApproverList());
    	    p.setStatus(h.getStatus());
    	    p.setCurrency(h.getCurrency());
    	    p.setPrNumber(h.getPrNumber());
    	    p.setOrgId(h.getOrgId());
    	    p.setBudgetId(h.getBudgetId());
    	    p.setPaymentTermId(h.getPaymentTermId());

    	    logger.info("Successfully fetched purchase order: {}", prNumber);

    	    return ResponseEntity.ok(
    	            ApiResponse.success(p, "Purchase order retrieved successfully"));

    	} catch (Exception e) {

    	    logger.error("Error fetching purchase order", e);

    	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    	            .body(ApiResponse.internalError("Error fetching purchase order"));
    	}
    }
}
