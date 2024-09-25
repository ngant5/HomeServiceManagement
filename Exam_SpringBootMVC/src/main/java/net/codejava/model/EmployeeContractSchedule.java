package net.codejava.model;

import java.time.LocalDateTime;

public class EmployeeContractSchedule {
    private int scheduleId;
    private int contractDetailId; 
    private int workDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime hoursWorked;
    private int status;

    
    public int getScheduleId() { 
    	return scheduleId; 
    	}
    public void setScheduleId(int scheduleId) { 
    	this.scheduleId = scheduleId; 
    	}
    public int getContractDetailId() {
    	return contractDetailId; 
    	}
    public void setContractDetailId(int contractDetailId) { 
    	this.contractDetailId = contractDetailId;
    	}
    public int getWorkDate() {
    	return workDate; 
    	}
    public void setWorkDate(int workDate) { 
    	this.workDate = workDate; 
    	}
    public LocalDateTime getStartTime() {
    	return startTime; 
    	}
    public void setStartTime(LocalDateTime startTime) { 
    	this.startTime = startTime; 
    	}
    public LocalDateTime getEndTime() { 
    	return endTime;
    	}
    public void setEndTime(LocalDateTime endTime) {
    	this.endTime = endTime;
    	}
    public LocalDateTime getHoursWorked() { 
    	return hoursWorked; 
    	}
    public void setHoursWorked(LocalDateTime hoursWorked) { 
    	this.hoursWorked = hoursWorked; 
    	}
    public int getStatus() { 
    	return status; 
    	}
    public void setStatus(int status) { 
    	this.status = status;
    	}
}
