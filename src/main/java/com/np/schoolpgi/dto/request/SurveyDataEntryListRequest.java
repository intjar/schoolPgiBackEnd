package com.np.schoolpgi.dto.request;

import lombok.Data;

@Data
public class SurveyDataEntryListRequest {
	private Integer surveyId;
	private Integer isThird;
	private Integer loginId;
	private String yearcode;
	private Integer instanceId;
}
