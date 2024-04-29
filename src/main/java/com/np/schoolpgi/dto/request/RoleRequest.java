package com.np.schoolpgi.dto.request;

import java.util.Date;

import javax.persistence.Column;

import lombok.Data;

@Data
public class RoleRequest {
	
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "code")
	private Integer code;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_at")
	private Date updatedAt;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	
}
