package com.agnosticcms.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agnosticcms.web.dao.ModuleDao;
import com.agnosticcms.web.dao.SchemaDao;
import com.agnosticcms.web.dao.UserDao;
import com.agnosticcms.web.dto.CmsTable;
import com.agnosticcms.web.dto.ColumnType;
import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.ModuleHierarchy;

/**
 * Service for initial Agnostic CMS database structure creation
 */
@Service
public class InitDbService {

	@Autowired
	private SchemaDao schemaDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CryptService crypService;
	
	@Autowired
	private ModuleDao moduleDao;
	
	@Autowired
	private ModuleService moduleService;
	
	/**
	 * Initializes Agnostic CMS database structure
	 * @return true, if creation successful, false if tables already exist
	 */
	public boolean initDb() {
		
		// if tables already exist - no-op
		if(schemaDao.cmsTablesExist()) {
			return false;
		}
		
		// create tables
		schemaDao.createCmsUsersTable();
		schemaDao.createCmsSessionsTable();
		schemaDao.createCmsModulesTable();
		schemaDao.createCmsModuleColumnsTable();
		schemaDao.createCmsModuleHierarchyTable();
		schemaDao.createCmsExtenalModulesTable();
		
		// Create inital user
		userDao.createUser("admin", crypService.getSha1Base64ForApache("admin"));
		
		// Create module meta-information into their own structure
		List<Module> modules = new ArrayList<>();
		//						name				title				tableName 									ordered activated 	lovColumnId orderNum
		modules.add(new Module(	"Modules",			"Modules", 			CmsTable.MODULES.getTableName(), 			true, 	true, 		1l, 		1l));
		modules.add(new Module(	"Module Columns",	"Module Columns", 	CmsTable.MODULE_COLUMNS.getTableName(), 	true, 	true, 		7l, 		2l));
		modules.add(new Module(	"Module Hierarchy",	"Module Hierarchy", CmsTable.MODULE_HIERARCHY.getTableName(), 	false, 	true, 		null, 		3l));
		modules.add(new Module(	"External Modules",	"External Modules", CmsTable.EXTERNAL_MODULES.getTableName(), 	true, 	true, 		18l, 		4l));
		moduleDao.insertModules(modules);
		
		// All column type in insert into ENUM's type info field
		String columnTypesConcat = moduleService.getAllColumnTypesAsString();
		
		// Create module column meta-information into their own structure
		List<ModuleColumn> moduleColumns = new ArrayList<>();
		//									modulesId 	name 			nameInDb 			type 				size	typeInfo			notNull	defaultValue	readOnly	showInList	showInEdit 	showInAdd 	orderNum
		moduleColumns.add(new ModuleColumn(	1l, 		"Name", 		"name", 			ColumnType.STRING, 	30, 	null, 				true, 	null, 			false, 		true, 		true, 		true, 		1));
		moduleColumns.add(new ModuleColumn(	1l, 		"Title", 		"title", 			ColumnType.STRING, 	140, 	null, 				true, 	null, 			false, 		true, 		true, 		true, 		2));
		moduleColumns.add(new ModuleColumn(	1l, 		"Table Name", 	"table_name", 		ColumnType.STRING, 	64, 	null, 				true, 	null, 			false, 		true, 		true, 		true, 		3));
		moduleColumns.add(new ModuleColumn(	1l, 		"Is Ordered", 	"ordered", 			ColumnType.BOOL, 	null, 	null, 				true, 	null, 			false, 		true, 		true, 		true, 		4));
		moduleColumns.add(new ModuleColumn(	1l, 		"Activated", 	"activated", 		ColumnType.BOOL, 	null, 	null, 				true, 	"false", 		true, 		true, 		false, 		false, 		5));
		moduleColumns.add(new ModuleColumn(	2l, 		"Name", 		"name", 			ColumnType.STRING, 	50, 	null, 				true, 	null, 			false, 		true, 		true, 		true, 		1));
		moduleColumns.add(new ModuleColumn(	2l, 		"Name id DB", 	"name_in_db", 		ColumnType.STRING, 	64, 	null, 				true, 	null, 			false, 		true, 		true, 		true, 		2));
		moduleColumns.add(new ModuleColumn(	2l, 		"Type", "type",						ColumnType.ENUM, 	20, 	columnTypesConcat,	true, 	null, 			false, 		true, 		true, 		true, 		3));
		moduleColumns.add(new ModuleColumn(	2l, 		"Size", "size",						ColumnType.INT, 	null, 	null, 				false, 	null, 			false, 		true, 		true, 		true, 		4));
		moduleColumns.add(new ModuleColumn(	2l, 		"Type Info",	"type_info", 		ColumnType.STRING, 	300, 	null, 				false, 	null, 			false, 		false, 		true, 		true, 		3));
		moduleColumns.add(new ModuleColumn(	2l, 		"Not Null",		"not_null", 		ColumnType.BOOL, 	null, 	null, 				true, 	null, 			false, 		true, 		true, 		true, 		5));
		moduleColumns.add(new ModuleColumn(	2l, 		"Default",		"default_value", 	ColumnType.STRING, 	200, 	null, 				false, 	null, 			false, 		true, 		true, 		true, 		6));
		moduleColumns.add(new ModuleColumn(	2l, 		"Read Only",	"read_only", 		ColumnType.BOOL, 	null, 	null, 				true, 	null, 			false, 		true, 		true, 		true, 		7));
		moduleColumns.add(new ModuleColumn(	2l, 		"Show in List",	"show_in_list", 	ColumnType.BOOL, 	null, 	null, 				true, 	null, 			false, 		true, 		true, 		true, 		8));
		moduleColumns.add(new ModuleColumn(	2l, 		"Show in Edit",	"show_in_edit", 	ColumnType.BOOL, 	null, 	null, 				true, 	null, 			false, 		true, 		true, 		true, 		9));
		moduleColumns.add(new ModuleColumn(	2l, 		"Show in Add",	"show_in_add", 		ColumnType.BOOL, 	null, 	null, 				true, 	"true", 		false, 		true, 		true, 		true, 		10));
		moduleColumns.add(new ModuleColumn(	3l, 		"Mandatory",	"mandatory", 		ColumnType.BOOL, 	null, 	null, 				true, 	"false", 		false, 		true, 		true, 		true, 		11));
		moduleColumns.add(new ModuleColumn(	4l, 		"Name", 		"name", 			ColumnType.STRING, 	30, 	null, 				true, 	null, 			false, 		true, 		true, 		true, 		1));
		moduleColumns.add(new ModuleColumn(	4l, 		"URL", 			"url", 				ColumnType.STRING, 	300, 	null, 				true, 	null, 			false, 		true, 		true, 		true, 		2));
		moduleColumns.add(new ModuleColumn(	4l, 		"Activated", 	"activated", 		ColumnType.BOOL, 	null, 	null, 				true, 	"true", 		false, 		true, 		true, 		true, 		3));
		moduleDao.insertModuleColumns(moduleColumns);
		
		// Create module hierarchy meta-information into their own structure
		List<ModuleHierarchy> moduleHierarchies = new ArrayList<>();
		// 											parentModule	childModule mandatory
		moduleHierarchies.add(new ModuleHierarchy(	2l, 			1l, 		false));
		moduleHierarchies.add(new ModuleHierarchy(	1l, 			2l, 		true));
		moduleHierarchies.add(new ModuleHierarchy(	1l, 			3l, 		true));
		moduleHierarchies.add(new ModuleHierarchy(	1l, 			3l, 		true));
		moduleDao.insertModuleHierarchies(moduleHierarchies);
		
		// Foreign keys for modules table are created as last ones because of cross-dependencies with module column table
		schemaDao.createCmsModulesFk();
		
		return true;
	}
	
}
