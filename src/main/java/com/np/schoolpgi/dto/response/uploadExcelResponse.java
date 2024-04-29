package com.np.schoolpgi.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class uploadExcelResponse {
	
	List<SurveyErrorReponse> errors;
	
	String message;
	
	Boolean status;
	
	

}
