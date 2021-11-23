@NAME(getReceiptById)
    SELECT 
        rd.id, rd.file_public_id, ur.insert_date_utc, ur.user_id, ur.location, ur.label, ur.notes
    FROM
        receipt_details rd
            LEFT JOIN
        user_receipts ur ON rd.id = ur.receipt_id
    WHERE
        rd.id = :id:

@NAME(getReceipts)
    SELECT 
        rd.id, rd.file_public_id, ur.insert_date_utc, ur.user_id, ur.location, ur.label, ur.notes
    FROM
        receipt_details rd
            LEFT JOIN
        user_receipts ur ON rd.id = ur.receipt_id
    @WHERE(:id:)
        rd.id = :id:
    @AND(:userId:)
        ur.user_id = :userId:
    @AND(:location: || :label: || :notes:)
        (
            @IF(:location:)
                ur.location LIKE :location:
            @IF(:label:)
                @OR(:location:)
                ur.label LIKE :label:
            @IF(:notes:)
                @OR(:location: || :label:)
                ur.notes LIKE :notes:
        )
    ORDER BY insert_date_utc DESC

@NAME(getCurrentUserReceiptById)
    SELECT 
        rd.id, rd.file_public_id, ur.insert_date_utc, ur.user_id, ur.location, ur.label, ur.notes
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
        @IF(:location: && :label:)
            ,
        @IF(:label:)
            label = :label:
        @IF(:location: || :label:)
            @IF(:notes:)
                ,
        @IF(:notes:)
            notes = :notes:
    WHERE 
        receipt_id = :id: 
    AND 
        user_id = :userId:

@NAME(deleteReceiptRecords)
    DELETE FROM receipt_details
    WHERE id = :id: