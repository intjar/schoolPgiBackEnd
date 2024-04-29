package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.json.simple.JSONArray;

import com.np.schoolpgi.constants.APPServiceCode;

import lombok.Data;

@Data
public class AssignSurveyReqDto {
	@NotNull(message = APPServiceCode.MUST_CONTAIN)
	private Long loginId;
	private Long surveyId;
	@NotBlank(message = APPServiceCode.MUST_CONTAIN)
	private String action;
	private JSONArray updatedData;
}
