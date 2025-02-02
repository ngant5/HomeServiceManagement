package net.codejava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EmployeeContractSchedule {
    private int scheduleId;
    private int contractDetailId; 
    private int employeeId; 
    private LocalDate workDate;  
    private LocalTime startTime;
    private LocalTime endTime;
    private int hoursWork;
    private int status;
    private LocalDateTime expireTime;

    
    public String getStartTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return startTime.format(formatter); 
    }

    public String getEndTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return endTime.format(formatter); 
    }
    // Getters and Setters
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

    public int getEmployeeId() { 
        return employeeId; 
    }

    public void setEmployeeId(int employeeId) { 
        this.employeeId = employeeId;
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
    
    public int getHoursWork() {
        return hoursWork;
    }

    public void setHoursWork(int hoursWork) {
        this.hoursWork = hoursWork;
    }


    public int getStatus() { 
        return status; 
    }

    public void setStatus(int status) { 
        this.status = status;
    }
    
    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
