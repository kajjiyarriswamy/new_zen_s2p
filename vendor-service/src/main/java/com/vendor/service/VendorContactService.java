package com.vendor.service;

import com.vendor.dto.ApiResponse;
import com.vendor.dto.VendorContact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VendorContactService {

    private static final Logger logger = LoggerFactory.getLogger(VendorContactService.class);

    public ApiResponse<List<VendorContact>> getContactsByVendor(String venId) {
        try {
            List<VendorContact> contacts = new ArrayList<>();
            contacts.add(new VendorContact("CONT_001", "Ravi Kumar", "Sales Manager", "+1234567890",
                    "ravi@example.com", venId, "2026-05-01T08:00:00", "system", "2026-05-10T08:00:00", "system"));
            return ApiResponse.success(contacts, "Vendor contacts retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving vendor contacts", e);
            return ApiResponse.internalError("Error retrieving vendor contacts");
        }
    }

    public ApiResponse<VendorContact> createContact(String venId, VendorContact contact) {
        try {
            if (contact.getName() == null || contact.getName().isEmpty()) {
                return ApiResponse.badRequest("Contact name is required");
            }
            contact.setId("CONT_" + System.currentTimeMillis());
            contact.setVenId(venId);
            return ApiResponse.created(contact, "Vendor contact created successfully");
        } catch (Exception e) {
            logger.error("Error creating vendor contact", e);
            return ApiResponse.internalError("Error creating vendor contact");
        }
    }
