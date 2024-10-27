package net.codejava.model;

public class Services {
    private int serviceId;
    private String serviceName;
    private String serviceDescription;
    private int servicePrice;
    private int serviceStatus;
    private String serviceImage;

    public Services() {
		super();
	}

	public Services(int serviceId, String serviceName, String serviceDescription, int servicePrice, int serviceStatus,
			String serviceImage) {
		super();
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.serviceDescription = serviceDescription;
		this.servicePrice = servicePrice;
		this.serviceStatus = serviceStatus;
		this.serviceImage = serviceImage;
	}
	
    public int getServiceId() { 
    	return serviceId; 
    	}
    
    public void setServiceId(int serviceId) { 
    	this.serviceId = serviceId; 
    	}
    
    public String getServiceName() { 
    	return serviceName; 
    	}
    
    public void setServiceName(String serviceName) { 
    	this.serviceName = serviceName; 
    	}
    
    public String getServiceDescription() { 
    	return serviceDescription;
    	}
    
    public void setServiceDescription(String serviceDescription) {
    	this.serviceDescription = serviceDescription; 
    	}
    
    public int getServicePrice() { 
    	return servicePrice; 
    	}
    
    public void setServicePrice(int servicePrice) { 
    	this.servicePrice = servicePrice;
    	}
    
    public int getServiceStatus() { 
    	return serviceStatus; 
    	}
    
    public void setServiceStatus(int serviceStatus) {
    	this.serviceStatus = serviceStatus; 
    	}
    
    public String getServiceImage() { 
    	return serviceImage; 
    	}
    public void setServiceImage(String serviceImage) {
    	this.serviceImage = serviceImage; 
    	}
}