package com.user.controller;

import com.user.dto.ApiResponse;
import com.user.dto.OrgMap;
import com.user.dto.RoleMap;
import com.user.dto.UserVendorMap;
import com.user.service.UserMappingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/mapping")
public class UserMappingController {

    private final UserMappingService userMappingService;

    public UserMappingController(UserMappingService userMappingService) {
        this.userMappingService = userMappingService;
    }

    @GetMapping("/org/{userId}")
    public ResponseEntity<ApiResponse<List<OrgMap>>> getOrgMappings(@PathVariable String userId) {
        ApiResponse<List<OrgMap>> resp = userMappingService.getOrgMappings(userId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @GetMapping("/role/{userId}")
    public ResponseEntity<ApiResponse<List<RoleMap>>> getRoleMappings(@PathVariable String userId) {
        ApiResponse<List<RoleMap>> resp = userMappingService.getRoleMappings(userId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @GetMapping("/vendor/{userId}")
    public ResponseEntity<ApiResponse<List<UserVendorMap>>> getVendorMappings(@PathVariable String userId) {
        ApiResponse<List<UserVendorMap>> resp = userMappingService.getVendorMappings(userId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/org/{userId}/{orgId}")
    public ResponseEntity<ApiResponse<OrgMap>> linkUserToOrg(@PathVariable String userId,
                                                             @PathVariable String orgId) {
        ApiResponse<OrgMap> resp = userMappingService.linkUserToOrg(userId, orgId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/role/{userId}/{roleId}")
    public ResponseEntity<ApiResponse<RoleMap>> linkUserToRole(@PathVariable String userId,
                                                               @PathVariable String roleId) {
        ApiResponse<RoleMap> resp = userMappingService.linkUserToRole(userId, roleId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/vendor/{userId}/{vendorId}")
    public ResponseEntity<ApiResponse<UserVendorMap>> linkUserToVendor(@PathVariable String userId,
                                                                       @PathVariable String vendorId) {
        ApiResponse<UserVendorMap> resp = userMappingService.linkUserToVendor(userId, vendorId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
}
