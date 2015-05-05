package com.agnosticcms.web.dto;

/**
 * Type describing Agnostic CMS module column
 */
public class ModuleColumn extends BaseDto {
	
	/**
	 * Id of the module the column belongs to
	 */
	private Long moduleId;
	
	/**
	 * Name of the column
	 */
	private String name;
	
	/**
	 * Name of the corresponding column in db
	 */
	private String nameInDb;
	
	/**
	 * Type of the column
	 */
	private ColumnType type;
	
	/**
	 * Size of the column. Can have different meanings in different types. For example string size
	 * in characters or file size in bytes
	 */
	private Integer size;
	
	/**
	 * Additional info for the type. Contents depend on column type
	 */
	private String typeInfo;
	
	/**
	 * Is the value of the column mandatory
	 */
	private Boolean notNull;
	
	/**
	 * Default value of the column
	 */
	private String defaultValue;
	
	/**
	 * Can the value be changed after it's initialization
	 */
	private Boolean readOnly;
	
	/**
	 * Should the column be displayed in UI's table view
	 */
	private Boolean showInList;
	
	/**
	 * Should the column be displayed in the "add new element" view
	 */
	private Boolean showInAdd;
	
	/**
	 * Should the column be displayed in the "edit existing element" view
	 */
	private Boolean showInEdit;
	
	/**
	 * Order number of the column
	 */
	private Integer orderNum;
	
	
	
	public ModuleColumn() {
		super();
	}

	public ModuleColumn(Long id, Long modulesId, String name, String nameInDb, ColumnType type, Integer size, String typeInfo, Boolean notNull, String defaultValue,
			Boolean readOnly, Boolean showInList, Boolean showInEdit, Boolean showInAdd, Integer orderNum) {
		super(id);
		init(modulesId, name, nameInDb, type, size, typeInfo, notNull, defaultValue, readOnly, showInList, showInEdit, showInAdd, orderNum);
		
	}
	
	public ModuleColumn(Long modulesId, String name, String nameInDb, ColumnType type, Integer size, String typeInfo, Boolean notNull, String defaultValue,
			Boolean readOnly, Boolean showInList, Boolean showInEdit, Boolean showInAdd, Integer orderNum) {
		init(modulesId, name, nameInDb, type, size, typeInfo, notNull, defaultValue, readOnly, showInList, showInEdit, showInAdd, orderNum);
	}
	
	private void init(Long modulesId, String name, String nameInDb, ColumnType type, Integer size, String typeInfo, Boolean notNull, String defaultValue,
			Boolean readOnly, Boolean showInList, Boolean showInEdit, Boolean showInAdd, Integer orderNum) {
		this.moduleId = modulesId;
		this.name = name;
		this.nameInDb = nameInDb;
		this.type = type;
		this.size = size;
		this.typeInfo = typeInfo;
		this.notNull = notNull;
		this.defaultValue = defaultValue;
		this.readOnly = readOnly;
		this.showInList = showInList;
		this.showInEdit = showInEdit;
		this.orderNum = orderNum;
		this.showInAdd = showInAdd;
		
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
	public Boolean getShowInAdd() {
		return showInAdd;
	}
	public void setShowInAdd(Boolean showInAdd) {
		this.showInAdd = showInAdd;
	}
	public String getTypeInfo() {
		return typeInfo;
	}
	public void setTypeInfo(String typeInfo) {
		this.typeInfo = typeInfo;
	}
	
}
