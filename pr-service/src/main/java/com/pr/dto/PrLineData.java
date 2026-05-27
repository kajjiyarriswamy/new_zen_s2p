package com.pr.dto;

public class PrLineData {
    private String id;
    private String prLineId;
    private String item;
    private String uom;
    private int qty;
    private double unitCost;
    private double taxPercentage;
    private double totalCost;
    private String category;
    private String prNumber;

    public PrLineData() {
    }

    public PrLineData(String id, String prLineId, String item, String uom, int qty, double unitCost,
                      double taxPercentage, double totalCost, String category, String prNumber) {
        this.id = id;
        this.prLineId = prLineId;
        this.item = item;
        this.uom = uom;
        this.qty = qty;
        this.unitCost = unitCost;
        this.taxPercentage = taxPercentage;
        this.totalCost = totalCost;
        this.category = category;
        this.prNumber = prNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrNumber() {
        return prNumber;
    }

    public void setPrNumber(String prNumber) {
        this.prNumber = prNumber;
    }
}
