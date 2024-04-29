package com.np.schoolpgi.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SurveyUserInstanceModel {

	private int surveyId;
	private int reviewMandatory;
	private long userId;
	private int instanceId;
}
