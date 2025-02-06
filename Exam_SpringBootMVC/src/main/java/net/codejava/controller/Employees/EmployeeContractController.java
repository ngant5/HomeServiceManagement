package net.codejava.controller.Employees;
import net.codejava.model.Contracts;
import net.codejava.model.Customers;
import net.codejava.model.Employees;
import net.codejava.service.CustomerService;
import net.codejava.service.EmployeeContractService;
import net.codejava.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeContractController {

    @Autowired
    private EmployeeContractService contractService;
    @Autowired
    private CustomerService customerService; 
    @Autowired
    private EmployeeService empService;
    
    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/contractsList")
    public String showAllContracts(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                   @RequestParam(value = "startDate", required = false) String startDateStr,
                                   @RequestParam(value = "endDate", required = false) String endDateStr,
                                   Model model, HttpSession session) {
        Timestamp startTimestamp = null;
        Timestamp endTimestamp = null;

        try {
            Integer employeeId = (Integer) session.getAttribute("employeeId");
            Employees employee = (Employees) session.getAttribute("employee");

            if (employeeId == null) {
                return "redirect:/employees/login"; // Redirect if not logged in
            }

            if (startDateStr != null && endDateStr != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate startDate = LocalDate.parse(startDateStr, formatter);
                LocalDate endDate = LocalDate.parse(endDateStr, formatter);
                startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
                endTimestamp = Timestamp.valueOf(endDate.atTime(23, 59, 59));
            }

            List<Contracts> contracts = contractService.getContractsByEmployeeIdWithPagination(employeeId, page, size, startTimestamp, endTimestamp);
            int totalPages = contractService.getTotalPagesForEmployee(employeeId, size, startTimestamp, endTimestamp);
            List<Customers> customers = customerService.getAllCustomers();
            model.addAttribute("customers" ,customers);
            model.addAttribute("contracts", contracts);
            model.addAttribute("employee", employee);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("startDate", startDateStr);
            model.addAttribute("endDate", endDateStr);
            model.addAttribute("noResultsMessage", contracts.isEmpty() ? "No results found" : null);
        } catch (Exception ex) {
            model.addAttribute("error", "An error occurred: " + ex.getMessage());
        }

        return "employees/emp_contracts_list";
    }
    
    @PostMapping("/updateStatus/{contractId}")
    public String updateContractStatus(@PathVariable("contractId") int contractId,
                                       @RequestParam("status") int status) {
        contractService.updateContractStatus(contractId, status);
        return "redirect:/employees/contractsList";
    }

}

