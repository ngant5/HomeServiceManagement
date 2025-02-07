package net.codejava.model;

public class EmployeeServiceList {
    private String serviceName;
    private String details;
    private int status; 
    
    public EmployeeServiceList(String serviceName, String details, int status) {
        this.serviceName = serviceName;
        this.details = details;
        this.status = status;
    }

    // Getters and Setters
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
