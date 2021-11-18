@NAME(getReceiptById)
    SELECT 
        rd.id, rd.file_public_id, ur.insert_date_utc, ur.user_id, ur.location, ur.label
    FROM
        receipt_details rd
            LEFT JOIN
        user_receipts ur ON rd.id = ur.receipt_id
    WHERE
        rd.id = :id:

@NAME(getReceipts)
    SELECT 
        rd.id, rd.file_public_id, ur.insert_date_utc, ur.user_id, ur.location, ur.label
    FROM
        receipt_details rd
            LEFT JOIN
        user_receipts ur ON rd.id = ur.receipt_id
    @WHERE(:id:)
        rd.id = :id:
    @AND(:userId:)
        ur.user_id = :userId:

@NAME(getCurrentUserReceiptById)
    SELECT 
        rd.id, rd.file_public_id, ur.insert_date_utc, ur.user_id, ur.location, ur.label
    FROM
        receipt_details rd
            LEFT JOIN
        user_receipts ur ON rd.id = ur.receipt_id
    WHERE
        ur.user_id = :userId: AND rd.id = :id:

@NAME(getAutoIncrementReceiptDetails)
    SELECT 
        AUTO_INCREMENT
    FROM
        information_schema.tables
    WHERE
        table_name = 'receipt_details'
            AND table_schema = DATABASE()
            
@NAME(insertUserReceipt)
    INSERT INTO receipt_details (`file_public_id`)
    VALUES (:name:)

@NAME(associateUserToReceipt)
    INSERT INTO user_receipts (`receipt_id`,`user_id`)
    VALUES (:id:, :userId:)

@NAME(updateCurrentUserAssociation)
    UPDATE user_receipts
    SET
        @IF(:location:)
            location = :location:
        @IF(:label: && :location:)
            ,
        @IF(:label:)
            label = :label:
    WHERE 
        receipt_id = :id: 
    AND 
        user_id = :userId:

@NAME(deleteReceiptRecords)
    DELETE FROM receipt_details
    WHERE id = :id: