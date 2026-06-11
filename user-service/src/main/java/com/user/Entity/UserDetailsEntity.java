package com.user.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="user_Details")
public class UserDetailsEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "id") 
	private Long id; 
	
	@Column(name = "name") 
	private String name; 
	
	@Column(name = "user_name") 
	private String userName; 
	@Column(name = "user_id") 
	private String userId; 
	@Column(name = "designation") 
	private String designation; 
	@Column(name = "email_id") 
	private String emailId; 
	@Column(name = "phone") 
	private String phone; 
	@Column(name = "address") 
	private String address; 
	@Column(name = "otp") 
	private String otp; 
	@Column(name = "user_status") 
	private String userStatus; 
	@Column(name = "user_type") 
	private String userType; 
	@Column(name = "login_failed_attempts") 
	private int loginFailedAttempts; 
	@Column(name = "locked_duration_time") 
	private String lockedDurationTime; 
	@Column(name = "created_time") 
	private String createdTime; 
	@Column(name = "created_by") 
	private String createdBy; 
	@Column(name = "last_updated_time") 
	private String lastUpdatedTime; 
	@Column(name = "last_updated_by") 
	private String lastUpdatedBy; 
	@Column(name = "document_path") 
	private String documentPath; 
	public UserDetailsEntity() {
		
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getDocumentPath() {
		return documentPath;
	}
	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}
	
	

}
