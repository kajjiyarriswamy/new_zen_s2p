package com.vendor.controller;

import com.vendor.dto.ApiResponse;
import com.vendor.dto.VendorContact;
import com.vendor.service.VendorContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendor/contacts")
public class VendorContactController {

    private final VendorContactService vendorContactService;

    public VendorContactController(VendorContactService vendorContactService) {
        this.vendorContactService = vendorContactService;
    }

    @GetMapping("/vendor/{venId}")
    public ResponseEntity<ApiResponse<List<VendorContact>>> getContactsByVendor(@PathVariable String venId) {
        ApiResponse<List<VendorContact>> resp = vendorContactService.getContactsByVendor(venId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/vendor/{venId}/create")
    public ResponseEntity<ApiResponse<VendorContact>> createContact(@PathVariable String venId,
                                                                   @RequestBody VendorContact contact) {
        ApiResponse<VendorContact> resp = vendorContactService.createContact(venId, contact);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
}
