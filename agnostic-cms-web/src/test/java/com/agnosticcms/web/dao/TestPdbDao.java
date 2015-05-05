package com.agnosticcms.web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.feedzai.commons.sql.abstraction.ddl.DbColumn;
import com.feedzai.commons.sql.abstraction.ddl.DbColumnType;
import com.feedzai.commons.sql.abstraction.ddl.DbEntity;
import com.feedzai.commons.sql.abstraction.dml.dialect.SqlBuilder;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngine;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngineException;

/**
 * Dao for testing PulseDB functionality
 */
@Repository
public class TestPdbDao {
	
	@Autowired
	private DatabaseEngine databaseEngine;
	
	public static final String TMP_TABLE_NAME = "tmp_table";
	public static final String TMP_FIELD_ID = "id";
	public static final String TMP_FIELD_DESCRIPTION = "description";
	
	/**
	 * Cretes a temporary table with id and description fields
	 */
	public void createTestTable() throws DatabaseEngineException {
		
		DbColumn descriptionColumn = SqlBuilder.dbColumn().name(TMP_FIELD_DESCRIPTION).type(DbColumnType.STRING).size(150).build();
		
		DbEntity data_type_table =
				SqlBuilder.dbEntity()
			        .name(TMP_TABLE_NAME)
			        .addColumn(TMP_FIELD_ID, DbColumnType.INT)
			        .addColumn(descriptionColumn)
			        .pkFields(TMP_FIELD_ID)
			        .build();
		
		databaseEngine.addEntity(data_type_table);
	}
	
	/**
	 * Drops the temporary table
	 */
	public void dropTestTable() throws DatabaseEngineException {
		databaseEngine.dropEntity(TMP_TABLE_NAME);
	}
	
}
