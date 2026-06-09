package com.vendor.controller;

import com.vendor.dto.ApiResponse;
import com.vendor.dto.DeliveryNoteRequestDto;
import com.vendor.dto.DeliveryNoteResponseDto;
import com.vendor.dto.VendorDetails;
import com.vendor.service.DeliveryNoteService;
import com.vendor.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<VendorDetails>>> getAllVendors() {
        ApiResponse<List<VendorDetails>> resp = vendorService.getAllVendors();
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorDetails>> getVendorById(@PathVariable String id) {
        ApiResponse<VendorDetails> resp = vendorService.getVendorById(id);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<VendorDetails>> signup(@RequestBody VendorDetails vendorDetails) {
        ApiResponse<VendorDetails> resp = vendorService.signup(vendorDetails);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorDetails>> updateVendor(@PathVariable String id,
                                                                   @RequestBody VendorDetails vendorDetails) {
        ApiResponse<VendorDetails> resp = vendorService.updateVendor(id, vendorDetails);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
  
    }


