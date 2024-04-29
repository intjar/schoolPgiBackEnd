package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.LevelMasterRequest;
import com.np.schoolpgi.model.LevelMaster;

public interface LevelMasterService {
	
	Integer createdLevel(LevelMasterRequest levelRequest);
	String viewLevelMaster(String isDropDown);
	Integer deleteLevel(DeleteRequestDto levelRequest);
	List<LevelMaster> childLevels(Integer l);
	
	
}
