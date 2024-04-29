package com.np.schoolpgi.util;

import java.util.Comparator;
import java.util.List;

import com.np.schoolpgi.model.LinkList;

public class SurveyManageSettingComparator implements Comparator<List<LinkList>> {

	@Override
	public int compare(List<LinkList> o1, List<LinkList> o2) {
		return o1.get(0).getId().compareTo(o2.get(0).getId());
	}

}
