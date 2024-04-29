package com.np.schoolpgi.dto.response;

import lombok.Data;

@Data
public class DownloadSurveyExcelResponse {

	private Boolean success;
	private Integer surveyId;
	private String fileurl;
	private String messageCode;
	private String message;
	private String errorCode;
	private String errorMessage;
}
