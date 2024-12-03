package net.codejava.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payments {
    private int payment_id;
    private int contract_id;
    private BigDecimal amount;
    private LocalDateTime payment_date;
    private String status;
    private String payment_method;  
    private String transaction_id;
    
    public Payments() {
    }
    
    public Payments(int paymentId, int contractId, BigDecimal  amount, LocalDateTime paymentDate, 
            String status, String paymentMethod, String transactionId) {
			this.payment_id = paymentId;
			this.contract_id = contractId;
			this.amount = amount;
			this.payment_date = paymentDate;
			this.status = status;
			this.payment_method = paymentMethod;
			this.transaction_id = transactionId;
			}

    // Getters and Setters

    public int getPaymentId() {
        return payment_id;
    }

    public void setPaymentId(int paymentId) {
        this.payment_id = paymentId;
    }

    public int getContractId() {
        return contract_id;
    }

    public void setContractId(int contractId) {
        this.contract_id = contractId;
    }

    public BigDecimal  getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal  amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return payment_date;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.payment_date = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPaymentMethod() {
        return payment_method;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.payment_method = paymentMethod;
    }

    public String getTransactionId() {
        return transaction_id;
    }

    public void setTransactionId(String transactionId) {
        this.transaction_id = transactionId;
    }
}
