package com.np.schoolpgi.util;

import java.util.Comparator;

import com.np.schoolpgi.model.LinkList;

public class SurveyManageSettingListComparator implements Comparator<LinkList> {
	@Override
	public int compare(LinkList o1, LinkList o2) {
		return o1.getId().compareTo(o2.getId());
	}
}
