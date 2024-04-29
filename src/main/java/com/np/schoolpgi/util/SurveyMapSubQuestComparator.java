package com.np.schoolpgi.util;

import java.util.Comparator;

import com.np.schoolpgi.dto.response.SurvMapSubQuestResponse;

public class SurveyMapSubQuestComparator implements Comparator<SurvMapSubQuestResponse>{

	@Override
	public int compare(SurvMapSubQuestResponse o1, SurvMapSubQuestResponse o2) {

		return o1.getSubQuestId().compareTo(o2.getSubQuestId());
	}

}
