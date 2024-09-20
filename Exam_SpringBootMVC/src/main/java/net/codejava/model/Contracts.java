package net.codejava.model;

import java.util.Date;

public class Contracts {
    private int contractId;
    private int customerId; 
    private int contractStatus;
    private int totalPrice;
    private int paymentStatus;
    private Date createdAt;
    private String contractFile;


    public int getContractId() { 
    	return contractId; 
    }
    
    public void setContractId(int contractId) { 
    	this.contractId = contractId; 
    }
    
    public int getCustomerId() { 
    	return customerId; 
    }
    
    
    public void setCustomerId(int customerId) {
    	this.customerId = customerId; 
    }
    
    public int getContractStatus() { 
    	return contractStatus; 
    }
    
    public void setContractStatus(int contractStatus) { 
    	this.contractStatus = contractStatus; 
    }
    
    public int getTotalPrice() { 
    	return totalPrice; 
    }
    
    public void setTotalPrice(int totalPrice) { 
    	this.totalPrice = totalPrice; 
    }
    
    public int getPaymentStatus() { 
    	return paymentStatus; 
    }
    
    public void setPaymentStatus(int paymentStatus) { 
    	this.paymentStatus = paymentStatus; 
    }
    
    public Date getCreatedAt() { 
    	return createdAt; 
    }
    
    public void setCreatedAt(Date createdAt) { 
    	this.createdAt = createdAt; 
    }
    
    public String getContractFile() { 
    	return contractFile; 
    }
    
    public void setContractFile(String contractFile) { 
    	this.contractFile = contractFile; 
    }
}