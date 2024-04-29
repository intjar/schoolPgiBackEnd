package com.np.schoolpgi.util;

import java.util.Comparator;

import com.np.schoolpgi.dto.response.UserDetailResponse;

public class UserRegViewComparator implements Comparator<UserDetailResponse>{

	@Override
	public int compare(UserDetailResponse o1, UserDetailResponse o2) {

		return o2.getUId().compareTo(o1.getUId());
	}

}
