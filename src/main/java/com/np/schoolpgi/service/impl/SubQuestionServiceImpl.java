package com.np.schoolpgi.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.QuestionMasterRepository;
import com.np.schoolpgi.dao.QuestionTypeMasterRepository;
import com.np.schoolpgi.dao.SubQuestionRepository;
import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.SubQuestionRequest;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.QuestionMaster;
import com.np.schoolpgi.model.QuestionTypeMaster;
import com.np.schoolpgi.model.SubQuestion;
import com.np.schoolpgi.service.SubQuestionService;
import com.np.schoolpgi.util.StringUtils;

@Service
public class SubQuestionServiceImpl implements SubQuestionService {

	Logger LOGGER = LogManager.getLogger(SubQuestionServiceImpl.class);

	@Autowired
	private SubQuestionRepository subQuestionRepository;

	@Autowired
	private QuestionMasterRepository questionMasterRepository;

	@Autowired
	private QuestionTypeMasterRepository questionTypeMasterRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Integer createSubQuestion(SubQuestionRequest subQuestReq) {
		try {

			if (StringUtils.isValidObj(subQuestReq.getId()) && subQuestReq.getId() > 0) {
				// Update Domain Master API Logic
				Optional<SubQuestion> subQuestionOpt = subQuestionRepository.findById(subQuestReq.getId());
				if (subQuestionOpt.isPresent()) {
					SubQuestion subQuestion = subQuestionOpt.get();

					Optional<QuestionTypeMaster> questType = questionTypeMasterRepository
							.findById(subQuestReq.getSubQuestionTypeMaster().getId());
					if (!questType.isPresent()) {
						return 6; // QuestionType ID does not exist in QuestTypeMaster Table
					}

					Optional<QuestionMaster> question = questionMasterRepository
							.findById(subQuestReq.getQuestionMaster().getId());
					if (!question.isPresent()) {
						return 4; // Question Master Id is not present in Question Master
					}
					Optional<SubQuestion> subQ = subQuestionRepository
							.findByQuestionMasterIdAndQuestionTypeMasterIdAndSubQuestionIgnoreCaseAndIdNot(
									subQuestReq.getQuestionMaster().getId(),
									subQuestReq.getSubQuestionTypeMaster().getId(), subQuestReq.getSubQuestion(),
									subQuestReq.getId());
					if (subQ.isPresent()) {
						return 3; //// Record is duplicate.
					}
					Optional<SubQuestion> subQByCode = subQuestionRepository
							.findBySubQuestionCodeAndIdNot(subQuestReq.getSubQuestionCode(), subQuestReq.getId());
					if (subQByCode.isPresent()) {
						return 5; // Source Code is already exist;
					}
					subQuestion.setOptions(subQuestReq.getOptions());
					subQuestion.setQuestionMaster(question.get());
					subQuestion.setStatus(subQuestReq.getStatus());
					subQuestion.setSubQuestionCode(subQuestReq.getSubQuestionCode());
					subQuestion.setSubQuestion(subQuestReq.getSubQuestion());
					subQuestion.setQuestionTypeMaster(questType.get());
					subQuestion.setUpdatedAt(new Date());
					subQuestion.setUpdatedBy(subQuestReq.getLoggedInUserId());
					subQuestion.setOnlyNumeric(subQuestReq.getOnlyNumeric());
					subQuestionRepository.save(subQuestion);
					return 1; // Updated Successfully.
				}
				return 2; // Source Id is not present.

			} else {
				// Create Sub Question Master API Logic

				Optional<QuestionTypeMaster> questType = questionTypeMasterRepository
						.findById(subQuestReq.getSubQuestionTypeMaster().getId());
				if (!questType.isPresent()) {
					return 6; // QuestionType ID does not exist in QuestTypeMaster Table
				}
				Optional<SubQuestion> subQByCode = subQuestionRepository
						.findBySubQuestionCode(subQuestReq.getSubQuestionCode());
				if (subQByCode.isPresent()) {
					return 5; // Source Code is already exist;
				}
				Optional<SubQuestion> duplicateSubQuestion = subQuestionRepository
						.findByQuestionMasterIdAndQuestionTypeMasterIdAndSubQuestionIgnoreCase(
								subQuestReq.getQuestionMaster().getId(), subQuestReq.getSubQuestionTypeMaster().getId(),
								subQuestReq.getSubQuestion());
				if (duplicateSubQuestion.isPresent()) {
					return 3; // Record is duplicate.
				}
				Optional<QuestionMaster> questMaster = questionMasterRepository
						.findById(subQuestReq.getQuestionMaster().getId());
				if (!questMaster.isPresent()) {
					return 4; // Question Master Id is not present in Question Master
				}
				SubQuestion subQuestion = modelMapper.map(subQuestReq, SubQuestion.class);
				subQuestion.setCreatedAt(new Date());
				subQuestion.setStatus(subQuestReq.getStatus());
				subQuestion.setCreatedBy(subQuestReq.getLoggedInUserId());
				subQuestion.setQuestionMaster(questMaster.get());
				subQuestion.setQuestionTypeMaster(questType.get());
				subQuestionRepository.save(subQuestion); 
				return 1; // Created Successfully.
			}

		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public List<SubQuestion> viewSubQuestionMaster(String sortByColumn, String sortDirection) {
		try {
			String validSortColumn = validateSortColumn(sortByColumn);
	        String validSortDirection = validateSortDirection(sortDirection);
			return subQuestionRepository
					.findAll(Sort.by(Sort.Direction.fromString(validSortDirection), validSortColumn));
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
	private String validateSortDirection(String sortDirection) {
		return sortDirection;
	}

	private String validateSortColumn(String sortByColumn) {
		return sortByColumn;
	}


	@Override
	public Integer deleteSubQuestion(DeleteRequestDto subQuestReq) {
		try {
			if (StringUtils.isValidObj(subQuestReq.getId()) && subQuestReq.getId() > 0) {

				Optional<SubQuestion> dataSourceOpt = subQuestionRepository.findById(subQuestReq.getId());
				if (dataSourceOpt.isPresent()) {
					dataSourceOpt.get().setStatus(false);
					dataSourceOpt.get().setUpdatedAt(new Date());
					;
					dataSourceOpt.get().setUpdatedBy(subQuestReq.getLoggedInUserId());
					subQuestionRepository.save(dataSourceOpt.get());
					return 1; // DELETED Successfully.
				}
				return 2; // Source ID is not exist.
			}
			return 3; // Please enter valid source ID.

		} catch (Exception e) {

			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

}
