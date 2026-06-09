package com.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateInvoiceResponseDTO {

    private Long id;

    private String invoiceNumber;

    private String status;

    private BigDecimal totalAmount;
    private LocalDate invoiceDate;
    private BigDecimal invoiceAmount;
    private BigDecimal taxAmount;

    public CreateInvoiceResponseDTO() {
    }

    public CreateInvoiceResponseDTO(Long id, String invoiceNumber,
                                    String status, BigDecimal totalAmount) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
    
}