package com.np.schoolpgi.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
	private Boolean success;
	private String message;
	private String accessToken;
	private String refreshToken;
	private Integer uId;
	private String name;
	private String email;
	private String username;
	private Long mobileNo;
	private Integer instanceId;
	private String instanceName;
	private Integer levelId;
	private String levelName;
	private Integer roleId;
	private String roleName;
	private Boolean status;
	
	public LoginResponse() {
		super();
	}
	public LoginResponse(Boolean success, String message, String accessToken, String refreshToken,Integer uId, String name, String email,
			String username, Long mobileNo, Integer instanceId, String instanceName, Integer levelId, String levelName, Integer roleId,
			String roleName,Boolean status) {
		super();
		this.success = success;
		this.message = message;
		this.accessToken = accessToken;
		this.refreshToken=refreshToken;
		this.uId = uId;
		this.name=name;
		this.email = email;
		this.username = username;
		this.mobileNo=mobileNo;
		this.instanceId = instanceId;
		this.instanceName = instanceName;
		this.levelId = levelId;
		this.levelName = levelName;
		this.roleId = roleId;
		this.roleName = roleName;
		this.status=status;
	}
	
		
	
}
