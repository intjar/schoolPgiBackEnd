package com.np.schoolpgi.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.DataSourceRepository;
import com.np.schoolpgi.dao.DomainRepository;
import com.np.schoolpgi.dao.QuestionMasterRepository;
import com.np.schoolpgi.dao.SubQuestionRepository;
import com.np.schoolpgi.dao.SurveyMapQuestionRepo;
import com.np.schoolpgi.dao.SurveyMasterRepository;
import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.STDResponse;
import com.np.schoolpgi.dto.request.ViewReqById;
import com.np.schoolpgi.dto.request.ViewSurMapSubQuestReq;
import com.np.schoolpgi.dto.response.ResponseWithPagination;
import com.np.schoolpgi.dto.response.SurvMapSubQuestResponse;
import com.np.schoolpgi.dto.response.SurveyMapQuestResponse;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.DataSource;
import com.np.schoolpgi.model.DomainMaster;
import com.np.schoolpgi.model.QuestionMaster;
import com.np.schoolpgi.model.SubQuestion;
import com.np.schoolpgi.model.SurveyMapQuestion;
import com.np.schoolpgi.model.SurveyMaster;
import com.np.schoolpgi.service.SurveyMapQuestionService;
import com.np.schoolpgi.util.StringUtils;
import com.np.schoolpgi.util.SurveyMapSubQuestComparator;

@Service
public class SurveyMapQuestionServiceImpl implements SurveyMapQuestionService {

	Logger LOGGER = LogManager.getLogger(SurveyMapQuestionServiceImpl.class);

	@Autowired
	private SurveyMapQuestionRepo surveyMapQuestionRepo;
	@Autowired
	private SurveyMasterRepository surveyMasterRepository;
	@Autowired
	private DomainRepository domainRepository;
//	@Autowired
//	private SubDomainRepository subDomainRepository;
	@Autowired
	private QuestionMasterRepository questionMasterRepository;

	@Autowired
	private SubQuestionRepository subQuestionRepository;

	@Autowired
	private DataSourceRepository dataSourceRepository;

	@Override
	public STDResponse createSurveyMapQuest(SurveyMapQuestion surveyMapQuestion) {
		try {
			STDResponse stdResponse = new STDResponse();
			Optional<SurveyMaster> survey = surveyMasterRepository
					.findById(surveyMapQuestion.getSurveyMaster().getId());
			if (!survey.isPresent()) {
				stdResponse.setSuccess(false);
				stdResponse.setErrorCode(APPServiceCode.APP525.getStatusCode());
				stdResponse.setErrorMessage(APPServiceCode.APP525.getStatusDesc());
				return stdResponse;
			}

			Optional<DomainMaster> domain = domainRepository.findById(surveyMapQuestion.getDomainMaster().getId());
			if (!domain.isPresent()) {
				stdResponse.setSuccess(false);
				stdResponse.setErrorCode(APPServiceCode.APP526.getStatusCode());
				stdResponse.setErrorMessage(APPServiceCode.APP526.getStatusDesc());
				return stdResponse;
			}

			Optional<QuestionMaster> question = questionMasterRepository
					.findById(surveyMapQuestion.getQuestionMaster().getId());
			if (!question.isPresent()) {
				stdResponse.setSuccess(false);
				stdResponse.setErrorCode(APPServiceCode.APP528.getStatusCode());
				stdResponse.setErrorMessage(APPServiceCode.APP528.getStatusDesc());
				return stdResponse;
			}
			
			Optional<SurveyMapQuestion> surveyMap = surveyMapQuestionRepo
					.findBySurveyMasterIdAndQuestionMasterId(
							surveyMapQuestion.getSurveyMaster().getId(),
							surveyMapQuestion.getQuestionMaster().getId());

			if (surveyMap.isPresent()) {
				stdResponse.setSuccess(false);
				stdResponse.setErrorCode(APPServiceCode.APP118.getStatusCode());
				stdResponse.setErrorMessage(APPServiceCode.APP118.getStatusDesc());
				return stdResponse;
			}

			Optional<DataSource> dataSource = dataSourceRepository.findById(surveyMapQuestion.getDataSource().getId());
			if (dataSource.isPresent()) {
				surveyMapQuestion.setDataSource(dataSource.get());
			} else {
				stdResponse.setSuccess(false);
				stdResponse.setErrorCode(APPServiceCode.APP538.getStatusCode());
				stdResponse.setErrorMessage(APPServiceCode.APP538.getStatusDesc());
				return stdResponse;
			}

			if (!StringUtils.isValidObj(surveyMapQuestion.getSubDomain().getId())) {
				surveyMapQuestion.setSubDomain(null);
			}
			
			surveyMapQuestion.setSurveyMaster(survey.get());
			surveyMapQuestion.setDomainMaster(domain.get());
			surveyMapQuestion.setQuestionMaster(question.get());
			surveyMapQuestion.setCreatedAt(new Date());
			surveyMapQuestion.setCreatedBy(surveyMapQuestion.getCreatedBy());
			surveyMapQuestion.setSNo(surveyMapQuestion.getSNo());
			
			Integer maxorderId=surveyMapQuestionRepo.findMaxOrderId(surveyMapQuestion.getSurveyMaster().getId());
			
			if(maxorderId==null)
			{
				surveyMapQuestion.setOrder(1);
			}
			else
			{
				surveyMapQuestion.setOrder(maxorderId+1);
			}
			
			//surveyMapQuestion.setOrder(surveyMapQuestion.getOrder());
			surveyMapQuestionRepo.save(surveyMapQuestion);
			stdResponse.setSuccess(true);
			stdResponse.setMessageCode(APPServiceCode.APP001.getStatusCode());
			stdResponse.setMessage(APPServiceCode.APP001.getStatusDesc());
			return stdResponse;
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public ResponseWithPagination viewSurveyMapQuestion(ViewReqById viewReq,int pageNo, int pageSize, String sortDir, String sortBy, String searchKey) {
		try {
			
			ResponseWithPagination responseWithPagination = new ResponseWithPagination();

//			Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
//					: Sort.by(sortBy).ascending();
			Sort sort;
			if (sortByExistsInSurveyMaster(sortBy)) {
				sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
						: Sort.by(sortBy).ascending();
			}else {
				sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by("id").descending()
						: Sort.by("id").ascending();
			}
			
			Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
			
			Page<SurveyMapQuestion> pageableData = null;
			
			if (StringUtils.isValidObj(viewReq.getId()) && viewReq.getId() > 0) {
				pageableData = surveyMapQuestionRepo.findSurveyMapQuestBySurveyMasterId(viewReq.getId(),searchKey,pageable);
				List<SurveyMapQuestion> surveyMapQuests = new ArrayList<SurveyMapQuestion>();
				surveyMapQuests = pageableData.getContent();
				
				if (surveyMapQuests.isEmpty()) {
					responseWithPagination.setSuccess(false);
					responseWithPagination.setErrorCode(APPServiceCode.APP504.getStatusCode());
					responseWithPagination.setErrorMessage(APPServiceCode.APP504.getStatusDesc());
					return responseWithPagination;
				}

				List<SurveyMapQuestResponse> surveyList = new ArrayList<SurveyMapQuestResponse>();

				for (SurveyMapQuestion surveyItem : surveyMapQuests) {
					if (surveyItem != null) {
						SurveyMapQuestResponse surveyResp = new SurveyMapQuestResponse();
						surveyResp.setId(surveyItem.getId());
						surveyResp.setSurveyId(surveyItem.getSurveyMaster().getId());
						surveyResp.setDomainId(surveyItem.getDomainMaster().getId());
						surveyResp.setDomainName(surveyItem.getDomainMaster().getDomainName());
						if (StringUtils.isValidObj(surveyItem.getSubDomain())) {
							surveyResp.setSubDomainId(surveyItem.getSubDomain().getId());
							surveyResp.setSubDomainName(surveyItem.getSubDomain().getSubDomainName());
						}
						surveyResp.setQuestionId(surveyItem.getQuestionMaster().getId());
						surveyResp.setQuestion(surveyItem.getQuestionMaster().getQuestion());
						surveyResp.setQuestionCode(surveyItem.getQuestionMaster().getQuestionCode());
						surveyResp.setQuestionType(
								surveyItem.getQuestionMaster().getQuestionTypeMaster().getAnswerType());
						surveyResp.setQuestionStatus(surveyItem.getQuestionMaster().getStatus());

						surveyResp.setSubQuestionIds(surveyItem.getSubQuestionIds());
						String subQuestIds = surveyItem.getSubQuestionIds();
						List<Long> subQuestIdsList = null;
						if (StringUtils.isNotEmpty(subQuestIds)) {
							subQuestIdsList = Arrays.stream(subQuestIds.split(",")).map((x) -> Long.parseLong(x))
									.collect(Collectors.toList());
							for (Long i : subQuestIdsList) {
								Optional<SubQuestion> findById = subQuestionRepository.findById(i);
								if (findById.isPresent()) {
									surveyResp.getSubQuestionName().add(findById.get().getSubQuestion());
									surveyResp.getSubQuestionCode().add(findById.get().getSubQuestionCode());
								}

							}
						}
						surveyResp.setValueLogic(surveyItem.getValueLogic());
						surveyResp.setPointerLogic(surveyItem.getPointerLogic());
						surveyResp.setWeightage(surveyItem.getWeightage());
						surveyResp.setIsMandatory(surveyItem.getIsMandatory());

						surveyResp.setIsThirdParty(surveyItem.getIsThirdParty());
						surveyResp.setDataSourceId(surveyItem.getDataSource().getId());
						surveyResp.setDataSourceName(surveyItem.getDataSource().getName());
						surveyResp.setIsThirdParty(surveyItem.getIsThirdParty());
						surveyResp.setIsDeleted(surveyItem.getIsDeleted());
						surveyResp.setSNo(surveyItem.getSNo());
						surveyResp.setOrder(surveyItem.getOrder());
						surveyList.add(surveyResp);
					}

				}
//				Collections.sort(surveyList, new SurveyMapQuestComparator());
				
				responseWithPagination.setHttpStatus(HttpStatus.OK);
				responseWithPagination.setLast(pageableData.isLast());
				responseWithPagination.setMessage(APPServiceCode.APP001.getStatusDesc());
				responseWithPagination.setMessageCode(APPServiceCode.APP001.getStatusCode());
				responseWithPagination.setPageNo(pageableData.getNumber());
				responseWithPagination.setPageSize(pageableData.getSize());
				responseWithPagination.setResult(surveyList);
				responseWithPagination.setSuccess(true);
				responseWithPagination.setTotalElements(pageableData.getTotalElements());
				responseWithPagination.setTotalPages(pageableData.getTotalPages());
				return responseWithPagination;
				
				
			}
			
			responseWithPagination.setSuccess(false);
			responseWithPagination.setErrorCode(APPServiceCode.APP529.getStatusCode());
			responseWithPagination.setErrorMessage(APPServiceCode.APP529.getStatusDesc());
			return responseWithPagination;
			
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
	
	private boolean sortByExistsInSurveyMaster(String sortBy) {
		try {
			Field[] fields = SurveyMapQuestion.class.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals(sortBy)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public STDResponse updateSurveyMapQuestion(SurveyMapQuestion surveyMapQuestionReq) {
		try {
			STDResponse stdResponse = new STDResponse();
			if (StringUtils.isValidObj(surveyMapQuestionReq.getId()) && surveyMapQuestionReq.getId() > 0) {
				Optional<SurveyMapQuestion> surveyMapQuestions = surveyMapQuestionRepo
						.findById(surveyMapQuestionReq.getId());

				if (!surveyMapQuestions.isPresent()) {
					stdResponse.setSuccess(false);
					stdResponse.setErrorCode(APPServiceCode.APP506.getStatusCode());
					stdResponse.setErrorMessage(APPServiceCode.APP506.getStatusDesc());
					return stdResponse;
				}

				Optional<SurveyMaster> survey = surveyMasterRepository
						.findById(surveyMapQuestionReq.getSurveyMaster().getId());

				if (!survey.isPresent()) {
					stdResponse.setSuccess(false);
					stdResponse.setErrorCode(APPServiceCode.APP525.getStatusCode());
					stdResponse.setErrorMessage(APPServiceCode.APP525.getStatusDesc());
					return stdResponse;
				}

				Optional<DomainMaster> domain = domainRepository
						.findById(surveyMapQuestionReq.getDomainMaster().getId());
				if (!domain.isPresent()) {
					stdResponse.setSuccess(false);
					stdResponse.setErrorCode(APPServiceCode.APP526.getStatusCode());
					stdResponse.setErrorMessage(APPServiceCode.APP526.getStatusDesc());
					return stdResponse;
				}

				Optional<QuestionMaster> question = questionMasterRepository
						.findById(surveyMapQuestionReq.getQuestionMaster().getId());
				if (!question.isPresent()) {
					stdResponse.setSuccess(false);
					stdResponse.setErrorCode(APPServiceCode.APP528.getStatusCode());
					stdResponse.setErrorMessage(APPServiceCode.APP528.getStatusDesc());
					return stdResponse;
				}
				
				Optional<SurveyMapQuestion> surveyMap = surveyMapQuestionRepo
						.findBySurveyMasterIdAndQuestionMasterIdAndIdNot(
								surveyMapQuestionReq.getSurveyMaster().getId(),
								surveyMapQuestionReq.getQuestionMaster().getId(), surveyMapQuestionReq.getId());
				if (surveyMap.isPresent()) {
					stdResponse.setSuccess(false);
					stdResponse.setErrorCode(APPServiceCode.APP118.getStatusCode());
					stdResponse.setErrorMessage(APPServiceCode.APP118.getStatusDesc());
					return stdResponse;
				}
				
				Optional<DataSource> dataSource = dataSourceRepository
						.findById(surveyMapQuestionReq.getDataSource().getId());
				if (dataSource.isPresent()) {
					surveyMapQuestionReq.setDataSource(dataSource.get());
				} else {
					stdResponse.setSuccess(false);
					stdResponse.setErrorCode(APPServiceCode.APP538.getStatusCode());
					stdResponse.setErrorMessage(APPServiceCode.APP538.getStatusDesc());
					return stdResponse;
				}

				surveyMapQuestions.get().setSurveyMaster(survey.get());
				surveyMapQuestions.get().setDomainMaster(domain.get());
				surveyMapQuestions.get().setQuestionMaster(question.get());
				surveyMapQuestions.get().setSubQuestionIds(surveyMapQuestionReq.getSubQuestionIds());
				surveyMapQuestions.get().setIsDeleted(surveyMapQuestionReq.getIsDeleted());
				surveyMapQuestions.get().setValueLogic(surveyMapQuestionReq.getValueLogic());
				surveyMapQuestions.get().setPointerLogic(surveyMapQuestionReq.getPointerLogic());
				surveyMapQuestions.get().setWeightage(surveyMapQuestionReq.getWeightage());
				surveyMapQuestions.get().setIsMandatory(surveyMapQuestionReq.getIsMandatory());
				surveyMapQuestions.get().setIsThirdParty(surveyMapQuestionReq.getIsThirdParty());
				if (surveyMapQuestionReq.getDataSource().getId() != null) {
					surveyMapQuestions.get().setDataSource(surveyMapQuestionReq.getDataSource());
				}

				surveyMapQuestions.get().setUpdatedAt(new Date());
				surveyMapQuestions.get().setUpdatedBy(surveyMapQuestionReq.getUpdatedBy());
				surveyMapQuestions.get().setSNo(surveyMapQuestionReq.getSNo());
				surveyMapQuestions.get().setOrder(surveyMapQuestionReq.getOrder());
				surveyMapQuestionRepo.save(surveyMapQuestions.get());
				stdResponse.setSuccess(true);
				stdResponse.setMessageCode(APPServiceCode.APP001.getStatusCode());
				stdResponse.setMessage(APPServiceCode.APP001.getStatusDesc());
				return stdResponse;
			}
			stdResponse.setSuccess(false);
			stdResponse.setErrorCode(APPServiceCode.APP531.getStatusCode());
			stdResponse.setErrorMessage(APPServiceCode.APP531.getStatusDesc());
			return stdResponse;
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public STDResponse deleteSurveyMapQuestion(DeleteRequestDto deleteRequestDto) {
		try {
			STDResponse stdResponse = new STDResponse();
			if (StringUtils.isValidObj(deleteRequestDto.getId()) && deleteRequestDto.getId() > 0) {
				Optional<SurveyMapQuestion> surveyMapQuest = surveyMapQuestionRepo
						.findById(deleteRequestDto.getId().intValue());
				if (surveyMapQuest.isPresent()) {
					surveyMapQuest.get().setIsDeleted(1);
					surveyMapQuest.get().setUpdatedAt(new Date());
					surveyMapQuest.get().setUpdatedBy(deleteRequestDto.getLoggedInUserId());
					surveyMapQuestionRepo.save(surveyMapQuest.get());
					stdResponse.setSuccess(true);
					stdResponse.setMessageCode(APPServiceCode.APP001.getStatusCode());
					stdResponse.setMessage(APPServiceCode.APP001.getStatusDesc());
					return stdResponse;
				}
				stdResponse.setSuccess(false);
				stdResponse.setErrorCode(APPServiceCode.APP531.getStatusCode());
				stdResponse.setErrorMessage(APPServiceCode.APP531.getStatusDesc());
				return stdResponse;
			}
			stdResponse.setSuccess(false);
			stdResponse.setErrorCode(APPServiceCode.APP506.getStatusCode());
			stdResponse.setErrorMessage(APPServiceCode.APP506.getStatusDesc());
			return stdResponse;

		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public List<SurvMapSubQuestResponse> viewSurveyMapSubQuestByQuestId(ViewSurMapSubQuestReq req) {
		try {
			Optional<SurveyMapQuestion> surveyMapOptional = null;
			
			surveyMapOptional = surveyMapQuestionRepo
					.findBySurveyMasterIdAndQuestionMasterId(req.getSurveyId(), req.getQuestionId());
			

			List<Long> subQuestIdsList = null;

			List<SurvMapSubQuestResponse> subMapSubQuestResponses = new ArrayList<SurvMapSubQuestResponse>();
			if (!surveyMapOptional.isPresent()) {
				return subMapSubQuestResponses;
			}
			if (!StringUtils.isEmpty(surveyMapOptional.get().getSubQuestionIds())) {
				if (surveyMapOptional.get().getSubQuestionIds().length() > 0) {

					subQuestIdsList = Arrays.stream(surveyMapOptional.get().getSubQuestionIds().split(","))
							.map((x) -> Long.parseLong(x)).collect(Collectors.toList());

					for (Long i : subQuestIdsList) {
						SurvMapSubQuestResponse subMapSubQuestResponse = new SurvMapSubQuestResponse();
						Optional<SubQuestion> findById = subQuestionRepository.findById(i);
						if (findById.isPresent()) {
//							surveyResp.getSubQuestionName().add(findById.get().getSubQuestion());
							subMapSubQuestResponse.setSubQuestId(i);
							subMapSubQuestResponse.setSubQuestName(findById.get().getSubQuestion());
							subMapSubQuestResponse.setSubQuestTypeId(findById.get().getQuestionTypeMaster().getId());
							subMapSubQuestResponse
									.setSubQuestType(findById.get().getQuestionTypeMaster().getAnswerType());
							subMapSubQuestResponse.setSubQuestCode(findById.get().getSubQuestionCode());
							subMapSubQuestResponse.setSubQuestStatus(findById.get().getStatus());
							subMapSubQuestResponses.add(subMapSubQuestResponse);
						}
					}
				}

			}
			Collections.sort(subMapSubQuestResponses, new SurveyMapSubQuestComparator());
			return subMapSubQuestResponses;

		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
	
	
	
}
