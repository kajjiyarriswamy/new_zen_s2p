package com.vendor.entity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "delivery_note_line")
public class DeliveryNoteLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_note_header_id", nullable = false)
    private DeliveryNoteHeader deliveryNoteHeader;

    @Column(name = "po_line_id", nullable = false)
    private Long poLineId;

    @Column(name = "item_code", length = 100)
    private String itemCode;

    @Column(name = "item_name", length = 255)
    private String itemName;

    @Column(name = "uom", length = 50)
    private String uom;

    @Column(name = "ordered_quantity", precision = 19, scale = 4)
    private BigDecimal orderedQuantity;

    @Column(name = "delivered_quantity", precision = 19, scale = 4)
    private BigDecimal deliveredQuantity;

    @Column(name = "remaining_quantity", precision = 19, scale = 4)
    private BigDecimal remainingQuantity;

    @Column(name = "remarks", length = 1000)
    private String remarks;
}
