package net.codejava.repository;

import net.codejava.model.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ServiceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper for mapping SQL result to Service model
    private static class ServiceRowMapper implements RowMapper<Services> {
        @Override
        public Services mapRow(ResultSet rs, int rowNum) throws SQLException {
            Services service = new Services();
            service.setServiceId(rs.getInt("service_id"));
            service.setServiceName(rs.getString("service_name"));
            service.setServiceDescription(rs.getString("service_description"));
            service.setServicePrice(rs.getInt("service_price"));
            service.setServiceStatus(rs.getInt("service_status"));
            service.setServiceImage(rs.getString("service_image"));
            return service;
        }
    }

    // Get all services
    public List<Services> findAll() {
        String sql = "SELECT * FROM Services";
        return jdbcTemplate.query(sql, new ServiceRowMapper());
    }

    // Get service by ID
    public Services findById(int id) {
        String sql = "SELECT * FROM Services WHERE service_id = ?";
        return jdbcTemplate.queryForObject(sql, new ServiceRowMapper(), id);
    }

    // Save a new service
    public void save(Services service) {
        String sql = "INSERT INTO Services (service_name, service_description, service_price, service_status, service_image) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, service.getServiceName(), service.getServiceDescription(), service.getServicePrice(), service.getServiceStatus(), service.getServiceImage());
    }

    // Update an existing service
    public void update(Services service) {
        String sql = "UPDATE Services SET service_name = ?, service_description = ?, service_price = ?, service_status = ?, service_image = ? WHERE service_id = ?";
        jdbcTemplate.update(sql, service.getServiceName(), service.getServiceDescription(), service.getServicePrice(), service.getServiceStatus(), service.getServiceImage(), service.getServiceId());
    }

    // Delete a service by ID
    public void deleteById(int id) {
        String sql = "DELETE FROM Services WHERE service_id = ?";
        jdbcTemplate.update(sql, id);
    }
}