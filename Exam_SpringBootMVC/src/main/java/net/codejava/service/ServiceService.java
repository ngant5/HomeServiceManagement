package net.codejava.service;

import net.codejava.model.Services;
import net.codejava.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Services> getAllServices() {
        return serviceRepository.findAll();
    }

    public Services getServiceById(int id) {
        Services service = serviceRepository.findById(id).orElse(null);
        if (service == null) {
            System.out.println("No service found with ID: " + id);
        } else {
            System.out.println("Service found: " + service.getServiceName());
        }
        return service;
    }


    public void addService(Services service) {
        serviceRepository.save(service);
    }

    public void updateService(Services service) {
        serviceRepository.update(service);
    }

    public void deleteService(int id) {
        serviceRepository.deleteById(id);
    }
    
    
}
