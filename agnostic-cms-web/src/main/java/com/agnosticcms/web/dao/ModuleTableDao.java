package com.agnosticcms.web.dao;

import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.BooleanUtils;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SortField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.agnosticcms.web.dto.LovItem;

/**
 * Data access object for manipulating database table data without additional abstaction
 * level involved
 */
@Repository
public class ModuleTableDao {

	@Autowired
	private DSLContext dslContext;
	
	/**
	 * Get all rows of a given table
	 * @param tableName The name of the table
	 * @param ordered Does the table have a order_num column by which it should be ordered
	 * @return All rows of a given table
	 */
	public Result<Record> getRows(String tableName, Boolean ordered) {
		List<SortField<?>> sortFields = new ArrayList<>();
		if(BooleanUtils.isTrue(ordered)) {
			sortFields.add(field("order_num").asc());
		}
		
		sortFields.add(field("id").asc());
		
		return dslContext.selectFrom(table(tableName)).orderBy(sortFields).fetch();
	}
	
	/**
	 * Selects a map of a single column values and row ids
	 * @param tableName The name of the table
	 * @param columnName Column name to retrieve data from
	 * @param ids Row ids to retrieve data from
	 * @return Map of column values in form "row id => column value"
	 */
	public Map<Long, Object> getSingleFieldValuesMap(String tableName, String columnName, Collection<Long> ids) {
		
		return dslContext
					.select(field("id"), field(columnName))
					.from(table(tableName))
					.where(field("id", Long.class).in(ids))
					.fetchMap(field("id", Long.class), field(columnName));
	}
	
	/**
	 * Selects single value of a single column
	 * @param tableName The name of the table
	 * @param columnName Column name to retrieve value from
	 * @param ids Row id to retrieve value from
	 * @return The value of the field
	 */
	public Object getSingleFieldValue(String tableName, String columnName, Long id) {
		
		return dslContext
					.select(field(columnName))
					.from(table(tableName))
					.where(field("id", Long.class).equal(id))
					.fetchAny(columnName);
	}
	
	/**
	 * Deletes a row from the table
	 * @param tableName The name of the table
	 * @param rowId Id of the row to delete
	 */
	public void deleteRow(String tableName, Long rowId) {
		dslContext.delete(table(tableName)).where(field("id", Long.class).equal(rowId)).execute();
	}
	
	/**
	 * Fetches a list of values items for particular table column
	 * @param tableName Name of the table to fetch values form
	 * @param columnName Name of the column with whose data the list of values will be populated
	 * @return A list of list of values items with row ids and column values
	 */
	public List<LovItem> getClassifierItems(String tableName, String columnName) {
		return dslContext
				.select(field("id"), field(columnName))
				.from(table(tableName))
				.fetch(r -> {
					return new LovItem(r.getValue("id", Long.class), r.getValue(columnName));
				});
	}
	
	/**
	 * Inserts a row into table
	 * @param tableName Name of the table
	 * @param fieldNames Field names for which the values should be inserted
	 * @param values Field values (ordering corresponding to the field names) to insert
	 */
	public void insertRow(String tableName, List<String> fieldNames, List<Object> values) {
		
		assertSizeEquals(fieldNames, values);
		
		Field<?>[] fields = fieldNames.stream().map(f -> field(f)).toArray(Field<?>[]::new);
		List<?> jooqValues = values.stream().map(v -> getJooqValue(v)).collect(Collectors.toList());
		dslContext.insertInto(table(tableName), fields).values(jooqValues).execute();
	}
	
	/**
	 * Updates a table row
	 * @param tableName Name of the table
	 * @param rowId Id of the row to update
	 * @param fieldNames Field names for which the values should be updated
	 * @param values The new field values (ordering corresponding to the field names)
	 */
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
	 * Transforms value to jOOQ field 'NULL' if it is null
	 * This hack is needed because jOOQ tries to cast null values to varchar in PostgreSQL for some reason
	 * 
	 * @param value Object to trasform
	 * @return The transformed value
	 */
	private Object getJooqValue(Object value) {
		return value == null ? field("NULL") : value;
	}
	
	/**
	 * Asserts that count of field names and their values is equal
	 * @param fieldNames The field names
	 * @param values The values
	 * @throws IllegalArgumentException If count is not equal
	 */
	private void assertSizeEquals(List<String> fieldNames, List<Object> values) {
		if(fieldNames.size() != values.size()) {
			throw new IllegalArgumentException("Fields and values must be of the same size");
		}
	}
	
	/**
	 * Fetches a single row from a table
	 * @param tableName The name of the table
	 * @param id The id of the row
	 * @return A single row of a table
	 */
	public Record getRow(String tableName, Long id) {
		return dslContext.selectFrom(table(tableName)).where(field("id").equal(id)).fetchAny();
	}
	
}
