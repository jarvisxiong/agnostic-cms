package com.agnosticcms.web.dto;

public class ModuleColumn extends BaseDto {

	private Long modulesId;
	private String name;
	private String nameInDb;
	private String type;
	private String typeDesc;
	private Boolean allowNulls;
	private String dbDefault;
	private Boolean readOnly;
	private Boolean showInList;
	private Boolean showInEdit;
	private Boolean orderNum;
	
	
	
	public ModuleColumn() {
		super();
	}

	public ModuleColumn(Long id, Long modulesId, String name, String nameInDb, String type, String typeDesc, Boolean allowNulls, String dbDefault,
			Boolean readOnly, Boolean showInList, Boolean showInEdit, Boolean orderNum) {
		super(id);
		this.modulesId = modulesId;
		this.name = name;
		this.nameInDb = nameInDb;
		this.type = type;
		this.typeDesc = typeDesc;
		this.allowNulls = allowNulls;
		this.dbDefault = dbDefault;
		this.readOnly = readOnly;
		this.showInList = showInList;
		this.showInEdit = showInEdit;
		this.orderNum = orderNum;
	}
	
	public Long getModulesId() {
		return modulesId;
	}
	public void setModulesId(Long modulesId) {
		this.modulesId = modulesId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameInDb() {
		return nameInDb;
	}
	public void setNameInDb(String nameInDb) {
		this.nameInDb = nameInDb;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public Boolean getAllowNulls() {
		return allowNulls;
	}
	public void setAllowNulls(Boolean allowNulls) {
		this.allowNulls = allowNulls;
	}
	public String getDbDefault() {
		return dbDefault;
	}
	public void setDbDefault(String dbDefault) {
		this.dbDefault = dbDefault;
	}
	public Boolean getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	public Boolean getShowInList() {
		return showInList;
	}
	public void setShowInList(Boolean showInList) {
		this.showInList = showInList;
	}
	public Boolean getShowInEdit() {
		return showInEdit;
	}
	public void setShowInEdit(Boolean showInEdit) {
		this.showInEdit = showInEdit;
	}
	public Boolean getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Boolean orderNum) {
		this.orderNum = orderNum;
	}
	
}
