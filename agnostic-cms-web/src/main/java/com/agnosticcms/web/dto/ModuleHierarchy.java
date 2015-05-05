package com.agnosticcms.web.dto;

/**
 * Describes one edge in module hierarchy graph
 */
public class ModuleHierarchy extends BaseDto {

	/**
	 * Parent module id
	 */
	private Long moduleId;
	
	/**
	 * Child module id
	 */
	private Long module2Id;
	
	/**
	 * Is the relationship between module elements mandatory
	 */
	private Boolean mandatory;
	
	public ModuleHierarchy() {
		super();
	}

	public ModuleHierarchy(Long id, Long moduleId, Long module2Id, Boolean mandatory) {
		super(id);
		init(moduleId, module2Id, mandatory);
	}
	
	public ModuleHierarchy(Long moduleId, Long module2Id, Boolean mandatory) {
		init(moduleId, module2Id, mandatory);
	}
	
	private void init(Long moduleId, Long module2Id, Boolean mandatory) {
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
