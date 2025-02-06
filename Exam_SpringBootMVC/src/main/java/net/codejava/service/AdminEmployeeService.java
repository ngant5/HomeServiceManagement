package net.codejava.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.codejava.model.EmployeeReviews;
import net.codejava.model.Employees;
import net.codejava.repository.AdminEmployeeRepository;

@Service
public class AdminEmployeeService {
    private final AdminEmployeeRepository adminEmployeeRepository;

    @Autowired
    public AdminEmployeeService(AdminEmployeeRepository adminEmployeeRepository) {
        this.adminEmployeeRepository = adminEmployeeRepository;
    }
    
    public void updateExperienceYears(int employeeId, int experienceYears) {
        adminEmployeeRepository.updateExperienceYears(employeeId, experienceYears);
    }

    public void deleteEmployee(int employeeId, int status) {
    	adminEmployeeRepository.updateStatus(employeeId, status); 
    }
    
    public List<EmployeeReviews> findAllEmployeeReviews() {
   	 return adminEmployeeRepository.findAllEmployeeReviews();
   }
}
