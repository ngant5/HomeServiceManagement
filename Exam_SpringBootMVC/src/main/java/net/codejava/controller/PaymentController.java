package net.codejava.controller;

import net.codejava.model.Payments;
import net.codejava.service.PaymentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/contracts/{contractId}/payment")
public class PaymentController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @GetMapping("/list")
    public String getPaymentsByContractId(@PathVariable("contractId") int contractId, Model model) {
        List<Payments> payments = paymentService.getPaymentsByContractId(contractId);
        model.addAttribute("payments", payments);
        model.addAttribute("contractId", contractId);
        return "customer/payment/payment-list.html";  
    }


    @PostMapping("/create")
    public String createPayment(@PathVariable("contractId") int contractId,
                                @RequestParam("amount") BigDecimal amount,
                                @RequestParam("paymentMethod") String paymentMethod) {
    	logger.info("Received payment request: contractId={}, amount={}, paymentMethod={}", contractId, amount, paymentMethod);


        String transactionId = "TXN" + System.currentTimeMillis();  // Generate transaction ID
        try {
            // Call the service to create the payment
            paymentService.createPayment(contractId, amount, paymentMethod, transactionId);
            logger.info("Payment created successfully with transaction ID: {}", transactionId);

            // Redirect to a success page or show a success message
            return "redirect:/contracts/" + contractId + "/payment/list";
        } catch (Exception e) {
            // Log lỗi khi tạo payment
            logger.error("Error creating payment for contractId: {} with amount: {} and payment method: {}", contractId, amount, paymentMethod, e);

            // Trả về trang lỗi
            return "redirect:/error";
        }

    }
}
