package com.invoice.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.invoice.dto.CreateInvoiceLineRequestDTO;
import com.invoice.dto.CreateInvoiceRequestDTO;
import com.invoice.dto.CreateInvoiceResponseDTO;
import com.invoice.dto.InvoiceDetailsResponseDTO;
import com.invoice.dto.InvoiceHeaderDTO;
import com.invoice.dto.InvoiceLineDTO;
import com.invoice.dto.POReferenceDTO;
import com.invoice.dto.ReceiptReferenceDTO;
import com.invoice.dto.VendorDTO;
import com.invoice.entity.InvoiceHeader;
import com.invoice.entity.InvoiceLine;
import com.invoice.repository.InvoiceHeaderRepository;

@Service
public class InvoiceService {

    private final InvoiceHeaderRepository invoiceHeaderRepository;

    public InvoiceService(InvoiceHeaderRepository invoiceHeaderRepository) {
        this.invoiceHeaderRepository = invoiceHeaderRepository;
    }

    @Transactional
    public CreateInvoiceResponseDTO createInvoice(
            CreateInvoiceRequestDTO requestDTO) {

        InvoiceHeader header = new InvoiceHeader();

        String invoiceNumber =
                "INV-2026-" +
                String.format("%04d",
                invoiceHeaderRepository.count() + 1);

        BigDecimal invoiceAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;

        header.setInvoiceNumber(invoiceNumber);
        header.setInvoiceDate(LocalDate.now());
        header.setVendorId(requestDTO.getVendorId());
        header.setVendorCode(requestDTO.getVendorCode());
        header.setVendorName(requestDTO.getVendorName());
        header.setReceiptHeaderId(requestDTO.getReceiptHeaderId());
        header.setCurrencyCode(requestDTO.getCurrencyCode());
        header.setPaymentTerms(requestDTO.getPaymentTerms());
        header.setRemarks(requestDTO.getRemarks());
        header.setStatus("SUBMITTED");
        header.setCreatedBy(requestDTO.getCreatedBy());
        header.setCreatedDate(LocalDateTime.now());

        for (CreateInvoiceLineRequestDTO dto : requestDTO.getLines()) {

            InvoiceLine line = new InvoiceLine();

            line.setInvoiceHeader(header);
            line.setReceiptLineId(dto.getReceiptLineId());
            line.setPoLineId(dto.getPoLineId());
            line.setItemCode(dto.getItemCode());
            line.setItemName(dto.getItemName());
            line.setQuantity(dto.getQuantity());
            line.setUnitPrice(dto.getUnitPrice());
            line.setTaxPercentage(dto.getTaxPercentage());
            line.setRemarks(dto.getRemarks());

            BigDecimal lineAmount =
                    dto.getQuantity()
                       .multiply(dto.getUnitPrice());

            BigDecimal lineTax =
                    lineAmount.multiply(dto.getTaxPercentage())
                              .divide(BigDecimal.valueOf(100),
                                      4,
                                      RoundingMode.HALF_UP);

            BigDecimal totalLineAmount =
                    lineAmount.add(lineTax);

            line.setLineAmount(lineAmount);
            line.setTaxAmount(lineTax);
            line.setTotalLineAmount(totalLineAmount);

            line.setCreatedBy(requestDTO.getCreatedBy());
            line.setCreatedDate(LocalDateTime.now());

            header.getInvoiceLines().add(line);

            invoiceAmount = invoiceAmount.add(lineAmount);
            taxAmount = taxAmount.add(lineTax);
        }

        header.setInvoiceAmount(invoiceAmount);
        header.setTaxAmount(taxAmount);
        header.setTotalAmount(invoiceAmount.add(taxAmount));

        InvoiceHeader saved =
                invoiceHeaderRepository.save(header);

        CreateInvoiceResponseDTO response =
                new CreateInvoiceResponseDTO();

        response.setId(saved.getId());
        response.setInvoiceNumber(saved.getInvoiceNumber());
        response.setInvoiceDate(saved.getInvoiceDate());
        response.setStatus(saved.getStatus());
        response.setInvoiceAmount(saved.getInvoiceAmount());
        response.setTaxAmount(saved.getTaxAmount());
        response.setTotalAmount(saved.getTotalAmount());

        return response;
    }

	public InvoiceDetailsResponseDTO getInvoiceByNumber(String invoiceNumber) {
		
		InvoiceHeader h=invoiceHeaderRepository.findByInvoiceNumber(invoiceNumber)
				      .orElseThrow(() -> new RuntimeException("Invoice Not Found"));
		
		InvoiceDetailsResponseDTO response= new InvoiceDetailsResponseDTO();
		
		//Header
		InvoiceHeaderDTO hd=new InvoiceHeaderDTO();
		hd.setInvoiceNumber(h.getInvoiceNumber());
		hd.setInvoiceDate(h.getInvoiceDate());
		hd.setStatus(h.getStatus());
		hd.setInvoiceAmount(h.getInvoiceAmount());
		hd.setTaxAmount(h.getTaxAmount());
		hd.setTotalAmount(h.getTotalAmount());
		
		response.setInvoiceHeader(hd);
		
		//Vendor
	    VendorDTO v=new VendorDTO();
	    v.setVendorId(h.getVendorId());
	    v.setVendorCode(h.getVendorCode());
	    v.setVendorName(h.getVendorName());
	    
	    response.setVendorDetails(v);
	    
	    //Lines
	    List<InvoiceLineDTO> lined=new ArrayList<>();
	    List<Long> poIds=new ArrayList<>();
	    List<Long> receiptIds=new ArrayList<>();
	    
	    for(InvoiceLine line : h.getInvoiceLines()) {
	    	
	    	InvoiceLineDTO dto=new InvoiceLineDTO();
	    	dto.setId(line.getId());
	    	dto.setItemCode(line.getItemCode());
	        dto.setItemName(line.getItemName());
	        dto.setQuantity(line.getQuantity());
	        dto.setUnitPrice(line.getUnitPrice());
	        dto.setLineAmount(line.getLineAmount());
	        
	        lined.add(dto);
	        
	        poIds.add(line.getPoLineId());
	        receiptIds.add(line.getReceiptLineId());
	    }
	    response.setInvoiceLines(lined);
	    
	    //PO Reference
	    POReferenceDTO po=new POReferenceDTO();
	    po.setPoLineIds(poIds);
	    response.setPoReference(po);
	    
	    //Receipt Reference
	    ReceiptReferenceDTO receipt=new ReceiptReferenceDTO();
	    receipt.setReceiptHeaderId(h.getReceiptHeaderId());
	    receipt.setReceiptLineIds(receiptIds);
	    
	    response.setReceiptReference(receipt);
	    
	    return response;
	    
	}
}