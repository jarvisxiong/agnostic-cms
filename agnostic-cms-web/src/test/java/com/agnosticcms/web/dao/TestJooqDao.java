package com.agnosticcms.web.dao;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dao for testing jOOQ functionality
 */
@Repository
public class TestJooqDao {

	@Autowired
	private DSLContext dslContext;
	
	/**
	 * Inserts temporary row into temporary database table
	 */
	public void insertTmpRow() {
		dslContext.insertInto(DSL.table(TestPdbDao.TMP_TABLE_NAME), DSL.field(TestPdbDao.TMP_FIELD_ID), DSL.field(TestPdbDao.TMP_FIELD_DESCRIPTION))
			.values(1, "It Works!").execute();
	}
	
	/**
	 * Inserts two temporary rows but fails in the end of transaction
	 */
	@Transactional
	public void insertFailingTransactionRows() {
		dslContext.insertInto(DSL.table(TestPdbDao.TMP_TABLE_NAME), DSL.field(TestPdbDao.TMP_FIELD_ID), DSL.field(TestPdbDao.TMP_FIELD_DESCRIPTION))
			.values(1, "It Works!").execute();
		
		dslContext.insertInto(DSL.table(TestPdbDao.TMP_TABLE_NAME), DSL.field(TestPdbDao.TMP_FIELD_ID), DSL.field(TestPdbDao.TMP_FIELD_DESCRIPTION))
			.values(2, "It Works2!").execute();
		
		throw new DataAccessException("Expected exception");
	}
	
	/**
	 * Selects all contents from the temporary table
	 */
	public Result<Record2<Object, Object>> selectTmpRows() {
		return dslContext.select(DSL.field(TestPdbDao.TMP_FIELD_ID), DSL.field(TestPdbDao.TMP_FIELD_DESCRIPTION))
        	.from(DSL.table(TestPdbDao.TMP_TABLE_NAME)).fetch();
	}
	
}
