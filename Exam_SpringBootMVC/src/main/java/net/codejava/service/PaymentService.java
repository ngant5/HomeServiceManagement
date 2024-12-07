package net.codejava.service;

import net.codejava.model.Payments;
import net.codejava.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void createPayment(int contractId, BigDecimal amount, String paymentMethod, String transactionId) {
        Payments payment = new Payments();
        payment.setContractId(contractId);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("PENDING");  // Set status as pending initially
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionId(transactionId);

        paymentRepository.createPayment(payment);  // Call repository to save the payment
    }
    public List<Payments> getPaymentsByContractId(int contractId) {
        return paymentRepository.getPaymentsByContractId(contractId);
    }
    
}
