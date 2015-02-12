package com.agnosticcms.web.dbutil;

import org.springframework.stereotype.Component;

import com.feedzai.commons.sql.abstraction.engine.DatabaseEngine;

@Component
public abstract class PdbEngineProvider {

	public abstract DatabaseEngine getEngine();
	
}
