package net.codejava.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.time.ZoneId;

public class Contracts {
	private Customers customer;
    private int contractId;
    private int customerId; 
    private int contractStatus;
    private int totalPrice;
    private int paymentStatus;
    private LocalDateTime createdAt;
    private String contractFile;

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

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
    
    public LocalDateTime getCreatedAt() { 
    	return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
    	this.createdAt = createdAt; 
    }
    
    public String getContractFile() { 
    	return contractFile; 
    }
    
    public void setContractFile(String contractFile) { 
    	this.contractFile = contractFile; 
    }
    public Date getCreatedAtAsDate() {
        return Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant());
    }

}
