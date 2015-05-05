package com.agnosticcms.web.dbutil;

import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultExecuteListener;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;

/**
 * This class transforms @SQLException thrown by the jOOQ container into a Spring specific @DataAccessException.
 * This is needed for complience with Spring's transaction mechanism
 */
public class JooqExceptionTranslator extends DefaultExecuteListener {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void exception(ExecuteContext ctx) {
		SQLDialect dialect = ctx.configuration().dialect();
		SQLExceptionTranslator translator = (dialect != null) ? new SQLErrorCodeSQLExceptionTranslator(dialect.name()) : new SQLStateSQLExceptionTranslator();
		// Translate into Spring's exception
		ctx.exception(translator.translate("jOOQ", ctx.sql(), ctx.sqlException()));
	}
}