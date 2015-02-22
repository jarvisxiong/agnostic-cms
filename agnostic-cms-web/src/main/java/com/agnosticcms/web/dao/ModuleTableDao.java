package com.agnosticcms.web.dao;

import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.field;

import java.util.Collection;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.agnosticcms.web.dto.Module;

@Repository
public class ModuleTableDao {

	@Autowired
	private DSLContext dslContext;
	
	public Result<Record> getRows(Module module) {
		return dslContext.selectFrom(table(module.getTableName())).fetch();
	}
	
	public Map<Long, Object> getFieldsByIds(String tableName, String columnName, Collection<Long> ids) {
		
		return dslContext
					.select(field("id"), field(columnName))
					.from(table(tableName))
					.where(field("id").in(ids))
					.fetchMap(field("id", Long.class), field(columnName));
	}
	
	
	
}
