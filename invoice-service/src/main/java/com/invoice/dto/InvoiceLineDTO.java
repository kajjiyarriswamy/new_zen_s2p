package com.invoice.dto;

import java.math.BigDecimal;

public class InvoiceLineDTO {
	
	 private Long id;
	    private String itemCode;
	    private String itemName;
	    private BigDecimal quantity;
	    private BigDecimal unitPrice;
	    private BigDecimal lineAmount;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
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
		public BigDecimal getLineAmount() {
			return lineAmount;
		}
		public void setLineAmount(BigDecimal lineAmount) {
			this.lineAmount = lineAmount;
		}
	    
	    

}
