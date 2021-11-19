package com.digital.receipt.app.receipt.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.digital.receipt.app.receipt.client.domain.Receipt;

import org.apache.commons.lang3.time.DateUtils;
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
        receipt.setFilePublicId(rs.getString("file_public_id"));
        receipt.setInsertDate(
                rs.getDate("insert_date_utc") == null ? null : DateUtils.addDays(rs.getDate("insert_date_utc"), 1));
        receipt.setLocation(rs.getString("location"));
        receipt.setLabel(rs.getString("label"));

        return receipt;
    }
}
