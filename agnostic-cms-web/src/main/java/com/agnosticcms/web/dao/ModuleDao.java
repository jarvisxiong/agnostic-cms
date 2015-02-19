package com.agnosticcms.web.dao;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.InsertValuesStep12;
import org.jooq.InsertValuesStep3;
import org.jooq.InsertValuesStep8;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.ModuleHierarchy;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
public class ModuleDao {

	@Autowired
	private DSLContext dslContext;
	
	public void insertModules(Collection<Module> modules) {
		this.<Module, InsertValuesStep8<Record, ?, ?, ?, ?, ?, ?, ?, ?>>executeInsertBatch(modules, module -> {
			return dslContext.insertInto(
					table(SchemaDao.TABLE_NAME_CMS_MODULES), field("id"), field("name"), field("title"), field("table_name"), field("ordered"),
							field("activated"), field("lov_column_id"), field("order_num"))
					.values(module.getId(), module.getName(), module.getTitle(), module.getTableName(), module.getOrdered(), module.getOrdered(),
							module.getLovColumnId(), module.getOrderNum()
				);
		});
	}
	
	public void insertModuleColumns(Collection<ModuleColumn> moduleColumns) {
		this.<ModuleColumn, InsertValuesStep12<Record, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>executeInsertBatch(moduleColumns, moduleColumn -> {
			return dslContext.insertInto(
					table(SchemaDao.TABLE_NAME_CMS_MODULE_COLUMNS), field("id"), field("module_id"), field("name"), field("name_in_db"), field("type"),
							field("size"), field("not_null"), field("default_value"), field("read_only"), field("show_in_list"), field("show_in_edit"), field("order_num"))
					.values(moduleColumn.getId(), moduleColumn.getModuleId(), moduleColumn.getName(), moduleColumn.getNameInDb(), moduleColumn.getType(),
							moduleColumn.getSize(), moduleColumn.getNotNull(), moduleColumn.getDefaultValue(), moduleColumn.getReadOnly(),
							moduleColumn.getShowInEdit(), moduleColumn.getShowInEdit(), moduleColumn.getOrderNum()
				);
		});
	}
	
	public void insertModuleHierarchies(Collection<ModuleHierarchy> moduleHierarchies) {
		this.<ModuleHierarchy, InsertValuesStep3<Record, ?, ?, ?>>executeInsertBatch(moduleHierarchies, moduleHierarchy -> {
			return dslContext.insertInto(
					table(SchemaDao.TABLE_NAME_CMS_MODULES_HIERARCHY), field("id"), field("module_id"), field("module2_id"))
					.values(moduleHierarchy.getId(), moduleHierarchy.getModule_id(), moduleHierarchy.getModule2_id());
		});
	}
	
	public List<Module> getAllModules() {
		return dslContext.selectFrom(DSL.table(SchemaDao.TABLE_NAME_CMS_MODULES)).fetch(r -> {
			return new Module(r.getValue("id", Long.class), r.getValue("name", String.class), r.getValue("title", String.class),
					r.getValue("table_name", String.class), r.getValue("ordered", Boolean.class),
					r.getValue("activated", Boolean.class), r.getValue("lov_column_id", Long.class), r.getValue("order_num", Long.class));
		});
	}
	
	public List<ModuleColumn> getAllModuleColumns() {
		return dslContext.selectFrom(DSL.table(SchemaDao.TABLE_NAME_CMS_MODULES)).fetch(r -> {
			return new ModuleColumn(r.getValue("id", Long.class), r.getValue("module_id", Long.class), r.getValue("name", String.class),
					r.getValue("name_in_db", String.class), r.getValue("type", String.class), r.getValue("size", Integer.class),
					r.getValue("not_null", Boolean.class), r.getValue("default_value", String.class), r.getValue("read_only", Boolean.class),
					r.getValue("show_in_list", Boolean.class), r.getValue("show_in_edit", Boolean.class), r.getValue("order_num", Integer.class));
		});
	}
	
	private <T, Q extends Query> void executeInsertBatch(Collection<T> objectsToInsert, Function<? super T, ? extends Q> mapFunction) {
		List<Q> insertStatements = objectsToInsert.stream().map(mapFunction).collect(Collectors.toList());
		dslContext.batch(insertStatements).execute();
	}
	
}
