package com.agnosticcms.web.dao;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.InsertValuesStep13;
import org.jooq.InsertValuesStep3;
import org.jooq.InsertValuesStep7;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.agnosticcms.web.dto.CmsTable;
import com.agnosticcms.web.dto.ColumnType;
import com.agnosticcms.web.dto.ExternalModule;
import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.ModuleHierarchy;

/**
 * Data access object for handling module data in database
 */
@Repository
public class ModuleDao {

	@Autowired
	private DSLContext dslContext;
	
	/**
	 * Adds new modules to the database
	 * @param modules Modules to add
	 */
	public void insertModules(Collection<Module> modules) {
		this.<Module, InsertValuesStep7<Record, ?, ?, ?, ?, ?, ?, ?>>executeInsertBatch(modules, module -> {
			return dslContext.insertInto(
					table(CmsTable.MODULES.getTableName()), field("name"), field("title"), field("table_name"), field("ordered"),
							field("activated"), field("cms_module_column_id"), field("order_num"))
					.values(module.getName(), module.getTitle(), module.getTableName(), module.getOrdered(), module.getActivated(),
							module.getLovColumnId(), module.getOrderNum()
				);
		});
	}
	
	/**
	 * Registers new module columns in the database
	 * @param moduleColumns The module columns to add
	 */
	public void insertModuleColumns(Collection<ModuleColumn> moduleColumns) {
		this.<ModuleColumn, InsertValuesStep13<Record, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>executeInsertBatch(moduleColumns, moduleColumn -> {
			return dslContext.insertInto(
					table(CmsTable.MODULE_COLUMNS.getTableName()), field("cms_module_id"), field("name"), field("name_in_db"), field("type"),
							field("size"), field("type_info"), field("not_null"), field("default_value"), field("read_only"), field("show_in_list"), field("show_in_edit"),
							field("show_in_add"), field("order_num"))
					.values(moduleColumn.getModuleId(), moduleColumn.getName(), moduleColumn.getNameInDb(), moduleColumn.getType(),
							moduleColumn.getSize(), moduleColumn.getTypeInfo(), moduleColumn.getNotNull(), moduleColumn.getDefaultValue(), moduleColumn.getReadOnly(),
							moduleColumn.getShowInList(), moduleColumn.getShowInEdit(), moduleColumn.getShowInAdd(), moduleColumn.getOrderNum()
				);
		});
	}
	
	/**
	 * Registers new module hierachy elements to the database
	 * @param moduleColumns The module hierarchies to add
	 */
	public void insertModuleHierarchies(Collection<ModuleHierarchy> moduleHierarchies) {
		this.<ModuleHierarchy, InsertValuesStep3<Record, ?, ?, ?>>executeInsertBatch(moduleHierarchies, moduleHierarchy -> {
			return dslContext.insertInto(
					table(CmsTable.MODULE_HIERARCHY.getTableName()), field("cms_module_id"), field("cms_module2_id"), field("mandatory"))
					.values(moduleHierarchy.getModuleId(), moduleHierarchy.getModule2Id(), moduleHierarchy.getMandatory());
		});
	}
	
	/**
	 * @return An ordered list of all modules in database
	 */
	public List<Module> getAllModules() {
		return dslContext.selectFrom(table(CmsTable.MODULES.getTableName()))
				.orderBy(field("order_num"), field("id"))
				.fetch(new ModuleRecordMapper());
	}
	
	/**
	 * @return An ordered list of all external modules in database
	 */
	public List<ExternalModule> getAllExternalModules() {
		return dslContext.selectFrom(table(CmsTable.EXTERNAL_MODULES.getTableName()))
				.orderBy(field("order_num"), field("id"))
				.fetch(new ExternalModuleRecordMapper());
	}
	
	/**
	 * Gets a module for the given id
	 * @param id Id of module to retrieve
	 * @return The module
	 */
	public Module getModule(Long id) {
		Record row = dslContext.selectFrom(table(CmsTable.MODULES.getTableName()))
				.where(field("id").equal(id)).fetchOne();
		
		return row == null ? null : row.map(new ModuleRecordMapper());
	}
	
	/**
	 * Gets all columns for a module
	 * @param moduleId The id of the module
	 * @return An ordered list of all columns for a module
	 */
	public List<ModuleColumn> getModuleColumns(Long moduleId) {
		return dslContext.selectFrom(table(CmsTable.MODULE_COLUMNS.getTableName()))
				.where(field("cms_module_id", Long.class).equal(moduleId))
				.orderBy(field("order_num"), field("id"))
				.fetch(new ModuleColumnRecordMapper());
	}
	
	/**
	 * Retrieves multiple module columns by their ids
	 * @param columnIds Ids of the columns to retrieve
	 * @return A map of module columns in form columnId => @ModuleColumn
	 */
	public Map<Long, ModuleColumn> getColumnsByIds(List<Long> columnIds) {
		return dslContext.selectFrom(table(CmsTable.MODULE_COLUMNS.getTableName()))
				.where(field("id", Long.class).in(columnIds)).fetchMap(field("id", Long.class), new ModuleColumnRecordMapper());
	}
	
	/**
	 * Gets all parent modules of the given module id
	 * @param moduleId The id of the module to retrieve parents for
	 * @return Parent modules of the given module
	 */
	public List<Module> getParentModules(Long moduleId) {
		return dslContext.select(
					field("cm.id").as("id"), field("cm.name").as("name"), field("cm.title").as("title"),
					field("cm.table_name").as("table_name"), field("cm.ordered").as("ordered"),
					field("cm.activated").as("activated"), field("cm.cms_module_column_id").as("cms_module_column_id"),
					field("cm.order_num").as("order_num"))
				// determination of parent modules happens via module_hierarchy table
				.from(table(CmsTable.MODULE_HIERARCHY.getTableName()).as("cmh"))
				.join(table(CmsTable.MODULES.getTableName()).as("cm"))
				.on(field("cmh.cms_module_id").equal(field("cm.id")))
				.where(field("cmh.cms_module2_id").equal(moduleId))
				.orderBy(field("cmh.id"))
				.fetch(new ModuleRecordMapper());
	}
	
	/**
	 * Gets list of module hierarchy elements containing given module id as a child id
	 * @param moduleId Child module id of module hierarchies to retrieve
	 * @return List of module hierarchy element with a child id equal to given one
	 */
	public List<ModuleHierarchy> getModuleHierarchies(Long moduleId) {
		return dslContext.select(field("id"), field("cms_module_id"), field("cms_module2_id"), field("mandatory"))
				.from(table(CmsTable.MODULE_HIERARCHY.getTableName()))
				.where(field("cms_module2_id").equal(moduleId))
				.orderBy(field("id"))
				.fetch(new ModuleHierarchyMapper());
	}
	
	/**
	 * Helper function that executes batches in jOOQ manner
	 * @param objectsToInsert List of objects to insert into database
	 * @param mapFunction Function that maps given objects into insert statements
	 */
	private <T, Q extends Query> void executeInsertBatch(Collection<T> objectsToInsert, Function<? super T, ? extends Q> mapFunction) {
		List<Q> insertStatements = objectsToInsert.stream().map(mapFunction).collect(Collectors.toList());
		dslContext.batch(insertStatements).execute();
	}
	
	/**
	 * Updates activation status for given module
	 * @param moduleId Module id for which the activation status should be set
	 * @param activated Activation status to set
	 */
	public void setActivated(Long moduleId, boolean activated) {
		dslContext.update(table(CmsTable.MODULES.getTableName())).set(field("activated"), activated).where(field("id").eq(moduleId)).execute();
	}
	
	/**
	 * Mapper which maps database records into @Module objects
	 */
	private class ModuleRecordMapper implements RecordMapper<Record, Module> {

		@Override
		public Module map(Record r) {
			return new Module(r.getValue("id", Long.class), r.getValue("name", String.class), r.getValue("title", String.class),
					r.getValue("table_name", String.class), r.getValue("ordered", Boolean.class),
					r.getValue("activated", Boolean.class), r.getValue("cms_module_column_id", Long.class), r.getValue("order_num", Long.class));
		}
		
	}
	
	/**
	 * Mapper which maps database records into @ExternalModule objects
	 */
	private class ExternalModuleRecordMapper implements RecordMapper<Record, ExternalModule> {

		@Override
		public ExternalModule map(Record r) {
			return new ExternalModule(r.getValue("id", Long.class), r.getValue("name", String.class), r.getValue("url", String.class),
					r.getValue("activated", Boolean.class), r.getValue("order_num", Long.class));
		}
		
	}
	
	/**
	 * Mapper which maps database records into @ModuleColumn objects
	 */
	private class ModuleColumnRecordMapper implements RecordMapper<Record, ModuleColumn> {

		@Override
		public ModuleColumn map(Record r) {
			return new ModuleColumn(r.getValue("id", Long.class), r.getValue("cms_module_id", Long.class), r.getValue("name", String.class),
					r.getValue("name_in_db", String.class), r.getValue("type", ColumnType.class), r.getValue("size", Integer.class),
					r.getValue("type_info", String.class),
					r.getValue("not_null", Boolean.class), r.getValue("default_value", String.class), r.getValue("read_only", Boolean.class),
					r.getValue("show_in_list", Boolean.class), r.getValue("show_in_edit", Boolean.class), r.getValue("show_in_add", Boolean.class),
					r.getValue("order_num", Integer.class));
		}
		
	}
	
	/**
	 * Mapper which maps database records into @ModuleHierarchy objects
	 */
	private class ModuleHierarchyMapper implements RecordMapper<Record, ModuleHierarchy> {

		@Override
		public ModuleHierarchy map(Record r) {
			return new ModuleHierarchy(r.getValue("id", Long.class), r.getValue("cms_module_id", Long.class), r.getValue("cms_module2_id", Long.class), r.getValue("mandatory", Boolean.class));
		}
		
	}

	
	
}
