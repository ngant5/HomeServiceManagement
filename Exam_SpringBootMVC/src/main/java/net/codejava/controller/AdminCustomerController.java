package net.codejava.controller;

import net.codejava.model.Contracts;
import net.codejava.model.Customers;
import net.codejava.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/customers")
public class AdminCustomerController {

    private final CustomerService customerService;

    @Autowired
    public AdminCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/list")
    public String getAllCustomers(Model model) {
        List<Customers> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "admin/customers/cus_list"; 
    }

    @GetMapping("/{customerId}")
    public String getCustomerContracts(@PathVariable int customerId, Model model) {
        List<Contracts> contracts = customerService.getCustomerContracts(customerId);
        if (contracts == null) {
            model.addAttribute("errorMessage", "No contracts found for this customer.");
            return "error"; 
        }
        
        model.addAttribute("contracts", contracts);
        return "admin/customers/contracts_by_cus"; 
    }
}
