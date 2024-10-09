package net.codejava.repository;

import net.codejava.model.Contracts;
import net.codejava.model.Customers;
import net.codejava.model.Payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Customers WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        return count != null && count > 0;
    }

    
    public Optional<Customers> findById(int id) {
        String sql = "SELECT * FROM Customers WHERE customer_id = ?";
        try {
            Customers customer = jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                new BeanPropertyRowMapper<>(Customers.class)
            );
            return Optional.of(customer);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    

   
    public Optional<Customers> findByEmail(String email) {
        String sql = "SELECT * FROM Customers WHERE email = ?";
        try {
        	Customers customer = jdbcTemplate.queryForObject(
                sql,
                new Object[]{email},
                new BeanPropertyRowMapper<>(Customers.class)
            );
            return Optional.of(customer);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    
    public Optional<Customers> findByVerifyCode(String verifyCode) {
        String sql = "SELECT * FROM Customers WHERE verify_code = ?";
        try {
        	Customers customer = jdbcTemplate.queryForObject(
                sql,
                new Object[]{verifyCode},
                new BeanPropertyRowMapper<>(Customers.class)
            );
            return Optional.of(customer);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    
    public int saveUser(Customers customer) {
        String sql = "INSERT INTO Customers (fullname, password, email, phone, address, profile_image, status, created_at, verify_code, token ) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
            sql,
            customer.getFullname(),
            customer.getPassword(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getAddress(),
            customer.getProfileImage(),
            customer.getStatus(),
            customer.getCreatedAt(),
            customer.getVerifyCode(),
            customer.getToken()
        );
    }

    
    public int updateUser(Customers customer) {
    	String sql = "UPDATE Customers SET fullname=?, email=?, phone=?, address=?, profile_image=? WHERE customer_id=?";
    	return jdbcTemplate.update(
            sql,
            customer.getFullname(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getAddress(),
            customer.getProfileImage(),
            customer.getCustomerId()
        );
    }

    
    public int deleteById(int id) {
        String sql = "DELETE FROM Customers WHERE customer_id = ?";
        return jdbcTemplate.update(sql, id);
    }
    

    public List<Customers> findAll() {
        String sql = "SELECT * FROM Customers";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Customers.class));
    }

   
    public List<Contracts> findContractsByCustomerId(int customerId) {
        String sql = "SELECT * FROM Contracts WHERE customer_id = ?";
        return jdbcTemplate.query(sql, new Object[]{customerId}, new BeanPropertyRowMapper<>(Contracts.class));
    }

    public List<Payments> findPaymentsByContractId(int contractId) {
        String sql = "SELECT * FROM Payments WHERE contract_id = ?";
        return jdbcTemplate.query(sql, new Object[]{contractId}, new BeanPropertyRowMapper<>(Payments.class));
    }

}