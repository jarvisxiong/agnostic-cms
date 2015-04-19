package com.agnosticcms.web.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Repository;

import com.agnosticcms.web.dbutil.PdbEngineProvider;
import com.agnosticcms.web.dto.CmsTable;
import com.agnosticcms.web.dto.ColumnType;
import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.ModuleHierarchy;
import com.agnosticcms.web.exception.DaoRuntimeException;
import com.agnosticcms.web.exception.TypeConversionException;
import com.agnosticcms.web.service.ColumnTypeService;
import com.agnosticcms.web.service.CrypService;
import com.feedzai.commons.sql.abstraction.ddl.DbColumn.Builder;
import com.feedzai.commons.sql.abstraction.ddl.DbColumnConstraint;
import com.feedzai.commons.sql.abstraction.ddl.DbColumnType;
import com.feedzai.commons.sql.abstraction.ddl.DbEntity;
import com.feedzai.commons.sql.abstraction.dml.K;
import com.feedzai.commons.sql.abstraction.dml.dialect.SqlBuilder;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngine;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngineException;

@Repository
public class SchemaDao {
	
	@Autowired
	private PdbEngineProvider pdbEngineProvider;
	
	@Autowired
	private CrypService crypService;
	
	@Autowired
	private DSLContext dslContext;
	
	@Autowired
	private ColumnTypeService columnTypeService;
	
	
	public boolean cmsTablesExist() {
		
		for(CmsTable cmsTable : CmsTable.values()) {
			if(!tableExists(cmsTable.getTableName())) {
				return false;
			}
		}
		
		return true;
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
			        .name(CmsTable.SESSIONS.getTableName())
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
	        .name(CmsTable.MODULES.getTableName())
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
				SqlBuilder.dbFk().addColumn("cms_module_column_id").foreignTable(CmsTable.MODULE_COLUMNS.getTableName()).addForeignColumn("id")
			).build();
			
			engine.updateEntity(fkUpdate);
		}, "Unable to create cms modules table");
	}
	
	public void createCmsModuleColumnsTable() {
		
		execute((engine) -> {
			
			DbEntity cmsModulesTable = SqlBuilder.dbEntity()
			        .name(CmsTable.MODULE_COLUMNS.getTableName())
			        .addColumn(SqlBuilder.dbColumn().name("id").type(DbColumnType.LONG).autoInc(true).addConstraints(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("cms_module_id").type(DbColumnType.LONG).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("name").type(DbColumnType.STRING).addConstraint(DbColumnConstraint.NOT_NULL).size(50).build())
			        .addColumn(SqlBuilder.dbColumn().name("name_in_db").type(DbColumnType.STRING).addConstraint(DbColumnConstraint.NOT_NULL).size(64).build())
			        .addColumn(SqlBuilder.dbColumn().name("type").type(DbColumnType.STRING).addConstraint(DbColumnConstraint.NOT_NULL).size(20).build())
			        .addColumn(SqlBuilder.dbColumn().name("size").type(DbColumnType.INT).build())
			        .addColumn(SqlBuilder.dbColumn().name("type_info").type(DbColumnType.STRING).build())
			        .addColumn(SqlBuilder.dbColumn().name("not_null").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("default_value").type(DbColumnType.STRING).size(200).build())
			        .addColumn(SqlBuilder.dbColumn().name("read_only").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("show_in_list").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("show_in_edit").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("show_in_add").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("order_num").type(DbColumnType.LONG).defaultValue(new K(0)).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .pkFields("id")
			        .addFk(SqlBuilder.dbFk().addColumn("cms_module_id").foreignTable(CmsTable.MODULES.getTableName()).addForeignColumn("id"))
			        .build();
			
			engine.addEntity(cmsModulesTable);
			
		}, "Unable to create cms module columns table");
	}
	
	public void createCmsModuleHierarchyTable() {
		
		execute((engine) -> {
			
			DbEntity cmsModuleHierarchyTable = SqlBuilder.dbEntity()
			        .name(CmsTable.MODULE_HIERARCHY.getTableName())
			        .addColumn(SqlBuilder.dbColumn().name("id").type(DbColumnType.LONG).autoInc(true).addConstraints(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("cms_module_id").type(DbColumnType.LONG).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("cms_module2_id").type(DbColumnType.LONG).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .addColumn(SqlBuilder.dbColumn().name("mandatory").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
			        .pkFields("id")
			        .addFk(SqlBuilder.dbFk().addColumn("cms_module_id").foreignTable(CmsTable.MODULES.getTableName()).addForeignColumn("id"))
			        .addFk(SqlBuilder.dbFk().addColumn("cms_module2_id").foreignTable(CmsTable.MODULES.getTableName()).addForeignColumn("id"))
			        .build();
			
			engine.addEntity(cmsModuleHierarchyTable);
			
		}, "Unable to create cms module columns table");
	}
	
	public void createCmsExtenalModulesTable() {
		
		execute((engine) -> {
			
			DbEntity cmsExtenalModulesTable = SqlBuilder.dbEntity()
				        .name(CmsTable.EXTERNAL_MODULES.getTableName())
				        .addColumn(SqlBuilder.dbColumn().name("id").type(DbColumnType.LONG).autoInc(true).addConstraints(DbColumnConstraint.NOT_NULL).build())
				        .addColumn(SqlBuilder.dbColumn().name("name").type(DbColumnType.STRING).addConstraint(DbColumnConstraint.NOT_NULL).size(30).build())
				        .addColumn(SqlBuilder.dbColumn().name("url").type(DbColumnType.STRING).addConstraint(DbColumnConstraint.NOT_NULL).size(400).build())
				        .addColumn(SqlBuilder.dbColumn().name("activated").type(DbColumnType.BOOLEAN).addConstraint(DbColumnConstraint.NOT_NULL).build())
				        .addColumn(SqlBuilder.dbColumn().name("order_num").type(DbColumnType.LONG).defaultValue(new K(0)).build())
				        .pkFields("id")
				        .build();
			
			engine.addEntity(cmsExtenalModulesTable);
			
		}, "Unable to create cms external modules table");
	}
	
	public void createOrUpdateModuleSchema(Module module, List<Module> parentModules, List<ModuleHierarchy> moduleHierarchies, List<ModuleColumn> moduleColumns) {
		
		execute((engine) -> {
		
			DbEntity.Builder tableBuilder = SqlBuilder.dbEntity();
			tableBuilder.name(module.getTableName());
			tableBuilder.addColumn(SqlBuilder.dbColumn().name("id").type(DbColumnType.LONG).autoInc(true).addConstraints(DbColumnConstraint.NOT_NULL).build());
			tableBuilder.pkFields("id");
			
			if(CollectionUtils.isNotEmpty(parentModules)) {
				Iterator<Module> parentModulesIt = parentModules.iterator();
				Iterator<ModuleHierarchy> moduleHierarchiesIt = moduleHierarchies.iterator();
				Iterator<String> foreignKeyColumnNamesIt = getForeignKeyColumnNames(parentModules).iterator();
				
				while(parentModulesIt.hasNext()) {
					
					Module parentModule = parentModulesIt.next();
					ModuleHierarchy moduleHierarchy = moduleHierarchiesIt.next();
					String foreignKeyColumnName = foreignKeyColumnNamesIt.next();

					Builder foreignKeyColumnBuilder = SqlBuilder.dbColumn().name(foreignKeyColumnName).type(DbColumnType.LONG);
					if(moduleHierarchy.getMandatory()) {
						foreignKeyColumnBuilder.addConstraint(DbColumnConstraint.NOT_NULL);
					}
					
					tableBuilder.addColumn(foreignKeyColumnBuilder.build());
					tableBuilder.addFk(SqlBuilder.dbFk().addColumn(foreignKeyColumnName).foreignTable(parentModule.getTableName()).addForeignColumn("id"));
				}
			}
			
			for(ModuleColumn moduleColumn : moduleColumns) {
				Builder columnBuilder = SqlBuilder.dbColumn();
				columnBuilder.name(moduleColumn.getNameInDb());
				
				DbColumnType dbColumnType;
				ColumnType moduleColumnType = moduleColumn.getType();
				switch (moduleColumnType) {
				case BOOL:
					dbColumnType = DbColumnType.BOOLEAN;
					break;
				case INT:
					dbColumnType = DbColumnType.INT;
					break;
				case LONG:
					dbColumnType = DbColumnType.LONG;
					break;
				case DECIMAL:
					dbColumnType = DbColumnType.DOUBLE;
					break;
				case HTML:
				case STRING:
					dbColumnType = DbColumnType.STRING;
					
					Integer size = moduleColumn.getSize();
					if(size != null) {
						columnBuilder.size(size);
					}
					
					break;
				case ENUM:
				case IMAGE:
					dbColumnType = DbColumnType.STRING;
					break;
				default:
					throw new DaoRuntimeException("Unsupported module column type " + moduleColumnType);
				}
				
				columnBuilder.type(dbColumnType);
				
				if(moduleColumn.getNotNull()) {
					columnBuilder.addConstraint(DbColumnConstraint.NOT_NULL);
				}
				
				
				String defaultValueString = moduleColumn.getDefaultValue();
				if(StringUtils.isNotEmpty(defaultValueString)) {
					try {
						Object defaultValue = columnTypeService.parseFromString(moduleColumn.getDefaultValue(), moduleColumnType);
						columnBuilder.defaultValue(new K(defaultValue));
					} catch (TypeConversionException e) {
						throw new DaoRuntimeException("Type conversion error during creation of a table", e);
					}
				}
				
				tableBuilder.addColumn(columnBuilder.build());
			}
			
			if(module.getOrdered()) {
				tableBuilder.addColumn(SqlBuilder.dbColumn().name("order_num").type(DbColumnType.LONG).defaultValue(new K(0)).addConstraint(DbColumnConstraint.NOT_NULL).build());
			}
		
			engine.updateEntity(tableBuilder.build());
			
		}, "Unable to create schema for module with name " + module.getName());
	}
	
	public List<String> getForeignKeyColumnNames(List<Module> parentModules) {
		
		Map<String, Integer> tableNameCountMap = new HashMap<>();
		List<String> fkNames = new ArrayList<>();
		
		for(Module parentModule : parentModules) {
			String tableName = parentModule.getTableName();
			Integer tableNameCount = tableNameCountMap.get(tableName);
			
			tableNameCount = (tableNameCount == null) ? 1 : tableNameCount + 1;
			tableNameCountMap.put(tableName, tableNameCount);
			
			if(tableNameCount > 1) {
				// TODO this should be brought to view someday
				parentModule.setName(parentModule.getName() + " " + tableNameCount);
			}
			
			fkNames.add(getForeignKeyColumnId(tableName, tableNameCount));
		}
		
		return fkNames;
	}
	
	private String getForeignKeyColumnId(String tableName, Integer tableNameCount) {
		
		String foreignKeyId;
		
		// TODO re-enable this
		if(tableName.endsWith("ies")) {
			foreignKeyId = curFromEnd(tableName, 3) + "y";
		} else if(tableName.endsWith("s")) {
			foreignKeyId = curFromEnd(tableName, 1);
		} else {
			throw new IllegalArgumentException("Unable to singularize table name " + tableName);
		}
		
		if(tableNameCount > 1) {
			foreignKeyId += tableNameCount;
		}
		
		foreignKeyId += "_id";
		
		return foreignKeyId;
	}
	
	private String curFromEnd(String string, int count) {
		return string.substring(0, string.length() - count);
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
