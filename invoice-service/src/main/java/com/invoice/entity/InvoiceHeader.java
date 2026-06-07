package com.invoice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoice_header")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "invoiceLines")
public class InvoiceHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", nullable = false, length = 100)
    private String invoiceNumber;

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column(name = "vendor_code", nullable = false, length = 100)
    private String vendorCode;

    @Column(name = "vendor_name", nullable = false, length = 255)
    private String vendorName;

    @Column(name = "receipt_header_id", nullable = false)
    private Long receiptHeaderId;

    @Column(name = "currency_code", nullable = false, length = 10)
    private String currencyCode;

    @Column(name = "invoice_amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal invoiceAmount;

    @Column(name = "tax_amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "payment_terms", length = 255)
    private String paymentTerms;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "remarks", length = 1000)
    private String remarks;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_by", length = 100)
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "invoiceHeader", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InvoiceLine> invoiceLines = new ArrayList<>();
}
