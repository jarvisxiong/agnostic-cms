package com.agnosticcms.web.dto;

public class ModuleColumn extends BaseDto {

	private Long moduleId;
	private String name;
	private String nameInDb;
	private ColumnType type;
	private Integer size;
	private Boolean notNull;
	private String defaultValue;
	private Boolean readOnly;
	private Boolean showInList;
	private Boolean showInEdit;
	private Integer orderNum;
	
	
	
	public ModuleColumn() {
		super();
	}

	public ModuleColumn(Long id, Long modulesId, String name, String nameInDb, ColumnType type, Integer size, Boolean notNull, String defaultValue,
			Boolean readOnly, Boolean showInList, Boolean showInEdit, Integer orderNum) {
		super(id);
		init(modulesId, name, nameInDb, type, size, notNull, defaultValue, readOnly, showInList, showInEdit, orderNum);
		
	}
	
	public ModuleColumn(Long modulesId, String name, String nameInDb, ColumnType type, Integer size, Boolean notNull, String defaultValue,
			Boolean readOnly, Boolean showInList, Boolean showInEdit, Integer orderNum) {
		init(modulesId, name, nameInDb, type, size, notNull, defaultValue, readOnly, showInList, showInEdit, orderNum);
	}
	
	private void init(Long modulesId, String name, String nameInDb, ColumnType type, Integer size, Boolean notNull, String defaultValue,
			Boolean readOnly, Boolean showInList, Boolean showInEdit, Integer orderNum) {
		this.moduleId = modulesId;
		this.name = name;
		this.nameInDb = nameInDb;
		this.type = type;
		this.size = size;
		this.notNull = notNull;
		this.defaultValue = defaultValue;
		this.readOnly = readOnly;
		this.showInList = showInList;
		this.showInEdit = showInEdit;
		this.orderNum = orderNum;
	}
	
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
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
	public ColumnType getType() {
		return type;
	}
	public void setType(ColumnType type) {
		this.type = type;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Boolean getNotNull() {
		return notNull;
	}
	public void setNotNull(Boolean notNull) {
		this.notNull = notNull;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
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
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
}
