package com.np.schoolpgi.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.constants.Regex;

import lombok.Data;

@Data
public class LevelMasterRequest {

	private Integer id;
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	@Size(min = 3, max = 100, message = APPServiceCode.NAME_LENGTH_MESSAGE)
	@Pattern(regexp = Regex.NAME_VALIDATION, message = APPServiceCode.NAME_FORMAT_MESSAGE)
	private String levelName;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	@Min(value = 1, message = APPServiceCode.CODE_LENGTH_MESSAGE)
	@Max(value = 999999, message = APPServiceCode.CODE_LENGTH_MESSAGE)
	private Integer levelCode;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer parentLevelId;
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	private String roleIds;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Boolean status;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer loggedInUserId;
}
