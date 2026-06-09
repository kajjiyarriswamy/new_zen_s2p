package com.user.service;

import com.user.dto.ApiResponse;
import com.user.dto.OrgMap;
import com.user.dto.RoleMap;
import com.user.dto.UserVendorMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMappingService {

    private static final Logger logger = LoggerFactory.getLogger(UserMappingService.class);

    public ApiResponse<List<OrgMap>> getOrgMappings(String userId) {
        try {
            List<OrgMap> mappings = new ArrayList<>();
            mappings.add(new OrgMap("MAP_001", userId, "ORG_001"));
            return ApiResponse.success(mappings, "Organization mappings retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving org mappings", e);
            return ApiResponse.internalError("Error retrieving org mappings");
        }
    }

    public ApiResponse<List<RoleMap>> getRoleMappings(String userId) {
        try {
            List<RoleMap> mappings = new ArrayList<>();
            mappings.add(new RoleMap("MAP_002", userId, "ROLE_2"));
            return ApiResponse.success(mappings, "Role mappings retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving role mappings", e);
            return ApiResponse.internalError("Error retrieving role mappings");
        }
    }

    public ApiResponse<List<UserVendorMap>> getVendorMappings(String userId) {
        try {
            List<UserVendorMap> mappings = new ArrayList<>();
            mappings.add(new UserVendorMap("MAP_003", userId, "VEND_001"));
            return ApiResponse.success(mappings, "Vendor mappings retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving vendor mappings", e);
            return ApiResponse.internalError("Error retrieving vendor mappings");
        }
    }

    public ApiResponse<OrgMap> linkUserToOrg(String userId, String orgId) {
        try {
            OrgMap mapping = new OrgMap("MAP_" + System.currentTimeMillis(), userId, orgId);
            return ApiResponse.created(mapping, "User organization mapping created");
        } catch (Exception e) {
            logger.error("Error creating org mapping", e);
            return ApiResponse.internalError("Error creating org mapping");
        }
    }

    public ApiResponse<RoleMap> linkUserToRole(String userId, String roleId) {
        try {
            RoleMap mapping = new RoleMap("MAP_" + System.currentTimeMillis(), userId, roleId);
            return ApiResponse.created(mapping, "User role mapping created");
        } catch (Exception e) {
            logger.error("Error creating role mapping", e);
            return ApiResponse.internalError("Error creating role mapping");
        }
    }

    public ApiResponse<UserVendorMap> linkUserToVendor(String userId, String vendorId) {
        try {
            UserVendorMap mapping = new UserVendorMap("MAP_" + System.currentTimeMillis(), userId, vendorId);
            return ApiResponse.created(mapping, "User vendor mapping created");
        } catch (Exception e) {
            logger.error("Error creating vendor mapping", e);
            return ApiResponse.internalError("Error creating vendor mapping");
        }
    }
}