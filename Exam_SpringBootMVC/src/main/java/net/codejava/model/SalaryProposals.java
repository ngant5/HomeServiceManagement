package net.codejava.model;

import java.time.LocalDateTime;

public class SalaryProposals {
    private int proposalId;
    private int employeeId; 
    private int proposedSalary;
    private int fromDate;
    private int toDate;
    private int approvalStatus;
    private LocalDateTime createdAt;

    
    public int getProposalId() { 
    	return proposalId; 
    	}
    public void setProposalId(int proposalId) {
    	this.proposalId = proposalId; 
    	}
    public int getEmployeeId() {
    	return employeeId; 
    	}
    public void setEmployeeId(int employeeId) { 
    	this.employeeId = employeeId;
    	}
    public int getProposedSalary() { 
    	return proposedSalary; 
    	}
    public void setProposedSalary(int proposedSalary) {
    	this.proposedSalary = proposedSalary;
    	}
    public int getFromDate() {
    	return fromDate; 
    	}
    public void setFromDate(int fromDate) { 
    	this.fromDate = fromDate; 
    	}
    public int getToDate() { 
    	return toDate; 
    	}
    public void setToDate(int toDate) { 
    	this.toDate = toDate; 
    	}
    public int getApprovalStatus() { 
    	return approvalStatus; 
    	}
    public void setApprovalStatus(int approvalStatus) {
    	this.approvalStatus = approvalStatus; 
    	}
    public LocalDateTime getCreatedAt() { 
    	return createdAt; 
    	}
    public void setCreatedAt(LocalDateTime createdAt) {
    	this.createdAt = createdAt;
    	}
}

