package com.po.dto;

public class PoData {

    private Long id;
    private String poNumber;
    private String poDescription;
    private String requestor;
    private String ownerBuyer;
    private String vendorId;
    private Double amount;
    private Double taxAmount;
    private Double totalAmount;
    private String approverList;
    private String status;
    private String currency;
    private String prNumber;
    private String orgId;
    private String budgetId;
    private String paymentTermId;

    public PoData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getPoDescription() {
        return poDescription;
    }

    public void setPoDescription(String poDescription) {
        this.poDescription = poDescription;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getOwnerBuyer() {
        return ownerBuyer;
    }

    public void setOwnerBuyer(String ownerBuyer) {
        this.ownerBuyer = ownerBuyer;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getApproverList() {
        return approverList;
    }

    public void setApproverList(String approverList) {
        this.approverList = approverList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrNumber() {
        return prNumber;
    }

    public void setPrNumber(String prNumber) {
        this.prNumber = prNumber;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getPaymentTermId() {
        return paymentTermId;
    }

    public void setPaymentTermId(String paymentTermId) {
        this.paymentTermId = paymentTermId;
    }
}