@NAME(getUserById)
	SELECT 
		up.id,
		up.first_name,
		up.last_name,
		up.email,
		up.web_role_id,
		up.insert_date_utc
	FROM
		user_profile up
			JOIN
		user_credentials uc ON up.id = uc.user_id
	WHERE 
		up.id = :userId:

@NAME(getUsers)
	SELECT 
		up.id,
		up.first_name,
		up.last_name,
		up.email,
		up.web_role_id,
		up.insert_date_utc
	FROM
		user_profile up
			JOIN
		user_credentials uc ON up.id = uc.user_id
	@WHERE(:id:)
		up.id = :id:
	@OR(:email:)
		up.email = :email:
	@OR(:firstName:)
		up.first_name = :firstName:
	@OR(:lastName:)
		up.last_name = :lastName:

@NAME(updateUserProfile)
    UPDATE user_profile 
	SET 
    	first_name = :firstName:,
    	last_name  = :lastName:,
		email      = :email:
	WHERE
		id = :id:

@NAME(createUserProfile)
	INSERT INTO `user_profile` (`first_name`,`last_name`,`email`)
	VALUES (:firstName:,:lastName:,:email:)

@NAME(createUserPassword)
	INSERT INTO `user_credentials` (`user_id`,`password`,`salt`)
	VALUES (:id:,:password:,:salt:)

@NAME(updateUserPassword)
    UPDATE user_credentials 
	SET 
    	password = :password:,
		salt = :salt:
	WHERE
		user_id = :id:

@NAME(updateUserRole)
	UPDATE user_profile
	SET
		web_role_id = :roleId:
	WHERE
		id = :id:

@NAME(deleteUser)
	DELETE FROM user_profile
	WHERE
    	id = :id: