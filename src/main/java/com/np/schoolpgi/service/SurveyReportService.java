package com.np.schoolpgi.service;

import com.np.schoolpgi.dao.SurveyInstanceRequest;
import com.np.schoolpgi.dto.request.SurveyStatusRequest;
import com.np.schoolpgi.dto.request.SuveyReportBasedRequest;

public interface SurveyReportService {

	String assignSurveyReport(Integer pageNo, Integer pageSize, String sortDir, String sortBy, String searchKey,
			Integer loginId, String levelId, String instanceId);

	String surveyNameReport(Integer pageNo, Integer pageSize, String sortDir, String sortBy, String searchKey,
			Integer surveyId, Integer loginId, String levelId, String instanceId);

	public String getSurveyStatus(SurveyStatusRequest surveyStatusRequest, Integer pageNo, Integer pageSize,
			String sortDir, String sortBy, String searchKey);

	public String getInstanceWiseList(SurveyInstanceRequest surveyInstanceRequest, Integer pageNo, Integer pageSize,
			String sortDir, String sortBy, String searchKey);

	public String getSurveyDataAccToReport(SuveyReportBasedRequest suveyReportBasedRequest, Integer pageNo,
			Integer pageSize, String sortDir, String sortBy, String searchKey);
}
