package net.codejava.repository;

import net.codejava.model.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ServiceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Services> findAll() {
        String sql = "SELECT * FROM Services";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Services.class));
    }

    public Optional<Services> findById(int id) {
        String sql = "SELECT * FROM Services WHERE service_id = ?";
        try {
            Services service = jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                new BeanPropertyRowMapper<>(Services.class)
            );
            return Optional.of(service);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    public double getServicePrice(int serviceId) {
    	String sql = "SELECT service_price FROM services WHERE service_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{serviceId}, Double.class);
    }
    

    public int save(Services service) {
        String sql = "INSERT INTO Services (serviceName, serviceDescription, servicePrice, serviceStatus, serviceImage) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
            sql,
            service.getServiceName(),
            service.getServiceDescription(),
            service.getServicePrice(),
            service.getServiceStatus(),
            service.getServiceImage()
        );
    }

    public int update(Services service) {
        String sql = "UPDATE Services SET serviceName = ?, serviceDescription = ?, servicePrice = ?, serviceStatus = ?, serviceImage = ? WHERE service_id = ?";
        return jdbcTemplate.update(
            sql,
            service.getServiceName(),
            service.getServiceDescription(),
            service.getServicePrice(),
            service.getServiceStatus(),
            service.getServiceImage(),
            service.getServiceId()
        );
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM Services WHERE service_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
