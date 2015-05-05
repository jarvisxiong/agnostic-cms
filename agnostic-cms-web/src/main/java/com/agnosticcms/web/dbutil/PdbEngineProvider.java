package com.agnosticcms.web.dbutil;

import org.springframework.stereotype.Component;

import com.feedzai.commons.sql.abstraction.engine.DatabaseEngine;

/**
 * Facade factory for creating PulseDB's @DatabaseEngine instances.
 * The calss is overriden by IoC container configured in spring's XML configuration
 */
@Component
public abstract class PdbEngineProvider {

	/**
	 * @return PulseDB's dabase engine instance
	 */
	public abstract DatabaseEngine getEngine();
	
}
