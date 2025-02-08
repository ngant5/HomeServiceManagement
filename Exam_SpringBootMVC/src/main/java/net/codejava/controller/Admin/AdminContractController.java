package net.codejava.controller.Admin;

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
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    public String getAllContracts(@RequestParam(value = "page", defaultValue = "1") int page,
            					  @RequestParam(value = "size", defaultValue = "10") int size,
            			          @RequestParam(value = "startDate", required = false) String startDateStr,
            			          @RequestParam(value = "endDate", required = false) String endDateStr,
            			          HttpServletRequest request, Model model) {
    	Timestamp startTimestamp = null;
        Timestamp endTimestamp = null;
        if (startDateStr != null && endDateStr != null) {
            try {
                // Chuyển đổi chuỗi ngày tháng từ form thành LocalDate
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate startDate = LocalDate.parse(startDateStr, formatter);
                LocalDate endDate = LocalDate.parse(endDateStr, formatter);

                // Chuyển đổi LocalDate thành Timestamp
                startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
                endTimestamp = Timestamp.valueOf(endDate.atTime(23, 59, 59));
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Invalid date format");
                return "error";
            }
        }
        
        List<Contracts> contracts = contractService.getContractsWithPagination(page, size, startTimestamp, endTimestamp);
        
        for (Contracts contract : contracts) {
            Customers customer = customerService.findById(contract.getCustomerId());
            contract.setCustomer(customer);
        }
        int totalPages = contractService.getTotalPages(size, startTimestamp, endTimestamp);
        

        // Thêm vào model để hiển thị trên giao diện
        model.addAttribute("contracts", contracts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startDate", startDateStr);
        model.addAttribute("endDate", endDateStr);
        model.addAttribute("noResultsMessage", contracts.isEmpty() ? "No results found" : null);
        

        if (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            // Trả về fragment hợp đồng nếu có dữ liệu
        	if (contracts != null && !contracts.isEmpty()) {
        	    System.out.println("Returning fragment: contractTable");
        	    return "admin/contracts/partials/search_have_results :: contractTable";
        	} else {
        	    System.out.println("Returning fragment: contractNoTable");
        	    return "admin/contracts/partials/search_no_result :: contractNoTable";
        	}
        }
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
    private static final String FILE_DIRECTORY = "uploads/contracts/";
    
 
    @PostMapping("/{contractId}/uploadFile")
    public ResponseEntity<String> uploadContractFile(@PathVariable("contractId") Long contractId,  
                                                     @RequestParam("contractFile") MultipartFile file) 
    	throws IOException {
    	    if (file.isEmpty()) {
    	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    	                             .body("Please select a file to upload");
    	    }

        try {
        	Path path = Paths.get(FILE_DIRECTORY + contractId + ".pdf");
            Files.createDirectories(path.getParent()); 
            file.transferTo(path); 

            contractService.uploadContractFile(file, contractId.intValue());  
			/* contractService.updateContractStatus(contractId.intValue(), 3); */
            return ResponseEntity.ok("File uploaded successfully!!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error occurred while uploading the file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to upload the file: " + e.getMessage());
        }
    }

   
    @GetMapping("/view-file/{contractId}")
    public ResponseEntity<Resource> viewFile(@PathVariable Long contractId) throws Exception {
       
        String fileName = contractId + ".pdf"; 
        Path filePath = Paths.get(FILE_DIRECTORY + fileName);
        
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")  
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")  
                .body(resource);
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
    
    
    @GetMapping("/new")
    public String getAllContracts(HttpServletRequest request, Model model) {
        // Lấy tất cả hợp đồng có status = 0
        List<Contracts> contracts = contractService.getContractsByStatus(0);
        
        for (Contracts contract : contracts) {
            Customers customer = customerService.findById(contract.getCustomerId());
            contract.setCustomer(customer);
        }

        model.addAttribute("contracts", contracts);
        model.addAttribute("noResultsMessage", contracts.isEmpty() ? "No results found" : null);

        if (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            if (contracts != null && !contracts.isEmpty()) {
                return "admin/contracts/partials/search_have_results :: contractTable";
            } else {
                return "admin/contracts/partials/search_no_result :: contractNoTable";
            }
        }

        return "admin/contracts/con_new";
    }

   
}