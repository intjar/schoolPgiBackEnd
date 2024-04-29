package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Validated
public class DeleteUpdateUserRequest {
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	private String email;
	
	@NotNull
	private Long mobileNo;
	
	@NotNull
	private Integer levelId;
	
	@NotNull
	private Integer instanceId;
	
	@NotNull
	private Integer roleId;	
	
	@NotNull
	private Integer loggedInUserId;
	
	@NotNull
	private Integer id;
	
	@NotNull
	private Boolean status;
	
	private Boolean isprofile;

}
