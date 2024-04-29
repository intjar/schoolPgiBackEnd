package com.np.schoolpgi.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SurveyInstanceRequest {

	private int userId;
	private int surveyId;
}
