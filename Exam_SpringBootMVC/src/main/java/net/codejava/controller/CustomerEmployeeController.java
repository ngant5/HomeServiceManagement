package net.codejava.controller;

import net.codejava.model.EmployeeServices;
import net.codejava.model.Employees;
import net.codejava.model.Services;
import net.codejava.repository.EmployeeServicesRepository;
import net.codejava.service.EmployeeService;
import net.codejava.service.EmployeeServicesService;
import net.codejava.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employee")
public class CustomerEmployeeController {
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceController.class);

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private EmployeeServicesService employeeServicesService;
    
    @Autowired
    private ServiceService serviceService; 
    
    @Autowired
    private EmployeeServicesRepository employeeServicesRepository; 
    
    @GetMapping("")
    public String listEmployees(Model model, HttpSession session) {
        List<Employees> employees = employeeService.findAllEmployees();
        model.addAttribute("employees", employees);
        if (!employees.isEmpty()) {
            Employees firstEmployee = employees.get(0);
            session.setAttribute("employee", firstEmployee);
        }

        return "customer/employee/list";
    }

    @GetMapping("/{id}")
    public String getService(@PathVariable("id") int id, Model model, HttpSession session) {
        logger.info("Fetching services for employee with ID: {}", id);
        
        // Lấy thông tin nhân viên từ session
        Employees employee = (Employees) session.getAttribute("employee");

        // Kiểm tra nếu employee không có trong session
        if (employee == null) {
            logger.warn("No employee found in session.");
            return "redirect:/error"; // Chuyển hướng đến trang lỗi hoặc trang khác
        }

        // Lưu fullname của nhân viên vào session
        session.setAttribute("fullname", employee.getFullname());
        logger.debug("Fullname of employee saved in session: {}", employee.getFullname());


        // Lấy danh sách dịch vụ của nhân viên
        List<EmployeeServices> employeeServices = employeeServicesService.findByEmployeeId(id);
        logger.debug("Number of services found for employee ID {}: {}", id, employeeServices.size());
        

        if (employeeServices.isEmpty()) {
            logger.warn("No services found for employee ID: {}", id);
            model.addAttribute("message", "Không có dịch vụ nào cho nhân viên này.");
        } else {

        for (EmployeeServices empService : employeeServices) {
        	logger.info("Fetching service with ID: {}", empService.getServiceId());

            Services service = serviceService.getServiceById(empService.getServiceId());
            empService.setService(service); 
            if (service != null) {
                logger.info("Service found: {} - Price: {} - Description: {}", service.getServiceName(), service.getServicePrice(), service.getServiceDescription());
            } else {
                logger.info("No service found for empService.serviceId: {}", empService.getServiceId());
            }
        }
     }
        model.addAttribute("employee", employee); 
        model.addAttribute("employeeServices", employeeServices);

        return "customer/employee/select-service"; 
    }

}
