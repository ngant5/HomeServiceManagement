package net.codejava.service;

import net.codejava.model.Employees;
import net.codejava.model.EmployeeServices;
import net.codejava.repository.EmployeeServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServicesService {

    @Autowired
    private EmployeeServicesRepository employeeServicesRepository; 

    public void save(EmployeeServices employeeService) {
        employeeServicesRepository.save(employeeService);
    }

    public EmployeeServices findById(int empServiceId) {
        return employeeServicesRepository.findById(empServiceId);
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
}
