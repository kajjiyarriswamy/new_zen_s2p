package com.admin.controller;

import com.admin.dto.ApiResponse;
import com.admin.dto.OrgDetails;
import com.admin.service.OrgService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orgs")
public class OrgController {

    private final OrgService orgService;

    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<OrgDetails>>> getAllOrgs() {
        ApiResponse<List<OrgDetails>> resp = orgService.getAllOrgs();
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrgDetails>> getOrgById(@PathVariable String id) {
        ApiResponse<OrgDetails> resp = orgService.getOrgById(id);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<OrgDetails>> createOrg(@RequestBody OrgDetails orgDetails) {
        ApiResponse<OrgDetails> resp = orgService.createOrg(orgDetails);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrgDetails>> updateOrg(@PathVariable String id,
                                                             @RequestBody OrgDetails orgDetails) {
        ApiResponse<OrgDetails> resp = orgService.updateOrg(id, orgDetails);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
}
