package net.codejava.controller;

import net.codejava.model.EmployeeServices;
import net.codejava.model.Employees;
import net.codejava.model.Services;
import net.codejava.service.EmployeeServicesService;
import net.codejava.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/customer/services")
public class CustomerServiceController {

    @Autowired
    private ServiceService serviceService;
    
    @Autowired
    private EmployeeServicesService employeeServicesService;

    @GetMapping("")
    public String listServices(Model model) {
        List<Services> services = serviceService.getAllServices();
        model.addAttribute("services", services);
        return "customer/service/list";
    }

    @GetMapping("/{id}")
    public String getService(@PathVariable("id") int id, Model model, HttpSession session) {
        Services service = serviceService.getServiceById(id);
        if (service != null) {
            List<EmployeeServices> employeeServices = employeeServicesService.findByServiceId(id);
            
            // Thêm logic để lấy thông tin nhân viên cho mỗi dịch vụ
            for (EmployeeServices employeeService : employeeServices) {
                Employees employee = employeeServicesService.getEmployeeInfo(employeeService.getEmployeeId());
                employeeService.setEmployee(employee); // Thêm setter cho Employee vào EmployeeServices
            }

            session.setAttribute("serviceId", id);
            
            model.addAttribute("service", service);
            model.addAttribute("employeeServices", employeeServices);
            return "customer/service/select-employee"; // Tên view
        } else {
            return "error"; // Trang lỗi
        }
    }

}