package net.codejava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.time.ZoneId;

public class Contracts {
	private Customers customer;
    private int contractId;
    private int customerId; 
    private int contractStatus;
    private double totalPrice;
    private Integer paymentStatus;
    private LocalDateTime createdAt;
    private String contractFile;
    private ContractDetails contractDetail;
    private Integer employeeId;
    
    private String paymentStatusText;
    private String contractStatusText;
    
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
    
    public double getTotalPrice() { 
    	return totalPrice; 
    }
    
    public void setTotalPrice(double totalPrice) { 
    	this.totalPrice = totalPrice; 
    }
    
    public Integer getPaymentStatus() { 
    	return paymentStatus; 
    }
    
    public void setPaymentStatus(Integer paymentStatus) { 
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
    
    public ContractDetails getContractDetail() {
        return contractDetail;
    }

    public void setContractDetail(ContractDetails contractDetail) {
        this.contractDetail = contractDetail;
    }
    public String getPaymentStatusText() {
        return paymentStatusText;
    }

    public void setPaymentStatusText(String paymentStatusText) {
        this.paymentStatusText = paymentStatusText;
    }

    public String getContractStatusText() {
        return contractStatusText;
    }
    
    public void setContractStatusText(String contractStatusText) {
        this.contractStatusText = contractStatusText;
    }
    
    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
