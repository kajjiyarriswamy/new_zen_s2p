package com.invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.invoice.entity.InvoiceHeader;

public interface InvoiceHeaderRepository
        extends JpaRepository<InvoiceHeader, Long> {

}