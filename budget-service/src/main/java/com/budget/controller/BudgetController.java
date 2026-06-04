package com.budget.controller;

import com.budget.dto.ApiResponse;
import com.budget.dto.BudgetHeader;
import com.budget.entity.BudgetHeaderEntity;
import com.budget.kafka.KafkaProducer;
import com.budget.repository.BudgetHeaderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    private static final Logger logger = LoggerFactory.getLogger(BudgetController.class);

    private final KafkaProducer kafkaProducer;
    private final BudgetHeaderRepository budgetHeaderRepository;

    public BudgetController(KafkaProducer kafkaProducer, BudgetHeaderRepository budgetHeaderRepository) {
        this.kafkaProducer = kafkaProducer;
        this.budgetHeaderRepository = budgetHeaderRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BudgetHeader>>> getAllBudgets() {
        logger.info("Fetching all budgets");
        List<BudgetHeaderEntity> entities = budgetHeaderRepository.findAll();
        List<BudgetHeader> dtos = new ArrayList<>();
        for (BudgetHeaderEntity e : entities) {
            BudgetHeader d = toDto(e);
            dtos.add(d);
        }
        return ResponseEntity.ok(ApiResponse.success(dtos, "Budgets retrieved successfully"));
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<ApiResponse<BudgetHeader>> getBudgetById(@PathVariable String budgetId) {
        logger.info("Fetching budget with id {}", budgetId);
        if (budgetId == null || budgetId.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest("Budget id is required"));
        }

        Optional<BudgetHeaderEntity> opt = budgetHeaderRepository.findByBudgetId(budgetId);
        if (!opt.isPresent()) {
            return ResponseEntity.status(404).body(ApiResponse.notFound("Budget not found"));
        }

        return ResponseEntity.ok(ApiResponse.success(toDto(opt.get()), "Budget retrieved successfully"));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BudgetHeader>> createBudget(@RequestBody BudgetHeader budgetHeader) {
        logger.info("Creating budget {}", budgetHeader.getBudgetId());
        if (budgetHeader.getBudgetId() == null || budgetHeader.getBudgetId().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest("Budget id is required"));
        }

        BudgetHeaderEntity e = toEntity(budgetHeader);
        e.setStatus("ACTIVE");
        e.setCreatedTime(LocalDateTime.now());
        budgetHeaderRepository.save(e);

        BudgetHeader dto = toDto(e);
        kafkaProducer.send("budget-created", dto);
        return ResponseEntity.status(201).body(ApiResponse.created(dto, "Budget created successfully"));
    }

    @PutMapping("/{budgetId}/status")
    public ResponseEntity<ApiResponse<BudgetHeader>> updateBudgetStatus(@PathVariable String budgetId,
                                                                        @RequestParam String status) {
        logger.info("Updating budget {} status to {}", budgetId, status);
        if (budgetId == null || budgetId.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest("Budget id is required"));
        }

        Optional<BudgetHeaderEntity> opt = budgetHeaderRepository.findByBudgetId(budgetId);
        if (!opt.isPresent()) {
            return ResponseEntity.status(404).body(ApiResponse.notFound("Budget not found"));
        }
        BudgetHeaderEntity e = opt.get();
        e.setStatus(status);
        e.setLastUpdatedTime(LocalDateTime.now());
        budgetHeaderRepository.save(e);

        return ResponseEntity.ok(ApiResponse.success(toDto(e), "Budget status updated successfully"));
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<ApiResponse<BudgetHeader>> updateBudget(@PathVariable String budgetId,
                                                                   @RequestBody BudgetHeader budgetHeader) {
        logger.info("Updating budget {}", budgetId);
        if (budgetId == null || budgetId.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest("Budget id is required"));
        }

        Optional<BudgetHeaderEntity> opt = budgetHeaderRepository.findByBudgetId(budgetId);
        if (!opt.isPresent()) {
            return ResponseEntity.status(404).body(ApiResponse.notFound("Budget not found"));
        }
        BudgetHeaderEntity e = opt.get();
        e.setBudgetName(budgetHeader.getBudgetDescription());
        e.setBudgetDescription(budgetHeader.getBudgetDescription());
        e.setApproverList(budgetHeader.getApproverList());
        e.setAvailableAmount(budgetHeader.getAvailableAmount());
        e.setConsumedAmount(budgetHeader.getConsumedAmount());
        e.setTotalAmount(budgetHeader.getTotalAmount());
        e.setLastUpdatedTime(LocalDateTime.now());
        budgetHeaderRepository.save(e);

        return ResponseEntity.ok(ApiResponse.success(toDto(e), "Budget updated successfully"));
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
        e.setCreatedBy(d.getCreatedBy());
        e.setLastUpdatedBy(d.getLastUpdatedBy());
        return e;
    }
}
