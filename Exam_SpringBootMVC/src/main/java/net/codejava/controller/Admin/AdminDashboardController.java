package net.codejava.controller.Admin;

import net.codejava.model.Contracts;
import net.codejava.model.Customers;
import net.codejava.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final CustomerService customerService;

    @Autowired
    public AdminDashboardController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Customers> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);

        List<Contracts> contracts = customerService.getAllContracts();
        model.addAttribute("contracts", contracts);

        return "admin/dashboard";
    }

    @GetMapping("/tables")
    public String table() {
        return "admin/tables";
    }
    
    @GetMapping("/index")
    public String index() {
        return "admin/ad_index";
    }
}
