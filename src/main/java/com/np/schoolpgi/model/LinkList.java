package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "link_list")
@ToString
public class LinkList {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Integer id;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "title")
	private String title;
	
//	@Column(name = "menu_group_id")
//	private Integer menuGroupId;
	@Column(name = "menu_group_id")
	private String menuGroupId;
	
	@Column(name = "order_id")
	private String orderId;
	
	@Column(name = "position_id")
	private Integer positionId;
	
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "access_type")
	private String accesstype;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "updated_at")
	private Date updatedAt;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name="status")
	private Boolean status;
	
	@Column(name="icon")
	private String icons;

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

//	public Integer getMenuGroupId() {
//		return menuGroupId;
//	}
//
//	public void setMenuGroupId(Integer menuGroupId) {
//		this.menuGroupId = menuGroupId;
//	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getAccesstype() {
		return accesstype;
	}

	public void setAccesstype(String accesstype) {
		this.accesstype = accesstype;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getMenuGroupId() {
		return menuGroupId;
	}

	public void setMenuGroupId(String menuGroupId) {
		this.menuGroupId = menuGroupId;
	}
	
	
	
}
