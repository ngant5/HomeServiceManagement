package net.codejava.model;

import java.util.Date;

public class Employees {
    private int employeeId;
    private String fullname;
    private String password;
    private String userType;
    private String email;
    private String phone;
    private String address;
    private String profileImage;
    private int experienceYears;
    private int salary;
    private int status;
    private String verifyCode;
    private String token;
    private Date createdAt;

    
    public int getEmployeeId() {
    	return employeeId;
    	}
    
    public void setEmployeeId(int employeeId) {
    	this.employeeId = employeeId; 
    	}
    
    public String getFullname() { 
    	return fullname; 
    	}
    
    public void setFullname(String fullname) { 
    	this.fullname = fullname; 
    	}
    
    public String getPassword() { 
    	return password; 
    	}
    
    public void setPassword(String password) { 
    	this.password = password;
    	}
    
    public String getUserType() { 
    	return userType; 
    	}
    
    public void setUserType(String userType) {
    	this.userType = userType;
    	}
    
    public String getEmail() { 
    	return email; 
    	}
    
    public void setEmail(String email) { 
    	this.email = email; 
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
    
    public String getProfileImage() { 
    	return profileImage; 
    	}
    
    public void setProfileImage(String profileImage) { 
    	this.profileImage = profileImage;
    	}
    
    public int getExperienceYears() {
    	return experienceYears; 
    	}
    
    public void setExperienceYears(int experienceYears) {
    	this.experienceYears = experienceYears;
    	}
    
    public int getSalary() { 
    	return salary; 
    	}
    public void setSalary(int salary) {
    	this.salary = salary; 
    	}
    
    public int getStatus() {
    	return status; 
    	}
    
    public void setStatus(int status) { 
    	this.status = status;
    	}
    
    public String getVerifyCode() { 
    	return verifyCode; 
    	}
    
    public void setVerifyCode(String verifyCode) { 
    	this.verifyCode = verifyCode;
    	}
    
    public String getToken() { 
    	return token; 
    	}
    
    public void setToken(String token) { 
    	this.token = token; 
    	}
    
    public Date getCreatedAt() {
    	return createdAt;
    	}
    
    public void setCreatedAt(Date createdAt) {
    	this.createdAt = createdAt; 
    	}
}
