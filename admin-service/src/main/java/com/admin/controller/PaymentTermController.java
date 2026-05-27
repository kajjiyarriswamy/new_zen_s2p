package com.admin.controller;

import com.admin.dto.ApiResponse;
import com.admin.dto.PaymentTerm;
import com.admin.service.PaymentTermService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/payment-terms")
public class PaymentTermController {

    private final PaymentTermService paymentTermService;

    public PaymentTermController(PaymentTermService paymentTermService) {
        this.paymentTermService = paymentTermService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PaymentTerm>>> getAllPaymentTerms() {
        ApiResponse<List<PaymentTerm>> resp = paymentTermService.getAllPaymentTerms();
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentTerm>> getPaymentTermById(@PathVariable String id) {
        ApiResponse<PaymentTerm> resp = paymentTermService.getPaymentTermById(id);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PaymentTerm>> createPaymentTerm(@RequestBody PaymentTerm term) {
        ApiResponse<PaymentTerm> resp = paymentTermService.createPaymentTerm(term);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentTerm>> updatePaymentTerm(@PathVariable String id,
                                                                      @RequestBody PaymentTerm term) {
        ApiResponse<PaymentTerm> resp = paymentTermService.updatePaymentTerm(id, term);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
}
