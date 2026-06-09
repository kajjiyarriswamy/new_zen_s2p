package com.invoice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.invoice.dto.ApiResponse;
import com.invoice.dto.CreateInvoiceRequestDTO;
import com.invoice.dto.CreateInvoiceResponseDTO;
import com.invoice.dto.InvoiceDetailsResponseDTO;
import com.invoice.service.InvoiceService;

@RestController
@RequestMapping("/invoice/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("create/invoices")
    public ResponseEntity<ApiResponse<CreateInvoiceResponseDTO>> createInvoice(
            @RequestBody CreateInvoiceRequestDTO requestDTO) {

        CreateInvoiceResponseDTO response =
                invoiceService.createInvoice(requestDTO);

        return ResponseEntity.ok(
                ApiResponse.success(response,
                        "Invoice created successfully"));
    }
    
    @GetMapping("/{invoiceNumber}")
    public ResponseEntity<ApiResponse<InvoiceDetailsResponseDTO>>
             getInvoiceByNumber(@PathVariable String invoiceNumber){
    	
    	InvoiceDetailsResponseDTO response=invoiceService.getInvoiceByNumber(invoiceNumber);
    	
    	return ResponseEntity.ok(ApiResponse.success(response, "Invoice retrived successfully"));
    }
}