package com.digital.receipt.app.user.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.common.enums.WebRole;

import org.springframework.jdbc.core.RowMapper;

/**
 * Mapper class to map a User Profile Object {@link User}
 * 
 * @author Sam Butler
 * @since June 25, 2020
 */
public class UserMapper implements RowMapper<User> {
    public static UserMapper USER_MAPPER = new UserMapper();

    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setWebRole(WebRole.getRole(rs.getInt("web_role_id")));
        user.setInsertDate(rs.getDate("insert_date_utc"));

        return user;
    }
}
