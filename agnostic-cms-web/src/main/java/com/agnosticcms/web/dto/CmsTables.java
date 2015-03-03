package com.agnosticcms.web.dto;

public enum CmsTables {

	USERS(null, "cms_users"),
	SESSIONS(null, "cms_sessions"),
	MODULES(1l, "cms_modules"),
	MODULE_COLUMNS(2l, "cms_module_columns"),
	MODULE_HIERARCHY(3l, "cms_module_hierarchy");
	
	private Long moduleId;
	private String tableName;
	

	private CmsTables(Long moduleId, String tableName) {
		this.moduleId = moduleId;
		this.tableName = tableName;
	}


	public Long getModuleId() {
		return moduleId;
	}

	public String getTableName() {
		return tableName;
	}
	
}
