package com.vendor.entity;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "delivery_note_header")
public class DeliveryNoteHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delivery_note_number", nullable = false, length = 100)
    private String deliveryNoteNumber;

    @Column(name = "delivery_note_date", nullable = false)
    private LocalDate deliveryNoteDate;

    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column(name = "vendor_code", length = 100)
    private String vendorCode;

    @Column(name = "vendor_name", length = 255)
    private String vendorName;

    @Column(name = "po_id", nullable = false)
    private Long poId;

    @Column(name = "po_number", length = 100)
    private String poNumber;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "remarks", length = 1000)
    private String remarks;

    @Column(name = "total_quantity", precision = 19, scale = 4)
    private BigDecimal totalQuantity;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_by", length = 100)
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "deliveryNoteHeader", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DeliveryNoteLine> deliveryNoteLines = new ArrayList<>();
}
