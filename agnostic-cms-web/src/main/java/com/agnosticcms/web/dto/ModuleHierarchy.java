package com.agnosticcms.web.dto;

public class ModuleHierarchy extends BaseDto {

	private Long moduleId;
	private Long module2Id;
	
	public ModuleHierarchy() {
		super();
	}

	public ModuleHierarchy(Long id, Long moduleId, Long module2Id) {
		super(id);
		this.moduleId = moduleId;
		this.module2Id = module2Id;
	}

	public Long getModuleId() {
		return moduleId;
	}
	
	public void setModule_id(Long moduleId) {
		this.moduleId = moduleId;
	}
	
	public Long getModule2Id() {
		return module2Id;
	}
	
	public void setModule2Id(Long module2Id) {
		this.module2Id = module2Id;
	}
	
}
