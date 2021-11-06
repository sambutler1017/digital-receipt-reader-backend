@NAME(insertUserReceipt)
    INSERT INTO receipt_details (`file_public_id`)
    VALUES (:name:)

@NAME(getReceiptById)
    SELECT 
        rd.id, rd.file_public_id, rd.insert_date_utc, ur.user_id
    FROM
        receipt_details rd
            LEFT JOIN
        user_receipts ur ON rd.id = ur.receipt_id
    WHERE
        rd.id = :id:

@NAME(getCurrentUserReceiptById)
    SELECT 
        rd.id, rd.file_public_id, rd.insert_date_utc, ur.user_id
    FROM
        receipt_details rd
            LEFT JOIN
        user_receipts ur ON rd.id = ur.receipt_id
    WHERE
        ur.user_id = :userId: AND rd.id = :id:

@NAME(associateUserToReceipt)
    INSERT INTO user_receipts (`receipt_id`,`user_id`)
    VALUES (:id:, :userId:)

@NAME(getCurrentUserReceipts)
    SELECT 
        rd.id, rd.file_public_id, rd.insert_date_utc, ur.user_id
    FROM
        receipt_details rd
            LEFT JOIN
        user_receipts ur ON rd.id = ur.receipt_id
    WHERE
        ur.user_id = :userId:

@NAME(getAutoIncrementReceiptDetails)
    SELECT 
        AUTO_INCREMENT
    FROM
        information_schema.tables
    WHERE
        table_name = 'receipt_details'
            AND table_schema = DATABASE()

@NAME(deleteCurrentUserReceipt)
    DELETE FROM receipt_details
    WHERE id = :id: