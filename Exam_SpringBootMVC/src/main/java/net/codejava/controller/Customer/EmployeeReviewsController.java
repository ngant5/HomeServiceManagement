package net.codejava.controller.Customer;

import net.codejava.model.EmployeeReviews;
import net.codejava.model.Employees;
import net.codejava.service.EmployeeReviewsService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EmployeeReviewsController {

    private final EmployeeReviewsService employeeReviewsService;

    public EmployeeReviewsController(EmployeeReviewsService employeeReviewsService) {
        this.employeeReviewsService = employeeReviewsService;
    }

    @GetMapping("/employee-reviews/view/{contractId}")
    public String viewReviews(@PathVariable("contractId") int contractId, Model model) {
        List<EmployeeReviews> employeeReviews = employeeReviewsService.getReviewsByContractId(contractId);

        List<Integer> empServiceIds = employeeReviewsService.getEmpServiceIdsByContractId(contractId);
        
        Integer contractDetailId = employeeReviewsService.getContractDetailIdByContractId(contractId);


        List<String> employeeNames = new ArrayList<>();
        System.out.println("Emp Service IDs: " + empServiceIds);
        for (Integer serviceId : empServiceIds) {
            List<String> names = employeeReviewsService.getEmployeeNamesByServiceId(serviceId);
            System.out.println("Names for Service ID " + serviceId + ": " + names);

            employeeNames.addAll(names);  
        }
        int employeeId = empServiceIds.isEmpty() ? 0 : empServiceIds.get(0); 
        boolean hasReview = false;
        EmployeeReviews existingReview = null;

        for (EmployeeReviews review : employeeReviews) {
            if (review.getEmployeeId() == employeeId) {  
                hasReview = true;
                existingReview = review;
                break;
            }
        }

        model.addAttribute("contractId", contractId);
        model.addAttribute("contractDetailId", contractDetailId);
        model.addAttribute("empServiceIds", empServiceIds);
        model.addAttribute("employeeReviews", employeeReviews);
        model.addAttribute("employeeNames", employeeNames); 
        model.addAttribute("employeeId", employeeId);
        
        model.addAttribute("hasReview", hasReview); 
        model.addAttribute("existingReview", existingReview);
        return "customer/reviews/form";  
    }
    
    @PostMapping("/employee-reviews/submit")
    @ResponseBody
    public ResponseEntity<?> submitReview(
	    		@RequestParam(required = true) Integer contractDetailId,
	            @RequestParam(required = true) Integer employeeId,
	            @RequestParam(required = true) Integer rating,
	            @RequestParam(required = true) String comment) {
		    	
    			if (contractDetailId == null || employeeId == null || rating == null || comment == null || rating < 1 || rating > 5) {
    				return ResponseEntity.badRequest().body("Invalid data submitted!");
		        }


        boolean exists = employeeReviewsService.existsByContractDetailIdAndEmployeeId(contractDetailId, employeeId);

        EmployeeReviews review = new EmployeeReviews();
        review.setContractDetailId(contractDetailId);
        review.setEmployeeId(employeeId);
        review.setRating(rating);
        review.setComment(comment);
        
        LocalDateTime localDateTime = LocalDateTime.now();  
        java.util.Date utilDate = java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());  
        review.setCreatedAt(sqlDate);
        
        if (exists) {
            employeeReviewsService.updateReview(review);
            return ResponseEntity.ok("Review updated successfully!");
        } else {
            employeeReviewsService.saveReview(review);
            return ResponseEntity.ok("Review submitted successfully!");
        }
    }
    
    @GetMapping("/employee-reviews/all")
    public String getAllReviews(Model model) {
        List<EmployeeReviews> allReviews = employeeReviewsService.getAllReviews();
        
        System.out.println("All Reviews: " + allReviews);

        model.addAttribute("allReviews", allReviews);
        
        return "admin/customers/cus_review"; 
    }


}