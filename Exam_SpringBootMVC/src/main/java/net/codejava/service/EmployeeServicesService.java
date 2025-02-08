package net.codejava.service;

import net.codejava.model.Employees;
import net.codejava.model.EmployeeServiceList;
import net.codejava.model.EmployeeServices;
import net.codejava.repository.EmployeeServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmployeeServicesService {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeServicesService.class);

    @Autowired
    private EmployeeServicesRepository employeeServicesRepository; 
    

    public void save(EmployeeServices employeeService) {
        employeeServicesRepository.save(employeeService);
    }
    
    public boolean addEmployeeService(EmployeeServices employeeService) {
        if (employeeServicesRepository.existsByEmployeeIdAndServiceId(employeeService.getEmployeeId(), employeeService.getServiceId())) {
            return false; // Duplicate found
        }
        employeeServicesRepository.save(employeeService);
        return true;
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

    public EmployeeServices getEmployeeServiceById(int empServiceId) {
        EmployeeServices employeeService = employeeServicesRepository.findByEmpServiceId(empServiceId);
        if (employeeService == null) {
            logger.error("Không tìm thấy EmployeeService với ID: {}", empServiceId);
            throw new RuntimeException("Không tìm thấy EmployeeService với ID: " + empServiceId);
        }
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
    
    public int getEmpServiceIdFromContractDetailId(int contractDetailId) {
        return employeeServicesRepository.getEmpServiceIdFromContractDetailId(contractDetailId);
    }

    // Lấy service_id từ emp_service_id
    public int getServiceIdFromEmpServiceId(int empServiceId) {
        return employeeServicesRepository.getServiceIdFromEmpServiceId(empServiceId);
    }

    // Lấy danh sách nhân viên theo service_id
    public List<Employees> getEmployeesByServiceId(int serviceId) {
        return employeeServicesRepository.getEmployeesByServiceId(serviceId);
    }
    
    public List<Employees> getEmployeesByContractDetailId(int contractDetailId) {
        // Lấy danh sách EmployeeServices dựa trên contractDetailId
        List<EmployeeServices> employeeServicesList = employeeServicesRepository.findByContractDetailId(contractDetailId);
        
        // Tạo danh sách các nhân viên từ danh sách EmployeeServices
        List<Employees> employeesList = new ArrayList<>();
        for (EmployeeServices employeeServices : employeeServicesList) {
            if (employeeServices.getEmployee() != null) {
                employeesList.add(employeeServices.getEmployee());
            }
        }
        return employeesList;
    }
  
    public List<EmployeeServiceList> getEmployeeServices(Long employeeId) {
        return employeeServicesRepository.getEmployeeServices(employeeId);
    }
    
    public boolean updateServiceStatus(Long empServiceId, int status) {
        return employeeServicesRepository.updateServiceStatus(empServiceId, status);
    }

}