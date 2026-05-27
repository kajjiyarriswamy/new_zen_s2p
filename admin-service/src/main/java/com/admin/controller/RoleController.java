package com.admin.controller;

import com.admin.dto.ApiResponse;
import com.admin.dto.RoleDetails;
import com.admin.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RoleDetails>>> getAllRoles() {
        ApiResponse<List<RoleDetails>> resp = roleService.getAllRoles();
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDetails>> getRoleById(@PathVariable String id) {
        ApiResponse<RoleDetails> resp = roleService.getRoleById(id);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<RoleDetails>> createRole(@RequestBody RoleDetails role) {
        ApiResponse<RoleDetails> resp = roleService.createRole(role);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDetails>> updateRole(@PathVariable String id,
                                                               @RequestBody RoleDetails role) {
        ApiResponse<RoleDetails> resp = roleService.updateRole(id, role);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
}
