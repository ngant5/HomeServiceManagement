package net.codejava.repository;

import net.codejava.model.Payments;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class PaymentRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // SQL query to insert payment into the database
    private static final String INSERT_PAYMENT_SQL = "INSERT INTO payments (contract_id, amount, payment_date, status, payment_method, transaction_id) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_PAYMENTS_BY_CONTRACT_ID = "SELECT * FROM payments WHERE contract_id = ?";

    // Method to create payment
    public void createPayment(Payments payment) {
        jdbcTemplate.update(INSERT_PAYMENT_SQL,
                payment.getContractId(),
                payment.getAmount(),
                Timestamp.valueOf(payment.getPaymentDate()),  // Convert LocalDateTime to Timestamp
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getTransactionId()
        );
    }
    
    public List<Payments> getPaymentsByContractId(int contractId) {
        return jdbcTemplate.query(SELECT_PAYMENTS_BY_CONTRACT_ID,
                new BeanPropertyRowMapper<>(Payments.class),
                contractId);
    }
}
