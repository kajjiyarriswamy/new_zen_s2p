package com.budget.controller;

import com.budget.async.BudgetAsyncService;
import com.budget.dto.ApiResponse;
import com.budget.dto.BudgetHeader;
import com.budget.entity.BudgetHeaderEntity;
import com.budget.repository.BudgetHeaderRepository;
import com.budget.service.BudgetService;
import com.budget.storage.S3StorageService;
import org.springframework.beans.factory.ObjectProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    private static final Logger logger = LoggerFactory.getLogger(BudgetController.class);

    private final BudgetHeaderRepository budgetHeaderRepository;
    private final S3StorageService s3StorageService;
    private final BudgetService budgetService;
    private final BudgetAsyncService budgetAsyncService;

    public BudgetController(BudgetHeaderRepository budgetHeaderRepository,
                            ObjectProvider<S3StorageService> s3StorageServiceProvider, BudgetService budgetService,
                            BudgetAsyncService budgetAsyncService) {
        this.budgetHeaderRepository = budgetHeaderRepository;
        this.s3StorageService = s3StorageServiceProvider.getIfAvailable();
        this.budgetService = budgetService;
        this.budgetAsyncService = budgetAsyncService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BudgetHeader>>> getAllBudgets() {
        logger.info("Fetching all budgets");
        ApiResponse<List<BudgetHeader>> response = budgetService.getAllBudgets();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<ApiResponse<BudgetHeader>> getBudgetById(@PathVariable String budgetId) {
        logger.info("Fetching budget with id {}", budgetId);
        ApiResponse<BudgetHeader> response = budgetService.getBudgetById(budgetId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BudgetHeader>> createBudget(@RequestBody BudgetHeader budgetHeader) {
        logger.info("Creating budget {}", budgetHeader.getBudgetId());
        ApiResponse<BudgetHeader> res=budgetService.createBudget(budgetHeader);
        return ResponseEntity.status(res.getStatusCode()).body(res); 
    }

    @PostMapping("/{budgetId}/upload-document")
    public ResponseEntity<ApiResponse<BudgetHeader>> uploadBudgetDocument(@PathVariable String budgetId,
                                                                          @RequestParam("file") MultipartFile file) {
        logger.info("Uploading document for budget {}", budgetId);
        if (budgetId == null || budgetId.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest("Budget id is required"));
        }
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest("Document file is required"));
        }

        Optional<BudgetHeaderEntity> opt = budgetHeaderRepository.findByBudgetId(budgetId);
        if (!opt.isPresent()) {
            return ResponseEntity.status(404).body(ApiResponse.notFound("Budget not found"));
        }

        try {
            if (s3StorageService == null) {
                return ResponseEntity.status(503).body(ApiResponse.internalError("S3 storage is not configured"));
            }
            String objectKey = String.format("budget/%s/%s_%s", budgetId, UUID.randomUUID(), file.getOriginalFilename());
            String documentPath = s3StorageService.uploadFile(file, objectKey);
            BudgetHeaderEntity entity = opt.get();
            entity.setDocumentPath(documentPath);
            entity.setLastUpdatedTime(LocalDateTime.now());
            budgetHeaderRepository.save(entity);
            budgetAsyncService.auditBudgetUpload(budgetId, documentPath);
            return ResponseEntity.ok(ApiResponse.success(toDto(entity), "Budget document uploaded successfully"));
        } catch (Exception e) {
            logger.error("Failed to upload budget document", e);
            return ResponseEntity.status(500).body(ApiResponse.internalError("Failed to upload budget document: " + e.getMessage()));
        }
    }

    @PutMapping("/{budgetId}/status")
    public ResponseEntity<ApiResponse<BudgetHeader>> updateBudgetStatus(@PathVariable String budgetId,
                                                                        @RequestParam String status) {
        logger.info("Updating budget {} status to {}", budgetId, status);
        ApiResponse<BudgetHeader> response = budgetService.updateBudgetStatus(budgetId, status);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<ApiResponse<BudgetHeader>> updateBudget(@PathVariable String budgetId,
                                                                   @RequestBody BudgetHeader budgetHeader) {
        logger.info("Updating budget {}", budgetId);
        ApiResponse<BudgetHeader> response = budgetService.updateBudget(budgetId, budgetHeader);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    private BudgetHeader toDto(BudgetHeaderEntity e) {
        BudgetHeader d = new BudgetHeader();
        d.setBudgetId(e.getBudgetId());
        d.setBudgetDescription(e.getBudgetDescription());
        d.setRequestor(e.getRequestor());
        d.setOrgId(e.getOrgName());
        d.setApproverList(e.getApproverList());
        d.setStatus(e.getStatus());
        d.setAvailableAmount(e.getAvailableAmount() == null ? 0.0 : e.getAvailableAmount());
        d.setConsumedAmount(e.getConsumedAmount() == null ? 0.0 : e.getConsumedAmount());
        d.setTotalAmount(e.getTotalAmount() == null ? 0.0 : e.getTotalAmount());
        d.setDocumentPath(e.getDocumentPath());
        d.setCreatedTime(e.getCreatedTime() == null ? null : e.getCreatedTime().toString());
        d.setCreatedBy(e.getCreatedBy());
        d.setLastUpdatedTime(e.getLastUpdatedTime() == null ? null : e.getLastUpdatedTime().toString());
        d.setLastUpdatedBy(e.getLastUpdatedBy());
        d.setBudgetName(e.getBudgetName());
        return d;
    }
   
    private BudgetHeaderEntity toEntity(BudgetHeader d) {
        BudgetHeaderEntity e = new BudgetHeaderEntity();
        e.setBudgetId(d.getBudgetId());
        e.setBudgetName(null);
        e.setBudgetDescription(d.getBudgetDescription());
        e.setRequestor(d.getRequestor());
        e.setOrgName(d.getOrgId());
        e.setApproverList(d.getApproverList());
        e.setStatus(d.getStatus());
        e.setAvailableAmount(d.getTotalAmount());
        e.setConsumedAmount(0.0);
        e.setReservedAmount(0.0);
        e.setTotalAmount(d.getTotalAmount());
        e.setDocumentPath(d.getDocumentPath());
        e.setCreatedBy(d.getCreatedBy());
        e.setLastUpdatedBy(d.getLastUpdatedBy());
        return e;
    }
}
