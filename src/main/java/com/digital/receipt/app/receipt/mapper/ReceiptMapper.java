package com.digital.receipt.app.receipt.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.digital.receipt.app.receipt.client.domain.Receipt;

import org.springframework.jdbc.core.RowMapper;

/**
 * Mapper class to map a User Profile Object {@link User}
 * 
 * @author Sam Butler
 * @since June 25, 2020
 */
public class ReceiptMapper implements RowMapper<Receipt> {
    public static ReceiptMapper RECEIPT_MAPPER = new ReceiptMapper();

    public Receipt mapRow(ResultSet rs, int rowNum) throws SQLException {
        Receipt receipt = new Receipt();

        receipt.setId(rs.getInt("id"));
        receipt.setUserId(rs.getInt("user_id"));
        receipt.setFileName(rs.getString("file_name"));
        receipt.setInsertDate(rs.getDate("insert_date_utc"));

        return receipt;
    }
}
