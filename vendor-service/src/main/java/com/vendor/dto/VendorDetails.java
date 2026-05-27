package com.vendor.dto;

public class VendorDetails {
    private String id;
    private String companyName;
    private String emailId;
    private String password;
    private String address;
    private String pan;
    private String tan;
    private String companyRegistrationDate;
    private String userRegistered;
    private String logoPath;
    private String createdTime;
    private String createdBy;
    private String lastUpdatedTime;
    private String lastUpdatedBy;

    public VendorDetails() {
    }

    public VendorDetails(String id, String companyName, String emailId, String password, String address,
                         String pan, String tan, String companyRegistrationDate, String userRegistered,
                         String logoPath, String createdTime, String createdBy, String lastUpdatedTime,
                         String lastUpdatedBy) {
        this.id = id;
        this.companyName = companyName;
        this.emailId = emailId;
        this.password = password;
        this.address = address;
        this.pan = pan;
        this.tan = tan;
        this.companyRegistrationDate = companyRegistrationDate;
        this.userRegistered = userRegistered;
        this.logoPath = logoPath;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getTan() {
        return tan;
    }

    public void setTan(String tan) {
        this.tan = tan;
    }

    public String getCompanyRegistrationDate() {
        return companyRegistrationDate;
    }

    public void setCompanyRegistrationDate(String companyRegistrationDate) {
        this.companyRegistrationDate = companyRegistrationDate;
    }

    public String getUserRegistered() {
        return userRegistered;
    }

    public void setUserRegistered(String userRegistered) {
        this.userRegistered = userRegistered;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
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
