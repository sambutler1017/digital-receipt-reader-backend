DELETE FROM user_profile;
DELETE FROM user_credentials;
DELETE FROM web_role;

ALTER TABLE user_profile AUTO_INCREMENT = 1;
ALTER TABLE user_credentials AUTO_INCREMENT = 1;
ALTER TABLE web_role AUTO_INCREMENT = 1;