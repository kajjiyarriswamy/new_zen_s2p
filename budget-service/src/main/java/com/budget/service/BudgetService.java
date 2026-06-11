package com.budget.service;

import com.budget.async.BudgetAsyncService;
import com.budget.dto.ApiResponse;
import com.budget.dto.BudgetHeader;
import com.budget.entity.BudgetHeaderEntity;
import com.budget.exception.InvalidRequestException;
import com.budget.exception.ResourceNotFoundException;
import com.budget.kafka.KafkaProducer;
import com.budget.repository.BudgetHeaderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private static final Logger logger = LoggerFactory.getLogger(BudgetService.class);

    private final KafkaProducer kafkaProducer;
    private final BudgetHeaderRepository budgetHeaderRepository;
    private final BudgetAsyncService budgetAsyncService;

    public BudgetService(KafkaProducer kafkaProducer, BudgetHeaderRepository budgetHeaderRepository,
                         BudgetAsyncService budgetAsyncService) {
        this.kafkaProducer = kafkaProducer;
        this.budgetHeaderRepository = budgetHeaderRepository;
        this.budgetAsyncService = budgetAsyncService;
    }

    public ApiResponse<List<BudgetHeader>> getAllBudgets() {
        List<BudgetHeaderEntity> entities = budgetHeaderRepository.findAll();
        List<BudgetHeader> dtos = new ArrayList<>();
        for (BudgetHeaderEntity e : entities) {
            dtos.add(toDto(e));
        }
        return ApiResponse.success(dtos, "Budgets retrieved successfully");
    }

    public ApiResponse<BudgetHeader> getBudgetById(String budgetId) {
        if (budgetId == null || budgetId.isEmpty()) {
            throw new InvalidRequestException("Budget id is required");
        }
        BudgetHeaderEntity entity = budgetHeaderRepository.findByBudgetId(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        return ApiResponse.success(toDto(entity), "Budget retrieved successfully");
    }

    public ApiResponse<BudgetHeader> createBudget(BudgetHeader budgetHeader) {
        if (budgetHeader == null || budgetHeader.getBudgetId() == null || budgetHeader.getBudgetId().isEmpty()) {
            throw new InvalidRequestException("Budget id is required");
        }
        if (budgetHeaderRepository.findByBudgetId(budgetHeader.getBudgetId()).isPresent()) {
            throw new InvalidRequestException("Budget id already exists");
        }
        BudgetHeaderEntity entity = toEntity(budgetHeader);
        entity.setStatus("SUBMITTED");
        entity.setCreatedTime(LocalDateTime.now());
        budgetHeaderRepository.save(entity);
        BudgetHeader dto = toDto(entity);
        budgetAsyncService.publishBudgetCreatedEvent(dto);
        return ApiResponse.created(dto, "Budget created successfully");
    }

    public ApiResponse<BudgetHeader> updateBudgetStatus(String budgetId, String status) {
        if (budgetId == null || budgetId.isEmpty()) {
            throw new InvalidRequestException("Budget id is required");
        }
        if (status == null || status.isEmpty()) {
            throw new InvalidRequestException("Status is required");
        }
        BudgetHeaderEntity entity = budgetHeaderRepository.findByBudgetId(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        entity.setStatus(status);
        entity.setLastUpdatedTime(LocalDateTime.now());
        budgetHeaderRepository.save(entity);
        return ApiResponse.success(toDto(entity), "Budget status updated successfully");
    }

    public ApiResponse<BudgetHeader> updateBudget(String budgetId, BudgetHeader budgetHeader) {
        if (budgetId == null || budgetId.isEmpty()) {
            throw new InvalidRequestException("Budget id is required");
        }
        if (budgetHeader == null) {
            throw new InvalidRequestException("Budget details are required");
        }
        BudgetHeaderEntity entity = budgetHeaderRepository.findByBudgetId(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        entity.setBudgetName(budgetHeader.getBudgetDescription());
        entity.setBudgetDescription(budgetHeader.getBudgetDescription());
        entity.setApproverList(budgetHeader.getApproverList());
        entity.setAvailableAmount(budgetHeader.getAvailableAmount());
        entity.setConsumedAmount(budgetHeader.getConsumedAmount());
        entity.setTotalAmount(budgetHeader.getTotalAmount());
        entity.setLastUpdatedTime(LocalDateTime.now());
        budgetHeaderRepository.save(entity);
        return ApiResponse.success(toDto(entity), "Budget updated successfully");
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
        e.setAvailableAmount(d.getAvailableAmount());
        e.setConsumedAmount(d.getConsumedAmount());
        e.setTotalAmount(d.getTotalAmount());
        e.setCreatedBy(d.getCreatedBy());
        e.setLastUpdatedBy(d.getLastUpdatedBy());
        return e;
    }
    

    public void updateBudgetAmount(PrData event) {
        try {

            Optional<BudgetHeaderEntity> opt =
                    budgetHeaderRepository.findByBudgetId(event.getBudgetId());

            if (opt.isEmpty()) {
                logger.error("Budget not availab"
                		+ "le for budgetId: {}", event.getBudgetId());
                return;
            }

            BudgetHeaderEntity entity = opt.get();

            Double amount = event.getBudget();

            if (entity.getAvailableAmount() < amount) {
                logger.error("Insufficient budget for budgetId: {}", event.getBudgetId());
                return;
            }
            Double available = entity.getAvailableAmount() != null ? entity.getAvailableAmount() : 0.0;
            Double reserved = entity.getReservedAmount() != null ? entity.getReservedAmount() : 0.0;
            Double amountVal = amount != null ? amount : 0.0;

            entity.setAvailableAmount(available - amountVal);
            entity.setReservedAmount(reserved + amountVal);
            entity.setLastUpdatedTime(LocalDateTime.now());

            budgetHeaderRepository.save(entity);

            logger.info("Budget updated successfully for budgetId: {}", event.getBudgetId());

        } catch (Exception e) {
            logger.error("Error updating budget status", e);
        }
    }
    
    
    private void getTestMethod() {
    	System.out.println("Test");
    	System.out.println("Test");
    }
}