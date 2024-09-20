package net.codejava.model;

import java.util.Date;

public class EmployeeContractSchedule {
    private int scheduleId;
    private int contractDetailId; 
    private Date workDate;
    private String startTime;
    private String endTime;
    private int hoursWorked;
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
    public Date getWorkDate() {
    	return workDate; 
    	}
    public void setWorkDate(Date workDate) { 
    	this.workDate = workDate; 
    	}
    public String getStartTime() {
    	return startTime; 
    	}
    public void setStartTime(String startTime) { 
    	this.startTime = startTime; 
    	}
    public String getEndTime() { 
    	return endTime;
    	}
    public void setEndTime(String endTime) {
    	this.endTime = endTime;
    	}
    public int getHoursWorked() { 
    	return hoursWorked; 
    	}
    public void setHoursWorked(int hoursWorked) { 
    	this.hoursWorked = hoursWorked; 
    	}
    public int getStatus() { 
    	return status; 
    	}
    public void setStatus(int status) { 
    	this.status = status;
    	}
}
