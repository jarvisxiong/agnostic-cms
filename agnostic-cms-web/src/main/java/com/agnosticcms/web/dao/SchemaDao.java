package com.agnosticcms.web.dao;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Repository;


import com.agnosticcms.web.dbutil.PdbEngineProvider;
import com.agnosticcms.web.exception.DaoRuntimeException;
import com.agnosticcms.web.service.CrypService;
import com.feedzai.commons.sql.abstraction.ddl.DbColumn;
import com.feedzai.commons.sql.abstraction.ddl.DbColumnConstraint;
import com.feedzai.commons.sql.abstraction.ddl.DbColumnType;
import com.feedzai.commons.sql.abstraction.ddl.DbEntity;
import com.feedzai.commons.sql.abstraction.dml.K;
import com.feedzai.commons.sql.abstraction.dml.dialect.SqlBuilder;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngine;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngineException;

@Repository
public class SchemaDao {

	private static final String TABLE_NAME_CMS_USERS = "cms_users";
	private static final String TABLE_NAME_CMS_SESSIONS = "cms_sessions";
	private static final String TABLE_NAME_CMS_MODULES = "cms_modules";
	private static final String TABLE_NAME_CMS_MODULES_COLUMNS = "cms_modules_columns";
	private static final String TABLE_NAME_CMS_MODULES_HIERARCHY = "cms_modules_hierarchy";
	
	@Autowired
	private PdbEngineProvider pdbEngineProvider;
	
	@Autowired
	private CrypService crypService;
	
	@Autowired
	private DSLContext dslContext;
	
	public boolean cmsTablesExist() {
		return tableExists(TABLE_NAME_CMS_USERS) && tableExists(TABLE_NAME_CMS_SESSIONS);
	}

	public void createCmsUsersTable() {
		
		execute((engine) -> {
			DbColumn usernameColumn = SqlBuilder
					.dbColumn()
					.name("username")
					.type(DbColumnType.STRING)
					.size(30)
					.addConstraints(DbColumnConstraint.NOT_NULL)
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
					.addConstraints(DbColumnConstraint.NOT_NULL)
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
					.name("expiry")
					.type(DbColumnType.LONG)
					.addConstraints(DbColumnConstraint.NOT_NULL)
					.build();
			
			DbEntity cmsSessionsTable = SqlBuilder.dbEntity()
			        .name(TABLE_NAME_CMS_SESSIONS)
			        .addColumn(keyColumn)
			        .addColumn(valueColumn)
			        .addColumn(expiryColumn)
			        .pkFields("key")
			        .build();
			
			engine.addEntity(cmsSessionsTable);
		}, "Unable to create cms users table");
	}
	
	public void createCmsModulesTable() {
		
		execute((engine) -> {
			DbColumn idColumn = SqlBuilder
					.dbColumn()
					.name("id")
					.type(DbColumnType.LONG)
					.autoInc(true)
					.addConstraints(DbColumnConstraint.NOT_NULL)
					.build();
			
			DbColumn nameColumn = SqlBuilder
					.dbColumn()
					.name("name")
					.type(DbColumnType.STRING)
					.addConstraint(DbColumnConstraint.NOT_NULL)
					.size(30)
					.build();
			
			DbColumn titleColumn = SqlBuilder
					.dbColumn()
					.name("title")
					.type(DbColumnType.STRING)
					.addConstraint(DbColumnConstraint.NOT_NULL)
					.size(140)
					.build();
			
			DbColumn tableNameColumn = SqlBuilder
					.dbColumn()
					.name("table_name")
					.type(DbColumnType.STRING)
					.addConstraint(DbColumnConstraint.NOT_NULL)
					.size(64)
					.build();
			
			DbColumn orderedColumn = SqlBuilder
					.dbColumn()
					.name("ordered")
					.type(DbColumnType.BOOLEAN)
					.addConstraint(DbColumnConstraint.NOT_NULL)
					.build();
			
			DbColumn activatedColumn = SqlBuilder
					.dbColumn()
					.name("activated")
					.type(DbColumnType.BOOLEAN)
					.addConstraint(DbColumnConstraint.NOT_NULL)
					.build();
			
			DbColumn lovColumnIdColumn = SqlBuilder
					.dbColumn()
					.name("lov_column_id")
					.type(DbColumnType.LONG)
					.build();
			
			DbColumn orderNumColumn = SqlBuilder
					.dbColumn()
					.name("order_num")
					.type(DbColumnType.INT)
					.defaultValue(new K(0))
					.build();
			
			DbEntity cmsSessionsTable = SqlBuilder.dbEntity()
			        .name(TABLE_NAME_CMS_MODULES)
			        .addColumn(idColumn)
			        .addColumn(nameColumn)
			        .addColumn(titleColumn)
			        .addColumn(tableNameColumn)
			        .addColumn(orderedColumn)
			        .addColumn(activatedColumn)
			        .addColumn(lovColumnIdColumn)
			        .addColumn(orderNumColumn)
			        .pkFields("id")
			        .build();
			
		}, "Unable to create cms modules table");
	}
	
	public boolean tableExists(String tableName) {
		try {
			dslContext.fetchCount(DSL.selectFrom(DSL.table(tableName)));
		} catch(BadSqlGrammarException e) {
			return false;
		}
		
		return true;
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
