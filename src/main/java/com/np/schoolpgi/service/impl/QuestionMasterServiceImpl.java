package com.np.schoolpgi.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.dao.QuestionMasterRepository;
import com.np.schoolpgi.dao.QuestionTypeMasterRepository;
import com.np.schoolpgi.dao.SubQuestionRepository;
import com.np.schoolpgi.dao.SurveyMapQuestionRepo;
import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.QuestDropDownForSurveyMapQuest;
import com.np.schoolpgi.dto.request.QuestionMasterRequest;
import com.np.schoolpgi.dto.request.ViewReqById;
import com.np.schoolpgi.dto.response.QuestionSubQuestionResponse;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.QuestionMaster;
import com.np.schoolpgi.model.QuestionTypeMaster;
import com.np.schoolpgi.model.SubQuestion;
import com.np.schoolpgi.model.SurveyMapQuestion;
import com.np.schoolpgi.service.QuestionMasterService;
import com.np.schoolpgi.util.StringUtils;
import com.np.schoolpgi.util.ViewQuestsComparator;

@Service
public class QuestionMasterServiceImpl implements QuestionMasterService {

	Logger LOGGER = LogManager.getLogger(QuestionMasterServiceImpl.class);

	@Autowired
	private QuestionMasterRepository questRepository;

	@Autowired
	private QuestionTypeMasterRepository questionTypeRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SurveyMapQuestionRepo surveyMapQuestionRepo;

	@Autowired
	private SubQuestionRepository subQuestionRepository;

	@Override
	public Integer createQuestionMaster(QuestionMasterRequest questMasterReq) {

		try {
			if (StringUtils.isValidObj(questMasterReq.getId()) && questMasterReq.getId() > 0) {
				// Update Logic
				Optional<QuestionMaster> questOptional = questRepository.findById(questMasterReq.getId());
				if (questOptional.isPresent()) {
					QuestionMaster questionMaster = questOptional.get();
//					if(StringUtils.isValidObj(questMasterReq.getQuestionType()) && questMasterReq.getId()>0) {
//					}

					Optional<QuestionTypeMaster> questType = questionTypeRepository
							.findById(questMasterReq.getQuestionTypeMaster().getId());

					if (!questType.isPresent()) {
						return 5; // QuestionType ID does not exist in QuestTypeMaster Table
					}

					Optional<QuestionMaster> questByCode = questRepository
							.findByQuestionCodeAndIdNot(questMasterReq.getQuestionCode(), questMasterReq.getId());
					if (questByCode.isPresent()) {
						return 1;
						// Question Code of this Quest is already exist.
					}

					Optional<QuestionMaster> quest = questRepository
							.findByQuestionTypeMasterIdAndQuestionIgnoreCaseAndIdNot(
									questMasterReq.getQuestionTypeMaster().getId(), questMasterReq.getQuestion(),
									questMasterReq.getId());
					if (quest.isPresent()) {
						return 2; // Question name of this Quest Type is already exist.
					}
					questionMaster.setQuestion(questMasterReq.getQuestion());
					questionMaster.setOptions(questMasterReq.getOptions());
					questionMaster.setQuestionCode(questMasterReq.getQuestionCode());
					questionMaster.setQuestionTypeMaster(questType.get());
					questionMaster.setStatus(questMasterReq.getStatus());
					questionMaster.setUpdatedAt(new Date());
					questionMaster.setUpdatedBy(questMasterReq.getLoggedInUserId());
					questionMaster.setOnlyNumeric(questMasterReq.getOnlyNumeric());
					questRepository.save(questionMaster);
					return 3; // Updated Successfully

				} else {
					return 4; // Source ID is not present
				}

			} else {
				// CreateLevel API Logic -----

				Optional<QuestionTypeMaster> questType = questionTypeRepository
						.findById(questMasterReq.getQuestionTypeMaster().getId());
				if (!questType.isPresent()) {
					return 5; // QuestionType ID does not exist in QuestTypeMaster Table
				}

				Optional<QuestionMaster> quest = questRepository.findByQuestionTypeMasterIdAndQuestionIgnoreCase(
						questMasterReq.getQuestionTypeMaster().getId(), questMasterReq.getQuestion());
				if (quest.isPresent()) {
					return 2; // Question name of this Quest Type is already exist.
				}

				Optional<QuestionMaster> questByCode = questRepository
						.findByQuestionCode(questMasterReq.getQuestionCode());
				if (questByCode.isPresent()) {
					return 1;
					// Question Code of this Quest is already exist.
				}
				QuestionMaster questionMaster = modelMapper.map(questMasterReq, QuestionMaster.class);
				questionMaster.setCreatedAt(new Date());
				questionMaster.setQuestionTypeMaster(questType.get());
				questionMaster.setCreatedBy(questMasterReq.getLoggedInUserId());
				questionMaster.setStatus(questMasterReq.getStatus());
				questionMaster.setOnlyNumeric(questMasterReq.getOnlyNumeric());
				questRepository.save(questionMaster);
				return 3; // Created Successfully
			}

		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public List<QuestionMaster> viewQuestionMaster(QuestDropDownForSurveyMapQuest questRequest) {
		try {

			List<QuestionMaster> questionList = questRepository.findSurveyMapQuests(questRequest.getSurveyId());

			List<QuestionMaster> questionResList = new ArrayList<QuestionMaster>();

			for (QuestionMaster quest : questionList) {

				QuestionMaster questionMaster = new QuestionMaster();

				List<SurveyMapQuestion> surveyMapQuestions = surveyMapQuestionRepo
						.findByQuestionMasterId(quest.getId());

				BeanUtils.copyProperties(quest, questionMaster);

				if (surveyMapQuestions.isEmpty())

					questionMaster.setIsEditable(true);
				else
					questionMaster.setIsEditable(false);

				questionResList.add(questionMaster);
			}
			Collections.sort(questionResList, new ViewQuestsComparator());
			return questionResList;
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public Integer deleteQuestionMaster(DeleteRequestDto questMasterReq) {
		try {
			if (StringUtils.isValidObj(questMasterReq.getId()) && questMasterReq.getId() > 0) {

				Optional<QuestionMaster> questOpt = questRepository.findById(questMasterReq.getId());
				if (questOpt.isPresent()) {
					questOpt.get().setStatus(false);
					questOpt.get().setUpdatedAt(new Date());
					;
					questOpt.get().setUpdatedBy(questMasterReq.getLoggedInUserId());
					questRepository.save(questOpt.get());
					return 1; // DELETED Successfully.
				}
				return 2; // Source ID is not exist.
			}
			return 3; // Please enter valid source ID.

		} catch (Exception e) {

			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public List<QuestionSubQuestionResponse> viewQuestionSubquestionMaster(ViewReqById questRequest) {
		// TODO Auto-generated method stub

		try {
			List<QuestionSubQuestionResponse> questionResponses = new ArrayList<>();
			System.out.println(questRequest.getId());

			List<SurveyMapQuestion> mapQuest = surveyMapQuestionRepo.findActiveQuesBySurveyMasterId(questRequest.getId());

			if (!mapQuest.isEmpty()) {

				for (SurveyMapQuestion q : mapQuest) {
					QuestionSubQuestionResponse ques = new QuestionSubQuestionResponse();
					ques.setCreatedAt(q.getQuestionMaster().getCreatedAt());
					ques.setCreatedBy(q.getQuestionMaster().getCreatedBy());
					ques.setId(q.getQuestionMaster().getId());
					ques.setSNo(q.getSNo());
					ques.setOnlyNumeric(q.getQuestionMaster().getOnlyNumeric());
					ques.setOptions(q.getQuestionMaster().getOptions());
					ques.setQuestion(q.getQuestionMaster().getQuestion());
					ques.setDomain(q.getDomainMaster());
					ques.setSubDomain(q.getSubDomain());
					ques.setValueLogic(q.getValueLogic());
					ques.setPointerLogic(q.getPointerLogic());
					ques.setWeightage(q.getWeightage());
					List<SubQuestion> subQuestions = new ArrayList<>();
					if (!q.getSubQuestionIds().equals("") && q.getSubQuestionIds() != null)

					{

						String[] subQuestionIds = q.getSubQuestionIds().split(",");
						List<String> myList = Arrays.asList(subQuestionIds);

						System.out.println("list of sub questio :" + myList);
						for (String s : myList) {
							Long i = Long.parseLong(s);
							Optional<SubQuestion> subQuestion = subQuestionRepository.findById(i);
							if (subQuestion.isPresent()) {
								subQuestions.add(subQuestion.get());
							}

						}

					}
					ques.setSubQuestion(subQuestions);
					questionResponses.add(ques);

				}
			}

			return questionResponses;

		} catch (Exception e) {

			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

}
