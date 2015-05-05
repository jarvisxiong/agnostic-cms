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

/**
 * Service for module element (stored in database tables) management
 */
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
	
	/**
	 * Gets all elements for a module
	 * @param module Module from whict to retrieve elements from
	 * @return All elements of a module
	 */
	public Result<Record> getRows(Module module) {
		return moduleTableDao.getRows(module.getTableName(), module.getOrdered());
	}
	
	/**
	 * Retrieves list of values for a module's foreign dependencies
	 * @param parentModules Parent modules of a module to retrieve lovs for
	 * @param fkNames Foreign key names of these modules (in the same order)
	 * @param resultsRetriever Function that will retrieve the LOV value itself
	 * @return Map in form "LOV's order number => LOV"
	 * @throws DataIntegrityException in case there is no list of values column for one of the parent tables
	 */
	private <T> Map<Integer, T> getLov(List<Module> parentModules, List<String> fkNames, LovResultsRetriever<T> resultsRetriever) {
		
		// ids of parent module LOV columns
		List<Long> parentLovColumnIds = parentModules.stream().map(mc -> mc.getLovColumnId()).collect(Collectors.toList());
		Map<Long, ModuleColumn> lovColumns = moduleDao.getColumnsByIds(parentLovColumnIds);
		Map<Integer, T> lovs = new HashMap<>();
		
		int i = 0;
		for(Module parentModule : parentModules) {
			
			String tableName = parentModule.getTableName();
			
			ModuleColumn lovColumn = lovColumns.get(parentModule.getLovColumnId());
			
			if(lovColumn != null) {
				// Use the passed function to retrieve the LOV value
				lovs.put(i, resultsRetriever.retrieve(tableName, lovColumn, fkNames.get(i)));
			} else {
				throw new DataIntegrityException("No List Of Values column for parent table " + tableName);
			}
			
			i++;
		}
		
		return lovs;
	}
	
	/**
	 * Retrieves List of Values for a module
	 * @param parentModules Parents of a module to retrieve LOVs for
	 * @param fkNames Foreign key names of parent modules
	 * @param moduleTableRecords Module elements (rows) for which to retrieve the low values
	 * @returnin LOVs in form "Lov order num => (row id => column value)"
	 */
	public Map<Integer, Map<Long, Object>> getLovs(List<Module> parentModules, List<String> fkNames, Result<Record> moduleTableRecords) {
		
		return getLov(parentModules, fkNames, (String tableName, ModuleColumn lovColumn, String fkName) -> {
			// Foreign key ids for the parent table
			final Set<Long> parentRowIds = moduleTableRecords.intoSet(fkName, Long.class);
			// remove null for rows that do not have association with particular parent
			parentRowIds.remove(null);

			// selects a LOV column in form "row id => column value"
			return moduleTableDao.getSingleFieldValuesMap(tableName, lovColumn.getNameInDb(), parentRowIds);
		});
	}
	
	/**
	 * Retrieves a single value for each of the LOVs
	 * @param parentModules Parents of a module to retrieve LOVs for
	 * @param moduleRow Single element of a module to retrieve LOVs for
	 * @return LOV values in form "LOV order number => lov value"
	 */
	public Map<Integer, Object> getLovsSingleValue(List<Module> parentModules, Record moduleRow) {
		
		return getLov(parentModules, getForeignKeyColumnNames(parentModules), (String tableName, ModuleColumn lovColumn, String fkName) -> {
			// Gets single LOV value for a particular foreign key
			return moduleTableDao.getSingleFieldValue(tableName, lovColumn.getNameInDb(), (Long) moduleRow.getValue(fkName));
		});
	}
	
	/**
	 * Retrieves classifier items for a module to show in drop-down lists
	 * @param parentModules Parents of a module to retrieve LOVs for
	 * @return Classifier values in form "LOV order number => classifier values in LOV container"
	 */
	public Map<Integer, Lov> getClassifierItems(List<Module> parentModules) {
		
		List<String> fkNames = getForeignKeyColumnNames(parentModules);
		
		return getLov(parentModules, fkNames, (String tableName, ModuleColumn lovColumn, String fkName) -> {
			// new LOV container for classifiers for each of the foreign keys
			Lov lov = new Lov();
			lov.setType(lovColumn.getType());
			lov.setItems(moduleTableDao.getClassifierItems(tableName, lovColumn.getNameInDb()));
			return lov;
		});
	}
	
	/**
	 * Stores module input submitted by user into database
	 * @param module The module for which the data will be inserted 
	 * @param moduleInput The module input to store
	 * @param parentModules Parent modules of the module
	 * @param moduleColumns Columns of the module
	 * @param existingRowId If update, this field should be set to the existing row id. Otherwise set as null
	 */
	public void saveModuleInput(Module module, ModuleInput moduleInput, List<Module> parentModules, List<ModuleColumn> moduleColumns, Long existingRowId) {
		
		boolean update = existingRowId != null;
		
		Map<Integer, Long> lovValues = moduleInput.getLovValues();
		Map<Long, String> columnStringValues = moduleInput.getColumnValues();
		
		List<String> foreignKeyNames = getForeignKeyColumnNames(parentModules);
		// Forming foreign keys retrieved from dropdowns into list
		List<Long> foreignKeyValues = IntStream.range(0, parentModules.size()).boxed().map(i -> lovValues.get(i)).collect(Collectors.toList());
		
		List<String> columnNames = new ArrayList<>();
		List<Object> columnValues = new ArrayList<>();
		for(ModuleColumn moduleColumn : moduleColumns) {
			
			// Allow to update only updatable items
			if(!update || moduleColumn.getShowInEdit()) {
				columnNames.add(moduleColumn.getNameInDb());
				
				try {
					// Convert posted values to appropriate Java types
					columnValues.add(columnTypeService.parseFromString(columnStringValues.get(moduleColumn.getId()), moduleColumn.getType()));
				} catch (TypeConversionException e) {
					throw new RuntimeException("TypeConversionException occured after validation. This should not happen.", e);
				}
			}
			
		}
		
		// include foreing keys into submission of fields and their values 
		List<String> fieldNames = ListUtils.union(foreignKeyNames, columnNames);
		List<Object> fieldValues = ListUtils.union(foreignKeyValues, columnValues);
		
		if(update) {
			moduleTableDao.updateRow(module.getTableName(), existingRowId, fieldNames, fieldValues);
		} else {
			moduleTableDao.insertRow(module.getTableName(), fieldNames, fieldValues);
		}
		
	}
	
	/**
	 * Pre-fills module input to show existing values in module element's edit form
	 * @param module Module whose element should be pre-filled
	 * @param parentModules Parents of the module
	 * @param moduleColumns Columns of the module
	 * @param itemId The element id of the module that is to be edited
	 * @return Pre-filled {@link ModuleInput}
	 */
	public ModuleInput getFilledModuleInput(Module module, List<Module> parentModules, List<ModuleColumn> moduleColumns, Long itemId) {
		List<String> foreignKeyNames = getForeignKeyColumnNames(parentModules);
		Record row = moduleTableDao.getRow(module.getTableName(), itemId);
		
		Map<Integer, Long> lovValues = new HashMap<>();
		Map<Long, String> columnValues =  new HashMap<>();
		
		int i = 0;
		// pre-fill foreign key values for drop-downs
		for(String foreignKeyName : foreignKeyNames) {
			lovValues.put(i, row.getValue(foreignKeyName, Long.class));
			i++;
		}
		
		for(ModuleColumn moduleColumn : moduleColumns) {
			// Add only the values that are editable
			if(moduleColumn.getShowInEdit()) {
				// All values are parsed as strings for their display in view
				columnValues.put(moduleColumn.getId(), columnTypeService.parseToString(row.getValue(moduleColumn.getNameInDb()), moduleColumn.getType()));
			}
		}
		
		return new ModuleInput(lovValues, columnValues);
	}
	
	/**
	 * {@link SchemaDao#getForeignKeyColumnNames(List)}
	 */
	public List<String> getForeignKeyColumnNames(List<Module> parentModules) {
		return schemaDao.getForeignKeyColumnNames(parentModules);
	}
	
	/**
	 * {@link ModuleTableDao#getRow(String, Long)}
	 */
	public Record getRow(Module module, Long id) {
		return moduleTableDao.getRow(module.getTableName(), id);
	}
	
	/**
	 * {@link ModuleTableDao#deleteRow(String, Long)}
	 * @throws DataIntegrityException in case a row deletion would break data integrity
	 */
	public void deleteRow(Module module, Long id) {
		try {
			moduleTableDao.deleteRow(module.getTableName(), id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(e, "error.foreignkey.violation");
		}
	}
	
	/**
	 * Activates given module and updates database schema accordingly
	 * @param module The module to activate
	 */
	public void activate(Module module) {
		if(module.getActivated()) {
			return;
		}
		
		Long moduleId = module.getId();
		List<ModuleColumn> moduleColumns = moduleDao.getModuleColumns(moduleId);
		List<ModuleHierarchy> moduleHierarchies = moduleDao.getModuleHierarchies(moduleId);
		List<Module> parentModules = moduleDao.getParentModules(moduleId);
		
		schemaDao.createOrUpdateModuleSchema(module, parentModules, moduleHierarchies, moduleColumns);
		
		moduleDao.setActivated(moduleId, true);
	}

	/**
	 * De-activates the module
	 * @param module The module to de-activate
	 */
	public void deactivate(Module module) {
		if(!module.getActivated()) {
			return;
		}
		
		moduleDao.setActivated(module.getId(), false);
	}

	/**
	 * Manually adds values for file columns from database as they are not posted back by user
	 * @param module Module in terms of which the post will be made
	 * @param itemId Id of the module element with whose date the column values should be pre-filled
	 * @param moduleColumns Columns of the module
	 * @param columnValues Current posted column values
	 */
	public void populateWithFileColumnValues(Module module, Long itemId, List<ModuleColumn> moduleColumns, Map<Long, String> columnValues) {
		Record row = getRow(module, itemId);
		
		for(ModuleColumn moduleColumn : moduleColumns) {
			if(moduleColumn.getType() == ColumnType.IMAGE) {
				columnValues.put(moduleColumn.getId(), row.getValue(moduleColumn.getNameInDb(), String.class));
			}
		}
	}
	
	/**
	 * Functional interface for retrieving LOV values from the database
	 * @param <T> The type of the retrieval result
	 */
	@FunctionalInterface
	private interface LovResultsRetriever<T> {
		public T retrieve(String tableName, ModuleColumn moduleColumn, String fkName);
	}
}
