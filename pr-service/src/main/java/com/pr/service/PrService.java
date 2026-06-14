package com.pr.service;

import com.pr.dto.*;

import com.pr.entity.PrHeader;
import com.pr.entity.PrLine;
import com.pr.exception.InvalidPrIdException;
import com.pr.exception.PrNotFoundException;
import com.pr.exception.PrValidationException;
import com.pr.kafka.KafkaProducer;
import com.pr.repository.PrHeaderRepository;
import com.pr.repository.PrLineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrService {

    private static final Logger logger = LoggerFactory.getLogger(PrService.class);

    private final KafkaProducer kafkaProducer;
    private final PrHeaderRepository prHeaderRepository;
    private final PrLineRepository prLineRepository;
    private final BudgetClientService service;
    private final PoClientService poService;
    private final PrAsyncService prAsyncService;

    public PrService(KafkaProducer kafkaProducer,
                     PrHeaderRepository prHeaderRepository,
                     PrLineRepository prLineRepository, BudgetClientService service, PoClientService poService, PrAsyncService prAsyncService) {;
    	
        this.kafkaProducer = kafkaProducer;
        this.prHeaderRepository = prHeaderRepository;
        this.prLineRepository = prLineRepository;
        this.service= service;
        this.poService = poService;
        this.prAsyncService = prAsyncService;
    }

    public ApiResponse<List<PrData>> getAllPurchaseRequests() {
        try {
            List<PrHeader> headers = prHeaderRepository.findAll();
            List<PrData> prList = new ArrayList<>();
            for (PrHeader h : headers) {
                PrData d = new PrData();
                d.setId(h.getId() == null ? null : String.valueOf(h.getId()));
                d.setPrNumber(h.getPrNumber());
                d.setDepartment(h.getOrgId());
                d.setRequester(h.getRequestor());
                d.setBudget(h.getAmount() == null ? 0.0 : h.getAmount());
                d.setStatus(h.getStatus());
                d.setDescription(h.getPrDescription());
                prList.add(d);
            }
            return ApiResponse.success(prList, "Purchase requests retrieved successfully");
        } catch (Exception e) {
            logger.error("Error fetching purchase requests", e);
            return ApiResponse.internalError("Error fetching purchase requests");
        }
    }

    public ApiResponse<PrData> getPurchaseRequestByPrNumber(String prNumber) {
        try {
            if (prNumber == null || prNumber.isEmpty()) {
                throw new PrValidationException(
                        "Purchase Request PR number is required");
            }
            Optional<PrHeader> opt = Optional.of(prHeaderRepository.findByPrNumber(prNumber).orElseThrow(() -> new PrNotFoundException("Purchase Request not found")));;
            if (!opt.isPresent()) return ApiResponse.notFound("Purchase Request not found");
            PrHeader h = opt.get();
            PrData prData = new PrData();
            prData.setId(h.getId() == null ? null : String.valueOf(h.getId()));
            prData.setPrNumber(h.getPrNumber());
            prData.setDepartment(h.getOrgId());
            prData.setRequester(h.getRequestor());
            prData.setBudget(h.getAmount() == null ? 0.0 : h.getAmount());
            prData.setStatus(h.getStatus());
            prData.setDescription(h.getPrDescription());
            BudgetDTO budgetDto=service.getBudget(h.getBudgetId());
            PoDTO poDto= poService.getPoByPrNumber(h.getPrNumber());
            prData.setBudgetId(budgetDto.getBudgetId());

            logger.info(budgetDto.getBudgetName());
            logger.info(budgetDto.getBudgetId());
            logger.info(budgetDto.getBudgetDescription());
            logger.info("PO DTO : {}", poDto);

            prData.setPoDescription(poDto.getPoDescription()!=null?poDto.getPoDescription():"Po service is un available");
            prData.setBudgetName(budgetDto.getBudgetName()!=null?budgetDto.getBudgetName():"Budget service is un available");
            prData.setBudgetId(budgetDto.getBudgetId());
            return ApiResponse.success(prData, "Purchase request retrieved successfully");
        } catch (Exception e) {
            logger.error("Error fetching purchase request", e);
            return ApiResponse.internalError("Error fetching purchase request");
        }
    }

    public ApiResponse<PrData> createPurchaseRequest(PrData prData) {
        try {
            if (prData.getPrNumber() == null || prData.getPrNumber().isEmpty()){
                throw new PrValidationException(
                        "PR Number is required");
            }
            PrHeader header = new PrHeader();
            header.setPrNumber(prData.getPrNumber());
            header.setPrDescription(prData.getDescription());
            header.setRequestor(prData.getRequester());
            header.setOrgId(prData.getDepartment());
            header.setAmount(prData.getBudget());
            header.setStatus("SUBMITTED");
            header.setCreatedTime(LocalDateTime.now());
            header.setBudgetId(prData.getBudgetId());
            header = prHeaderRepository.save(header);

            prData.setId(header.getId() == null ? null : String.valueOf(header.getId()));
            prData.setStatus(header.getStatus());
            prAsyncService.publishPrCreatedEvent(prData);

            return ApiResponse.created(prData, "Purchase request created successfully");
        } catch (Exception e) {
            logger.error("Error creating purchase request", e);
            return ApiResponse.internalError("Error creating purchase request");
        }
    }

    public ApiResponse<List<PrLineData>> getPrLines(String prNumber) {
        try {
            Optional<PrHeader> opt = Optional.of(prHeaderRepository.findByPrNumber(prNumber).orElseThrow(() -> new PrNotFoundException("PR not found")));
            PrHeader header = opt.get();
            List<PrLine> lines = prLineRepository.findByPrHeader(header);
            List<PrLineData> dto = new ArrayList<>();
            for (PrLine l : lines) {
                PrLineData d = new PrLineData();
                d.setId(l.getId() == null ? null : String.valueOf(l.getId()));
                d.setPrLineId(l.getPrLineId());
                d.setItem(l.getItem());
                d.setUom(l.getUom());
                d.setQty(l.getQty() == null ? 0 : l.getQty());
                d.setUnitCost(l.getUnitCost() == null ? 0.0 : l.getUnitCost());
                d.setTaxPercentage(l.getTaxPercentage() == null ? 0.0 : l.getTaxPercentage());
                d.setTotalCost(l.getTotalCost() == null ? 0.0 : l.getTotalCost());
                d.setCategory(l.getCategory());
                d.setPrNumber(header.getPrNumber());
                dto.add(d);
            }
            return ApiResponse.success(dto, "PR line items retrieved successfully");
        } catch (Exception e) {
            logger.error("Error fetching PR lines", e);
            return ApiResponse.internalError("Error fetching PR lines");
            
        }
    }

    public ApiResponse<PrLineData> addPrLine(String prNumber, PrLineData lineData) {
        try {
            if (lineData.getItem() == null || lineData.getItem().isEmpty()) {
                throw new PrValidationException(
                        "Item name is required");
            };
            Optional<PrHeader> opt = Optional.of(prHeaderRepository.findByPrNumber(prNumber).orElseThrow(() -> new PrNotFoundException("PR not found")));
            PrHeader header = opt.get();
            PrLine line = new PrLine();
            line.setPrLineId(lineData.getPrLineId());
            line.setItem(lineData.getItem());
            line.setUom(lineData.getUom());
            line.setQty(lineData.getQty());
            line.setUnitCost(lineData.getUnitCost());
            line.setTaxPercentage(lineData.getTaxPercentage());
            line.setTotalCost(lineData.getTotalCost());
            line.setCategory(lineData.getCategory());
            line.setPrHeader(header);
            line = prLineRepository.save(line);

            Double headerTotal = header.getTotalAmount() == null ? 0.0 : header.getTotalAmount();
            headerTotal += line.getTotalCost() == null ? 0.0 : line.getTotalCost();
            header.setTotalAmount(headerTotal);
            prHeaderRepository.save(header);

            lineData.setId(line.getId() == null ? null : String.valueOf(line.getId()));
            lineData.setPrNumber(prNumber);
            return ApiResponse.created(lineData, "PR line created successfully");
        } catch (Exception e) {
            logger.error("Error adding PR line", e);
            return ApiResponse.internalError("Error creating PR line");
        }
    }

    public ApiResponse<PrData> updatePrStatus(String id, String status) {
        try {
            if (id == null || id.isEmpty()) {
                throw new PrValidationException(
                        "Purchase Request ID is required");
            }
            Long lid;
            try { lid = Long.valueOf(id); } catch (NumberFormatException nfe) { throw new InvalidPrIdException(id); }
            Optional<PrHeader> opt = Optional.of(prHeaderRepository.findById(lid).orElseThrow(() ->
                                                new PrNotFoundException("Purchase Request not found")));

            PrHeader header = opt.get();
            header.setStatus(status);
            header.setLastUpdatedTime(LocalDateTime.now());
            prHeaderRepository.save(header);

            PrData prData = new PrData();
            prData.setId(header.getId() == null ? null : String.valueOf(header.getId()));
            prData.setPrNumber(header.getPrNumber());
            prData.setDepartment(header.getOrgId());
            prData.setRequester(header.getRequestor());
            prData.setBudget(header.getAmount() == null ? 0.0 : header.getAmount());
            prData.setStatus(header.getStatus());
            prData.setDescription(header.getPrDescription());
            return ApiResponse.success(prData, "Purchase request status updated successfully");
        } catch (Exception e) {
            logger.error("Error updating purchase request status", e);
            return ApiResponse.internalError("Error updating purchase request status");
        }
    }

    public ApiResponse<PrData> approvePurchaseRequest(String id) {
        try {
            if (id == null || id.isEmpty()) {
                throw new PrValidationException(
                        "Purchase Request ID is required");
            }
            Long lid;
            try { lid = Long.valueOf(id); } catch (NumberFormatException nfe) { throw new InvalidPrIdException(id); }
            Optional<PrHeader> opt = Optional.of(prHeaderRepository.findById(lid).orElseThrow(() ->
                                                 new PrNotFoundException("Purchase Request not found")));
            PrHeader header = opt.get();
            header.setStatus("APPROVED");
            header.setLastUpdatedTime(LocalDateTime.now());
            prHeaderRepository.save(header);

            PrData prData = new PrData();
            prData.setId(header.getId() == null ? null : String.valueOf(header.getId()));
            prData.setPrNumber(header.getPrNumber());
            prData.setDepartment(header.getOrgId());
            prData.setRequester(header.getRequestor());
            prData.setBudget(header.getAmount() == null ? 0.0 : header.getAmount());
            prData.setStatus(header.getStatus());
            prData.setDescription(header.getPrDescription());
            prData.setBudgetId(header.getBudgetId()); 
            kafkaProducer.send("pr-approved", prData);
            
            return ApiResponse.success(prData, "Purchase request approved successfully");
        } catch (Exception e) {
            logger.error("Error approving purchase request", e);
            return ApiResponse.internalError("Error approving purchase request");
        }
    }

    public ApiResponse<PrData> decidePurchaseRequest(String prNumber, String action, String approver) {
        try {
            if (prNumber == null || prNumber.isEmpty()) {
                throw new PrValidationException(
                        "PR number is required");
            }
            Optional<PrHeader> opt = Optional.of(prHeaderRepository.findByPrNumber(prNumber).orElseThrow(() ->
                                             new PrNotFoundException("Purchase Request not found")));
            PrHeader header = opt.get();
            if ("APPROVED".equalsIgnoreCase(header.getStatus())) {

                throw new PrValidationException(
                        "Approved PR cannot be modified");
            }


            String act = action == null ? "" : action.trim().toLowerCase();
            switch (act) {
                case "approve":
                case "approved":
                case "accept":
                    header.setStatus("APPROVED");
                    break;
                case "reject":
                case "rejected":
                    header.setStatus("REJECTED");
                    break;
                default:
                    header.setStatus("MORE_INFO_REQUIRED");
            }

            header.setLastUpdatedTime(LocalDateTime.now());
            if (approver != null && !approver.isEmpty()) header.setLastUpdatedBy(approver);
            prHeaderRepository.save(header);

            PrData prData = new PrData();
            prData.setId(header.getId() == null ? null : String.valueOf(header.getId()));
            prData.setPrNumber(header.getPrNumber());
            prData.setDepartment(header.getOrgId());
            prData.setRequester(header.getRequestor());
            prData.setBudget(header.getAmount() == null ? 0.0 : header.getAmount());
            prData.setStatus(header.getStatus());
            prData.setDescription(header.getPrDescription());

            kafkaProducer.send("pr-decision", prData);
            return ApiResponse.success(prData, "Decision applied");
        } catch (Exception e) {
            logger.error("Error applying decision to PR", e);
            return ApiResponse.internalError("Error applying decision to PR");
        }
    }


	public ApiResponse<PrData> rejectPurchaseRequest(String id) {
        try {
        	logger.info("Reject Request ID = [{}]", id);
            if (id == null || id.isEmpty()) return ApiResponse.badRequest("Purchase Request ID is required");
            Long lid;
            try { lid = Long.valueOf(id); } catch (NumberFormatException nfe) { return ApiResponse.badRequest("Invalid Purchase Request ID"); }
            Optional<PrHeader> opt = prHeaderRepository.findById(lid);
            if (!opt.isPresent()) return ApiResponse.notFound("Purchase Request not found");
            PrHeader header = opt.get();
            header.setStatus("REJECTED");
            header.setLastUpdatedTime(LocalDateTime.now());
            prHeaderRepository.save(header);

            PrData prData = new PrData();
            prData.setId(header.getId() == null ? null : String.valueOf(header.getId()));
            prData.setPrNumber(header.getPrNumber());
            prData.setDepartment(header.getOrgId());
            prData.setRequester(header.getRequestor());
            prData.setBudget(header.getAmount() == null ? 0.0 : header.getAmount());
            prData.setStatus(header.getStatus());
            prData.setDescription(header.getPrDescription());
            prData.setBudgetId(header.getBudgetId()); 
            kafkaProducer.send("pr-rejected", prData);
            
            return ApiResponse.success(prData, "Purchase request Rejected successfully");
        } catch (Exception e) {
            logger.error("Error approving purchase request", e);
            return ApiResponse.internalError("Error approving purchase request");
        }
    }

}
