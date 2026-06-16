package com.invoice.service;

import com.invoice.dto.InvoiceDetailsResponseDTO;

public interface InvoiceServiceImpl  {

	InvoiceDetailsResponseDTO getInvoiceDetails(String invoiceNumber);

}
