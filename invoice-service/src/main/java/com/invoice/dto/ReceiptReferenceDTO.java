package com.invoice.dto;

import java.util.List;

public class ReceiptReferenceDTO {

	
	 private Long receiptHeaderId;

	    private List<Long> receiptLineIds;

		public Long getReceiptHeaderId() {
			return receiptHeaderId;
		}

		public void setReceiptHeaderId(Long receiptHeaderId) {
			this.receiptHeaderId = receiptHeaderId;
		}

		public List<Long> getReceiptLineIds() {
			return receiptLineIds;
		}

		public void setReceiptLineIds(List<Long> receiptLineIds) {
			this.receiptLineIds = receiptLineIds;
		}
	    
	    

}
