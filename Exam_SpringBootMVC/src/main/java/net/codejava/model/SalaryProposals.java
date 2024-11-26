package net.codejava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SalaryProposals {
    private int proposalId;
    private int employeeId; 
    private int proposedSalary;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int approvalStatus;
    private LocalDateTime createdAt;
    private String refusalReason;

    

	public SalaryProposals() {
		super();
	}
	public SalaryProposals(int proposalId, int employeeId, int proposedSalary, LocalDate fromDate, LocalDate toDate,
			int approvalStatus, LocalDateTime createdAt, String refusalReason) {
		super();
		this.proposalId = proposalId;
		this.employeeId = employeeId;
		this.proposedSalary = proposedSalary;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.approvalStatus = approvalStatus;
		this.createdAt = createdAt;
		this.refusalReason = refusalReason;
	}
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
    public LocalDate getFromDate() {
    	return fromDate; 
    	}
    public void setFromDate(LocalDate fromDate) { 
    	this.fromDate = fromDate; 
    	}
    public LocalDate getToDate() { 
    	return toDate; 
    	}
    public void setToDate(LocalDate toDate) { 
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
    public String getRefusalReason() {
		return refusalReason;
	}
	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}
}