package com.pr.entity;

import javax.persistence.*;

@Entity
@Table(name = "pr_line_dtls")
public class PrLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pr_line_id")
    private String prLineId;

    @Column(name = "item")
    private String item;

    @Column(name = "uom")
    private String uom;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "unit_cost")
    private Double unitCost;

    @Column(name = "tax_percentage")
    private Double taxPercentage;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(name = "category")
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_header_id")
    private PrHeader prHeader;

    public PrLine() {
    }

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrLineId() {
        return prLineId;
    }

    public void setPrLineId(String prLineId) {
        this.prLineId = prLineId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public Double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(Double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public PrHeader getPrHeader() {
        return prHeader;
    }

    public void setPrHeader(PrHeader prHeader) {
        this.prHeader = prHeader;
    }
}
