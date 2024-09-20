package net.codejava.model;

import java.util.Date;

public class ContractDetails {
    private int contractDetailId;
    private int contractId; 
    private int empServiceId; 
    private String serviceAddress;
    private String servicePhone;
    private Date startDate;
    private Date endDate;
    private int status;
    private int hoursWorked;
    private int totalPrice;

    
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
    public Date getStartDate() { 
    	return startDate; 
    	}
    public void setStartDate(Date startDate) { 
    	this.startDate = startDate;
    	}
    public Date getEndDate() {
    	return endDate; 
    	}
    public void setEndDate(Date endDate) { 
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
    public int getTotalPrice() { 
    	return totalPrice; 
    	}
    public void setTotalPrice(int totalPrice) { 
    	this.totalPrice = totalPrice;
    	}
}
