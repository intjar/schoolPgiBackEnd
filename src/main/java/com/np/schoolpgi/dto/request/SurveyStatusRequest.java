package com.np.schoolpgi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SurveyStatusRequest {

	private Integer userId;
	private String filterStatus;

}
