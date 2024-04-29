package com.np.schoolpgi.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.constants.Regex;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Validated
public class AddInstanceRequest {

	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer parentInstanceId;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer levelId;
	
	@NotEmpty(message = APPServiceCode.MUST_CONTAIN)
	@Size(min = 3, max = 100)
	@Pattern(regexp = Regex.NAME_VALIDATION, message = APPServiceCode.NAME_FORMAT_MESSAGE)
	private String instanceName;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	@Min(value = 1, message = APPServiceCode.CODE_LENGTH_MESSAGE)
	@Max(value = 999999, message = APPServiceCode.CODE_LENGTH_MESSAGE)
	private Long instanceCode;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Boolean status;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	@Min(value = 1)
	private Integer loggedInUserId;
	
}
