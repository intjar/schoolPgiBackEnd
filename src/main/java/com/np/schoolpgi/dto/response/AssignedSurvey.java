package com.np.schoolpgi.dto.response;

import java.util.List;

import com.np.schoolpgi.model.SurveyMaster;

import lombok.Data;

@Data
public class AssignedSurvey {
	
	private Long userId;
	private String yearCode;
	private List<SurveyMaster> deoSurvey;
	private List<SurveyMaster> approvedSurvey;
	private List<SurveyMaster> viewerSurvey;
	

}
