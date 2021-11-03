package com.digital.receipt.app.receipt.dao;

import static com.digital.receipt.app.receipt.mapper.ReceiptMapper.RECEIPT_MAPPER;

import java.util.Date;
import java.util.Optional;

import com.digital.receipt.app.receipt.client.domain.Receipt;
import com.digital.receipt.common.abstracts.AbstractSqlDao;

import org.springframework.stereotype.Repository;

/**
 * Class that handles all the dao calls to the database for receipts
 * 
 * @author Sam Butler
 * @since November 1, 2021
 */
@Repository
public class ReceiptDao extends AbstractSqlDao {

    /**
     * This will create the receipt in the database for the given userId and the
     * receipt data to be attatched.
     * 
     * @param receipt UserEmail object to get the mail properties from
     * @return {@link Receipt} of the added receipt.
     * @throws Exception
     */
    public Receipt insertReceipt(String publicId) throws Exception {
        Optional<Integer> id = sqlClient.post(getSql("insertUserReceipt"), params("name", publicId));
        return new Receipt(id.get(), publicId, new Date());
    }

    /**
     * Get the receipt for the given receipt id.
     * 
     * @param id The id of the receipt to get
     * @return {@link Receipt} of the id.
     * @throws Exception
     */
    public Receipt getReceiptById(int id) throws Exception {
        return sqlClient.getTemplate(getSql("getReceiptById"), params("id", id), RECEIPT_MAPPER);
    }

    /**
     * This will get the user id and receipt id and associate the two together.
     * 
     * @param rec Receipt object containing the user ID and receipt id.
     * @return {@link Receipt}
     * @throws Exception
     */
    public Receipt associateUserToReceipt(Receipt rec) throws Exception {
        sqlClient.post(getSql("associateUserToReceipt"), params("id", rec.getId()).addValue("userId", rec.getUserId()));
        return getReceiptById(rec.getId());
    }

    /**
     * Get the next auto increment value for the receipt details table.
     * 
     * @return {@link Long} of the next auto increment id.
     * @throws Exception
     */
    public long getAutoIncrementReceiptDetails() throws Exception {
        return sqlClient.queryForLong(getSql("getAutoIncrementReceiptDetails"), params("", null));
    }
}
