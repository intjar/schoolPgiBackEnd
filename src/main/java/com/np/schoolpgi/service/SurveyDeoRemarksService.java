package com.np.schoolpgi.service;

import com.np.schoolpgi.dto.request.RemarksRequestDto;
import com.np.schoolpgi.dto.request.ReviewDataListReqDto;

public interface SurveyDeoRemarksService{
	String getRemarks(RemarksRequestDto remarksRequestDto);
	
	String getSurveyDataListReview(Integer pageNo,Integer pageSize, String sortDir, String sortBy, String searchKey,ReviewDataListReqDto reviewDataListReqDto);
	
	String getSurveyDataListReviewDetails(RemarksRequestDto remarksRequestDto);
	
	String updSurveyReviewApprove(String req);
	
}
