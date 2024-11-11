package net.codejava.service;

import net.codejava.model.Employees;
import net.codejava.model.EmployeeServices;
import net.codejava.repository.EmployeeServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmployeeServicesService {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeServices.class);

    @Autowired
    private EmployeeServicesRepository employeeServicesRepository; 
    

    public void save(EmployeeServices employeeService) {
        employeeServicesRepository.save(employeeService);
    }
    
    public List<EmployeeServices> findByEmployeeId(int employeeId) {
        return employeeServicesRepository.findByEmployeeId(employeeId);
    }
    
    public EmployeeServices findById(int empServiceId) {
        EmployeeServices employeeService = employeeServicesRepository.findById(empServiceId);
        if (employeeService == null) {
            logger.error("EmployeeService not found with ID: {}", empServiceId);
            throw new RuntimeException("EmployeeService not found with ID: " + empServiceId);
        }
        logger.info("Found EmployeeService: {}", employeeService.getEmpServiceId());
        return employeeService;
    }


    public List<EmployeeServices> findAll() {
        return employeeServicesRepository.findAll();
    }

    public void update(EmployeeServices employeeService) {
        employeeServicesRepository.update(employeeService);
    }

    public void deleteById(int empServiceId) {
        employeeServicesRepository.deleteById(empServiceId);
    }
    
    public List<EmployeeServices> findByServiceId(int serviceId) {
        return employeeServicesRepository.findByServiceId(serviceId);
    }


    public Employees getEmployeeInfo(int employeeId) {
        return employeeServicesRepository.findEmployeeByEmployeeId(employeeId);
    }
    
    public EmployeeServices findByEmpServiceId(int empServiceId) {
        return employeeServicesRepository.findByEmpServiceId(empServiceId);
    }
}