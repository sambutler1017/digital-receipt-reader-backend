@NAME(authenticateUser)
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
		up.email = :email:
	AND
		uc.password = :password:

@NAME(getUserAuthenticationSalt)
	SELECT
		uc.password,
		uc.salt
	FROM
		user_profile up
			JOIN
		user_credentials uc ON up.id = uc.user_id
	WHERE
		up.email = :email: