package net.codejava.controller;

import net.codejava.model.Services;
import net.codejava.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public String listServices(Model model) {
        List<Services> services = serviceService.getAllServices();
        model.addAttribute("services", services);
        return "admin/service/service_list";
    }

    @GetMapping("/{id}")
    public String getServiceById(@PathVariable int id, Model model) {
        Services service = serviceService.getServiceById(id);
        model.addAttribute("service", service);
        return "admin/service/service_detail";
    }

    @GetMapping("/add")
    public String addServiceForm(Model model) {
        model.addAttribute("service", new Services());
        return "admin/service/service_add";
    }

    @PostMapping("/add")
    public String addService(@ModelAttribute Services service) {
        serviceService.addService(service);
        return "redirect:/admin/services";
    }

    @GetMapping("/edit/{id}")
    public String editServiceForm(@PathVariable int id, Model model) {
        Services service = serviceService.getServiceById(id);
        model.addAttribute("service", service);
        return "admin/service/service_edit";
    }

    @PostMapping("/edit/{id}")
    public String editService(@PathVariable int id, @ModelAttribute Services service) {
        service.setServiceId(id);
        serviceService.updateService(service);
        return "redirect:/admin/services";
    }

    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable int id) {
        serviceService.deleteService(id);
        return "redirect:/admin/services";
    }
}