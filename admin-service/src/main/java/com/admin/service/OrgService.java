package com.admin.service;

import com.admin.dto.ApiResponse;
import com.admin.dto.OrgDetails;
import com.admin.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrgService {

    private static final Logger logger = LoggerFactory.getLogger(OrgService.class);

    private final KafkaProducer kafkaProducer;

    public OrgService(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public ApiResponse<List<OrgDetails>> getAllOrgs() {
        try {
            List<OrgDetails> orgs = new ArrayList<>();
            orgs.add(new OrgDetails("ORG_001", "Acme Corporation", "ACME", "2026-01-01T08:00:00", "admin",
                    "2026-01-02T09:00:00", "admin"));
            orgs.add(new OrgDetails("ORG_002", "Contoso Ltd", "CONT", "2026-02-01T08:00:00", "admin",
                    "2026-02-02T09:00:00", "admin"));
            return ApiResponse.success(orgs, "Organizations retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving organizations", e);
            return ApiResponse.internalError("Error retrieving organizations");
        }
    }

    public ApiResponse<OrgDetails> getOrgById(String id) {
        try {
            if (id == null || id.isEmpty()) {
                return ApiResponse.badRequest("Organization id is required");
            }
            OrgDetails org = new OrgDetails(id, "Acme Corporation", "ACME", "2026-01-01T08:00:00", "admin",
                    "2026-01-02T09:00:00", "admin");
            return ApiResponse.success(org, "Organization retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving organization", e);
            return ApiResponse.internalError("Error retrieving organization");
        }
    }

    public ApiResponse<OrgDetails> createOrg(OrgDetails orgDetails) {
        try {
            if (orgDetails.getOrgName() == null || orgDetails.getOrgName().isEmpty()) {
                return ApiResponse.badRequest("Organization name is required");
            }
            orgDetails.setId("ORG_" + System.currentTimeMillis());
            kafkaProducer.send("org-created", orgDetails);
            return ApiResponse.created(orgDetails, "Organization created successfully");
        } catch (Exception e) {
            logger.error("Error creating organization", e);
            return ApiResponse.internalError("Error creating organization");
        }
    }

    public ApiResponse<OrgDetails> updateOrg(String id, OrgDetails orgDetails) {
        try {
            if (id == null || id.isEmpty()) {
                return ApiResponse.badRequest("Organization id is required");
            }
            orgDetails.setId(id);
            return ApiResponse.success(orgDetails, "Organization updated successfully");
        } catch (Exception e) {
            logger.error("Error updating organization", e);
            return ApiResponse.internalError("Error updating organization");
        }
    }
}
