package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.np.schoolpgi.constants.APPServiceCode;

import lombok.Data;
import lombok.ToString;

@Data
@Validated
@ToString
public class UserCreateRequest {

	@NotEmpty(message = APPServiceCode.MUST_CONTAIN)
	@Size(min = 3, max = 100, message = APPServiceCode.NAME_LENGTH_MESSAGE)
	private String name;
	
	@NotEmpty(message = APPServiceCode.MUST_CONTAIN)
	@Size(min = 3, max = 100, message = APPServiceCode.NAME_LENGTH_MESSAGE)
	private String username;
	
	@NotEmpty(message = APPServiceCode.MUST_CONTAIN)
	@Size(min = 6, max = 256, message = APPServiceCode.EMAIL_LENGTH_MESSAGE)
	private String email;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Long mobileNo;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer levelId;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer instanceId;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer roleId;	
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer loggedInUserId;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Boolean status;
}
