package net.codejava.model;

import java.time.LocalDate;

public class ContractDetails {
    private int contractDetailId;
    private int contractId; 
    private int empServiceId; 
    private String serviceAddress;
    private String servicePhone;
    private LocalDate startDate;
    private LocalDate endDate;
    private int status;
    private int hoursWorked;
    private double totalPrice;
    private String contractType;
    private int employeeId;  

    
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
    public LocalDate getStartDate() { 
    	return startDate; 
    	}
    public void setStartDate(LocalDate startDate) { 
    	this.startDate = startDate;
    	}
    public LocalDate getEndDate() {
    	return endDate; 
    	}
    public void setEndDate(LocalDate endDate) { 
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
    
    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }
    
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
