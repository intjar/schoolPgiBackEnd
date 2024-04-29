package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.dto.response.AssignedSurvey;

public interface SurveyUserMappingService {
	String surveyUserMapping(String req);
	String getUpdSurveySummary(Integer pageNo,Integer pageSize, String sortDir, String sortBy, String searchKey, Long login_id, Long sur_id, String inp_type, String _jsontext);
	List<AssignedSurvey> assignedSurveyList(Long loggedinUserId);
}
