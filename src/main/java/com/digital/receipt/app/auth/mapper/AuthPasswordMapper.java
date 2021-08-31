package com.digital.receipt.app.auth.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.digital.receipt.app.auth.client.domain.AuthPassword;

import org.springframework.jdbc.core.RowMapper;

/**
 * Mapper class to map a Auth Password Object.
 * 
 * @author Sam Butler
 * @since June 25, 2021
 */
public class AuthPasswordMapper implements RowMapper<AuthPassword> {
    public static AuthPasswordMapper AUTH_PASSWORD_MAPPER = new AuthPasswordMapper();

    public AuthPassword mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthPassword authPassword = new AuthPassword();
        authPassword.setPassword(rs.getString("password"));
        authPassword.setSalt(rs.getLong("salt"));

        return authPassword;
    }
}
