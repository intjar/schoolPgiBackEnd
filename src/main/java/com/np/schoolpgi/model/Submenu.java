package com.np.schoolpgi.model;

import java.util.List;

public class Submenu {
	private Integer id;
	private String url;
	private String title;
	private Integer mgId;
	private String orderID;
	private Integer positionId;
	private String type;
	   private String accessType;
   private List<Subsubmenu> subsubmenus;

public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public Integer getMgId() {
	return mgId;
}
public void setMgId(Integer mgId) {
	this.mgId = mgId;
}
public String getOrderID() {
	return orderID;
}
public void setOrderID(String orderID) {
	this.orderID = orderID;
}
public Integer getPositionId() {
	return positionId;
}
public void setPositionId(Integer positionId) {
	this.positionId = positionId;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getAccessType() {
	return accessType;
}
public void setAccessType(String accessType) {
	this.accessType = accessType;
}
   
public List<Subsubmenu> getSubsubmenus() {
	return subsubmenus;
}
public void setSubsubmenus(List<Subsubmenu> subsubmenus) {
	this.subsubmenus = subsubmenus;
}
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}

}
