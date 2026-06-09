package com.invoice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.invoice.entity.InvoiceHeader;


public interface InvoiceHeaderRepository
        extends JpaRepository<InvoiceHeader, Long> {

	Optional<InvoiceHeader> findByInvoiceNumber(String invoiceNumber);

}