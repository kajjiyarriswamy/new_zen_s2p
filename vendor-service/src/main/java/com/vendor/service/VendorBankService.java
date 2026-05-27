package com.vendor.service;

import com.vendor.dto.ApiResponse;
import com.vendor.dto.VendorBank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VendorBankService {

    private static final Logger logger = LoggerFactory.getLogger(VendorBankService.class);

    public ApiResponse<List<VendorBank>> getBanksByVendor(String venId) {
        try {
            List<VendorBank> banks = new ArrayList<>();
            banks.add(new VendorBank("BANK_001", "HDFC Bank", "HDFC0001234", "123456789012", "Mumbai",
                    "/files/bank-proof.pdf", "Supplier A Pvt Ltd", venId, "2026-05-01T08:00:00", "system",
                    "2026-05-10T08:00:00", "system"));
            return ApiResponse.success(banks, "Vendor banks retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving vendor banks", e);
            return ApiResponse.internalError("Error retrieving vendor banks");
        }
    }

    public ApiResponse<VendorBank> createBank(String venId, VendorBank bank) {
        try {
            if (bank.getBankName() == null || bank.getBankName().isEmpty()) {
                return ApiResponse.badRequest("Bank name is required");
            }
            bank.setId("BANK_" + System.currentTimeMillis());
            bank.setVenId(venId);
            return ApiResponse.created(bank, "Vendor bank details created successfully");
        } catch (Exception e) {
            logger.error("Error creating vendor bank", e);
            return ApiResponse.internalError("Error creating vendor bank");
        }
    }
