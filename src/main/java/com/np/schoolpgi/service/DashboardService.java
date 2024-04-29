package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.dao.UserLevelIdRequest;
import com.np.schoolpgi.dto.request.ViewReqById;
import com.np.schoolpgi.dto.response.DashboardLevelIdResponse;
import com.np.schoolpgi.dto.response.DashboardResponse;
import com.np.schoolpgi.dto.response.DashboardUserListResponse;
import com.np.schoolpgi.model.SurveyMaster;

public interface DashboardService {

	List<SurveyMaster> getSurveyList(ViewReqById viewReqById);

	DashboardResponse getDashboardCount(List<SurveyMaster> surveyMasters, int id);

	DashboardUserListResponse getSurveyDetails(ViewReqById viewReqById);
	
	public DashboardLevelIdResponse getLevelId(UserLevelIdRequest userLevelIdRequest);
	
	public String moveWebsiteData(Integer year_code);

	
}
