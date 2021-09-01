INSERT INTO web_role (web_role)
VALUES ("ADMIN"), ("USER");

INSERT INTO user_profile (first_name, last_name, email, web_role_id)
VALUES  ("User1", "Test1", "user1@mail.com", 2), 
        ("User2", "Test2", "user2@mail.com", 2),
        ("Admin1", "Test1", "admin1@mail.com", 1),
        ("Admin3", "Test3", "admin2@mail.com", 1);

INSERT INTO user_credentials (user_id, password, salt)
VALUES  (1, "fa1e2d102c06ecd5d6d4dbfb4c9ddeca01c3e890d58ffcaf1b04b9afe4590e78", 2835126461), 
        (2, "ad5e2c66fb2961536ac3639adaa5ee52f01ed6d7613b821a7b1e310b9025e130", 7079486872),
        (3, "a6300782d05c314b926f6507e1eb7197dac3923a5458c3f25598654049a9dd23", 8739167383),
        (4, "34a0d107b2df2ad7a106299dc05168983096b185ac3337dc501fdcbbae5f4c05", 3902975478);