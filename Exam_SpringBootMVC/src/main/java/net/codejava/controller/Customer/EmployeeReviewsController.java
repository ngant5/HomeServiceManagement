package net.codejava.controller.Customer;

import net.codejava.model.EmployeeReviews;
import net.codejava.model.Employees;
import net.codejava.service.EmployeeReviewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
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
        // Lấy danh sách các đánh giá nhân viên theo contractId
        List<EmployeeReviews> employeeReviews = employeeReviewsService.getReviewsByContractId(contractId);

        // Lấy danh sách emp_service_id theo contractId
        List<Integer> empServiceIds = employeeReviewsService.getEmpServiceIdsByContractId(contractId);
        
        Integer contractDetailId = employeeReviewsService.getContractDetailIdByContractId(contractId);


        // Danh sách lưu trữ tên nhân viên
        List<String> employeeNames = new ArrayList<>();
        System.out.println("Emp Service IDs: " + empServiceIds);
        // Lặp qua các empServiceIds và lấy thông tin tên nhân viên
        for (Integer serviceId : empServiceIds) {
            // Lấy tên nhân viên theo serviceId
            List<String> names = employeeReviewsService.getEmployeeNamesByServiceId(serviceId);
            System.out.println("Names for Service ID " + serviceId + ": " + names);

            employeeNames.addAll(names);  // Thêm tên nhân viên vào danh sách
        }
        Integer employeeId = empServiceIds.isEmpty() ? null : empServiceIds.get(0);

        // Thêm vào model để hiển thị trong view
        model.addAttribute("contractId", contractId);
        model.addAttribute("contractDetailId", contractDetailId);
        model.addAttribute("empServiceIds", empServiceIds);
        model.addAttribute("employeeReviews", employeeReviews);
        model.addAttribute("employeeNames", employeeNames); 
        model.addAttribute("employeeId", employeeId);

        return "customer/reviews/form";  // Trả về view HTML
    }
    
    @PostMapping("/employee-reviews/submit")
    @ResponseBody
    public String submitReview(@RequestParam(required = true) Integer contractDetailId,
            @RequestParam(required = true) Integer employeeId,
            @RequestParam(required = true) Integer rating,
            @RequestParam(required = true) String comment) {
    	if (contractDetailId == null || employeeId == null || rating == null || comment == null || rating < 1 || rating > 5) {
            return "Invalid data submitted!";
        }


        boolean exists = employeeReviewsService.existsByContractDetailIdAndEmployeeId(contractDetailId, employeeId);

        EmployeeReviews review = new EmployeeReviews();
        review.setContractDetailId(contractDetailId);
        review.setEmployeeId(employeeId);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());

        if (exists) {
            // Nếu có, thực hiện update
            employeeReviewsService.updateReview(review);
        } else {
            // Nếu không, tạo mới
            employeeReviewsService.saveReview(review);
        }

        // Trả về thông báo kết quả thành công
        return "Review submitted successfully!";
    }

}