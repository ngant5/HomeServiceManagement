package net.codejava.model;

import java.util.Date;

public class ContractRefusals {
    private int refusalId;
    private int contractDetailId; 
    private String refusalReason;
    private int status;
    private Date refusalDate;
    private Date createdAt;

    
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
    public Date getRefusalDate() { 
    	return refusalDate; 
    	}
    public void setRefusalDate(Date refusalDate) { 
    	this.refusalDate = refusalDate;
    	}
    public Date getCreatedAt() {
    	return createdAt; 
    	}
    public void setCreatedAt(Date createdAt) { 
    	this.createdAt = createdAt;
    	}
}

