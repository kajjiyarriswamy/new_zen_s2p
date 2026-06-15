package com.invoice.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.invoice.dto.ApiResponse;
import com.invoice.dto.CreateInvoiceRequestDTO;
import com.invoice.dto.CreateInvoiceResponseDTO;
import com.invoice.dto.UploadDocumentResponseDTO;
import com.invoice.entity.InvoiceHeader;
import com.invoice.exception.ResourceNotFoundException;
import com.invoice.repository.InvoiceHeaderRepository;
import com.invoice.async.InvoiceAsyncService;
import com.invoice.service.InvoiceService;
import com.invoice.storage.S3StorageService;

@RestController
@RequestMapping("/invoice/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceHeaderRepository invoiceHeaderRepository;
    private final S3StorageService s3StorageService;
    private final InvoiceAsyncService invoiceAsyncService;

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);


    public InvoiceController(InvoiceService invoiceService, 
                             InvoiceHeaderRepository invoiceHeaderRepository, 
                             S3StorageService s3StorageService,
                             InvoiceAsyncService invoiceAsyncService) {
        this.invoiceService = invoiceService;
        this.invoiceHeaderRepository = invoiceHeaderRepository;
        this.s3StorageService = s3StorageService;
        this.invoiceAsyncService = invoiceAsyncService;
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
    
    @PostMapping("/{invoiceNumber}/upload-document")
    public ResponseEntity<ApiResponse<UploadDocumentResponseDTO>> uploadBudgetDocument(@PathVariable String invoiceNumber,
                                                                          @RequestParam("file") MultipartFile file) {
        logger.info("Uploading document for invoice {}", invoiceNumber);
        if (invoiceNumber == null || invoiceNumber.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest("invoiceNumber id is required"));
        }
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest("Document file is required"));
        }
        
        InvoiceHeader entity = invoiceHeaderRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with number: " + invoiceNumber));

        try {
            String objectKey = String.format("invoice/%s/%s_%s", invoiceNumber, UUID.randomUUID(), file.getOriginalFilename());
            String documentPath = s3StorageService.uploadFile(file, objectKey);
            entity.setDocumentPath(documentPath);
            entity.setModifiedDate(LocalDateTime.now());
            invoiceHeaderRepository.save(entity);
            invoiceAsyncService.auditInvoiceUpload(invoiceNumber, documentPath);
            return ResponseEntity.ok(ApiResponse.success(toDto(entity), "Budget document uploaded successfully"));
        } catch (Exception e) {
            logger.error("Failed to upload budget document", e);
            return ResponseEntity.status(500).body(ApiResponse.internalError("Failed to upload budget document: " + e.getMessage()));
        }
    }

	private UploadDocumentResponseDTO toDto(InvoiceHeader e) {
		UploadDocumentResponseDTO d = new UploadDocumentResponseDTO();
        d.setInvoiceNumber(e.getInvoiceNumber());
        d.setInvoiceDate(e.getInvoiceDate());
        d.setReceiptHeaderId(e.getReceiptHeaderId());
        d.setInvoiceAmount(e.getInvoiceAmount());
        d.setModifiedDate(e.getModifiedDate());
        d.setDocumentPath(e.getDocumentPath());
        return d;
    }
}