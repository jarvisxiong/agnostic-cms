package com.agnosticcms.web.dao;

import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.field;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.agnosticcms.web.dto.LovItem;

@Repository
public class ModuleTableDao {

	@Autowired
	private DSLContext dslContext;
	
	public Result<Record> getRows(String tableName) {
		return dslContext.selectFrom(table(tableName)).fetch();
	}
	
	public Map<Long, Object> getSingleFieldValuesMap(String tableName, String columnName, Collection<Long> ids) {
		
		return dslContext
					.select(field("id"), field(columnName))
					.from(table(tableName))
					.where(field("id", Long.class).in(ids))
					.fetchMap(field("id", Long.class), field(columnName));
	}
	
	public Object getSingleFieldValue(String tableName, String columnName, Long id) {
		
		return dslContext
					.select(field(columnName))
					.from(table(tableName))
					.where(field("id", Long.class).equal(id))
					.fetchAny(columnName);
	}
	
	public void deleteRow(String tableName, Long rowId) {
		dslContext.delete(table(tableName)).where(field("id", Long.class).equal(rowId)).execute();
	}
	
	public List<LovItem> getClassifierItems(String tableName, String columnName) {
		return dslContext
				.select(field("id"), field(columnName))
				.from(table(tableName))
				.fetch(r -> {
					return new LovItem(r.getValue("id", Long.class), r.getValue(columnName));
				});
	}
	
	public void insertRow(String tableName, List<String> fieldNames, List<Object> values) {
		
		assertSizeEquals(fieldNames, values);
		
		Field<?>[] fields = fieldNames.stream().map(f -> field(f)).toArray(Field<?>[]::new);
		List<?> jooqValues = values.stream().map(v -> getJooqValue(v)).collect(Collectors.toList());
		dslContext.insertInto(table(tableName), fields).values(jooqValues).execute();
	}
	
	public void updateRow(String tableName, Long rowId, List<String> fieldNames, List<Object> values) {
		
		assertSizeEquals(fieldNames, values);
		
		Map<Field<?>, Object> updateValues =  new HashMap<>();
		
		Iterator<String> fieldNamesIterator = fieldNames.iterator();
		Iterator<Object> valuesIterator = values.iterator();
		while (fieldNamesIterator.hasNext()) {
			updateValues.put(field(fieldNamesIterator.next()), getJooqValue(valuesIterator.next()));
		}
		
		dslContext.update(table(tableName)).set(updateValues).where(field("id").equal(rowId)).execute();
	}
	
	/**
	 * This hack is needed because jooq tries to cast null values to varchar in postgres for some reason
	 */
	private Object getJooqValue(Object value) {
		return value == null ? field("NULL") : value;
	}
	
	private void assertSizeEquals(List<String> fieldNames, List<Object> values) {
		if(fieldNames.size() != values.size()) {
			throw new IllegalArgumentException("Fields and values must be of the same size");
		}
	}
	
	public Record getRow(String tableName, Long id) {
		return dslContext.selectFrom(table(tableName)).where(field("id").equal(id)).fetchAny();
	}
	
}
