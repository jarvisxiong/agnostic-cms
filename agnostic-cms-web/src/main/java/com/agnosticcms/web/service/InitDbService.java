package com.agnosticcms.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agnosticcms.web.dao.SchemaDao;
import com.agnosticcms.web.dao.UserDao;

@Service
public class InitDbService {

	@Autowired
	private SchemaDao schemaDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CrypService crypService;
	
	public boolean initDb() {
		
		if(schemaDao.cmsTablesExist()) {
			return false;
		}
		
		schemaDao.createCmsUsersTable();
		userDao.createUser("admin", crypService.getSha1Base64ForApache("admin"));
		schemaDao.createCmsSessionsTable();
		return true;
	}
	
}
