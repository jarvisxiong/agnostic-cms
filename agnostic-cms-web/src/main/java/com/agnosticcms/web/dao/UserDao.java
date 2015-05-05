package com.agnosticcms.web.dao;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Data access object for manipulation of user data in the database
 */
@Repository
public class UserDao {

	@Autowired
	private DSLContext dslContext;
	
	/**
	 * Adds a new user to the database
	 * @param username Username of the new user
	 * @param encryptedApachePassword Password in encrypted format so that it could be handled and compared by Apache server
	 */
	public void createUser(String username, String encryptedApachePassword) {
		dslContext.insertInto(DSL.table("cms_users"), DSL.field("username"), DSL.field("password"))
			.values(username, encryptedApachePassword).execute();
	}
	
}
