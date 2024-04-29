package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DownloadExcelRequest {
	
	@NotNull
	private Integer surveyId;
	
	@NotNull
	private Integer loggedInUserId;
	
	@NotNull
	private Integer instanceId;

}
