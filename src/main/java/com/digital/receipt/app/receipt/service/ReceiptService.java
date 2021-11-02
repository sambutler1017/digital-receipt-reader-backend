package com.digital.receipt.app.receipt.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.digital.receipt.app.receipt.client.domain.Receipt;
import com.digital.receipt.app.receipt.dao.ReceiptDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Receipt Service class that handles all service calls to the dao.
 * 
 * @author Sam Butler
 * @since November 1, 2021
 */
@Component
public class ReceiptService {

    @Autowired
    private ReceiptDao dao;

    /**
     * This will create the receipt in the database for the given userId and the
     * receipt data to be attatched.
     * 
     * @param fileName Add the file name the pdf is stored under.
     * @return {@link Receipt} of the added receipt.
     * @throws Exception
     */
    public Receipt insertReceipt(String fileName) throws Exception {
        return dao.insertReceipt(fileName);
    }

    /**
     * Get the receipt for the given receipt id.
     * 
     * @param id The id of the receipt to get
     * @return {@link Receipt} of the id.
     * @throws Exception
     */
    public Receipt getReceiptById(int id) throws Exception {
        Receipt rec = dao.getReceiptById(id);
        return rec;
    }

    /**
     * This will get the user id and receipt id and associate the two together.
     * 
     * @param rec Receipt object containing the user ID and receipt id.
     * @return {@link Receipt}
     * @throws Exception
     */
    public Receipt associateUserToReceipt(Receipt rec) throws Exception {
        return dao.associateUserToReceipt(rec);
    }
}

// Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name",
// "hwxm9amax", "api_key",
// "656249988229398", "api_secret", "NO6Ydnn_UIFwAzanYJL3Xm0xkb8", "secure",
// true));

// cloudinary.uploader().upload("C:\\Users\\sambu\\Desktop\\EECS1100_fall_2021_opp0_reva[8100].pdf",
// ObjectUtils.emptyMap());