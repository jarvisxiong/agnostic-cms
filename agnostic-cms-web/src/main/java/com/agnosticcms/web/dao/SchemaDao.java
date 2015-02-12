package com.agnosticcms.web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.agnosticcms.web.dbutil.PdbEngineProvider;
import com.agnosticcms.web.exception.DaoRuntimeException;
import com.agnosticcms.web.service.CrypService;
import com.feedzai.commons.sql.abstraction.ddl.DbColumn;
import com.feedzai.commons.sql.abstraction.ddl.DbColumnConstraint;
import com.feedzai.commons.sql.abstraction.ddl.DbColumnType;
import com.feedzai.commons.sql.abstraction.ddl.DbEntity;
import com.feedzai.commons.sql.abstraction.dml.dialect.SqlBuilder;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngine;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngineException;

@Repository
public class SchemaDao {

	@Autowired
	private PdbEngineProvider pdbEngineProvider;
	
	@Autowired
	private CrypService crypService;

	public static final String TMP_TABLE_NAME = "tmp_table";

	public void createCmsUsersTable() {
		
		execute((engine) -> {
			DbColumn usernameColumn = SqlBuilder
					.dbColumn()
					.name("username")
					.type(DbColumnType.STRING)
					.size(30)
					.addConstraints(DbColumnConstraint.NOT_NULL, DbColumnConstraint.UNIQUE)
					.build();
			
			DbColumn passwordColumn = SqlBuilder
					.dbColumn()
					.name("password")
					.type(DbColumnType.STRING)
					.size(33)
					.addConstraints(DbColumnConstraint.NOT_NULL)
					.build();
			
			DbEntity cmsUsersTable = SqlBuilder.dbEntity()
			        .name("cms_users")
			        .addColumn(usernameColumn)
			        .addColumn(passwordColumn)
			        .pkFields("username")
			        .build();
			
			engine.addEntity(cmsUsersTable);
		}, "Unable to create cms users table");
	}
	
	public void createCmsSessionsTable() {
		
		execute((engine) -> {
			DbColumn keyColumn = SqlBuilder
					.dbColumn()
					.name("key")
					.type(DbColumnType.STRING)
					.size(36)
					.addConstraints(DbColumnConstraint.NOT_NULL, DbColumnConstraint.UNIQUE)
					.build();
			
			DbColumn valueColumn = SqlBuilder
					.dbColumn()
					.name("value")
					.type(DbColumnType.STRING)
					.size(5000)
					.addConstraints(DbColumnConstraint.NOT_NULL)
					.build();
			
			DbColumn expiryColumn = SqlBuilder
					.dbColumn()
					.name("expiryColumn")
					.type(DbColumnType.LONG)
					.addConstraints(DbColumnConstraint.NOT_NULL)
					.build();
			
			DbEntity cmsUsersTable = SqlBuilder.dbEntity()
			        .name("cms_users")
			        .addColumn(keyColumn)
			        .addColumn(valueColumn)
			        .addColumn(expiryColumn)
			        .pkFields("username")
			        .build();
			
			engine.addEntity(cmsUsersTable);
		}, "Unable to create cms users table");
	}
	
	private void execute(PdbWorkContainer pdbInterface, String errorMsg) {
		DatabaseEngine engine = pdbEngineProvider.getEngine();
		
		try {
			pdbInterface.executeWork(engine);
		} catch (DatabaseEngineException e) {
			throw new DaoRuntimeException(errorMsg, e);
		} finally {
			engine.close();
		}
	}
	
	@FunctionalInterface
	private interface PdbWorkContainer {
		public void executeWork(DatabaseEngine engine) throws DatabaseEngineException;
	}

}
