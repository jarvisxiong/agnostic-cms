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

@Service
public class InitDbService {

	@Autowired
	private SchemaDao schemaDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CrypService crypService;
	
	@Autowired
	private ModuleDao moduleDao;
	
	@Autowired
	private ModuleService moduleService;
	
	public boolean initDb() {
		
		if(schemaDao.cmsTablesExist()) {
			return false;
		}
		
		schemaDao.createCmsUsersTable();
		schemaDao.createCmsSessionsTable();
		schemaDao.createCmsModulesTable();
		schemaDao.createCmsModuleColumnsTable();
		schemaDao.createCmsModuleHierarchyTable();
		schemaDao.createCmsExtenalModulesTable();
		
		userDao.createUser("admin", crypService.getSha1Base64ForApache("admin"));
		
		List<Module> modules = new ArrayList<>();
		modules.add(new Module("Modules", "Modules", CmsTable.MODULES.getTableName(), true, true, 1l, 1l));
		modules.add(new Module("Module Columns", "Module Columns", CmsTable.MODULE_COLUMNS.getTableName(), true, true, 7l, 2l));
		modules.add(new Module("Module Hierarchy", "Module Hierarchy", CmsTable.MODULE_HIERARCHY.getTableName(), false, true, null, 3l));
		modules.add(new Module("External Modules", "External Modules", CmsTable.EXTERNAL_MODULES.getTableName(), true, true, 18l, 4l));
		moduleDao.insertModules(modules);
		
		String columnTypesConcat = moduleService.getAllColumnTypesAsString();
		
		List<ModuleColumn> moduleColumns = new ArrayList<>();
		moduleColumns.add(new ModuleColumn(1l, "Name", "name", ColumnType.STRING, 30, null, true, null, false, true, true, true, 1));
		moduleColumns.add(new ModuleColumn(1l, "Title", "title", ColumnType.STRING, 140, null, true, null, false, true, true, true, 2));
		moduleColumns.add(new ModuleColumn(1l, "Table Name", "table_name", ColumnType.STRING, 64, null, true, null, false, true, true, true, 3));
		moduleColumns.add(new ModuleColumn(1l, "Is Ordered", "ordered", ColumnType.BOOL, null, null, true, null, false, true, true, true, 4));
		moduleColumns.add(new ModuleColumn(1l, "Activated", "activated", ColumnType.BOOL, null, null, true, "false", true, true, false, false, 5));
		moduleColumns.add(new ModuleColumn(2l, "Name", "name", ColumnType.STRING, 50, null, true, null, false, true, true, true, 1));
		moduleColumns.add(new ModuleColumn(2l, "Name id DB", "name_in_db", ColumnType.STRING, 64, null, true, null, false, true, true, true, 2));
		moduleColumns.add(new ModuleColumn(2l, "Type", "type", ColumnType.ENUM, 20, columnTypesConcat, true, null, false, true, true, true, 3));
		moduleColumns.add(new ModuleColumn(2l, "Size", "size", ColumnType.INT, null, null, false, null, false, true, true, true, 4));
		moduleColumns.add(new ModuleColumn(2l, "Type Info", "type_info", ColumnType.STRING, 300, null, false, null, false, false, true, true, 3));
		moduleColumns.add(new ModuleColumn(2l, "Not Null", "not_null", ColumnType.BOOL, null, null, true, null, false, true, true, true, 5));
		moduleColumns.add(new ModuleColumn(2l, "Default", "default_value", ColumnType.STRING, 200, null, false, null, false, true, true, true, 6));
		moduleColumns.add(new ModuleColumn(2l, "Read Only", "read_only", ColumnType.BOOL, null, null, true, null, false, true, true, true, 7));
		moduleColumns.add(new ModuleColumn(2l, "Show in List", "show_in_list", ColumnType.BOOL, null, null, true, null, false, true, true, true, 8));
		moduleColumns.add(new ModuleColumn(2l, "Show in Edit", "show_in_edit", ColumnType.BOOL, null, null, true, null, false, true, true, true, 9));
		moduleColumns.add(new ModuleColumn(2l, "Show in Add", "show_in_add", ColumnType.BOOL, null, null, true, "true", false, true, true, true, 10));
		moduleColumns.add(new ModuleColumn(3l, "Mandatory", "mandatory", ColumnType.BOOL, null, null, true, "false", false, true, true, true, 11));
		moduleColumns.add(new ModuleColumn(4l, "Name", "name", ColumnType.STRING, 30, null, true, null, false, true, true, true, 1));
		moduleColumns.add(new ModuleColumn(4l, "URL", "url", ColumnType.STRING, 300, null, true, null, false, true, true, true, 2));
		moduleColumns.add(new ModuleColumn(4l, "Activated", "activated", ColumnType.BOOL, null, null, true, "true", false, true, true, true, 3));
		moduleDao.insertModuleColumns(moduleColumns);
		
		List<ModuleHierarchy> moduleHierarchies = new ArrayList<>();
		moduleHierarchies.add(new ModuleHierarchy(2l, 1l, false));
		moduleHierarchies.add(new ModuleHierarchy(1l, 2l, true));
		moduleHierarchies.add(new ModuleHierarchy(1l, 3l, true));
		moduleHierarchies.add(new ModuleHierarchy(1l, 3l, true));
		moduleDao.insertModuleHierarchies(moduleHierarchies);
		
		schemaDao.createCmsModulesFk();
		
		return true;
	}
	
}
