package com.vendor.controller;

import com.vendor.dto.ApiResponse;
import com.vendor.dto.VendorBank;
import com.vendor.service.VendorBankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendor/banks")
public class VendorBankController {

    private final VendorBankService vendorBankService;

    public VendorBankController(VendorBankService vendorBankService) {
        this.vendorBankService = vendorBankService;
    }

    @GetMapping("/vendor/{venId}")
    public ResponseEntity<ApiResponse<List<VendorBank>>> getBanksByVendor(@PathVariable String venId) {
        ApiResponse<List<VendorBank>> resp = vendorBankService.getBanksByVendor(venId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/vendor/{venId}/create")
    public ResponseEntity<ApiResponse<VendorBank>> createBank(@PathVariable String venId,
                                                              @RequestBody VendorBank bank) {
        ApiResponse<VendorBank> resp = vendorBankService.createBank(venId, bank);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
}
