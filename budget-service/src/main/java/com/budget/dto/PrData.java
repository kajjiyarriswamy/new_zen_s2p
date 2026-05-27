package com.budget.dto;

public class PrData {
    
    private String id;
    private String prNumber;
    private String department;
    private String requester;
    private Double budget;
    private String status;
    private String description;
    private String budgetId;

    public PrData() {
    }

    public PrData(String id, String prNumber, String department, String requester, Double budget, String status, String description) {
        this.id = id;
        this.prNumber = prNumber;
        this.department = department;
        this.requester = requester;
        this.budget = budget;
        this.status = status;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrNumber() {
        return prNumber;
    }

    public void setPrNumber(String prNumber) {
        this.prNumber = prNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
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

	public String getBudgetId() {
		return budgetId;
	}

	public void setBudgetId(String budgetId) {
		this.budgetId = budgetId;
	}
    
}

