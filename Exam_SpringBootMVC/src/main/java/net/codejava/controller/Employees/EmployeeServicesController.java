package net.codejava.controller.Employees;

import net.codejava.model.EmployeeServices;
import net.codejava.model.Employees;
import net.codejava.model.Services;
import net.codejava.service.EmployeeService;
import net.codejava.service.EmployeeServicesService;
import net.codejava.service.ServiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/employees/services")
public class EmployeeServicesController {

    @Autowired
    private EmployeeServicesService employeeServicesService;
    
    @Autowired
    private ServiceService serviceService;
    

    @GetMapping
    public String listEmployeeServices(Model model) {
        List<EmployeeServices> employeeServices = employeeServicesService.findAll();
        model.addAttribute("employeeServices", employeeServices);
        return "/employee_service/employee_service_list";
    }

    @GetMapping("/add")
    public String addEmployeeServiceForm(Model model, HttpSession session) {
        model.addAttribute("employeeService", new EmployeeServices());
        List<Services> services = serviceService.getAllServices();
        model.addAttribute("services", services);
        Employees employee = (Employees) session.getAttribute("employee");
        model.addAttribute("employee", employee);
        return "/employees/emp_update_info";
    }

    @PostMapping("/add")
    public String addEmployeeService(@ModelAttribute EmployeeServices employeeService, Model model, HttpSession session) {
        boolean isAdded = employeeServicesService.addEmployeeService(employeeService);
        if (!isAdded) {
            // If the service is already assigned, set an error message and reload the form
            model.addAttribute("errorMessage", "This service is already assigned to the employee.");
            
            // Reload employee and service information for the form
            List<Services> services = serviceService.getAllServices();
            model.addAttribute("services", services);
            Employees employee = (Employees) session.getAttribute("employee");
            model.addAttribute("employee", employee);
            
            return "/employees/emp_update_info";
        }
        return "redirect:/employees/updateInfo"; 
    }


    @GetMapping("/edit/{id}")
    public String editEmployeeServiceForm(@PathVariable int id, Model model) {
        EmployeeServices employeeService = employeeServicesService.findById(id);
        model.addAttribute("employeeService", employeeService);
        return "/employee_service/employee_service_edit";
    }

    @PostMapping("/edit/{id}")
    public String editEmployeeService(@PathVariable int id, @ModelAttribute EmployeeServices employeeService) {
        employeeService.setEmpServiceId(id);
        employeeServicesService.update(employeeService);
        return "redirect://employees/services?success=update";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployeeService(@PathVariable int id) {
        employeeServicesService.deleteById(id);
        return "redirect://employees/services?success=delete";
    }
}