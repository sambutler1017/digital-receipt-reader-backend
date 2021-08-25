@NAME(getUserById)
	SELECT 
		up.id,
		up.first_name,
		up.last_name,
		up.email,
		up.web_role_id,
		uc.forgot_password_flag,
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
		uc.forgot_password_flag,
		up.insert_date_utc
	FROM
		user_profile up
			JOIN
		user_credentials uc ON up.id = uc.user_id
	@WHERE(:id:)
		up.id = :id:
	@AND(:email:)
		up.email = :email:
	@AND(:firstName:)
		up.first_name = :firstName:
	@AND(:lastName:)
		up.lastr_name = :lastName:

@NAME(updateUserProfile)
    UPDATE user_profile 
	SET 
    	first_name = :firstName:,
    	last_name  = :lastName:,
		email      = :email:
	WHERE
		id = :id:

@NAME(updateUserPassword)
    UPDATE user_credentials 
	SET 
    	password = :password:
	WHERE
		user_id = :id:

@NAME(updateUserForgotPassword)
    UPDATE user_credentials 
	SET 
    	forgot_password_flag = :flag:
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