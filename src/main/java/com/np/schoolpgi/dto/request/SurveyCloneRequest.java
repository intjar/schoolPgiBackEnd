package com.np.schoolpgi.dto.request;

import java.util.List;

import com.np.schoolpgi.model.SurveyMapQuestion;

import lombok.Data;

@Data
public class SurveyCloneRequest {
	private CreateSurveyRequest surveyMaster;
	private List<SurveyMapQuestion> mapQuestion;
}
