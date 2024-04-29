package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.QuestDropDownForSurveyMapQuest;
import com.np.schoolpgi.dto.request.QuestionMasterRequest;
import com.np.schoolpgi.dto.request.ViewReqById;
import com.np.schoolpgi.dto.response.QuestionSubQuestionResponse;
import com.np.schoolpgi.model.QuestionMaster;

public interface QuestionMasterService {
	Integer createQuestionMaster(QuestionMasterRequest questMasterReq);
	List<QuestionMaster> viewQuestionMaster(QuestDropDownForSurveyMapQuest questRequest);
	List<QuestionSubQuestionResponse> viewQuestionSubquestionMaster(ViewReqById questRequest);
	Integer deleteQuestionMaster(DeleteRequestDto questMasterReq);
}
