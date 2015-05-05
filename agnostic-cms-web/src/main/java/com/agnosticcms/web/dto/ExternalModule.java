package com.agnosticcms.web.dto;

/**
 * Type describing external modules
 */
public class ExternalModule extends BaseDto {

	/**
	 * External module's name
	 */
	private String name;
	
	/**
	 * Relative URL of the external module's entry page 
	 */
	private String url;
	
	/**
	 * Is the external module actived
	 */
	private Boolean activated;
	
	/**
	 * Order number of the external module's name
	 */
	private Long orderNum;
	
	public ExternalModule() {
		super();
	}

	public ExternalModule(Long id, String name, String url, Boolean activated, Long orderNum) {
		super(id);
		init(name, url, activated, orderNum);
	}
	
	public ExternalModule(String name, String url, Boolean activated, Long orderNum) {
		init(name, url, activated, orderNum);
	}
	
	private void init(String name, String url, Boolean activated, Long orderNum) {
		this.name = name;
		this.url = url;
		this.activated = activated;
		this.orderNum = orderNum;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Long orderNum) {
		this.orderNum = orderNum;
	}
	public Boolean getActivated() {
		return activated;
	}
	public void setActivated(Boolean activated) {
		this.activated = activated;
	}
}
