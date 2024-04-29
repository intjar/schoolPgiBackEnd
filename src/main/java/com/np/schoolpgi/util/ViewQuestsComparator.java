package com.np.schoolpgi.util;

import java.util.Comparator;

import com.np.schoolpgi.model.QuestionMaster;

public class ViewQuestsComparator implements Comparator<QuestionMaster>{

	@Override
	public int compare(QuestionMaster o1, QuestionMaster o2) {

		return o2.getId().compareTo(o1.getId());
	}

}
