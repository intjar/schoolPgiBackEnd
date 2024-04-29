package com.np.schoolpgi.dto.response;

import lombok.Data;

@Data
public class DashboardResponse {

	int totalSurvey;
	int activeSurvey;
	int completeSurvey;
	int pendingForDataEntry;
	int pendingForReview;
	int pendingForApprove;
	int pendingForNotifiedToTPD;
	int totalNoOfChildUsers;
	int completedDataEntry;
	int completedReview;
	int completedApprove;
	int userCountTable;

}
