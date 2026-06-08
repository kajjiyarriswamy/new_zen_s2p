package com.invoice.dto;

import java.util.List;




public class CreateInvoiceRequestDTO {

    private Long receiptHeaderId;

    private Long vendorId;

    private String vendorCode;

    private String vendorName;

    private String currencyCode;

    private String paymentTerms;

    private String remarks;

    private String createdBy;

    private List<CreateInvoiceLineRequestDTO> lines;

	public Long getReceiptHeaderId() {
		return receiptHeaderId;
	}

	public void setReceiptHeaderId(Long receiptHeaderId) {
		this.receiptHeaderId = receiptHeaderId;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<CreateInvoiceLineRequestDTO> getLines() {
		return lines;
	}

	public void setLines(List<CreateInvoiceLineRequestDTO> lines) {
		this.lines = lines;
	}
    
    
}