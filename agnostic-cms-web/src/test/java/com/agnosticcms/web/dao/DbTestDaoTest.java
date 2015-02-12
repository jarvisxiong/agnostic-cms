package com.agnosticcms.web.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.agnosticcms.test.SpringJUnitTest;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngineException;

public class DbTestDaoTest extends SpringJUnitTest {
	
	private Logger logger = LogManager.getLogger(DbTestDaoTest.class);

	@Autowired
	private TestPdbDao testPdbDao;
	
	@Autowired
	private TestJooqDao testJooqDao;
	
	
	@Test
	public void testAndDropCreateTable() throws Exception {
		logger.info("Starting testAndDropCreateTable function");
		testPdbDao.createTestTable();
		testJooqDao.insertTmpRow();
		Result<Record2<Object, Object>> result = testJooqDao.selectTmpRows();
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(1, result.getValue(0, 0));
		Assert.assertEquals("It Works!", result.getValue(0, 1));
		logger.info("Exiting testAndDropCreateTable function");
	}
	
	@Test
	public void testTransactions() throws Exception {
		testPdbDao.createTestTable();
		
		try {
			testJooqDao.insertFailingTransactionRows();
		} catch (DataAccessException e) {
		}
		
		Result<Record2<Object, Object>> result = testJooqDao.selectTmpRows();
		Assert.assertEquals(0, result.size());
	}
	
	@After
	public void doCleanup() throws DatabaseEngineException {
		testPdbDao.dropTestTable();
	}
	
	
	
}
