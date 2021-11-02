@NAME(insertUserReceipt)
    INSERT INTO `receipt_details`(`file_name`)
    VALUES (:name:)

@NAME(getReceiptById)
    SELECT 
        rd.id, rd.file_name, rd.insert_date_utc, ur.user_id
    FROM
        receipt_details rd
            LEFT JOIN
        user_receipts ur ON rd.id = ur.receipt_id
    WHERE
        rd.id = :id:

@NAME(associateUserToReceipt)
    INSERT INTO `user_receipts` (`receipt_id`,`user_id`)
    VALUES (:id:, :userId:);