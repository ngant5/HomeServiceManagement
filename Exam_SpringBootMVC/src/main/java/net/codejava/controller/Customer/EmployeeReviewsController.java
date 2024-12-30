package net.codejava.controller.Customer;

import net.codejava.model.EmployeeReviews;
import net.codejava.service.EmployeeReviewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
public class EmployeeReviewsController {

    @Autowired
    private EmployeeReviewsService employeeReviewsService;

    // Tạo hoặc cập nhật đánh giá
    @PostMapping("/saveOrUpdate/{contractDetailId}")
    public String saveOrUpdateReview(@PathVariable int contractDetailId,
                                     @RequestParam int rating,
                                     @RequestParam String comment,
                                     Model model) {
        EmployeeReviews employeeReviews = employeeReviewsService.saveOrUpdateReview(contractDetailId, rating, comment);
        model.addAttribute("employeeReviews", employeeReviews);
        return "redirect:/contractDetails/" + contractDetailId; // Redirect lại về trang chi tiết hợp đồng
    }

    // Xem thông tin đánh giá
    @GetMapping("/{contractDetailId}")
    public String getReview(@PathVariable int contractDetailId, Model model) {
        EmployeeReviews employeeReviews = employeeReviewsService.getReviewByContractDetailId(contractDetailId);
        model.addAttribute("employeeReviews", employeeReviews);
        return "reviews/view"; // Trang để hiển thị đánh giá
    }
}
