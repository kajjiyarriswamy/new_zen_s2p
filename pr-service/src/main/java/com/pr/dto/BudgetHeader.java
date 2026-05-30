package com.pr.dto;

public class BudgetHeader {
    private String budgetId;
    private String budgetDescription;
    private String requestor;
    private String orgId;
    private String approverList;
    private String status;
    private double availableAmount;
    private double consumedAmount;
    private double totalAmount;
    private String createdTime;
    private String createdBy;
    private String lastUpdatedTime;
    private String lastUpdatedBy;

    public BudgetHeader() {
    }

    public BudgetHeader(String budgetId, String budgetDescription, String requestor, String orgId,
                        String approverList, String status, double availableAmount, double consumedAmount,
                        double totalAmount, String createdTime, String createdBy, String lastUpdatedTime,
                        String lastUpdatedBy) {
        this.budgetId = budgetId;
        this.budgetDescription = budgetDescription;
        this.requestor = requestor;
        this.orgId = orgId;
        this.approverList = approverList;
        this.status = status;
        this.availableAmount = availableAmount;
        this.consumedAmount = consumedAmount;
        this.totalAmount = totalAmount;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastUpdatedTime = lastUpdatedTime;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getBudgetDescription() {
        return budgetDescription;
    }

    public void setBudgetDescription(String budgetDescription) {
        this.budgetDescription = budgetDescription;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public double getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(double availableAmount) {
        this.availableAmount = availableAmount;
    }

    public double getConsumedAmount() {
        return consumedAmount;
    }

    public void setConsumedAmount(double consumedAmount) {
        this.consumedAmount = consumedAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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
