package net.codejava.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.codejava.model.Services;
import net.codejava.utils.Views;

public class ServicesMapper implements RowMapper<Services> {
    @Override
    public Services mapRow(ResultSet rs, int rowNum) throws SQLException {
        Services service = new Services();
        service.setServiceId(rs.getInt(Views.COL_SERVICES_ID));
        service.setServiceName(rs.getString(Views.COL_SERVICES_NAME));
        service.setServiceDescription(rs.getString(Views.COL_SERVICES_DESCRIPTION));
        service.setServicePrice(rs.getInt(Views.COL_SERVICES_PRICE));
        service.setServiceStatus(rs.getInt(Views.COL_SERVICES_STATUS));
        service.setServiceImage(rs.getString(Views.COL_SERVICES_IMAGE));
        return service;
    }
}
