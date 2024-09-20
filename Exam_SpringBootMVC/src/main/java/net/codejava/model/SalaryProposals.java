package net.codejava.model;

import java.util.Date;

public class SalaryProposals {
    private int proposalId;
    private int employeeId; 
    private int proposedSalary;
    private Date fromDate;
    private Date toDate;
    private int approvalStatus;
    private Date createdAt;

    
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
    public Date getFromDate() {
    	return fromDate; 
    	}
    public void setFromDate(Date fromDate) { 
    	this.fromDate = fromDate; 
    	}
    public Date getToDate() { 
    	return toDate; 
    	}
    public void setToDate(Date toDate) { 
    	this.toDate = toDate; 
    	}
    public int getApprovalStatus() { 
    	return approvalStatus; 
    	}
    public void setApprovalStatus(int approvalStatus) {
    	this.approvalStatus = approvalStatus; 
    	}
    public Date getCreatedAt() { 
    	return createdAt; 
    	}
    public void setCreatedAt(Date createdAt) {
    	this.createdAt = createdAt;
    	}
}

