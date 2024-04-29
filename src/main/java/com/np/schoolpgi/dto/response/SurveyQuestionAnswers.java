package com.np.schoolpgi.dto.response;

import lombok.Data;

@Data
public class SurveyQuestionAnswers {

	
	
	@Data
	public class ResultHead{
		private Integer survey_id;
		private String year_code;
		private String survey_description;
		
		
	}
	
}

