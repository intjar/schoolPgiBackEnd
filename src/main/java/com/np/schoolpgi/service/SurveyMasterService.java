package com.np.schoolpgi.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.np.schoolpgi.constants.AppConstants;
import com.np.schoolpgi.dao.SurveyIdRequest;
import com.np.schoolpgi.dao.SurveyInstanceRequest;
import com.np.schoolpgi.dto.request.CreateSurveyRequest;
import com.np.schoolpgi.dto.request.DeleteSurveyRequest;
import com.np.schoolpgi.dto.request.EncryptDataRequest;
import com.np.schoolpgi.dto.request.SurveyCloneRequest;
import com.np.schoolpgi.dto.request.SurveyDataEntryListRequest;
import com.np.schoolpgi.dto.request.ViewReqById;
import com.np.schoolpgi.dto.response.ResponseWithPagination;

public interface SurveyMasterService {

	Integer createSurvey(CreateSurveyRequest createSurveyRequest);

	Integer updateSurvey(CreateSurveyRequest createSurveyRequest);

	ResponseWithPagination surveyList(ViewReqById viewReqById, int pageNo, int pageSize, String sortDir, String sortBy,
			String searchKey);

	Integer deleteSurvey(DeleteSurveyRequest deleteSurveyRequest);

	String surveyDataEntryById(SurveyDataEntryListRequest surveyDataEntryListRequest);

	Integer cloneSurvey(SurveyCloneRequest surveyCloneRequest);

	String getSurveyListForDeo(SurveyDataEntryListRequest surveyDataEntryListRequest, Integer pageNo, Integer pageSize,
			String sortDir, String sortBy, String searchKey);

	String insertSurveyDataEntry(String _jsontext);

	public String surveyProcedureCall(SurveyIdRequest surveyIdRequest, Integer pageNo, Integer pageSize,
			String sortDir, String sortBy, String searchKey);
}
