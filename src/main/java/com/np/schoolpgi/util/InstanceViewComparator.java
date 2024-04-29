package com.np.schoolpgi.util;

import java.util.Comparator;

import com.np.schoolpgi.dto.response.InstanceResponse;

public class InstanceViewComparator implements Comparator<InstanceResponse>{

	@Override
	public int compare(InstanceResponse o1, InstanceResponse o2) {

		return o2.getId().compareTo(o1.getId());
	}

}
