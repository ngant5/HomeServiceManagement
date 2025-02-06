package net.codejava.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class EmployeeSchedule {
	private int scheduleId;
    private String fullname;
    private String phone;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private int status; 
    
    public EmployeeSchedule(int scheduleId, String fullname, String phone, LocalDate workDate, LocalTime startTime, LocalTime endTime, int status) {
    	this.scheduleId = scheduleId;
    	this.fullname = fullname;
        this.phone = phone;
        this.workDate = workDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
