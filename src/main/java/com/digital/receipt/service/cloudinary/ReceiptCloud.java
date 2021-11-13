package com.digital.receipt.service.cloudinary;

import java.util.List;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.stereotype.Service;

/**
 * Receipt Cloud that manages the cloudinary S3 bucket so receipts can be
 * pulled, updated or deleted if need be.
 * 
 * @author Sam Butler
 * @since November 6, 2021
 */
@Service
public class ReceiptCloud {
    private final String CLOUD_NAME = "hwxm9amax";
    private final String API_KEY = "656249988229398";
    private final String API_SECRET = "NO6Ydnn_UIFwAzanYJL3Xm0xkb8";

    private Cloudinary cloud;

    /**
     * Default constructor for when the class is invoked, then the cloudinary can be
     * initialized and used to upload files.
     */
    public ReceiptCloud() {
        cloud = new Cloudinary(ObjectUtils.asMap("cloud_name", CLOUD_NAME, "api_key", API_KEY, "api_secret", API_SECRET,
                "secure", true));
    }

    /**
     * API call that will delete the given receipt from the cloudinary S3 bucket. It
     * will take in the public id in order to know what receipt it needs to delete.
     * It will first delete the receipt from the bucket and then invalidate that
     * public id so that it can not be used again.
     * 
     * @param pid The public id to delete.
     * @throws Exception
     */
    public void delete(String pid) throws Exception {
        cloud.uploader().destroy(pid, ObjectUtils.asMap("invalidate", true));
    }

    /**
     * API call that will delete all the receipts given in the list.
     * 
     * @param pids The public ids to delete.
     * @throws Exception
     */
    public void delete(List<String> pids) throws Exception {
        cloud.api().deleteResources(pids, ObjectUtils.asMap("invalidate", true));
    }

    /**
     * Get the url for the given public id. If the public id does not exist then it
     * will throw an error since it can not find the id.
     * 
     * @param pid The id of the image to search for.
     * @return {@link String} of the url.
     * @throws Exception If the public id can not be found.
     */
    public String getUrl(String pid) throws Exception {
        return cloud.api().resource(pid, ObjectUtils.emptyMap()).get("secure_url").toString();
    }
}
