package com.po.dto;

public class PoLineData {
    private String id;
    private String poLineId;
    private String item;
    private String uom;
    private int qty;
    private double unitCost;
    private double taxPercentage;
    private double totalCost;
    private String category;
    private String poNumber;
    private int receivedQty;
    private String receivedDate;
    private String createdTime;
    private String createdBy;
    private String lastUpdatedTime;
    private String lastUpdatedBy;

    public PoLineData() {
    }

    public PoLineData(String id, String poLineId, String item, String uom, int qty, double unitCost,
                      double taxPercentage, double totalCost, String category, String poNumber,
                      int receivedQty, String receivedDate, String createdTime, String createdBy,
                      String lastUpdatedTime, String lastUpdatedBy) {
        this.id = id;
        this.poLineId = poLineId;
        this.item = item;
        this.uom = uom;
        this.qty = qty;
        this.unitCost = unitCost;
        this.taxPercentage = taxPercentage;
        this.totalCost = totalCost;
        this.category = category;
        this.poNumber = poNumber;
        this.receivedQty = receivedQty;
        this.receivedDate = receivedDate;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastUpdatedTime = lastUpdatedTime;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoLineId() {
        return poLineId;
    }

    public void setPoLineId(String poLineId) {
        this.poLineId = poLineId;
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

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public int getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(int receivedQty) {
        this.receivedQty = receivedQty;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
