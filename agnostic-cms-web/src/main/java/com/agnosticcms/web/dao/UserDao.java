package com.agnosticcms.web.dao;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

	@Autowired
	private DSLContext dslContext;
	
	public void createUser(String user, String encryptedApachePassword) {
		dslContext.insertInto(DSL.table("cms_users"), DSL.field("username"), DSL.field("password"))
			.values(user, encryptedApachePassword).execute();
	}
	
}
