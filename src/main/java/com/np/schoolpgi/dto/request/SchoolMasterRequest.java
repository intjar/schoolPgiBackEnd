package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.constants.Regex;
import com.np.schoolpgi.model.InstanceMaster;
import com.np.schoolpgi.model.LevelMaster;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class SchoolMasterRequest {

	private Long id;

	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	@Size(min = 3, max = 250, message = APPServiceCode.NAME_LENGTH_MESSAGE)
	@Pattern(regexp = Regex.NAME_VALIDATION, message = APPServiceCode.NAME_FORMAT_MESSAGE)
	private String schoolName;
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	@Size(min = 11, max=11, message = APPServiceCode.UDISE_CODE_LENGTH)
	private String udiseCode;

	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer status;
	
	private LevelMaster levelMaster;
	
	private Integer blockLevelInstanceId;
	
	private InstanceMaster instanceMaster;

	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer loggedInUserId;
	
}
