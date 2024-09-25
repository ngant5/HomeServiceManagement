package net.codejava.model;

import java.time.LocalDateTime;

public class ContractRefusals {
    private int refusalId;
    private int contractDetailId; 
    private String refusalReason;
    private int status;
    private LocalDateTime refusalDate;
    private LocalDateTime createdAt;

    
    public int getRefusalId() { 
    	return refusalId; 
    	}
    public void setRefusalId(int refusalId) { 
    	this.refusalId = refusalId;
    	}
    public int getContractDetailId() { 
    	return contractDetailId;
    	}
    public void setContractDetailId(int contractDetailId) { 
    	this.contractDetailId = contractDetailId;
    	}
    public String getRefusalReason() {
    	return refusalReason; 
    	}
    public void setRefusalReason(String refusalReason) {
    	this.refusalReason = refusalReason; 
    	}
    public int getStatus() {
    	return status;
    	}
    public void setStatus(int status) { 
    	this.status = status;
    	}
    public LocalDateTime getRefusalDate() { 
    	return refusalDate; 
    	}
    public void setRefusalDate(LocalDateTime refusalDate) { 
    	this.refusalDate = refusalDate;
    	}
    public LocalDateTime getCreatedAt() {
    	return createdAt; 
    	}
    public void setCreatedAt(LocalDateTime createdAt) { 
    	this.createdAt = createdAt;
    	}
}

