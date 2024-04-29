package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.np.schoolpgi.constants.APPServiceCode;

import lombok.Data;

@Data
public class CreateSurveyRequest {

	private Integer id;
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	private String yearCode;
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	private String surveyName;
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	private String description;
	
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private String startDate;
	
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private String endDate;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer deoLevelId;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer deoRoleId;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer approverLevelId;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer approverRoleId;
	
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	private String viewerLevelId;
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	private String viewerRoleId;
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	private String reviewerLevelId;
	
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	private String reviewerRoleId;
	
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer reviewMandatory;
	private Boolean assignedSurveyStatus;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer loggedInUserId;
	
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Integer status;

	private String procedureName;
	
}
