package com.admin.service;

import com.admin.dto.ApiResponse;
import com.admin.dto.PaymentTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentTermService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentTermService.class);

    public ApiResponse<List<PaymentTerm>> getAllPaymentTerms() {
        try {
            List<PaymentTerm> terms = new ArrayList<>();
            terms.add(new PaymentTerm("TERM_30", "30 days", "Payment due after thirty days", "2026-01-01T08:00:00", "admin",
                    "2026-01-01T08:00:00", "admin"));
            terms.add(new PaymentTerm("TERM_60", "60 days", "Payment due after sixty days", "2026-01-01T08:00:00", "admin",
                    "2026-01-01T08:00:00", "admin"));
            return ApiResponse.success(terms, "Payment terms retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving payment terms", e);
            return ApiResponse.internalError("Error retrieving payment terms");
        }
    }

    public ApiResponse<PaymentTerm> getPaymentTermById(String id) {
        try {
            if (id == null || id.isEmpty()) {
                return ApiResponse.badRequest("Payment term id is required");
            }
            PaymentTerm term = new PaymentTerm(id, "30 days", "Payment due after thirty days", "2026-01-01T08:00:00", "admin",
                    "2026-01-01T08:00:00", "admin");
            return ApiResponse.success(term, "Payment term retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving payment term", e);
            return ApiResponse.internalError("Error retrieving payment term");
        }
    }

    public ApiResponse<PaymentTerm> createPaymentTerm(PaymentTerm term) {
        try {
            if (term.getPaymentTerm() == null || term.getPaymentTerm().isEmpty()) {
                return ApiResponse.badRequest("Payment term is required");
            }
            term.setId("TERM_" + System.currentTimeMillis());
            return ApiResponse.created(term, "Payment term created successfully");
        } catch (Exception e) {
            logger.error("Error creating payment term", e);
            return ApiResponse.internalError("Error creating payment term");
        }
    }

    public ApiResponse<PaymentTerm> updatePaymentTerm(String id, PaymentTerm term) {
        try {
            if (id == null || id.isEmpty()) {
                return ApiResponse.badRequest("Payment term id is required");
            }
            term.setId(id);
            return ApiResponse.success(term, "Payment term updated successfully");
        } catch (Exception e) {
            logger.error("Error updating payment term", e);
            return ApiResponse.internalError("Error updating payment term");
        }
    }
}