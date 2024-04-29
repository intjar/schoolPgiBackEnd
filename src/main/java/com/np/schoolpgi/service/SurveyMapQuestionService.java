package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.STDResponse;
import com.np.schoolpgi.dto.request.ViewReqById;
import com.np.schoolpgi.dto.request.ViewSurMapSubQuestReq;
import com.np.schoolpgi.dto.response.ResponseWithPagination;
import com.np.schoolpgi.dto.response.SurvMapSubQuestResponse;
import com.np.schoolpgi.model.SurveyMapQuestion;

public interface SurveyMapQuestionService {
	STDResponse createSurveyMapQuest(SurveyMapQuestion surveyMapQuestionReq);
	ResponseWithPagination viewSurveyMapQuestion(ViewReqById surveyId,int pageNo, int pageSize, String sortDir, String sortBy, String searchKey
);
	STDResponse updateSurveyMapQuestion(SurveyMapQuestion surveyMapQuestionReq);
	STDResponse deleteSurveyMapQuestion(DeleteRequestDto deleteRequestDto);
	List<SurvMapSubQuestResponse> viewSurveyMapSubQuestByQuestId(ViewSurMapSubQuestReq req);
}
