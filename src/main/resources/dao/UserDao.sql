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