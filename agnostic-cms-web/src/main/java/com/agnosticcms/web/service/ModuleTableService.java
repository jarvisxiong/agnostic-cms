package com.agnosticcms.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.ListUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.agnosticcms.web.dao.ModuleDao;
import com.agnosticcms.web.dao.ModuleTableDao;
import com.agnosticcms.web.dao.SchemaDao;
import com.agnosticcms.web.dto.ColumnType;
import com.agnosticcms.web.dto.Lov;
import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.ModuleHierarchy;
import com.agnosticcms.web.dto.form.ModuleInput;
import com.agnosticcms.web.exception.DataIntegrityException;
import com.agnosticcms.web.exception.TypeConversionException;

@Service
public class ModuleTableService {
	
	@Autowired
	private ModuleTableDao moduleTableDao;
	
	@Autowired
	private ModuleDao moduleDao;
	
	@Autowired
	private ColumnTypeService columnTypeService;
	
	@Autowired
	private SchemaDao schemaDao;
	
	
	public Result<Record> getRows(Module module) {
		return moduleTableDao.getRows(module.getTableName());
	}
	
	private <T> Map<Integer, T> getLov(List<Module> parentModules, List<String> fkNames, LovResultsRetriever<T> resultsRetriever) {
		
		List<Long> parentLovColumnIds = parentModules.stream().map(mc -> mc.getLovColumnId()).collect(Collectors.toList());
		Map<Long, ModuleColumn> lovColumns = moduleDao.getColumnsByIds(parentLovColumnIds);
		Map<Integer, T> lovs = new HashMap<>();
		
		int i = 0;
		for(Module parentModule : parentModules) {
			
			String tableName = parentModule.getTableName();
			
			ModuleColumn lovColumn = lovColumns.get(parentModule.getLovColumnId());
			
			if(lovColumn != null) {
				lovs.put(i, resultsRetriever.retrieve(tableName, lovColumn, fkNames.get(i)));
			} else {
				throw new DataIntegrityException("No List Of Values column for parent table " + tableName);
			}
			
			i++;
		}
		
		return lovs;
	}
	
	public Map<Integer, Map<Long, Object>> getLovs(List<Module> parentModules, List<String> fkNames, Result<Record> moduleTableRecords) {
		
		return getLov(parentModules, fkNames, (String tableName, ModuleColumn lovColumn, String fkName) -> {
			final Set<Long> parentRowIds = moduleTableRecords.intoSet(fkName, Long.class);
			// remove null for row that do not have association with particular parent enabled
			parentRowIds.remove(null);

			return moduleTableDao.getSingleFieldValuesMap(tableName, lovColumn.getNameInDb(), parentRowIds);
		});
	}
	
	public Map<Integer, Object> getLovsSingleValue(List<Module> parentModules, Record moduleRow) {
		
		return getLov(parentModules, getForeignKeyColumnNames(parentModules), (String tableName, ModuleColumn lovColumn, String fkName) -> {
			return moduleTableDao.getSingleFieldValue(tableName, lovColumn.getNameInDb(), (Long) moduleRow.getValue(fkName));
		});
	}
	
	public Map<Integer, Lov> getClassifierItems(List<Module> parentModules) {
		
		List<String> fkNames = getForeignKeyColumnNames(parentModules);
		
		return getLov(parentModules, fkNames, (String tableName, ModuleColumn lovColumn, String fkName) -> {
			Lov lov = new Lov();
			lov.setType(lovColumn.getType());
			lov.setItems(moduleTableDao.getClassifierItems(tableName, lovColumn.getNameInDb()));
			return lov;
		});
	}
	
	public void saveModuleInput(Module module, ModuleInput moduleInput, List<Module> parentModules, List<ModuleColumn> moduleColumns, Long existingRowId) {
		
		boolean update = existingRowId != null;
		
		Map<Integer, Long> lovValues = moduleInput.getLovValues();
		Map<Long, String> columnStringValues = moduleInput.getColumnValues();
		
		List<String> foreignKeyNames = getForeignKeyColumnNames(parentModules);
		List<Long> foreignKeyValues = IntStream.range(0, parentModules.size()).boxed().map(i -> lovValues.get(i)).collect(Collectors.toList());
		
		List<String> columnNames = new ArrayList<>();
		List<Object> columnValues = new ArrayList<>();
		for(ModuleColumn moduleColumn : moduleColumns) {
			
			if(!update || moduleColumn.getShowInEdit()) {
				columnNames.add(moduleColumn.getNameInDb());
				
				try {
					columnValues.add(columnTypeService.parseFromString(columnStringValues.get(moduleColumn.getId()), moduleColumn.getType()));
				} catch (TypeConversionException e) {
					throw new RuntimeException("TypeConversionException occured after validation. This should not happen.", e);
				}
			}
			
		}
		
		List<String> fieldNames = ListUtils.union(foreignKeyNames, columnNames);
		List<Object> fieldValues = ListUtils.union(foreignKeyValues, columnValues);
		
		if(update) {
			moduleTableDao.updateRow(module.getTableName(), existingRowId, fieldNames, fieldValues);
		} else {
			moduleTableDao.insertRow(module.getTableName(), fieldNames, fieldValues);
		}
		
	}
	
	
	
	public ModuleInput getFilledModuleInput(Module module, List<Module> parentModules, List<ModuleColumn> moduleColumns, Long itemId) {
		List<String> foreignKeyNames = getForeignKeyColumnNames(parentModules);
		Record row = moduleTableDao.getRow(module.getTableName(), itemId);
		
		Map<Integer, Long> lovValues = new HashMap<>();
		Map<Long, String> columnValues =  new HashMap<>();
		
		int i = 0;
		for(String foreignKeyName : foreignKeyNames) {
			lovValues.put(i, row.getValue(foreignKeyName, Long.class));
			i++;
		}
		
		for(ModuleColumn moduleColumn : moduleColumns) {
			if(moduleColumn.getShowInEdit()) {
				columnValues.put(moduleColumn.getId(), columnTypeService.parseToString(row.getValue(moduleColumn.getNameInDb()), moduleColumn.getType()));
			}
		}
		
		return new ModuleInput(lovValues, columnValues);
	}
	
	public List<String> getForeignKeyColumnNames(List<Module> parentModules) {
		return schemaDao.getForeignKeyColumnNames(parentModules);
	}
	
	public Record getRow(Module module, Long id) {
		return moduleTableDao.getRow(module.getTableName(), id);
	}
	
	public void deleteRow(Module module, Long id) {
		try {
			moduleTableDao.deleteRow(module.getTableName(), id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(e, "error.foreignkey.violation");
		}
		
	}
	
	public void activate(Module module) {
		if(module.getActivated()) {
			return;
		}
		
		Long moduleId = module.getId();
		List<ModuleColumn> moduleColumns = moduleDao.getModuleColumns(moduleId);
		List<Module> parentModules = moduleDao.getParentModules(moduleId);
		List<ModuleHierarchy> moduleHierarchies = moduleDao.getModuleHierarchies(moduleId);
		
		schemaDao.createOrUpdateModuleSchema(module, parentModules, moduleColumns, moduleHierarchies);
		
		moduleDao.setActivated(moduleId, true);
	}

	public void deactivate(Module module) {
		if(!module.getActivated()) {
			return;
		}
		
		moduleDao.setActivated(module.getId(), false);
	}
	
	@FunctionalInterface
	private interface LovResultsRetriever<T> {
		public T retrieve(String tableName, ModuleColumn moduleColumn, String fkName);
	}

	public void populateWithFileColumnValues(Module module, Long itemId, List<ModuleColumn> moduleColumns, Map<Long, String> columnValues) {
		Record row = getRow(module, itemId);
		
		for(ModuleColumn moduleColumn : moduleColumns) {
			if(moduleColumn.getType() == ColumnType.IMAGE) {
				columnValues.put(moduleColumn.getId(), row.getValue(moduleColumn.getNameInDb(), String.class));
			}
		}
	}
}
