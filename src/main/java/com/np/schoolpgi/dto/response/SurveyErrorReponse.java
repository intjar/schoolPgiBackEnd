package com.np.schoolpgi.dto.response;

import lombok.Data;

@Data
public class SurveyErrorReponse {

	private Boolean success = true;
	private String errorMessage;
	private double instanceCode;
	private double instanceId;
	private String instanceName;
	private double surveyId;
	private String surveyName;
	private String domain;
	private String subdomain;
	private String questionType;
	private double questionCode;
	private String questionName;
	private String answerType;
	private String answer;
	private String error;

}
