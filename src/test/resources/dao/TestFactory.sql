@NAME(setupTables)
    CREATE TABLE `user_profile` (
    `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `first_name` varchar(64) NOT NULL,
    `last_name` varchar(64) NOT NULL,
    `email` varchar(128) NOT NULL,
    `web_role_id` int(10) unsigned NOT NULL DEFAULT 2,
    `insert_date_utc` datetime NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`id`),
    UNIQUE KEY `email__AK1` (`email`)
    ) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

    CREATE TABLE `web_role` (
    `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `web_role` varchar(45) NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

@NAME(setupContraints)
    ALTER TABLE user_profile 
        ADD CONSTRAINT `user_profile__web_role__FK1` FOREIGN KEY (`web_role_id`) 
        REFERENCES `web_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
