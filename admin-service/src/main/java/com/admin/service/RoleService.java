package com.admin.service;

import com.admin.dto.ApiResponse;
import com.admin.dto.RoleDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public ApiResponse<List<RoleDetails>> getAllRoles() {
        try {
            List<RoleDetails> roles = new ArrayList<>();
            roles.add(new RoleDetails("ROLE_1", "Super_admin", "Full administration rights"));
            roles.add(new RoleDetails("ROLE_2", "Department_user", "Can submit PRs and view budgets"));
            roles.add(new RoleDetails("ROLE_3", "Supplier_Admin", "Supplier administration"));
            return ApiResponse.success(roles, "Roles retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving roles", e);
            return ApiResponse.internalError("Error retrieving roles");
        }
    }

    public ApiResponse<RoleDetails> getRoleById(String id) {
        try {
            if (id == null || id.isEmpty()) {
                return ApiResponse.badRequest("Role id is required");
            }
            RoleDetails role = new RoleDetails(id, "Department_user", "Can submit PRs and view budgets");
            return ApiResponse.success(role, "Role retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving role", e);
            return ApiResponse.internalError("Error retrieving role");
        }
    }

    public ApiResponse<RoleDetails> createRole(RoleDetails role) {
        try {
            if (role.getRoleName() == null || role.getRoleName().isEmpty()) {
                return ApiResponse.badRequest("Role name is required");
            }
            role.setId("ROLE_" + System.currentTimeMillis());
            return ApiResponse.created(role, "Role created successfully");
        } catch (Exception e) {
            logger.error("Error creating role", e);
            return ApiResponse.internalError("Error creating role");
        }
    }

    public ApiResponse<RoleDetails> updateRole(String id, RoleDetails role) {
        try {
            if (id == null || id.isEmpty()) {
                return ApiResponse.badRequest("Role id is required");
            }
            role.setId(id);
            return ApiResponse.success(role, "Role updated successfully");
        } catch (Exception e) {
            logger.error("Error updating role", e);
            return ApiResponse.internalError("Error updating role");
        }
    }
}
