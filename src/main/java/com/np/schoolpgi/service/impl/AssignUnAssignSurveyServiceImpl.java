package com.np.schoolpgi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.dao.SurveyUserMappingRepo;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.service.AssignUnAssignSurveyService;

@Service
public class AssignUnAssignSurveyServiceImpl implements AssignUnAssignSurveyService{
	

	final static Logger LOGGER = LogManager.getLogger(AssignUnAssignSurveyServiceImpl.class);
	
	@Autowired
	private SurveyUserMappingRepo surveyUserMappingRepo;

	@Override
	public String assignUnAssignSurvey(String req) {
		try {
			return surveyUserMappingRepo.surveyAssignUnAssign(req);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public String notifySurvey(Long lg_id, Long s_id) {
		try {
			return surveyUserMappingRepo.notifySurvey(lg_id, s_id, "np",null);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e);
		}
		
	}
	
	@Override
	public String notifyTPDSurvey(Long lg_id, Long s_id) {
		try {
			return surveyUserMappingRepo.notifySurvey(lg_id, s_id, "tp",null);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e);
		}
		
	}

}
