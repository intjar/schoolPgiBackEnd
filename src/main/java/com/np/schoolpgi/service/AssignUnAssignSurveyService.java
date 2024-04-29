package com.np.schoolpgi.service;

public interface AssignUnAssignSurveyService {

	String assignUnAssignSurvey(String req);
	
	String notifySurvey(Long lg_id, Long s_id);
	
	String notifyTPDSurvey(Long lg_id, Long s_id);
}
