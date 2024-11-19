package net.codejava.controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.codejava.model.Services;
import net.codejava.service.ServiceService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin/services")
public class AdminServiceController {

    @Autowired
    private ServiceService serviceService;

    private static final String UPLOAD_DIR = "uploads/";

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
    public String addService(@ModelAttribute Services service, @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        try {
            if (!imageFile.isEmpty()) {
                String imageName = saveImage(imageFile);
                service.setServiceImage(imageName);
            }
            serviceService.addService(service);
        } catch (IOException e) {
            model.addAttribute("error", "Failed to upload image.");
            return "admin/service/service_add";
        }
        return "redirect:/admin/services?success=add";
    }

    @GetMapping("/edit")
    public String editServiceForm(@RequestParam int id, Model model) {
        Services service = serviceService.getServiceById(id);
        model.addAttribute("service", service);
        return "admin/service/service_edit";
    }


    @PostMapping("/edit/{id}")
    public String editService(
            @PathVariable int id,
            @ModelAttribute Services service,
            @RequestParam("imageFile") MultipartFile imageFile) {

        // Retrieve the existing service from the database
        Services existingService = serviceService.getServiceById(id);

        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete the existing image before saving the new one
            deleteExistingImage(existingService.getServiceImage());

            // Handle image upload and set the new image path
            String newFileName = saveImageFile(imageFile);
            service.setServiceImage(newFileName);
        } else {
            // Keep the existing image if no new file was uploaded
            service.setServiceImage(existingService.getServiceImage());
        }

        // Set the ID to ensure the correct service is updated
        service.setServiceId(id);
        serviceService.updateService(service);

        return "redirect:/admin/services";
    }

    // Helper method to save the uploaded image and return its filename
    private String saveImageFile(MultipartFile imageFile) {
        String fileName = imageFile.getOriginalFilename();
        Path uploadPath = Paths.get(UPLOAD_DIR);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions here
        }
        return fileName;
    }


    private String saveImage(MultipartFile imageFile) throws IOException {
        String imageName = imageFile.getOriginalFilename();
        Path uploadPath = Paths.get(UPLOAD_DIR + imageName);
        imageFile.transferTo(uploadPath);
        return imageName;
    }

    private void deleteExistingImage(String imageName) {
        if (imageName != null && !imageName.isEmpty()) {
            File existingImage = new File(UPLOAD_DIR + imageName);
            if (existingImage.exists()) {
                existingImage.delete();
            }
        }
    }
    
    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable int id) {
        serviceService.deleteService(id);
        return "redirect:/admin/services?success=delete";
    }
}