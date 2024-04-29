package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.np.schoolpgi.constants.APPServiceCode;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Validated
public class DataSourceRequest {

	
	private Integer id;

	@NotEmpty(message = APPServiceCode.MUST_CONTAIN)
	private String name;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer code;
	
//	@NotEmpty(message = APPServiceCode.MUST_CONTAIN)
	private String url;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Boolean status;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer loggedInUserId;
}
