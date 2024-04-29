package com.np.schoolpgi.util;

import java.util.Comparator;

import com.np.schoolpgi.dto.response.SurveyMapQuestResponse;

public class SurveyMapQuestComparator implements Comparator<SurveyMapQuestResponse>{

	@Override
	public int compare(SurveyMapQuestResponse o1, SurveyMapQuestResponse o2) {

		return o2.getId().compareTo(o1.getId());
	}

}
