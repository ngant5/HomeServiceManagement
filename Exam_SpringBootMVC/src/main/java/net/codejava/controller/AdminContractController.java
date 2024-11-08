package net.codejava.controller;

import net.codejava.model.Contracts;
import net.codejava.model.Customers;
import net.codejava.model.Employees;
import net.codejava.model.Payments;
import net.codejava.service.ContractService;
import net.codejava.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin/contracts")
public class AdminContractController {

    private final ContractService contractService;
    private final CustomerService customerService;

    @Autowired
    public AdminContractController(ContractService contractService, CustomerService customerService) {
        this.contractService = contractService;
        this.customerService = customerService;
    }

    @GetMapping("/list")
    public String getAllContracts(Model model) {
        List<Contracts> contracts = contractService.getAllContracts();
        for (Contracts contract : contracts) {
            Customers customer = customerService.findById(contract.getCustomerId());
            contract.setCustomer(customer);
        }

        model.addAttribute("contracts", contracts);
        return "admin/contracts/con_list"; 
    }
    
    @PostMapping("/updateStatus/{contractId}")
    public String updateContractStatus(@PathVariable("contractId") int contractId,
                                       @RequestParam("status") int status) {
        contractService.updateContractStatus(contractId, status);
        return "redirect:/admin/contracts/list";
    }



    @GetMapping("/{contractId}")
    public String getContractById(@PathVariable int contractId, Model model) {
        Contracts contract = contractService.getContractById(contractId);
        if (contract == null) {
            model.addAttribute("errorMessage", "Contract Not Found with ID: " + contractId);
            return "error"; 
        }
        Customers customer = customerService.findById(contract.getCustomerId());
        if (customer == null) {
            model.addAttribute("errorMessage", "Customer Not Found for Contract ID: " + contractId);
            return "error"; 
        }

        contract.setCustomer(customer);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDate = contract.getCreatedAt().format(formatter);
        
        model.addAttribute("contract", contract);
        model.addAttribute("formattedCreatedAt", formattedDate);
        
        return "admin/contracts/detail"; 
    }

    @GetMapping("/{contractId}/payments")
    public List<Payments> getPaymentsByContract(@PathVariable int contractId) {
        return customerService.getPaymentsByContract(contractId);
    }

    @PostMapping("/{contractId}/reminder")
    public void sendPaymentReminder(@PathVariable int contractId) {
        customerService.sendPaymentReminder(contractId);
    }

    @PutMapping("/{contractId}/due-date")
    public void setPaymentDueDate(@PathVariable int contractId, @RequestParam LocalDate dueDate) {
        LocalDateTime dueDateTime = dueDate.atStartOfDay(); 
        customerService.setPaymentDueDate(contractId, dueDateTime);
    }

    @PutMapping("/{contractId}/employee")
    public void updateContractEmployee(@PathVariable int contractId, @RequestBody Employees employee) {
        int employeeId = employee.getEmployeeId(); 
        customerService.updateContractEmployee(contractId, employeeId);
    }
}
