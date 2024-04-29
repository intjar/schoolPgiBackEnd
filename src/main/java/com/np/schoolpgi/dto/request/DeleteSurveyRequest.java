package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DeleteSurveyRequest {
	
	@NotNull
	private int id;
	
	@NotNull
	private int loggedInUserId;

}
