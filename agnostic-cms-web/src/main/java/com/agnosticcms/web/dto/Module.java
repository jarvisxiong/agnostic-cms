package com.agnosticcms.web.dto;

/**
 * Type describing Agnostic CMS module
 */
public class Module extends BaseDto {

	/**
	 * Name of the module
	 */
	private String name;
	
	/**
	 * Title of the module to show when it's open in UI
	 */
	private String title;
	
	/**
	 * Name of the table containing module elements
	 */
	private String tableName;
	
	/**
	 * Are the module elements ordered
	 */
	private Boolean ordered;
	
	/**
	 * Is the module activated
	 */
	private Boolean activated;
	
	/**
	 * Id of the column that is used to describe module elements in dropdowns and tables
	 */
	private Long lovColumnId;
	
	/**
	 * Order number of the module
	 */
	private Long orderNum;
	
	public Module() {
		super();
	}

	public Module(Long id, String name, String title, String tableName, Boolean ordered, Boolean activated, Long lovColumnId, Long orderNum) {
		super(id);
		init(name, title, tableName, ordered, activated, lovColumnId, orderNum);
	}
	
	public Module(String name, String title, String tableName, Boolean ordered, Boolean activated, Long lovColumnId, Long orderNum) {
		init(name, title, tableName, ordered, activated, lovColumnId, orderNum);
	}
	
	private void init(String name, String title, String tableName, Boolean ordered, Boolean activated, Long lovColumnId, Long orderNum) {
		this.name = name;
		this.title = title;
		this.tableName = tableName;
		this.ordered = ordered;
		this.activated = activated;
		this.lovColumnId = lovColumnId;
		this.orderNum = orderNum;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Boolean getOrdered() {
		return ordered;
	}
	public void setOrdered(Boolean ordered) {
		this.ordered = ordered;
	}
	public Boolean getActivated() {
		return activated;
	}
	public void setActivated(Boolean activated) {
		this.activated = activated;
	}
	public Long getLovColumnId() {
		return lovColumnId;
	}
	public void setLovColumnId(Long lovColumnId) {
		this.lovColumnId = lovColumnId;
	}
	public Long getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Long orderNum) {
		this.orderNum = orderNum;
	}
	
}
