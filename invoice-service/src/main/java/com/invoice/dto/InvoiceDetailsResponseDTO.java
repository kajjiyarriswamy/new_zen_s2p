package com.invoice.dto;

import java.util.List;

public class InvoiceDetailsResponseDTO {

	    private InvoiceHeaderDTO invoiceHeader;

	    private List<InvoiceLineDTO> invoiceLines;

	    private VendorDTO vendorDetails;

	    private POReferenceDTO poReference;

	    private ReceiptReferenceDTO receiptReference;

		public InvoiceHeaderDTO getInvoiceHeader() {
			return invoiceHeader;
		}

		public void setInvoiceHeader(InvoiceHeaderDTO invoiceHeader) {
			this.invoiceHeader = invoiceHeader;
		}

		public List<InvoiceLineDTO> getInvoiceLines() {
			return invoiceLines;
		}

		public void setInvoiceLines(List<InvoiceLineDTO> invoiceLines) {
			this.invoiceLines = invoiceLines;
		}

		public VendorDTO getVendorDetails() {
			return vendorDetails;
		}

		public void setVendorDetails(VendorDTO vendorDetails) {
			this.vendorDetails = vendorDetails;
		}

		public POReferenceDTO getPoReference() {
			return poReference;
		}

		public void setPoReference(POReferenceDTO poReference) {
			this.poReference = poReference;
		}

		public ReceiptReferenceDTO getReceiptReference() {
			return receiptReference;
		}

		public void setReceiptReference(ReceiptReferenceDTO receiptReference) {
			this.receiptReference = receiptReference;
		}
	    
	    
}
