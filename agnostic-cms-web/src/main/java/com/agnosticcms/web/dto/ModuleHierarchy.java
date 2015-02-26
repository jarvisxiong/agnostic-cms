package com.agnosticcms.web.dto;

public class ModuleHierarchy extends BaseDto {

	private Long moduleId;
	private Long module2Id;
	private Boolean mandatory;
	
	public ModuleHierarchy() {
		super();
	}

	public ModuleHierarchy(Long id, Long moduleId, Long module2Id, Boolean mandatory) {
		super(id);
		this.moduleId = moduleId;
		this.module2Id = module2Id;
		this.mandatory = mandatory;
	}

	public Long getModuleId() {
		return moduleId;
	}
	
	public Long getModule2Id() {
		return module2Id;
	}
	
	public void setModule2Id(Long module2Id) {
		this.module2Id = module2Id;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	
	
}
