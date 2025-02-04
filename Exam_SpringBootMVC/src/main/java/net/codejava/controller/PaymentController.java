package net.codejava.controller;

import net.codejava.model.Payments;
import net.codejava.service.ContractService;
import net.codejava.service.PaymentService;
import net.codejava.service.VNPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/contracts/{contractId}/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private PaymentService paymentService;
    private VNPayService vnPayService;
    @Autowired
    private ContractService contractService;

    @Autowired
    public PaymentController(PaymentService paymentService, VNPayService vnPayService) {
        this.paymentService = paymentService;
        this.vnPayService = vnPayService;
    }

    // Lấy danh sách các khoản thanh toán của hợp đồng
    @GetMapping("/list")
    public String getPaymentsByContractId(@PathVariable("contractId") int contractId, Model model) {
        List<Payments> payments = paymentService.getPaymentsByContractId(contractId);
        model.addAttribute("payments", payments);
        model.addAttribute("contractId", contractId);
        return "customer/payment/payment-list.html";
    }

    // Tạo thanh toán mới và tích hợp với VNPay
    @PostMapping("/create")
    public String createPayment(@PathVariable("contractId") int contractId,
                                @RequestParam("amount") BigDecimal amount,
                                @RequestParam("paymentMethod") String paymentMethod,
                                HttpServletRequest request) {
        logger.info("Nhận yêu cầu thanh toán: contractId={}, amount={}, paymentMethod={}", contractId, amount, paymentMethod);
        String orderInfo = "Thanh toán cho hợp đồng " + contractId;

        if ("VNPAY".equalsIgnoreCase(paymentMethod)) {
            // Tạo URL thanh toán VNPay và chuyển hướng người dùng
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder(amount.intValue(), "Payment for contract " + contractId, baseUrl);

            // Chuyển hướng đến trang thanh toán VNPay
            return "redirect:" + vnpayUrl;
        } else {
                logger.error("Lỗi khi tạo thanh toán cho contractId");
                return "redirect:/error";
        }
    }

    @GetMapping("/vnpay-payment")
    public String vnpayReturn(@PathVariable("contractId") int contractId,
    						  HttpServletRequest request,
    						  Model model) {
        // Lấy trạng thái thanh toán từ VNPay
        int paymentStatus = vnPayService.orderReturn(request);

        // Lấy thông tin đơn hàng từ các tham số trong request
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        
        logger.info("vnp_OrderInfo: {}", orderInfo);
        logger.info("vnp_PayDate: {}", paymentTime);
        logger.info("vnp_TransactionNo: {}", transactionId);
        logger.info("vnp_Amount: {}", totalPrice);
        
        LocalDateTime paymentDate = null;


        // Kiểm tra nếu trạng thái thanh toán là thành công
        if (paymentStatus == 1) {
        	try {
                // Cố gắng chuyển đổi paymentTime sang LocalDateTime
                paymentDate = LocalDateTime.parse(paymentTime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                logger.info("Payment date parsed successfully: {}", paymentDate);
            } catch (Exception e) {
                // Nếu có lỗi trong việc chuyển đổi, ghi log lỗi và set paymentDate là null
                logger.error("Error parsing payment date: {}", paymentTime, e);
                paymentDate = null;  // Set default or handle as needed
            }
        	
            try {
                Payments payment = new Payments();
                payment.setContractId(contractId);
                payment.setAmount(new BigDecimal(totalPrice));
                payment.setPaymentDate(paymentDate);  
                payment.setStatus("SUCCESS"); 
                payment.setPaymentMethod("VNPAY"); 
                payment.setTransactionId(transactionId);

                // Lưu thanh toán vào cơ sở dữ liệu
                paymentService.createPayment(payment);
                contractService.updateContractPaymentStatus(contractId, 1); 
                logger.info("Thanh toán đã được cập nhật vào cơ sở dữ liệu với transactionId: {}", transactionId);
            } catch (Exception e) {
                logger.error("Lỗi khi lưu thanh toán vào cơ sở dữ liệu cho transactionId: {}", transactionId, e);
                return "orderfail"; // Trả về trang thất bại nếu có lỗi
            }
        }
        

        // Thêm các thông tin vào model để hiển thị
        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        // Chuyển hướng tới trang thành công nếu thanh toán thành công
        return paymentStatus == 1 ? "customer/payment/payment-success" : "customer/payment/payment-fail";
    }

}
