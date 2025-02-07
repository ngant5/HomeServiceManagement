package net.codejava.model;

public class EmployeeServiceList {
    private Long empServiceId;
    private Long employeeId;
    private Long serviceId;
    private String details;
    private int status;
    private String serviceName;
    private String serviceDescription;

    // Constructor không tham số
    public EmployeeServiceList() {}

    // Constructor với tất cả tham số
    public EmployeeServiceList(Long empServiceId, Long employeeId, Long serviceId, String details, int status, String serviceName, String serviceDescription) {
        this.empServiceId = empServiceId;
        this.employeeId = employeeId;
        this.serviceId = serviceId;
        this.details = details;
        this.status = status;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
    }

    // Getter và Setter cho empServiceId
    public Long getEmpServiceId() {
        return empServiceId;
    }

    public void setEmpServiceId(Long empServiceId) {
        this.empServiceId = empServiceId;
    }

    // Getter và Setter cho employeeId
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    // Getter và Setter cho serviceId
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    // Getter và Setter cho details
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // Getter và Setter cho status
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // Getter và Setter cho serviceName
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    // Getter và Setter cho serviceDescription
    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    @Override
    public String toString() {
        return "EmployeeServiceDto{" +
                "empServiceId=" + empServiceId +
                ", employeeId=" + employeeId +
                ", serviceId=" + serviceId +
                ", details='" + details + '\'' +
                ", status=" + status +
                ", serviceName='" + serviceName + '\'' +
                ", serviceDescription='" + serviceDescription + '\'' +
                '}';
    }
}

