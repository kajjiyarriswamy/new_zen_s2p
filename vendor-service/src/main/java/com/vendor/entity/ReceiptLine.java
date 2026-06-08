package com.vendor.entity;


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "receipt_line")
public class ReceiptLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_header_id", nullable = false)
    private ReceiptHeader receiptHeader;

    @Column(name = "delivery_note_line_id", nullable = false)
    private Long deliveryNoteLineId;

    @Column(name = "po_line_id", nullable = false)
    private Long poLineId;

    @Column(name = "item_code", length = 100)
    private String itemCode;

    @Column(name = "item_name", length = 255)
    private String itemName;

    @Column(name = "received_quantity", precision = 19, scale = 4)
    private BigDecimal receivedQuantity;

    @Column(name = "accepted_quantity", precision = 19, scale = 4)
    private BigDecimal acceptedQuantity;

    @Column(name = "rejected_quantity", precision = 19, scale = 4)
    private BigDecimal rejectedQuantity;

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason;

    @Column(name = "warehouse_location", length = 255)
    private String warehouseLocation;

    @Column(name = "status", length = 50)
    private String status;
}
