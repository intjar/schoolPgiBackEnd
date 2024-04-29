package com.np.schoolpgi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.dao.YearMasterRepository;
import com.np.schoolpgi.model.YearMaster;
import com.np.schoolpgi.service.YearMasterService;

@Service
public class YearMasterServiceImpl implements YearMasterService {

	@Autowired
	YearMasterRepository yearMasterRepository;
	
	@Override
	public List<YearMaster> getYearList() {
		// TODO Auto-generated method stub
		List<YearMaster> yearMasters=yearMasterRepository.findAll();
		return yearMasters;
	}

}
