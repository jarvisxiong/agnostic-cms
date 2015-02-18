package com.agnosticcms.web.dto;

public class ModuleHierarchy extends BaseDto {

	private Long module_id;
	private Long module2_id;
	
	public ModuleHierarchy() {
		super();
	}

	public ModuleHierarchy(Long id, Long module_id, Long module2_id) {
		super(id);
		this.module_id = module_id;
		this.module2_id = module2_id;
	}

	public Long getModule_id() {
		return module_id;
	}
	
	public void setModule_id(Long module_id) {
		this.module_id = module_id;
	}
	
	public Long getModule2_id() {
		return module2_id;
	}
	
	public void setModule2_id(Long module2_id) {
		this.module2_id = module2_id;
	}
	
}
