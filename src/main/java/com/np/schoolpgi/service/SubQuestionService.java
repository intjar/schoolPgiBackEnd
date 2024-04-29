package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.SubQuestionRequest;
import com.np.schoolpgi.model.SubQuestion;

public interface SubQuestionService {
	Integer createSubQuestion(SubQuestionRequest subQuestReq);
	List<SubQuestion> viewSubQuestionMaster(String sortByColumn, String sortDirection );
	Integer deleteSubQuestion(DeleteRequestDto subQuestReq);
}
