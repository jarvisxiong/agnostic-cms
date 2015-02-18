package com.agnosticcms.web.dto;

public class Module extends BaseDto {

	private String name;
	private String title;
	private String tableName;
	private Boolean ordered;
	private Boolean activated;
	private Long lovColumnId;
	private Long orderNum;
	
	public Module() {
		super();
	}

	public Module(Long id, String name, String title, String tableName, Boolean ordered, Boolean activated, Long lovColumnId, Long orderNum) {
		super(id);
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
