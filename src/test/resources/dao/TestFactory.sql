@NAME(setupTables)
    CREATE TABLE user_profile (
        `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
        `first_name` varchar(64) NOT NULL,
        `last_name` varchar(64) NOT NULL,
        `email` varchar(128) NOT NULL,
        `web_role_id` int(10) unsigned NOT NULL DEFAULT 2,
        `insert_date_utc` datetime NOT NULL DEFAULT current_timestamp(),
        PRIMARY KEY (`id`),
        UNIQUE KEY `email__AK1` (`email`)
    ) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

    CREATE TABLE web_role (
        `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
        `web_role` varchar(45) NOT NULL,
        PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

    CREATE TABLE user_credentials (
        `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
        `password` varchar(256) NOT NULL,
        `forgot_password_flag` tinyint(3) unsigned NOT NULL DEFAULT 0,
        PRIMARY KEY (`user_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

    CREATE TABLE receipts (
        `id` int(10) unsigned NOT NULL,
        `user_id` int(10) unsigned NOT NULL,
        `price` decimal(8,2) unsigned NOT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `user_id_UNIQUE` (`user_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

    CREATE TABLE receipt_details (
        `receipt_id` int(10) unsigned NOT NULL,
        PRIMARY KEY (`receipt_id`),
        UNIQUE KEY `receipt_id_UNIQUE` (`receipt_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


@NAME(setupContraints)
    ALTER TABLE user_profile 
        ADD CONSTRAINT `user_profile__web_role__FK1` FOREIGN KEY (`web_role_id`) 
        REFERENCES `web_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

    ALTER TABLE user_credentials 
        ADD CONSTRAINT `user_credentials__user_profile__FK1` FOREIGN KEY (`user_id`) 
        REFERENCES `user_profile` (`id`) ON DELETE CASCADE;

    ALTER TABLE receipts 
        ADD CONSTRAINT `receipts__user_profile__FK1` FOREIGN KEY (`user_id`) 
        REFERENCES `user_profile` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

    ALTER TABLE receipt_details 
        ADD CONSTRAINT `receipt_details__receipts__FK1` FOREIGN KEY (`receipt_id`) 
        REFERENCES `receipts` (`id`) ON DELETE CASCADE;
