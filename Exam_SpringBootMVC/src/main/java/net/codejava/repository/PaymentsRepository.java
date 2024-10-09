package net.codejava.repository;

import net.codejava.model.Payments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Lưu một payment mới
    public int savePayment(Payments payment) {
        String sql = "INSERT INTO Payments (contract_id, amount, payment_date, status) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, payment.getContractId(), payment.getAmount(), payment.getPaymentDate(), payment.getStatus());
    }

    // Lấy danh sách thanh toán theo contractId
    public List<Payments> findPaymentsByContractId(int contractId) {
        String sql = "SELECT * FROM Payments WHERE contract_id = ?";
        return jdbcTemplate.query(sql, new Object[]{contractId}, new BeanPropertyRowMapper<>(Payments.class));
    }

    // Xóa một payment theo ID
    public int deletePaymentById(int paymentId) {
        String sql = "DELETE FROM Payments WHERE payment_id = ?";
        return jdbcTemplate.update(sql, paymentId);
    }
}
