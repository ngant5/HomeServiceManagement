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

    public void createPayment(Payments payment) {
    	if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());  
        }
        if (payment.getStatus() == null) {
            payment.setStatus("PENDING");  
        }


        paymentRepository.createPayment(payment);  // Call repository to save the payment
    }
    public List<Payments> getPaymentsByContractId(int contractId) {
        return paymentRepository.getPaymentsByContractId(contractId);
    }
    
}
