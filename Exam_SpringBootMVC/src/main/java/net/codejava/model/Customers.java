package net.codejava.model;

import java.util.Date;

public class Customers {
    private int customerId;
    private String fullname;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String profileImage;
    private int status;
    private Date createdAt;
    private String verifyCode;
    private String token;

    // Getters and Setters
    public int getCustomerId() { 
    	return customerId; 
    	}
    
    public void setCustomerId(int customerId) { 
    	this.customerId = customerId; 
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
    
    public int getStatus() { 
    	return status; 
    	}
    
    public void setStatus(int status) { 
    	this.status = status; 
    	}
    
    public Date getCreatedAt() { 
    	return createdAt; 
    	}
    
    public void setCreatedAt(Date createdAt) {
    	this.createdAt = createdAt; 
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
}
