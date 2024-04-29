package com.np.schoolpgi.dto.request;

import lombok.Data;

@Data
public class SurveyReportRequest {

	private Integer surveyId;
	private Integer loginId;
	private String levelId;
	private String instanceId;
}
