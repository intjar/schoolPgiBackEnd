package com.np.schoolpgi.dto.response;

import com.np.schoolpgi.model.InstanceMaster;
import com.np.schoolpgi.model.LevelMaster;
import com.np.schoolpgi.model.SchoolMaster;

import lombok.Data;

@Data
public class SchoolResponse {

	private LevelMaster levelMaster;
	private InstanceMaster blockLevelInstanceMaster;
	private InstanceMaster instanceMaster;
	private SchoolMaster schoolMaster;
	
}
