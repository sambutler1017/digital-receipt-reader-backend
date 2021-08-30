INSERT INTO web_role (web_role)
VALUES ("ADMIN"), ("USER");

INSERT INTO user_profile (first_name, last_name, email, web_role_id)
VALUES  ("User1", "Test1", "user1@mail.com", 2), 
        ("User2", "Test2", "user2@mail.com", 2),
        ("Admin1", "Test1", "admin1@mail.com", 1),
        ("Admin3", "Test3", "admin2@mail.com", 1);

INSERT INTO user_credentials (user_id, password)
VALUES  (1, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"), 
        (2, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"),
        (3, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"),
        (4, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");