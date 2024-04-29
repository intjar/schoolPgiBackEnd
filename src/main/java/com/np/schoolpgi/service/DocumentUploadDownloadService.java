package com.np.schoolpgi.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.np.schoolpgi.dto.request.DownloadExcelRequest;
import com.np.schoolpgi.dto.response.DownloadSurveyExcelResponse;
import com.np.schoolpgi.dto.response.SurveyErrorReponse;

public interface DocumentUploadDownloadService {

	DownloadSurveyExcelResponse downloadSurveyExcel(DownloadExcelRequest id);

	List<SurveyErrorReponse> uploadSurveyExcel(MultipartFile file,Integer surveyId,Integer loginuserId, Integer instanceId, boolean isupload);


}
