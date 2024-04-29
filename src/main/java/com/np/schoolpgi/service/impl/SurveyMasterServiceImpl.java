package com.np.schoolpgi.service.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.LevelMasterRepository;
import com.np.schoolpgi.dao.RoleRepository;
import com.np.schoolpgi.dao.SurveyCloneMappingRepo;
import com.np.schoolpgi.dao.SurveyDataEntry;
import com.np.schoolpgi.dao.SurveyDataEntryRepo;
import com.np.schoolpgi.dao.SurveyIdRequest;
import com.np.schoolpgi.dao.SurveyMapQuestionRepo;
import com.np.schoolpgi.dao.SurveyMasterRepository;
import com.np.schoolpgi.dao.SurveyNotificationRepository;
import com.np.schoolpgi.dao.SurveyUserMappingRepo;
import com.np.schoolpgi.dto.request.CreateSurveyRequest;
import com.np.schoolpgi.dto.request.DeleteSurveyRequest;
import com.np.schoolpgi.dto.request.SurveyCloneRequest;
import com.np.schoolpgi.dto.request.SurveyDataEntryListRequest;
import com.np.schoolpgi.dto.request.ViewReqById;
import com.np.schoolpgi.dto.response.ResponseWithPagination;
import com.np.schoolpgi.dto.response.SurveyListResponse;
import com.np.schoolpgi.enums.StatusCodes;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.LevelMaster;
import com.np.schoolpgi.model.Roles;
import com.np.schoolpgi.model.SurveyCloneMapping;
import com.np.schoolpgi.model.SurveyMapQuestion;
import com.np.schoolpgi.model.SurveyMaster;
import com.np.schoolpgi.service.SurveyMasterService;
import com.np.schoolpgi.util.StringUtils;
import com.np.schoolpgi.util.SurveyMasterViewComparator;

//@Transactional
@Service
public class SurveyMasterServiceImpl implements SurveyMasterService {

	final static Logger LOGGER = LogManager.getLogger(SurveyMasterServiceImpl.class);

	@Autowired
	SurveyMasterRepository surveyMasterRepository;

	@Autowired
	private SurveyUserMappingRepo surveyUserMappingRepo;

	@Autowired
	SurveyMapQuestionRepo surveyMapQuestionRepo;

	@Autowired
	LevelMasterRepository levelMasterRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	SurveyCloneMappingRepo surveyCloneMappingRepo;

	@Autowired
	private SurveyNotificationRepository surveyNotificationRepository;

	@Autowired
	private SurveyDataEntryRepo surveyDataEntryRepo;

	@Override
	public Integer createSurvey(CreateSurveyRequest createSurveyRequest) {

		try {
			if (createSurveyRequest.getId() != null) {
				return 3;
			}
			Optional<SurveyMaster> find = surveyMasterRepository.findByYearCodeAndSurveyNameIgnoreCase(
					createSurveyRequest.getYearCode(), createSurveyRequest.getSurveyName());
			if (find.isPresent()) {
				return 2;
			}
			SurveyMaster surveyMaster = new SurveyMaster();
			surveyMaster.setYearCode(createSurveyRequest.getYearCode());
			surveyMaster.setSurveyName(createSurveyRequest.getSurveyName());
			Date startDate = new SimpleDateFormat("dd-MM-yyyy").parse(createSurveyRequest.getStartDate());
			surveyMaster.setSurveyStartDate(startDate);
			Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse(createSurveyRequest.getEndDate());
			surveyMaster.setSurveyEndDate(endDate);
			surveyMaster.setSurveyDescription(createSurveyRequest.getDescription());
			surveyMaster.setCreatedAt(new Date());
			surveyMaster.setCreatedBy(createSurveyRequest.getLoggedInUserId());
			surveyMaster.setProcedureName(createSurveyRequest.getProcedureName());

			// ---------------------Validate Viewer Level and Roles
			// -----------------------------------////////////
			List<Integer> x = Arrays.stream(createSurveyRequest.getViewerLevelId().split(",")).map(Integer::parseInt)
					.collect(Collectors.toList());

			// Map<Integer, List<Integer>> output = new HashMap<>();

			String[] pairs = createSurveyRequest.getViewerRoleId().split(",");
			for (String pair : pairs) {
				String[] values = pair.split("\\|\\|");
				int key = Integer.parseInt(values[0]);
				int value = Integer.parseInt(values[1]);
				Optional<LevelMaster> l = levelMasterRepository.findById(key);
				if (!l.isPresent()) {
					return 4;
				}
				Optional<Roles> r = roleRepository.findById(value);
				if (r.isPresent()) {
					continue;
				} else {
					return 5;
				}
//		            if (!output.containsKey(key)) {
//		                output.put(key, new ArrayList<>());
//		            }
//
//		            output.get(key).add(value);
			}

			for (Integer i : x) {
				Optional<LevelMaster> l = levelMasterRepository.findById(i);
				if (l.isPresent()) {
					continue;
				} else {
					return 4;
				}
			}
			surveyMaster.setViewerLevelId(createSurveyRequest.getViewerLevelId());
			surveyMaster.setViewerRoleId(createSurveyRequest.getViewerRoleId());
			// ---------------------Validate Reviewer Level and Roles
			// -----------------------------------////////////
			List<Integer> reviewerLevel = Arrays.stream(createSurveyRequest.getReviewerLevelId().split(","))
					.map(Integer::parseInt).collect(Collectors.toList());
			for (Integer i : reviewerLevel) {
				Optional<LevelMaster> l = levelMasterRepository.findById(i);
				if (l.isPresent()) {
					continue;
				} else {
					return 4;
				}
			}
			String[] pairsReviewer = createSurveyRequest.getReviewerRoleId().split(",");
			for (String pair : pairsReviewer) {
				String[] values = pair.split("\\|\\|");
				int key = Integer.parseInt(values[0]);
				int value = Integer.parseInt(values[1]);
				Optional<LevelMaster> l = levelMasterRepository.findById(key);
				if (!l.isPresent()) {
					return 4;
				}
				Optional<Roles> r = roleRepository.findById(value);
				if (r.isPresent()) {
					continue;
				} else {
					return 5;
				}
//		            if (!output.containsKey(key)) {
//		                output.put(key, new ArrayList<>());
//		            }
//
//		            output.get(key).add(value);
			}

//			for (Integer i : x) {
//				Optional<LevelMaster> l = levelMasterRepository.findById(i);
//				if (l.isPresent()) {
//					continue;
//				} else {
//					return 4;
//				}
//			}
			surveyMaster.setReviewerLevelId(createSurveyRequest.getReviewerLevelId());
			surveyMaster.setReviewerRoleId(createSurveyRequest.getReviewerRoleId());

			// ---------------------Validate Approver Level and Roles
			// -----------------------------------////////////
			Optional<LevelMaster> approvLevelMaster = levelMasterRepository
					.findById(createSurveyRequest.getApproverLevelId());
			if (approvLevelMaster.isPresent()) {
				surveyMaster.setApproverLevelId(approvLevelMaster.get());
			} else {
				return 3;
			}

			Optional<Roles> approvRoleMaster = roleRepository.findById(createSurveyRequest.getApproverRoleId());
			if (approvRoleMaster.isPresent()) {
				surveyMaster.setApproverRoleId(approvRoleMaster.get());
			} else {
				return 5;
			}

			// ---------------------Validate Deo Level and Roles
			// -----------------------------------////////////

			Optional<LevelMaster> deoLevelMaster = levelMasterRepository.findById(createSurveyRequest.getDeoLevelId());
			if (deoLevelMaster.isPresent()) {
				surveyMaster.setDeoLevelId(deoLevelMaster.get());
			} else {
				return 3;
			}
			Optional<Roles> deoRoleOptional = roleRepository.findById(createSurveyRequest.getDeoRoleId());
			if (deoRoleOptional.isPresent()) {
				surveyMaster.setDeoRoleId(deoRoleOptional.get());
			} else {
				return 5;
			}

			surveyMaster.setStatus(createSurveyRequest.getStatus());
			surveyMaster.setReviewMandatory(createSurveyRequest.getReviewMandatory());
			surveyMaster.setAssignedSurveyStatus(false);
			SurveyMaster savedSurvey = surveyMasterRepository.save(surveyMaster);

			// Call Assign Survey Using Stored Procedure
			if (savedSurvey != null) {
				String surveyAutoAssign = surveyUserMappingRepo.surveyAutoAssign(savedSurvey.getId(), null);
				LOGGER.info(surveyAutoAssign);
			}

			return 1;

		} catch (Exception e) {
			LOGGER.info(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public Integer updateSurvey(CreateSurveyRequest createSurveyRequest) {
		try {
			SurveyMaster surveyMaster = new SurveyMaster();
			if (createSurveyRequest.getId() != null) {

				Optional<SurveyMaster> surveyMasterFind = surveyMasterRepository.findById(createSurveyRequest.getId());
				if (surveyMasterFind.isPresent()) {
					Optional<SurveyMaster> find = surveyMasterRepository.findbysurvayname(
							createSurveyRequest.getYearCode(), createSurveyRequest.getSurveyName(),
							createSurveyRequest.getId());
					if (find.isPresent()) {
						return 2;
					}
					surveyMaster = surveyMasterFind.get();
					surveyMaster.setYearCode(createSurveyRequest.getYearCode());
					surveyMaster.setSurveyName(createSurveyRequest.getSurveyName());
					Date startDate = new SimpleDateFormat("dd-MM-yyyy").parse(createSurveyRequest.getStartDate());
					surveyMaster.setSurveyStartDate(startDate);
					Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse(createSurveyRequest.getEndDate());
					surveyMaster.setSurveyEndDate(endDate);
					surveyMaster.setSurveyDescription(createSurveyRequest.getDescription());
					surveyMaster.setCreatedAt(new Date());
					surveyMaster.setCreatedBy(createSurveyRequest.getLoggedInUserId());
					surveyMaster.setProcedureName(createSurveyRequest.getProcedureName());

					// ---------------------Validate Viewer Level and Roles
					// -----------------------------------////////////
					List<Integer> x = Arrays.stream(createSurveyRequest.getViewerLevelId().split(","))
							.map(Integer::parseInt).collect(Collectors.toList());

					// Map<Integer, List<Integer>> output = new HashMap<>();

					String[] pairs = createSurveyRequest.getViewerRoleId().split(",");
					for (String pair : pairs) {
						String[] values = pair.split("\\|\\|");
						int key = Integer.parseInt(values[0]);
						int value = Integer.parseInt(values[1]);
						Optional<LevelMaster> l = levelMasterRepository.findById(key);
						if (!l.isPresent()) {
							return 4;
						}
						Optional<Roles> r = roleRepository.findById(value);
						if (r.isPresent()) {
							continue;
						} else {
							return 5;
						}
//				            if (!output.containsKey(key)) {
//				                output.put(key, new ArrayList<>());
//				            }
						//
//				            output.get(key).add(value);
					}

					for (Integer i : x) {
						Optional<LevelMaster> l = levelMasterRepository.findById(i);
						if (l.isPresent()) {
							continue;
						} else {
							return 4;
						}
					}
					surveyMaster.setViewerLevelId(createSurveyRequest.getViewerLevelId());
					surveyMaster.setViewerRoleId(createSurveyRequest.getViewerRoleId());
					// ---------------------Validate Reviewer Level and Roles
					// -----------------------------------////////////
					List<Integer> reviewerLevel = Arrays.stream(createSurveyRequest.getReviewerLevelId().split(","))
							.map(Integer::parseInt).collect(Collectors.toList());
					for (Integer i : reviewerLevel) {
						Optional<LevelMaster> l = levelMasterRepository.findById(i);
						if (l.isPresent()) {
							continue;
						} else {
							return 4;
						}
					}
					String[] pairsReviewer = createSurveyRequest.getReviewerRoleId().split(",");
					for (String pair : pairsReviewer) {
						String[] values = pair.split("\\|\\|");
						int key = Integer.parseInt(values[0]);
						int value = Integer.parseInt(values[1]);
						Optional<LevelMaster> l = levelMasterRepository.findById(key);
						if (!l.isPresent()) {
							return 4;
						}
						Optional<Roles> r = roleRepository.findById(value);
						if (r.isPresent()) {
							continue;
						} else {
							return 5;
						}
//				            if (!output.containsKey(key)) {
//				                output.put(key, new ArrayList<>());
//				            }
						//
//				            output.get(key).add(value);
					}

//					for (Integer i : x) {
//						Optional<LevelMaster> l = levelMasterRepository.findById(i);
//						if (l.isPresent()) {
//							continue;
//						} else {
//							return 4;
//						}
//					}
					surveyMaster.setReviewerLevelId(createSurveyRequest.getReviewerLevelId());
					surveyMaster.setReviewerRoleId(createSurveyRequest.getReviewerRoleId());

					// ---------------------Validate Approver Level and Roles
					// -----------------------------------////////////
					Optional<LevelMaster> approvLevelMaster = levelMasterRepository
							.findById(createSurveyRequest.getApproverLevelId());
					if (approvLevelMaster.isPresent()) {
						surveyMaster.setApproverLevelId(approvLevelMaster.get());
					} else {
						return 3;
					}

					Optional<Roles> approvRoleMaster = roleRepository.findById(createSurveyRequest.getApproverRoleId());
					if (approvRoleMaster.isPresent()) {
						surveyMaster.setApproverRoleId(approvRoleMaster.get());
					} else {
						return 5;
					}

					// ---------------------Validate Deo Level and Roles
					// -----------------------------------////////////

					Optional<LevelMaster> deoLevelMaster = levelMasterRepository
							.findById(createSurveyRequest.getDeoLevelId());
					if (deoLevelMaster.isPresent()) {
						surveyMaster.setDeoLevelId(deoLevelMaster.get());
					} else {
						return 3;
					}
					Optional<Roles> deoRoleOptional = roleRepository.findById(createSurveyRequest.getDeoRoleId());
					if (deoRoleOptional.isPresent()) {
						surveyMaster.setDeoRoleId(deoRoleOptional.get());
					} else {
						return 5;
					}

					surveyMaster.setStatus(createSurveyRequest.getStatus());
					surveyMaster.setReviewMandatory(createSurveyRequest.getReviewMandatory());

					surveyMasterRepository.save(surveyMaster);

					// Call Assign Survey Using Stored Procedure
					String surveyAutoAssign = surveyUserMappingRepo.surveyAutoAssign(createSurveyRequest.getId(), null);
					LOGGER.info(surveyAutoAssign);

					return 1;

				} else {
					return 0;
				}

			} else {
				return 3;
			}

		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public ResponseWithPagination surveyList(ViewReqById viewReqById, int pageNo, int pageSize, String sortDir,
			String sortBy, String searchKey) {

		try {

//			Integer pageNo=0;
//			Integer pageSize=10;
//			String sortDir="asc";
//			String sortBy="created_at";
//			String searchKey="";
//			

			ResponseWithPagination responseWithPagination = new ResponseWithPagination();

			Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
					: Sort.by(sortBy).ascending();
			Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

			Page<SurveyMaster> pageableData = null;
			List<SurveyMaster> surveyMasters = new ArrayList<>();
			if (viewReqById.getId() == null) {
				if (viewReqById.getYearcode() == null || viewReqById.getYearcode().toLowerCase().equals("all")) {
					viewReqById.setYearcode("");
				}

//				surveyMasters = surveyMasterRepository.findAll();

				pageableData = surveyMasterRepository.findAllSurvey(pageable, searchKey, viewReqById.getYearcode());
				surveyMasters = pageableData.getContent();
			} else {

				Optional<SurveyMaster> surveyMaster = surveyMasterRepository.findById(viewReqById.getId());
				if (surveyMaster.isPresent()) {
					surveyMasters.add(surveyMaster.get());
				}
			}

			if (surveyMasters != null) {
				List<SurveyListResponse> surveyListResponses = new ArrayList<SurveyListResponse>();

				for (SurveyMaster s : surveyMasters) {
					SurveyListResponse surveyListResponse = new SurveyListResponse();

					List<SurveyMapQuestion> surveyMapQ = surveyMapQuestionRepo.findBySurveyMasterId(s.getId());

					if (surveyMapQ.isEmpty()) {
						surveyListResponse.setIsMappedQuest(false);
						surveyListResponse.setIsNotified(true);
						surveyListResponse.setIsNotifiedTpd(true);
					} else {
						String tpdNotify = surveyUserMappingRepo.surveyTPDNotifyNotify(s.getId(), null);
						JSONParser parser = new JSONParser();
						JSONObject json = (JSONObject) parser.parse(tpdNotify);
						System.out.println(json);

						if (json.get("success").toString() == "true") {
							System.out.println(json.get("result"));
							JSONArray jsonArray = (JSONArray) json.get("result");
							JSONObject jsonTpd = (JSONObject) jsonArray.get(0);
							if (jsonTpd.get("tpd_notify").toString().equals("1")) {
								System.out.println(jsonTpd.get("tpd_notify"));
								surveyListResponse.setIsNotifiedTpd(false);
							} else {
								surveyListResponse.setIsNotifiedTpd(true);
							}
							if (jsonTpd.get("notfy").toString().equals("1")) {
								System.out.println(jsonTpd.get("notfy"));
								surveyListResponse.setIsNotified(false);
							} else {
								surveyListResponse.setIsNotified(true);
							}
							if (jsonTpd.get("is_map_qus_enable").toString().equals("1")) {
								System.out.println(jsonTpd.get("notfy"));
								surveyListResponse.setIsMappedQuest(false);
							} else {
								surveyListResponse.setIsMappedQuest(true);
							}
							if (jsonTpd.get("is_assign_enable").toString().equals("1")) {
								System.out.println(jsonTpd.get("notfy"));
								surveyListResponse.setIsAssignEnable(false);
							} else {
								surveyListResponse.setIsAssignEnable(true);
							}

						} else {
							surveyListResponse.setIsNotified(true);
							surveyListResponse.setIsNotifiedTpd(true);
							surveyListResponse.setIsMappedQuest(true);
							surveyListResponse.setIsAssignEnable(true);
						}

					}
					if (s.getStatus().equals(StatusCodes.INACTIVE.getCode())) {
						surveyListResponse.setStatus(StatusCodes.INACTIVE.getCode());
					} else {
						surveyListResponse.setStatus(StatusCodes.ACTIVE.getCode());
						String status = surveyNotificationRepository.getStatusBySurveyId(String.valueOf(s.getId()));
						if (status.equalsIgnoreCase("A")) {
							surveyListResponse.setStatus(StatusCodes.CREATED.getCode());
						}

						Timestamp timestamp = (Timestamp) s.getSurveyEndDate();
						LocalDateTime endDate = timestamp.toLocalDateTime();
						LocalDate surveyEndDate = endDate.toLocalDate();
						
						if (surveyEndDate.isBefore(LocalDate.now())) {
							List<int[]> userIds = surveyUserMappingRepo.getIdBySurveyIds(String.valueOf(s.getId()));

							int isExpired = 0;

							for (int[] userId : userIds) {

								List<SurveyDataEntry> dataEntries = surveyDataEntryRepo.getDataEntryDetails(userId[0]);
								if (!dataEntries.isEmpty()) {
									if (dataEntries.get(0).getStatus().equalsIgnoreCase("A")) {
										isExpired++;
									}
								} else {
									surveyListResponse.setStatus(StatusCodes.EXPIRED.getCode());
									break;
								}
							}

							if (isExpired != userIds.size()) {
								surveyListResponse.setStatus(StatusCodes.EXPIRED.getCode());
							}
						}

						if (surveyListResponse.getStatus().equals(StatusCodes.EXPIRED.getCode())) {
							surveyListResponse.setIsNotified(true);
							surveyListResponse.setIsNotifiedTpd(true);
							surveyListResponse.setIsMappedQuest(true);
						}
					}

					List<int[]> userServeyMap = surveyUserMappingRepo.getUserServeyMap(String.valueOf(s.getId()));
					List<Integer> resultList = new ArrayList<>();

					for (int[] array : userServeyMap) {
						resultList.add(array[1]);

					}
					List<int[]> dataEntries = surveyDataEntryRepo.getDataEntryDetailsByUserIds(resultList);
					List<int[]> uniqueDataEntries = StringUtils.removeDuplicates(dataEntries);
					Boolean flag = StringUtils.compareListOfArrays(userServeyMap, uniqueDataEntries);
					if (flag) {
						List<Object[]> dataEntry = surveyDataEntryRepo
								.getDataEntryDetailsByServeyIds(String.valueOf(s.getId()));
						System.out.println(dataEntry.size() == dataEntries.size());
						if (dataEntry.size() == dataEntries.size()) {
							surveyListResponse.setStatus(StatusCodes.COMPLETED.getCode());
						}
					}

//					SurveyNotification surveyNotifications = surveyNotificationRepository.findSurveyNotificationBySurveyId(s.getId());
//					
//					if(surveyNotifications==null) {
//						surveyListResponse.setIsNotified(true);	
//					}
//					else 
//						if(surveyNotifications.getStatus().equalsIgnoreCase("A")){
//						surveyListResponse.setIsNotified(false);
//					}else 
////						if(surveyNotifications.getStatus().equalsIgnoreCase("D"))
//					{
//						surveyListResponse.setIsNotified(true);
//					}

					surveyListResponse.setId(s.getId());

					s.getApproverLevelId().setActiveRole(s.getApproverRoleId().getId().toString());
					surveyListResponse.setApproverLevelId(s.getApproverLevelId());
					surveyListResponse.setApproverRoleId(s.getApproverRoleId());

					s.getDeoLevelId().setActiveRole(s.getDeoRoleId().getId().toString());
					surveyListResponse.setDeoLevelId(s.getDeoLevelId());
					surveyListResponse.setDeoRoleId(s.getDeoRoleId());

					surveyListResponse.setSurveyDescription(s.getSurveyDescription());
					surveyListResponse.setSurveyStartDate(s.getSurveyStartDate());
					surveyListResponse.setSurveyEndDate(s.getSurveyEndDate());
					surveyListResponse.setSurveyName(s.getSurveyName());
					surveyListResponse.setYearCode(s.getYearCode());
					surveyListResponse.setAssignedSurveyStatus(s.getAssignedSurveyStatus());
					surveyListResponse.setProcedureName(s.getProcedureName());

					Map<String, List<Roles>> outputReviewerRole = new HashMap<>();
					Map<Integer, String> activeReviewerRoles = new HashMap<>();
					String[] pairs = s.getReviewerRoleId().split(",");
					for (String pair : pairs) {
						String[] values = pair.split("\\|\\|");
						int key = Integer.parseInt(values[0]);
						int value = Integer.parseInt(values[1]);
						Optional<LevelMaster> lrr = levelMasterRepository.findById(key);

						if (lrr.isPresent()) {
							if (!outputReviewerRole.containsKey(lrr.get().getLevelName())) {
								outputReviewerRole.put(lrr.get().getLevelName(), new ArrayList<>());
								activeReviewerRoles.put(lrr.get().getId(), "");
							}

							Optional<Roles> r = roleRepository.findById(value);
							if (r.isPresent()) {
								outputReviewerRole.get(lrr.get().getLevelName()).add(r.get());
								if (activeReviewerRoles.get(lrr.get().getId()) == "")
									activeReviewerRoles.put(lrr.get().getId(), r.get().getId().toString());
								else
									activeReviewerRoles.put(lrr.get().getId(),
											activeReviewerRoles.get(lrr.get().getId()) + ","
													+ r.get().getId().toString());

							}

						}

					}
					List<Integer> x1 = Arrays.stream(s.getReviewerLevelId().split(",")).map(Integer::parseInt)
							.collect(Collectors.toList());
					List<LevelMaster> reviewerLevel = levelMasterRepository.findByIdIn(x1);
					for (LevelMaster level : reviewerLevel) {
						level.setActiveRole(activeReviewerRoles.get(level.getId()));
					}
					surveyListResponse.setReviewerLevelId(reviewerLevel);

					Map<String, List<Roles>> outputViewerRole = new HashMap<>();
					Map<Integer, String> activeViewerRoles = new HashMap<>();
					String[] pairsViewer = s.getViewerRoleId().split(",");
					for (String pair : pairsViewer) {
						String[] values = pair.split("\\|\\|");
						int key = Integer.parseInt(values[0]);
						int value = Integer.parseInt(values[1]);
						Optional<LevelMaster> lvr = levelMasterRepository.findById(key);
						if (lvr.isPresent()) {
							if (!outputViewerRole.containsKey(lvr.get().getLevelName())) {
								outputViewerRole.put(lvr.get().getLevelName(), new ArrayList<>());
								activeViewerRoles.put(lvr.get().getId(), "");
							}

							Optional<Roles> rvr = roleRepository.findById(value);
							if (rvr.isPresent()) {
								outputViewerRole.get(lvr.get().getLevelName()).add(rvr.get());
								if (activeViewerRoles.get(lvr.get().getId()) == "")
									activeViewerRoles.put(lvr.get().getId(), rvr.get().getId().toString());
								else
									activeViewerRoles.put(lvr.get().getId(), activeViewerRoles.get(lvr.get().getId())
											+ "," + rvr.get().getId().toString());

							}

						}
					}
					List<Integer> x = Arrays.stream(s.getViewerLevelId().split(",")).map(Integer::parseInt)
							.collect(Collectors.toList());
					List<LevelMaster> viewLevels = levelMasterRepository.findByIdIn(x);
					for (LevelMaster level : viewLevels) {
						level.setActiveRole(activeViewerRoles.get(level.getId()));
					}

					surveyListResponse.setViewerLevelId(viewLevels);

					surveyListResponse.setReviewMandatory(s.getReviewMandatory());
					surveyListResponse.setReviewerRole(outputReviewerRole);
					surveyListResponse.setViewerRole(outputViewerRole);
					surveyListResponses.add(surveyListResponse);
				}
				Collections.sort(surveyListResponses, new SurveyMasterViewComparator());
				if (viewReqById.getId() == null) {

					responseWithPagination.setTotalElements(pageableData.getTotalElements());
					responseWithPagination.setTotalPages(pageableData.getTotalPages());
					responseWithPagination.setLast(pageableData.isLast());
					responseWithPagination.setPageNo(pageableData.getNumber());
					responseWithPagination.setPageSize(pageableData.getSize());
				}
				responseWithPagination.setHttpStatus(HttpStatus.OK);
				responseWithPagination.setMessage(APPServiceCode.APP001.getStatusDesc());
				responseWithPagination.setMessageCode(APPServiceCode.APP001.getStatusCode());
				responseWithPagination.setResult(surveyListResponses);
				responseWithPagination.setSuccess(true);
				return responseWithPagination;
			} else
				return null;

		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public Integer deleteSurvey(DeleteSurveyRequest deleteSurveyRequest) {
		Optional<SurveyMaster> surveyMasterFind = surveyMasterRepository.findById(deleteSurveyRequest.getId());
		if (surveyMasterFind.isPresent()) {
			surveyMasterFind.get().setStatus(StatusCodes.DELETED.getCode());
			surveyMasterFind.get().setUpdatedAt(new Date());
			surveyMasterFind.get().setUpdatedBy(deleteSurveyRequest.getLoggedInUserId());
			surveyMasterRepository.save(surveyMasterFind.get());
			return 1;
		}
		return 0;
	}

	@Override
	public String surveyDataEntryById(SurveyDataEntryListRequest surveyDataEntryListRequest) {
		try {
			return surveyMasterRepository.surveyDataEntryById(surveyDataEntryListRequest.getSurveyId(),
					surveyDataEntryListRequest.getIsThird(), surveyDataEntryListRequest.getLoginId(),
					surveyDataEntryListRequest.getInstanceId(), null);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Transactional
	@Override
	public Integer cloneSurvey(SurveyCloneRequest surveyCloneRequest) {

		try {
			Optional<SurveyMaster> findByYearCodeAndSurveyName = surveyMasterRepository
					.findByYearCodeAndSurveyNameIgnoreCase(surveyCloneRequest.getSurveyMaster().getYearCode(),
							surveyCloneRequest.getSurveyMaster().getSurveyName());
			if (findByYearCodeAndSurveyName.isPresent()) {
				return 0;
			}

			SurveyMaster surveyMaster = new SurveyMaster();
			Optional<LevelMaster> approverLevel = levelMasterRepository
					.findById(surveyCloneRequest.getSurveyMaster().getApproverLevelId());
			Optional<Roles> approverRole = roleRepository
					.findById(surveyCloneRequest.getSurveyMaster().getApproverRoleId());

			surveyMaster.setApproverRoleId(approverRole.get());
			surveyMaster.setApproverLevelId(approverLevel.get());

			Optional<LevelMaster> deoLevel = levelMasterRepository
					.findById(surveyCloneRequest.getSurveyMaster().getDeoLevelId());
			Optional<Roles> deoRole = roleRepository.findById(surveyCloneRequest.getSurveyMaster().getDeoRoleId());
			surveyMaster.setDeoLevelId(deoLevel.get());
			surveyMaster.setDeoRoleId(deoRole.get());

			surveyMaster.setViewerLevelId(surveyCloneRequest.getSurveyMaster().getViewerLevelId());
			surveyMaster.setViewerRoleId(surveyCloneRequest.getSurveyMaster().getViewerRoleId());

			surveyMaster.setReviewerLevelId(surveyCloneRequest.getSurveyMaster().getReviewerLevelId());
			surveyMaster.setReviewerRoleId(surveyCloneRequest.getSurveyMaster().getReviewerRoleId());

			surveyMaster.setCreatedAt(new Date());
			surveyMaster.setCreatedBy(surveyCloneRequest.getSurveyMaster().getLoggedInUserId());
			surveyMaster.setStatus(surveyCloneRequest.getSurveyMaster().getStatus());
			surveyMaster.setSurveyDescription(surveyCloneRequest.getSurveyMaster().getDescription());
			Date startDate = new SimpleDateFormat("dd-MM-yyyy")
					.parse(surveyCloneRequest.getSurveyMaster().getStartDate());
			surveyMaster.setSurveyStartDate(startDate);
			Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse(surveyCloneRequest.getSurveyMaster().getEndDate());
			surveyMaster.setSurveyEndDate(endDate);
			surveyMaster.setSurveyName(surveyCloneRequest.getSurveyMaster().getSurveyName());

			surveyMaster.setYearCode(surveyCloneRequest.getSurveyMaster().getYearCode());
			surveyMaster.setReviewMandatory(surveyCloneRequest.getSurveyMaster().getReviewMandatory());
			surveyMaster.setAssignedSurveyStatus(surveyCloneRequest.getSurveyMaster().getAssignedSurveyStatus());

			SurveyMaster savedSurvey = surveyMasterRepository.save(surveyMaster);

			// Call Assign Survey Using Stored Procedure
			if (savedSurvey != null) {
				surveyUserMappingRepo.surveyAutoAssign(savedSurvey.getId(), null);
				LOGGER.info("Survey auto assigned");
			}

			List<SurveyMapQuestion> mapQuestion = surveyCloneRequest.getMapQuestion();
			List<SurveyMapQuestion> mapQuests = new ArrayList<SurveyMapQuestion>();

			LOGGER.info("#Request mapQuestion size " + mapQuestion.size());
			int count = 0;
			for (SurveyMapQuestion surveyMap : mapQuestion) {
				SurveyMapQuestion surveyMapQuestion = new SurveyMapQuestion();
				if (surveyMap != null) {
					if (StringUtils.isValidObj(surveyMap.getSubDomain().getId())) {
						surveyMapQuestion.setSubDomain(surveyMap.getSubDomain());
					} else {
						surveyMapQuestion.setSubDomain(null);
					}

					surveyMapQuestion.setCreatedAt(new Date());
					surveyMapQuestion.setCreatedBy(surveyCloneRequest.getSurveyMaster().getLoggedInUserId());
					LOGGER.info("#surveyMap.getDataSource() " + surveyMap.getDataSource());
					if (surveyMap.getDataSource().getId() != null) {
						surveyMapQuestion.setDataSource(surveyMap.getDataSource());
					}
					surveyMapQuestion.setDomainMaster(surveyMap.getDomainMaster());
					surveyMapQuestion.setIsDeleted(surveyMap.getIsDeleted());
					surveyMapQuestion.setIsMandatory(surveyMap.getIsMandatory());
					surveyMapQuestion.setIsThirdParty(surveyMap.getIsThirdParty());
					surveyMapQuestion.setPointerLogic(surveyMap.getPointerLogic());
					surveyMapQuestion.setQuestionMaster(surveyMap.getQuestionMaster());
					surveyMapQuestion.setSubQuestionIds(surveyMap.getSubQuestionIds());
					surveyMapQuestion.setSurveyMaster(savedSurvey);
					surveyMapQuestion.setValueLogic(surveyMap.getValueLogic());
					surveyMapQuestion.setWeightage(surveyMap.getWeightage());
					count += 1;
					LOGGER.info("#count " + count);
					mapQuests.add(surveyMapQuestion);
				}

			}
			surveyMapQuestionRepo.saveAll(mapQuests);
			SurveyCloneMapping surveyCloneMapping = new SurveyCloneMapping();
			surveyCloneMapping.setCreatedAt(Instant.now());
			surveyCloneMapping.setCreatedBy(surveyCloneRequest.getSurveyMaster().getLoggedInUserId().longValue());
			surveyCloneMapping.setNewSurveyId(savedSurvey.getId().longValue());
			surveyCloneMapping.setOldSurveyId(surveyCloneRequest.getSurveyMaster().getId().longValue());

			surveyCloneMappingRepo.save(surveyCloneMapping);

			return 1;
		} catch (Exception e) {
			LOGGER.error(e);
			e.printStackTrace();
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public String getSurveyListForDeo(SurveyDataEntryListRequest surveyDataEntryListRequest, Integer pageNo,
			Integer pageSize, String sortDir, String sortBy, String searchKey) {
		try {
			return surveyMasterRepository.getSurveyListForDeo(pageNo, pageSize, sortDir, sortBy, searchKey,
					surveyDataEntryListRequest.getIsThird(), surveyDataEntryListRequest.getLoginId(),
					surveyDataEntryListRequest.getYearcode(), null);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + "-" + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public String insertSurveyDataEntry(String _jsontext) {

		try {
			return surveyMasterRepository.insertSurveyDataEntry(_jsontext);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + "-" + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public String surveyProcedureCall(SurveyIdRequest surveyIdRequest, Integer pageNo, Integer pageSize, String sortDir,
			String sortBy, String searchKey) {

		String procedureName = surveyMasterRepository.findProcedureBySurveyId(surveyIdRequest.getSurveyId());
		String result = "";
		if (StringUtils.isEmpty(procedureName)) {
			String response = "{\"success\": false,\r\n" + "    \"status\": \"INTERNAL_SERVER_ERROR\",\r\n"
					+ "    \"errorMessage\": \"Procedure Does Not Exist For This Survey.\",\r\n" + "}";
			return response;
		} else {

			try {
				int userId = 1;

				try (Connection connection = DriverManager.getConnection("jdbc:postgresql://10.0.4.180:5432/pgi_db",
						"pgi_dbu", "$ERfg@#^%56")) {
//					String sql = "CALL " + procedureName + "(?, ?, ?, ?, ?, ?, ?, ?)";

					CallableStatement callableStatement = connection
							.prepareCall("call " + procedureName + "(?, ?, ?, ?, ?, ?, ?,?)");

					callableStatement.setInt(1, pageNo);
					callableStatement.setInt(2, pageSize);
					callableStatement.setString(3, sortDir);
					callableStatement.setString(4, sortBy);
					callableStatement.setString(5, searchKey);
					callableStatement.setInt(6, userId);
					callableStatement.setInt(7, surveyIdRequest.getSurveyId());
					callableStatement.registerOutParameter(8, Types.VARCHAR);

					callableStatement.execute();

					result = callableStatement.getString(8);
					connection.close();
				} catch (SQLException e) {
					LOGGER.info(e.getMessage());
					throw new SomethingWentWrongException(e.getMessage());
				}

				if (!StringUtils.isEmpty(result)) {
					return result;
				} else {
					return "Could Not Execute Procedure";
				}
			} catch (Exception e) {
				LOGGER.info(e.getMessage());
				throw new SomethingWentWrongException(e.getMessage());
			}

		}
	}

}
