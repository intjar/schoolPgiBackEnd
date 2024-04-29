package com.np.schoolpgi.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.dao.QuestionTypeMasterRepository;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.QuestionTypeMaster;
import com.np.schoolpgi.service.QuestionTypeMasterService;
import com.np.schoolpgi.util.StringUtils;

@Service
public class QuestionTypeMasterServiceImpl implements QuestionTypeMasterService {
	
	final static Logger LOGGER = LogManager.getLogger(LevelMasterServiceImpl.class);
	
	@Autowired
	private QuestionTypeMasterRepository questionTypeMasterRepo;

	@Override
	public List<QuestionTypeMaster> viewQuestionTypeMaster(String str ,String sortByColumn ,String sortDirection) {
		
		try {
			String validSortColumn = validateSortColumn(sortByColumn);
	        String validSortDirection = validateSortDirection(sortDirection);
			if(StringUtils.isValidObj(str)) {
				return questionTypeMasterRepo.findByAnswerTypeNot("Add Sub Question");
			}
			return questionTypeMasterRepo.findAll(Sort.by(Sort.Direction.fromString(validSortDirection), validSortColumn));
			
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
	private String validateSortDirection(String sortDirection) {
		return sortDirection;
	}

	private String validateSortColumn(String sortByColumn) {
		return sortByColumn;
	}

}
