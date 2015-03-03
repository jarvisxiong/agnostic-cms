package com.agnosticcms.web.dao;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.InsertValuesStep11;
import org.jooq.InsertValuesStep3;
import org.jooq.InsertValuesStep7;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.agnosticcms.web.dto.CmsTables;
import com.agnosticcms.web.dto.ColumnType;
import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.ModuleHierarchy;

@Repository
public class ModuleDao {

	@Autowired
	private DSLContext dslContext;
	
	public void insertModules(Collection<Module> modules) {
		this.<Module, InsertValuesStep7<Record, ?, ?, ?, ?, ?, ?, ?>>executeInsertBatch(modules, module -> {
			return dslContext.insertInto(
					table(CmsTables.MODULES.getTableName()), field("name"), field("title"), field("table_name"), field("ordered"),
							field("activated"), field("cms_module_column_id"), field("order_num"))
					.values(module.getName(), module.getTitle(), module.getTableName(), module.getOrdered(), module.getOrdered(),
							module.getLovColumnId(), module.getOrderNum()
				);
		});
	}
	
	public void insertModuleColumns(Collection<ModuleColumn> moduleColumns) {
		this.<ModuleColumn, InsertValuesStep11<Record, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>executeInsertBatch(moduleColumns, moduleColumn -> {
			return dslContext.insertInto(
					table(CmsTables.MODULE_COLUMNS.getTableName()), field("cms_module_id"), field("name"), field("name_in_db"), field("type"),
							field("size"), field("not_null"), field("default_value"), field("read_only"), field("show_in_list"), field("show_in_edit"), field("order_num"))
					.values(moduleColumn.getModuleId(), moduleColumn.getName(), moduleColumn.getNameInDb(), moduleColumn.getType(),
							moduleColumn.getSize(), moduleColumn.getNotNull(), moduleColumn.getDefaultValue(), moduleColumn.getReadOnly(),
							moduleColumn.getShowInEdit(), moduleColumn.getShowInEdit(), moduleColumn.getOrderNum()
				);
		});
	}
	
	public void insertModuleHierarchies(Collection<ModuleHierarchy> moduleHierarchies) {
		this.<ModuleHierarchy, InsertValuesStep3<Record, ?, ?, ?>>executeInsertBatch(moduleHierarchies, moduleHierarchy -> {
			return dslContext.insertInto(
					table(CmsTables.MODULE_HIERARCHY.getTableName()), field("cms_module_id"), field("cms_module2_id"), field("mandatory"))
					.values(moduleHierarchy.getModuleId(), moduleHierarchy.getModule2Id(), moduleHierarchy.getMandatory());
		});
	}
	
	public List<Module> getAllModules() {
		return dslContext.selectFrom(table(CmsTables.MODULES.getTableName())).fetch(new ModuleRecordMapper());
	}
	
	public Module getModule(Long id) {
		Record row = dslContext.selectFrom(table(CmsTables.MODULES.getTableName()))
				.where(field("id").equal(id)).fetchOne();
		
		return row == null ? null : row.map(new ModuleRecordMapper());
	}
	
	public List<ModuleColumn> getModuleColumns(Long moduleId) {
		return dslContext.selectFrom(table(CmsTables.MODULE_COLUMNS.getTableName())).where(field("cms_module_id").equal(moduleId)).fetch(new ModuleColumnRecordMapper());
	}
	
	public Map<Long, ModuleColumn> getColumnsByIds(List<Long> columnIds) {
		return dslContext.selectFrom(table(CmsTables.MODULE_COLUMNS.getTableName())).where(field("id").in(columnIds)).fetchMap(field("id", Long.class), new ModuleColumnRecordMapper());
	}
	
	public List<Module> getParentModules(Long moduleId) {
		return dslContext.select(
					field("cm.id").as("id"), field("cm.name").as("name"), field("cm.title").as("title"),
					field("cm.table_name").as("table_name"), field("cm.ordered").as("ordered"),
					field("cm.activated").as("activated"), field("cm.cms_module_column_id").as("cms_module_column_id"),
					field("cm.order_num").as("order_num"))
				.from(table(CmsTables.MODULE_HIERARCHY.getTableName()).as("cmh"))
				.join(table(CmsTables.MODULES.getTableName()).as("cm"))
				.on(field("cmh.cms_module_id").equal(field("cm.id")))
				.where(field("cmh.cms_module2_id").equal(moduleId))
				.orderBy(field("cmh.id"))
				.fetch(new ModuleRecordMapper());
	}
	
	public List<ModuleHierarchy> getModuleHierarchies(Long moduleId) {
		return dslContext.select(field("id"), field("cms_module_id"), field("cms_module2_id"), field("mandatory"))
				.from(table(CmsTables.MODULE_HIERARCHY.getTableName()))
				.where(field("cms_module2_id").equal(moduleId))
				.orderBy(field("id"))
				.fetch(new ModuleHierarchyMapper());
	}
	
	private <T, Q extends Query> void executeInsertBatch(Collection<T> objectsToInsert, Function<? super T, ? extends Q> mapFunction) {
		List<Q> insertStatements = objectsToInsert.stream().map(mapFunction).collect(Collectors.toList());
		dslContext.batch(insertStatements).execute();
	}
	
	public void setActivated(Long moduleId, boolean activated) {
		dslContext.update(table(CmsTables.MODULES.getTableName())).set(field("activated"), activated).where(field("id").eq(moduleId)).execute();
	}
	
	private class ModuleRecordMapper implements RecordMapper<Record, Module> {

		@Override
		public Module map(Record r) {
			return new Module(r.getValue("id", Long.class), r.getValue("name", String.class), r.getValue("title", String.class),
					r.getValue("table_name", String.class), r.getValue("ordered", Boolean.class),
					r.getValue("activated", Boolean.class), r.getValue("cms_module_column_id", Long.class), r.getValue("order_num", Long.class));
		}
		
	}
	
	private class ModuleColumnRecordMapper implements RecordMapper<Record, ModuleColumn> {

		@Override
		public ModuleColumn map(Record r) {
			return new ModuleColumn(r.getValue("id", Long.class), r.getValue("cms_module_id", Long.class), r.getValue("name", String.class),
					r.getValue("name_in_db", String.class), r.getValue("type", ColumnType.class), r.getValue("size", Integer.class),
					r.getValue("not_null", Boolean.class), r.getValue("default_value", String.class), r.getValue("read_only", Boolean.class),
					r.getValue("show_in_list", Boolean.class), r.getValue("show_in_edit", Boolean.class), r.getValue("order_num", Integer.class));
		}
		
	}
	
	private class ModuleHierarchyMapper implements RecordMapper<Record, ModuleHierarchy> {

		@Override
		public ModuleHierarchy map(Record r) {
			return new ModuleHierarchy(r.getValue("id", Long.class), r.getValue("cms_module_id", Long.class), r.getValue("cms_module2_id", Long.class), r.getValue("mandatory", Boolean.class));
		}
		
	}

	
	
}
