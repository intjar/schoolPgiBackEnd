package com.np.schoolpgi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.SurveyDataEntryRepo;
import com.np.schoolpgi.dao.SurveyInstanceRequest;
import com.np.schoolpgi.dao.SurveyMapQuestionRepo;
import com.np.schoolpgi.dao.SurveyMasterRepository;
import com.np.schoolpgi.dao.SurveyNotificationRepository;
import com.np.schoolpgi.dao.SurveyUserMappingRepo;
import com.np.schoolpgi.dto.request.SurveyStatusRequest;
import com.np.schoolpgi.dto.request.SuveyReportBasedRequest;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.service.DashboardService;
import com.np.schoolpgi.service.SurveyReportService;

@Service
public class SurveyReportServiceImpl implements SurveyReportService {

	final static Logger LOGGER = LogManager.getLogger(SurveyReportServiceImpl.class);

	@Autowired
	private SurveyUserMappingRepo surveyUserMappingRepo;

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private SurveyNotificationRepository surveyNotificationRepository;

	@Autowired
	private SurveyMapQuestionRepo surveyMapQuestionRepo;

	@Autowired
	private SurveyMasterRepository surveyMasterRepository;

	@Autowired
	private SurveyDataEntryRepo surveyDataEntryRepo;

	@Override
	public String assignSurveyReport(Integer pageNo, Integer pageSize, String sortDir, String sortBy, String searchKey,
			Integer loginId, String levelId, String instanceId) {

		try {

			return surveyUserMappingRepo.assignSurveyReport(pageNo, pageSize, sortDir, sortBy, searchKey, loginId,
					levelId, instanceId, null);

		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public String surveyNameReport(Integer pageNo, Integer pageSize, String sortDir, String sortBy, String searchKey,
			Integer surveyId, Integer loginId, String levelId, String instanceId) {

		try {

			return surveyUserMappingRepo.surveyNameReport(pageNo, pageSize, sortDir, sortBy, searchKey, surveyId,
					loginId, levelId, instanceId, null);

		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public String getSurveyStatus(SurveyStatusRequest surveyStatusRequest, Integer pageNo, Integer pageSize,
			String sortDir, String sortBy, String searchKey) {
		try {
			return surveyMasterRepository.getSurveyStatusByLogin(pageNo, pageSize, sortDir, sortBy, searchKey,
					surveyStatusRequest.getUserId(),surveyStatusRequest.getFilterStatus(), null);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + "-" + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public String getInstanceWiseList(SurveyInstanceRequest surveyInstanceRequest, Integer pageNo, Integer pageSize,
			String sortDir, String sortBy, String searchKey) {
		try {
			return surveyMasterRepository.getInstanceWiseList(pageNo, pageSize, sortDir, sortBy, searchKey,
					surveyInstanceRequest.getUserId(), surveyInstanceRequest.getSurveyId(), null);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + "-" + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public String getSurveyDataAccToReport(SuveyReportBasedRequest suveyReportBasedRequest, Integer pageNo,
			Integer pageSize, String sortDir, String sortBy, String searchKey) {
		try {
			return surveyMasterRepository.getSurveyDataAccToReport(pageNo, pageSize, sortDir, sortBy, searchKey,
					suveyReportBasedRequest.getUserId(), suveyReportBasedRequest.getSurveyId(),
					suveyReportBasedRequest.getReportLevelId(), null);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + "-" + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

}
