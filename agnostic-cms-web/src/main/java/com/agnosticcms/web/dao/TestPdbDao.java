package com.agnosticcms.web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.feedzai.commons.sql.abstraction.ddl.DbColumn;
import com.feedzai.commons.sql.abstraction.ddl.DbColumnType;
import com.feedzai.commons.sql.abstraction.ddl.DbEntity;
import com.feedzai.commons.sql.abstraction.dml.dialect.SqlBuilder;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngine;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngineException;

@Repository
public class TestPdbDao {
	
	@Autowired
	private DatabaseEngine databaseEngine;
	
	public static final String TMP_TABLE_NAME = "tmp_table";
	
	public void createTestTable() throws DatabaseEngineException {
		
		DbColumn descriptionColumn = SqlBuilder.dbColumn().name("description").type(DbColumnType.STRING).size(150).build();
		
		DbEntity data_type_table =
				SqlBuilder.dbEntity()
			        .name(TMP_TABLE_NAME)
			        .addColumn("id", DbColumnType.INT)
			        .addColumn(descriptionColumn)
			        .pkFields("id")
			        .build();
		
		databaseEngine.addEntity(data_type_table);
	}
	
	public void dropTestTable() throws DatabaseEngineException {
		databaseEngine.dropEntity(TMP_TABLE_NAME);
	}
	
}
