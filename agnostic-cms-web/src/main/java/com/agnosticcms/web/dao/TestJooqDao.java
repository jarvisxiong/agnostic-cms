package com.agnosticcms.web.dao;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TestJooqDao {

	@Autowired
	private DSLContext create;
	
	public void insertTmpRow() {
		create.insertInto(DSL.table(TestPdbDao.TMP_TABLE_NAME), DSL.field("id"), DSL.field("description"))
			.values(1, "It Works!").execute();
	}
	
	@Transactional
	public void insertFailingTransactionRows() {
		create.insertInto(DSL.table(TestPdbDao.TMP_TABLE_NAME), DSL.field("id"), DSL.field("description"))
			.values(1, "It Works!").execute();
		
		create.insertInto(DSL.table(TestPdbDao.TMP_TABLE_NAME), DSL.field("id"), DSL.field("description"))
			.values(2, "It Works2!").execute();
		
		throw new DataAccessException("Expected exception");
	}
	
	public Result<Record2<Object, Object>> selectTmpRows() {
		return create.select(DSL.field("id"), DSL.field("description"))
        	.from(DSL.table(TestPdbDao.TMP_TABLE_NAME)).fetch();
	}
	
}
