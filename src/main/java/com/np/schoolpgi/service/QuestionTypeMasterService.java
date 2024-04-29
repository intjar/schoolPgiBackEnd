package com.np.schoolpgi.service;

import java.util.List;

import com.np.schoolpgi.model.QuestionTypeMaster;

public interface QuestionTypeMasterService {

	List<QuestionTypeMaster> viewQuestionTypeMaster(String str, String sortByColumn ,String sortDirection);
}
