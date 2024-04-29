package com.np.schoolpgi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role_link_mapping")
public class RoleLinkMappings {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "role_id")
	private Integer roleId;

	@Column(name = "json_url")
	private String json_url;

	@Column(name = "link_id")
	private String linkIds;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getJson_url() {
		return json_url;
	}

	public void setJson_url(String json_url) {
		this.json_url = json_url;
	}

	public String getLinkIds() {
		return linkIds;
	}

	public void setLinkIds(String linkIds) {
		this.linkIds = linkIds;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}



}
