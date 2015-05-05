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

/**
 * Tests for basic database funcitonality
 */
public class DbTestDaoTest extends SpringJUnitTest {
	
	private Logger logger = LogManager.getLogger(DbTestDaoTest.class);

	@Autowired
	private TestPdbDao testPdbDao;
	
	@Autowired
	private TestJooqDao testJooqDao;
	
	
	/**
	 * Tests that database table can be created (PulseDB), row can be inserted into it (jOOQ) and
	 * selected from it (jOOQ), and the same table can be dropped (PulseDB)
	 */
	@Test
	public void testAndDropCreateTable() throws Exception {
		logger.info("Starting testAndDropCreateTable function");
		testPdbDao.createTestTable();
		testJooqDao.insertTmpRow();
		Result<Record2<Object, Object>> result = testJooqDao.selectTmpRows();
		
		Assert.assertEquals("There should be one row in result set", 1, result.size());
		Assert.assertEquals("Wrong integer value retrieved from database", 1, result.getValue(0, 0));
		Assert.assertEquals("Wrong string value retrieved from database", "It Works!", result.getValue(0, 1));
		logger.info("Exiting testAndDropCreateTable function");
	}
	
	/**
	 * Tests that Spring transaction annotations are working with jOOQ
	 */
	@Test
	public void testTransactions() throws Exception {
		testPdbDao.createTestTable();
		
		try {
			testJooqDao.insertFailingTransactionRows();
		} catch (DataAccessException e) {
		}
		
		Result<Record2<Object, Object>> result = testJooqDao.selectTmpRows();
		Assert.assertEquals("Data should not be present as transaction failed", 0, result.size());
	}
	
	/**
	 * Removes temporary test table
	 */
	@After
	public void doCleanup() throws DatabaseEngineException {
		testPdbDao.dropTestTable();
	}
	
	
	
}
