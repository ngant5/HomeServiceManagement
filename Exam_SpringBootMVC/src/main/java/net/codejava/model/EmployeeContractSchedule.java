package net.codejava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmployeeContractSchedule {
    private int scheduleId;
    private int contractDetailId; 
    private int employeeId; 
    private LocalDate workDate;  
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int status;

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

    public int getStatus() { 
        return status; 
    }

    public void setStatus(int status) { 
        this.status = status;
    }
}
