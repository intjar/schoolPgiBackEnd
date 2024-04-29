package com.np.schoolpgi.util;

import java.util.Comparator;

import com.np.schoolpgi.dto.response.SurveyListResponse;

public class SurveyMasterViewComparator implements Comparator<SurveyListResponse>{

	@Override
	public int compare(SurveyListResponse o1, SurveyListResponse o2) {

		return o2.getId().compareTo(o1.getId());
	}

}
