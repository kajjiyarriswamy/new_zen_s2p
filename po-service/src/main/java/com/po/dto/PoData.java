package com.po.dto;

public class PoData {
    
    private String id;
    private String poNumber;
    private String vendor;
    private double amount;
    private String status;
    private String description;

    public PoData() {
    }

    public PoData(String id, String poNumber, String vendor, double amount, String status, String description) {
        this.id = id;
        this.poNumber = poNumber;
        this.vendor = vendor;
        this.amount = amount;
        this.status = status;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
