package com.agnosticcms.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agnosticcms.web.dao.ModuleDao;
import com.agnosticcms.web.dao.ModuleTableDao;
import com.agnosticcms.web.dto.Lov;
import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;

@Service
public class ModuleTableService {
	
	@Autowired
	private ModuleTableDao moduleTableDao;
	
	@Autowired
	private ModuleDao moduleDao;
	
	
	public Result<Record> getRows(Module module) {
		return moduleTableDao.getRows(module);
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
				lovs.put(i, resultsRetriever.retrieve(tableName, lovColumn, i));
			} else {
				throw new IllegalArgumentException("No List Of Values column for parent table " + tableName);
			}
			
			i++;
		}
		
		return lovs;
	}
	
	public Map<Integer, Map<Long, Object>> getLovs(List<Module> parentModules, List<String> fkNames, Result<Record> moduleTableRecords) {
		
		return getLov(parentModules, fkNames, (String tableName, ModuleColumn lovColumn, Integer index) -> {
			final Set<Long> parentRowIds = moduleTableRecords.intoSet(fkNames.get(index), Long.class);
			// remove null for row that do not have association with particular parent enabled
			parentRowIds.remove(null);

			return moduleTableDao.getSingleFieldValueMap(tableName, lovColumn.getNameInDb(), parentRowIds);
		});
	}
	
	public Map<Integer, Lov> getClassifierItems(List<Module> parentModules) {
		
		List<String> fkNames = getForeignKeyNames(parentModules);
		
		return getLov(parentModules, fkNames, (String tableName, ModuleColumn lovColumn, Integer index) -> {
			Lov lov = new Lov();
			lov.setType(lovColumn.getType());
			lov.setItems(moduleTableDao.getClassifierItems(tableName, lovColumn.getNameInDb()));
			return lov;
		});
	}
	
	public List<String> getForeignKeyNames(List<Module> parentModules) {
		
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
			
			fkNames.add(getForeignKeyId(tableName, tableNameCount));
		}
		
		return fkNames;
	}
	
	private String getForeignKeyId(String tableName, Integer tableNameCount) {
		
		String foreignKeyId;
		
		if(tableName.endsWith("s")) {
			foreignKeyId = tableName.substring(0, tableName.length() - 1);
		} else {
			throw new IllegalArgumentException("Unable to singularize table name " + tableName);
		}
		
		if(tableNameCount > 1) {
			foreignKeyId += tableNameCount;
		}
		
		foreignKeyId += "_id";
		
		return foreignKeyId;
	}
	
	@FunctionalInterface
	private interface LovResultsRetriever<T> {
		public T retrieve(String tableName, ModuleColumn moduleColumn, Integer index);
	}
}
