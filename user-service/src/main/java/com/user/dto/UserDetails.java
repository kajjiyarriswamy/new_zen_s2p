package com.user.dto;

public class UserDetails {
    private String id;
    private String name;
    private String userName;
    private String designation;
    private String emailId;
    private String phone;
    private String address;
    private String otp;
    private String userStatus;
    private String userType;
    private int loginFailedAttempts;
    private String lockedDurationTime;
    private String createdTime;
    private String createdBy;
    private String lastUpdatedTime;
    private String lastUpdatedBy;

    public UserDetails() {
    }

    public UserDetails(String id, String name, String userName, String designation, String emailId, String phone,
                       String address, String otp, String userStatus, String userType, int loginFailedAttempts,
                       String lockedDurationTime, String createdTime, String createdBy,
                       String lastUpdatedTime, String lastUpdatedBy) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.designation = designation;
        this.emailId = emailId;
        this.phone = phone;
        this.address = address;
        this.otp = otp;
        this.userStatus = userStatus;
        this.userType = userType;
        this.loginFailedAttempts = loginFailedAttempts;
        this.lockedDurationTime = lockedDurationTime;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getLoginFailedAttempts() {
        return loginFailedAttempts;
    }

    public void setLoginFailedAttempts(int loginFailedAttempts) {
        this.loginFailedAttempts = loginFailedAttempts;
    }

    public String getLockedDurationTime() {
        return lockedDurationTime;
    }

    public void setLockedDurationTime(String lockedDurationTime) {
        this.lockedDurationTime = lockedDurationTime;
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
