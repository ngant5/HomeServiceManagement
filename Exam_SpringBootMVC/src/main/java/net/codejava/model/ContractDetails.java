package net.codejava.model;

import java.time.LocalDateTime;

public class ContractDetails {
    private int contractDetailId;
    private int contractId; 
    private int empServiceId; 
    private String serviceAddress;
    private String servicePhone;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int status;
    private int hoursWorked;
    private double totalPrice;
    private int contractType;

    
    public int getContractDetailId() { 
    	return contractDetailId; 
    	}
    
    public void setContractDetailId(int contractDetailId) { 
    	this.contractDetailId = contractDetailId;
    	}
    
    public int getContractId() { 
    	return contractId; 
    	}
    public void setContractId(int contractId) { 
    	this.contractId = contractId; 
    	}
    public int getEmpServiceId() {
    	return empServiceId; 
    	}
    public void setEmpServiceId(int empServiceId) {
    	this.empServiceId = empServiceId; 
    	}
    public String getServiceAddress() { 
    	return serviceAddress; 
    	}
    public void setServiceAddress(String serviceAddress) {
    	this.serviceAddress = serviceAddress;
    	}
    public String getServicePhone() { 
    	return servicePhone; 
    	}
    public void setServicePhone(String servicePhone) { 
    	this.servicePhone = servicePhone; 
    	}
    public LocalDateTime getStartDate() { 
    	return startDate; 
    	}
    public void setStartDate(LocalDateTime startDate) { 
    	this.startDate = startDate;
    	}
    public LocalDateTime getEndDate() {
    	return endDate; 
    	}
    public void setEndDate(LocalDateTime endDate) { 
    	this.endDate = endDate;
    	}
    public int getStatus() { 
    	return status; 
    	}
    public void setStatus(int status) {
    	this.status = status; 
    	}
    public int getHoursWorked() {
    	return hoursWorked; 
    	}
    public void setHoursWorked(int hoursWorked) { 
    	this.hoursWorked = hoursWorked;
    	}
    public double getTotalPrice() { 
    	return totalPrice; 
    	}
    public void setTotalPrice(double totalPrice) { 
    	this.totalPrice = totalPrice;
    	}
    
    public int getContractType() {
        return contractType;
    }

    public void setContractType(int contractType) {
        this.contractType = contractType;
    }
}
