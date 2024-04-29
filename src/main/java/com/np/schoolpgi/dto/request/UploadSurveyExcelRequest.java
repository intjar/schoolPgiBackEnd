package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UploadSurveyExcelRequest {
	
	@NotNull
	private byte[] file;
	
	@NotNull
	private String fileName;
	
	@NotNull
	private String fileType;
	
	@NotNull
	private Integer surveyId;
	
	@NotNull                           
	private Integer loggedInUserId;

}
