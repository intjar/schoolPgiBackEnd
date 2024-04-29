package com.np.schoolpgi.dto.request;

import lombok.Data;

@Data
public class SurveyUserRequest {
	
	private Long user_id;
	private Integer level_id;
	private Integer instance_id;
	private Long created_by;
}
