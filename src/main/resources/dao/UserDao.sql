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