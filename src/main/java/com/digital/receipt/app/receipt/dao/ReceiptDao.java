package com.digital.receipt.app.receipt.dao;

import static com.digital.receipt.app.receipt.mapper.ReceiptMapper.RECEIPT_MAPPER;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.digital.receipt.app.receipt.client.domain.Receipt;
import com.digital.receipt.app.receipt.client.domain.request.ReceiptGetRequest;
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
     * This will get a list of all the receipts based on the
     * {@link ReceiptGetRequest}
     * 
     * @return {@link List<Receipt>} associated to that user.
     * @throws Exception
     */
    public List<Receipt> getReceipts(ReceiptGetRequest request) throws Exception {
        return sqlClient.getPage(getSql("getReceipts"),
                params("id", request.getId()).addValue("userId", request.getUserId()), RECEIPT_MAPPER);
    }

    /**
     * Get the receipt for the given receipt id from the current user account.
     * 
     * @param id     The id of the receipt to get
     * @param userId The user id of the user to get the receipt for.
     * @return {@link Receipt} of the id.
     * @throws Exception
     */
    public Receipt getCurrentUserReceiptById(int id, int userId) throws Exception {
        try {
            return sqlClient.getTemplate(getSql("getCurrentUserReceiptById"),
                    params("id", id).addValue("userId", userId), RECEIPT_MAPPER);
        } catch (Exception e) {
            throw new Exception(String.format("User id '%d' does not have access to receipt id '%d'", userId, id));
        }
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
        return new Receipt(id.get(), publicId, LocalDate.now());
    }

    /**
     * This will get the user id and receipt id and associate the two together.
     * 
     * @param receiptId Id of the receipt to associate current user too.
     * @param userId    The id of the user to put the receip to.
     * @return {@link Receipt}
     * @throws Exception
     */
    public void associateUserToReceipt(int receiptId, int userId) throws Exception {
        sqlClient.post(getSql("associateUserToReceipt"), params("id", receiptId).addValue("userId", userId));
    }

    /**
     * This will update information for a users association for the given receipt id
     * in in the request body.
     * 
     * @param receipt The receipt to be updated.
     * @return {@link Receipt} of the updated receipt.
     * @throws Exception
     */
    public void updateCurrentUserAssociation(Receipt receipt) throws Exception {
        try {
            sqlClient.update(getSql("updateCurrentUserAssociation"),
                    params("location", receipt.getLocation()).addValue("label", receipt.getLabel())
                            .addValue("id", receipt.getId()).addValue("userId", receipt.getUserId()));
        } catch (Exception e) {
            throw new Exception(String.format("User id %d does not have access to receipt id %d", receipt.getUserId(),
                    receipt.getId()));
        }

    }

    /**
     * Delete multiple receipt records at a time for the given list of receipts.
     * 
     * @param recs The receipts to be deleted.
     * @throws Exception
     */
    public void deleteReceiptRecords(List<Integer> recs) throws Exception {
        sqlClient.delete(getSql("deleteReceiptRecords"), params("id", recs));
    }
}
