package com.invoice.dto;

import java.math.BigDecimal;

public class CreateInvoiceLineRequestDTO {

    private Long receiptLineId;

    private Long poLineId;

    private String itemCode;

    private String itemName;

    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal taxPercentage;

    private String remarks;

	public Long getReceiptLineId() {
		return receiptLineId;
	}

	public void setReceiptLineId(Long receiptLineId) {
		this.receiptLineId = receiptLineId;
	}

	public Long getPoLineId() {
		return poLineId;
	}

	public void setPoLineId(Long poLineId) {
		this.poLineId = poLineId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(BigDecimal taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
    
}