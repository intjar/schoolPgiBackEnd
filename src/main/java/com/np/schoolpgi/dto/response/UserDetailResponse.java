package com.np.schoolpgi.dto.response;

import lombok.Data;

@Data
public class UserDetailResponse {
	
	private Integer uId;
	private String name;
	private String userName;
	private String emailId;
	private Long number;
	private String instance;
	private Integer instanceId;
	private String role;
	private Integer roleId;
	private String level;
	private Integer levelId;
	private Boolean status;
	private Boolean isEditable;
}
