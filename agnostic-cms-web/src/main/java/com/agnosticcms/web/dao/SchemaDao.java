package com.agnosticcms.web.dao;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Repository;


import com.agnosticcms.web.dbutil.PdbEngineProvider;
import com.agnosticcms.web.exception.DaoRuntimeException;
import com.agnosticcms.web.service.CrypService;
import com.feedzai.commons.sql.abstraction.ddl.DbColumnConstraint;
import com.feedzai.commons.sql.abstraction.ddl.DbColumnType;
import com.feedzai.commons.sql.abstraction.ddl.DbEntity;
import com.feedzai.commons.sql.abstraction.dml.K;
import com.feedzai.commons.sql.abstraction.dml.dialect.SqlBuilder;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngine;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngineException;

@Repository
public class SchemaDao {

	public static final String TABLE_NAME_CMS_USERS = "cms_users";
	public static final String TABLE_NAME_CMS_SESSIONS = "cms_sessions";
	public static final String TABLE_NAME_CMS_MODULES = "cms_modules";
	public static final String TABLE_NAME_CMS_MODULE_COLUMNS = "cms_module_columns";
	public static final String TABLE_NAME_CMS_MODULE_HIERARCHY = "cms_module_hierarchy";
	
	@Autowired
	private PdbEngineProvider pdbEngineProvider;
	
	@Autowired
	private CrypService crypService;
	
	@Autowired
	private DSLContext dslContext;
	
	public boolean cmsTablesExist() {
		return tableExists(TABLE_NAME_CMS_USERS) && tableExists(TABLE_NAME_CMS_SESSIONS) && tableExists(TABLE_NAME_CMS_MODULES)
				&& tableExists(TABLE_NAME_CMS_MODULE_COLUMNS) && tableExists(TABLE_NAME_CMS_MODULE_HIERARCHY);
	}

	public void createCmsUsersTable() {
		
		execute((engine) -> {
			
			DbEntity cmsUsersTable = SqlBuilder.dbEntity()
			        .name("cms_users")
					.addColumn(SqlBuilder.dbColumn().name("username").type(DbColumnType.STRING).size(30).addConstraints(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("password").type(DbColumnType.STRING).size(33).addConstraints(DbColumnConstraint.NOT_NULL).build())
			        .pkFields("username")
			        .build();
			
			engine.addEntity(cmsUsersTable);
		}, "Unable to create cms users table");
	}
	
	public void createCmsSessionsTable() {
		
		execute((engine) -> {
			
			DbEntity cmsSessionsTable = SqlBuilder.dbEntity()
			        .name(TABLE_NAME_CMS_SESSIONS)
			        .addColumn(SqlBuilder.dbColumn().name("key").type(DbColumnType.STRING).size(36).addConstraints(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("value").type(DbColumnType.STRING).size(5000).addConstraints(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("expiry").type(DbColumnType.LONG).addConstraints(DbColumnConstraint.NOT_NULL).build())
			        .pkFields("key")
			        .build();
			
			engine.addEntity(cmsSessionsTable);
		}, "Unable to create cms users table");
	}
	
	public void createCmsModulesTable() {
		
		execute((engine) -> {
			engine.addEntity(getCmsModulesTableDefinition());
		}, "Unable to create cms modules table");
	}
	
	private DbEntity getCmsModulesTableDefinition() {
		return SqlBuilder.dbEntity()
	        .name(TABLE_NAME_CMS_MODULES)
	        .addColumn(SqlBuilder.dbColumn().name("id").type(DbColumnType.LONG).autoInc(true).addConstraints(DbColumnConstraint.NOT_NULL).build())
	        .addColumn(SqlBuilder.dbColumn().name("name").type(DbColumnType.STRING).addConstraint(DbColumnConstraint.NOT_NULL).size(30).build())
	        .addColumn(SqlBuilder.dbColumn().name("title").type(DbColumnType.STRING).addConstraint(DbColumnConstraint.NOT_NULL).size(140).build())
	        .addColumn(SqlBuilder.dbColumn().name("table_name").type(DbColumnType.STRING).addConstraint(DbColumnConstraint.NOT_NULL).size(64).build())
	        .addColumn(SqlBuilder.dbColumn().name("ordered").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
	        .addColumn(SqlBuilder.dbColumn().name("activated").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
	        .addColumn(SqlBuilder.dbColumn().name("cms_module_column_id").type(DbColumnType.LONG).build())
	        .addColumn(SqlBuilder.dbColumn().name("order_num").type(DbColumnType.LONG).defaultValue(new K(0)).build())
	        .pkFields("id")
	        .build();
	}
	
	/**
	 * Needed because cms_modules and cms_module_columns have dependency on each other,
	 * hence fk cannot be inserted on table creation time
	 */
	public void createCmsModulesFk() {
		
		execute((engine) -> {
			
			DbEntity fkUpdate = getCmsModulesTableDefinition().newBuilder().addFk(
				SqlBuilder.dbFk().addColumn("cms_module_column_id").foreignTable(TABLE_NAME_CMS_MODULE_COLUMNS).addForeignColumn("id")
			).build();
			
			engine.updateEntity(fkUpdate);
		}, "Unable to create cms modules table");
	}
	
	public void createCmsModuleColumnsTable() {
		
		execute((engine) -> {
			
			DbEntity cmsModulesTable = SqlBuilder.dbEntity()
			        .name(TABLE_NAME_CMS_MODULE_COLUMNS)
			        .addColumn(SqlBuilder.dbColumn().name("id").type(DbColumnType.LONG).autoInc(true).addConstraints(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("cms_module_id").type(DbColumnType.LONG).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("name").type(DbColumnType.STRING).addConstraint(DbColumnConstraint.NOT_NULL).size(50).build())
			        .addColumn(SqlBuilder.dbColumn().name("name_in_db").type(DbColumnType.STRING).addConstraint(DbColumnConstraint.NOT_NULL).size(64).build())
			        .addColumn(SqlBuilder.dbColumn().name("type").type(DbColumnType.STRING).addConstraint(DbColumnConstraint.NOT_NULL).size(20).build())
			        .addColumn(SqlBuilder.dbColumn().name("size").type(DbColumnType.INT).build())
			        .addColumn(SqlBuilder.dbColumn().name("not_null").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("default_value").type(DbColumnType.STRING).size(200).build())
			        .addColumn(SqlBuilder.dbColumn().name("read_only").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("show_in_list").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("show_in_edit").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("order_num").type(DbColumnType.LONG).defaultValue(new K(0)).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .pkFields("id")
			        .addFk(SqlBuilder.dbFk().addColumn("cms_module_id").foreignTable(TABLE_NAME_CMS_MODULES).addForeignColumn("id"))
			        .build();
			
			engine.addEntity(cmsModulesTable);
			
		}, "Unable to create cms module columns table");
	}
	
	public void createCmsModuleHierarchyTable() {
		
		execute((engine) -> {
			
			DbEntity cmsModuleHierarchyTable = SqlBuilder.dbEntity()
			        .name(TABLE_NAME_CMS_MODULE_HIERARCHY)
			        .addColumn(SqlBuilder.dbColumn().name("id").type(DbColumnType.LONG).autoInc(true).addConstraints(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("cms_module_id").type(DbColumnType.LONG).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("cms_module2_id").type(DbColumnType.LONG).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("mandatory").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .pkFields("id")
			        .addFk(SqlBuilder.dbFk().addColumn("cms_module_id").foreignTable(TABLE_NAME_CMS_MODULES).addForeignColumn("id"))
			        .addFk(SqlBuilder.dbFk().addColumn("cms_module2_id").foreignTable(TABLE_NAME_CMS_MODULES).addForeignColumn("id"))
			        .build();
			
			engine.addEntity(cmsModuleHierarchyTable);
			
		}, "Unable to create cms module columns table");
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
