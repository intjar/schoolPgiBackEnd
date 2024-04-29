package com.np.schoolpgi.service.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.schoolpgi.dao.DashboardRepository;
import com.np.schoolpgi.dao.SurveyDataEntry;
import com.np.schoolpgi.dao.SurveyDataEntryRepo;
import com.np.schoolpgi.dao.SurveyMapQuestionRepo;
import com.np.schoolpgi.dao.SurveyNotificationRepository;
import com.np.schoolpgi.dao.SurveyUserMappingRepo;
import com.np.schoolpgi.dao.UserLevelIdRequest;
import com.np.schoolpgi.dao.UserRepository;
import com.np.schoolpgi.dto.request.LevelIdNameModel;
import com.np.schoolpgi.dto.request.ViewReqById;
import com.np.schoolpgi.dto.response.DashboardLevelIdResponse;
import com.np.schoolpgi.dto.response.DashboardResponse;
import com.np.schoolpgi.dto.response.DashboardUserListResponse;
import com.np.schoolpgi.enums.StatusCodes;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.InstanceMaster;
import com.np.schoolpgi.model.SurveyMapQuestion;
import com.np.schoolpgi.model.SurveyMaster;
import com.np.schoolpgi.model.User;
import com.np.schoolpgi.service.DashboardService;
import com.np.schoolpgi.service.InstanceMasterService;
import com.np.schoolpgi.util.StringUtils;

@Service
public class DashboardServiceImpl implements DashboardService {

	final static Logger LOGGER = LogManager.getLogger(SurveyMasterServiceImpl.class);

	@Autowired
	private DashboardRepository dashboardRepository;

	@Autowired
	private SurveyDataEntryRepo surveyDataEntryRepo;

	@Autowired
	private SurveyUserMappingRepo surveyUserMappingRepo;

	@Autowired
	private SurveyNotificationRepository surveyNotificationRepository;

	@Autowired
	private SurveyMapQuestionRepo surveyMapQuestionRepo;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private InstanceMasterService instanceService;

	@Value("${website.database.user}")
	private String WEBSITE_DATABASE_USER;
	@Value("${website.database.password}")
	private String WEBSITE_DATABASE_PASSWORD;
//	@Autowired
//	private SurveyMasterRepository surveyMasterRepository;

//	@Override
//	public List<SurveyMaster> getSurveyList(ViewReqById viewReqById) {
//		LOGGER.info("Inside DashboardServiceImpl, getSurveyList");
//		List<SurveyMaster> surveyMasters = new ArrayList<>();
//		if (viewReqById.getId() == null) {
//			if (viewReqById.getYearcode() == null || viewReqById.getYearcode().toLowerCase().equals("all")) {
//				viewReqById.setYearcode("");
//			}
//			surveyMasters = dashboardRepository.findAllSurvey(viewReqById.getYearcode());
//		} else {
//			Optional<SurveyMaster> surveyMaster = dashboardRepository.findByCreatedBy(viewReqById.getId());
//			if (surveyMaster.isPresent()) {
//				surveyMasters.add(surveyMaster.get());
//			}
//		}
//		// Set status
//		LOGGER.info(surveyMasters);
//		for (SurveyMaster surveyMaster : surveyMasters) {
//
//			if (!surveyMaster.getStatus().equals(StatusCodes.INACTIVE.getCode())) {
//				surveyMaster.setStatus(StatusCodes.ACTIVE.getCode());
//				String status = surveyNotificationRepository.getStatusBySurveyId(String.valueOf(surveyMaster.getId()));
//
//				if (status.equalsIgnoreCase("A")) {
//					surveyMaster.setStatus(StatusCodes.CREATED.getCode());
//				}
//				Timestamp timestamp = (Timestamp) surveyMaster.getSurveyEndDate();
//				LocalDateTime surveyEndDate = timestamp.toLocalDateTime();
//				if (surveyEndDate.isBefore(LocalDateTime.now())) {
//					List<int[]> userIds = surveyUserMappingRepo.getIdBySurveyIds(String.valueOf(surveyMaster.getId()));
//					int isExpired = 0;
//					for (int[] userId : userIds) {
//						List<SurveyDataEntry> dataEntries = surveyDataEntryRepo.getDataEntryDetails(userId[0]);
//						if (!dataEntries.isEmpty()) {
//							if (dataEntries.get(0).getStatus().equalsIgnoreCase("A")) {
//								isExpired++;
//							}
//						} else {
//							surveyMaster.setStatus(StatusCodes.EXPIRED.getCode());
//							break;
//						}
//					}
//
//					if (isExpired != userIds.size()) {
//						surveyMaster.setStatus(StatusCodes.EXPIRED.getCode());
//					}
//				}
//			}
//
//			List<int[]> userServeyMap = surveyUserMappingRepo.getUserServeyMap(String.valueOf(surveyMaster.getId()));
//			List<Integer> resultList = new ArrayList<>();
//
//			for (int[] array : userServeyMap) {
//				resultList.add(array[1]);
//			}
//			List<int[]> dataEntries = surveyDataEntryRepo.getDataEntryDetailsByUserIds(resultList);
//			List<int[]> uniqueDataEntries = StringUtils.removeDuplicates(dataEntries);
//			if (StringUtils.compareListOfArrays(userServeyMap, uniqueDataEntries)) {
//
//				List<Object[]> dataEntry = surveyDataEntryRepo
//						.getDataEntryDetailsByServeyIds(String.valueOf(surveyMaster.getId()));
//
//				if (dataEntry.size() == dataEntries.size()) {
//					surveyMaster.setStatus(StatusCodes.COMPLETED.getCode());
//				}
//			}
//		}
//		LOGGER.info(surveyMasters);
//
//		return surveyMasters;
//	}

	@Override
	public List<SurveyMaster> getSurveyList(ViewReqById viewReqById) {
		LOGGER.info("Inside DashboardServiceImpl, getSurveyList");
		List<SurveyMaster> surveyMasters = new ArrayList<>();
		if (viewReqById.getId() == null) {
			if (viewReqById.getYearcode() == null || viewReqById.getYearcode().toLowerCase().equals("all")) {
				viewReqById.setYearcode("");
			}
			surveyMasters = dashboardRepository.findAllSurvey(viewReqById.getYearcode());
		} else {
			if (viewReqById.getId().equals(1)) {
				surveyMasters = dashboardRepository.findByCreatedBy(viewReqById.getId());
			} else {

				List<Integer> surveyList = dashboardRepository.findAllSurveyIdByLoginId(viewReqById.getId());
				surveyMasters = dashboardRepository.findAllSurveyById(surveyList);
			}

		}
		// Set status
		for (SurveyMaster surveyMaster : surveyMasters) {

			if (!surveyMaster.getStatus().equals(StatusCodes.INACTIVE.getCode())) {
				surveyMaster.setStatus(StatusCodes.EXPIRED.getCode());
				String status = surveyNotificationRepository.getStatusBySurveyId(String.valueOf(surveyMaster.getId()));
				Timestamp timestamp = (Timestamp) surveyMaster.getSurveyEndDate();
				LocalDateTime surveyEndDate = timestamp.toLocalDateTime();
				if ((status.equalsIgnoreCase("T") || status.equalsIgnoreCase("D"))
						&& !surveyEndDate.isBefore(LocalDateTime.now())) {
					surveyMaster.setStatus(StatusCodes.ACTIVE.getCode());
				}
				if (status.equalsIgnoreCase("A")) {
					surveyMaster.setStatus(StatusCodes.CREATED.getCode());
				}
				if (surveyEndDate.isBefore(LocalDateTime.now())) {
					List<int[]> userIds = surveyUserMappingRepo.getIdBySurveyIds(String.valueOf(surveyMaster.getId()));
					int isExpired = 0;
					for (int[] userId : userIds) {
						List<SurveyDataEntry> dataEntries = surveyDataEntryRepo.getDataEntryDetails(userId[0]);
						if (!dataEntries.isEmpty()) {
							if (dataEntries.get(0).getStatus().equalsIgnoreCase("A")) {
								isExpired++;
							}
						} else {
							surveyMaster.setStatus(StatusCodes.EXPIRED.getCode());
							break;
						}
					}

					if (isExpired != userIds.size()) {
						surveyMaster.setStatus(StatusCodes.EXPIRED.getCode());
					}
				}
			}
			List<int[]> userServeyMap = surveyUserMappingRepo.getUserServeyMap(String.valueOf(surveyMaster.getId()));
			List<Integer> resultList = new ArrayList<>();

			for (int[] array : userServeyMap) {
				resultList.add(array[1]);
			}

			List<int[]> dataEntries = surveyDataEntryRepo.getDataEntryDetailsByUserIds(resultList);
			List<int[]> uniqueDataEntries = StringUtils.removeDuplicates(dataEntries);
			if (StringUtils.compareListOfArrays(userServeyMap, uniqueDataEntries)) {

				List<Object[]> dataEntry = surveyDataEntryRepo
						.getDataEntryDetailsByServeyIds(String.valueOf(surveyMaster.getId()));

				if (!userServeyMap.isEmpty() && dataEntry.size() == dataEntries.size()
						&& dataEntry.stream().allMatch(entry -> entry[1].equals("A"))) {
					surveyMaster.setStatus(StatusCodes.COMPLETED.getCode());
				}
			}
		}
		
		System.out.println("surveyMasters========="+surveyMasters);
		return surveyMasters;
	}

	@Override
	public DashboardResponse getDashboardCount(List<SurveyMaster> surveyMasters, int id) {
		LOGGER.info("Inside DashboardServiceImpl, getDashboardCount");
		DashboardResponse dashboardResponse = new DashboardResponse();
		for (SurveyMaster sm : surveyMasters) {
			if (sm.getStatus().equals(StatusCodes.ACTIVE.getCode())) {
				dashboardResponse.setActiveSurvey(dashboardResponse.getActiveSurvey() + 1);
			} else if (sm.getStatus().equals(StatusCodes.COMPLETED.getCode())) {
				dashboardResponse.setCompleteSurvey(dashboardResponse.getCompleteSurvey() + 1);
			}

			String status = surveyNotificationRepository.getStatusBySurveyId(String.valueOf(sm.getId()));
			List<int[]> userServeyMap = surveyUserMappingRepo.getUserServeyMap(String.valueOf(sm.getId()));

			if (status.equalsIgnoreCase("T") && sm.getStatus().equals(StatusCodes.ACTIVE.getCode())) {
				dashboardResponse.setPendingForNotifiedToTPD(dashboardResponse.getPendingForNotifiedToTPD() + 1);
			} else if (status.equalsIgnoreCase("D") && (sm.getStatus().equals(StatusCodes.ACTIVE.getCode())
					|| sm.getStatus().equals(StatusCodes.COMPLETED.getCode()))) {
				List<Integer> resultList = new ArrayList<>();
				for (int[] array : userServeyMap) {
					resultList.add(array[1]);
				}
				List<int[]> dataEntries = surveyDataEntryRepo.getDataEntryDetailsByUserIds(resultList);
				List<int[]> uniqueDataEntries = StringUtils.removeDuplicates(dataEntries);
//				Boolean flag = StringUtils.compareListOfArrays(userServeyMap, uniqueDataEntries);
//				Boolean flag = false;
//				for (int i = 0; i < userServeyMap.size(); i++) {
//					flag = uniqueDataEntries.contains(userServeyMap.get(i));

				Boolean flag = containsElements(uniqueDataEntries, userServeyMap);
				if (flag) {

//					Map<String, Integer> map = new HashMap<String, Integer>();
//					map.put("C", 1);
//					map.put("R", 2);
//					map.put("A", 3);
//					map.put("S", 4);

					List<SurveyDataEntry> dataEntry = new ArrayList<SurveyDataEntry>();
					if (id == 1) {
						dataEntry = surveyDataEntryRepo.getDataEntryByServeyIds(String.valueOf(sm.getId()));
					} else {
						dataEntry = surveyDataEntryRepo.getDataEntryByServeyIdAndUserId(String.valueOf(sm.getId()), id);
					}
					boolean allStatusAreC = dataEntry.stream().allMatch(entry -> entry.getStatus().equals("C"));
					boolean allStatusAreR = dataEntry.stream().allMatch(entry -> entry.getStatus().equals("R"));
					boolean allStatusAreA = dataEntry.stream().allMatch(entry -> entry.getStatus().equals("A"));
					boolean hasStatusS = dataEntry.stream().anyMatch(entry -> entry.getStatus().equals("S"));
					boolean hasAnyC = dataEntry.stream().anyMatch(entry -> entry.getStatus().equals("C"));
					if (allStatusAreC) {
						dashboardResponse.setCompletedDataEntry(dashboardResponse.getCompletedDataEntry() + 1);
					} else if (!allStatusAreC && hasStatusS) {
						dashboardResponse.setPendingForDataEntry(dashboardResponse.getPendingForDataEntry() + 1);
					} else if (allStatusAreR) {
						dashboardResponse.setCompletedReview(dashboardResponse.getCompletedReview() + 1);
						dashboardResponse.setPendingForApprove(dashboardResponse.getPendingForApprove() + 1);
					} else if (allStatusAreA) {
						dashboardResponse.setCompletedApprove(dashboardResponse.getCompletedApprove() + 1);
					} else if (!allStatusAreA && hasAnyC) {
						dashboardResponse.setCompletedDataEntry(dashboardResponse.getCompletedDataEntry() + 1);
					} else {
						dashboardResponse.setPendingForDataEntry(dashboardResponse.getCompletedDataEntry() + 1);
					}
					List<SurveyMapQuestion> surveyMapQuestion = surveyMapQuestionRepo.findBySurveyMasterId(sm.getId());

//					List<SurveyMapQuestion> surveyMapQuestion = surveyMapQuestionRepo.findBySurveyId(sm.getId());
					boolean isMandatory = surveyMapQuestion.stream()
							.allMatch(entry -> entry.getIsMandatory().equals(1));

					if (allStatusAreC && isMandatory) {

						dashboardResponse.setPendingForReview(dashboardResponse.getPendingForReview() + 1);
					} else if (allStatusAreC && !isMandatory) {
						dashboardResponse.setPendingForApprove(dashboardResponse.getPendingForApprove() + 1);
					}
				} else {

					dashboardResponse.setPendingForDataEntry(dashboardResponse.getPendingForDataEntry() + 1);
				}
//				}
			}

		}

		List<User> childUsers = userRepository.findByCreatedBy(id);

		dashboardResponse.setTotalNoOfChildUsers(dashboardResponse.getTotalNoOfChildUsers() + childUsers.size());
		dashboardResponse.setTotalSurvey(dashboardResponse.getActiveSurvey() + dashboardResponse.getCompleteSurvey());
		return dashboardResponse;
//		return null;
	}

	@Override
	public DashboardUserListResponse getSurveyDetails(ViewReqById viewReqById) {
		LOGGER.info("Inside DashboardServiceImpl, getSurveyDetails");
		DashboardUserListResponse dashboardUserListResponse = new DashboardUserListResponse();
		Optional<User> users = userRepository.findById(viewReqById.getId());
		List<InstanceMaster> instanceMaster = instanceService.childInstances(users.get().getInstanceId().getId());
		List<Integer> instanceIds = instanceMaster.stream().map(InstanceMaster::getId).collect(Collectors.toList());
		List<Object[]> userDetails = userRepository.getUserDetails(instanceIds);
		List<List<String>> rows = new ArrayList<>();
		for (Object[] user : userDetails) {
			List<String> row = new ArrayList<>();
			boolean containsXyz = false;
			int deoCount = 0;
			int nonDeoCount = 0;
			int index = 0;
			for (int i = 0; i < rows.size(); i++) {
				List<String> lst = rows.get(i);
				if (!lst.isEmpty() && lst.get(0).equals(user[0])) {
					containsXyz = true;
					index = i;
					break;
				}
			}
			if (user[1].equals("Data Entry")) {
				String temp = user[2].toString();
				deoCount = deoCount + Integer.valueOf(temp);
			} else {
				String temp = user[2].toString();
				nonDeoCount = nonDeoCount + Integer.valueOf(temp);
			}
			if (containsXyz) {
				row = rows.get(index);
				row.set(1, String.valueOf(Integer.valueOf(row.get(1)) + nonDeoCount));
				row.set(2, String.valueOf(Integer.valueOf(row.get(2)) + deoCount));
			} else {
				row.add(String.valueOf(user[0]));
				row.add(String.valueOf(nonDeoCount));
				row.add(String.valueOf(deoCount));
				rows.add(row);
			}
		}
		dashboardUserListResponse.setRow(rows);
		return dashboardUserListResponse;
	}

	@Override
	public DashboardLevelIdResponse getLevelId(UserLevelIdRequest userLevelIdRequest) {
		LOGGER.info("Inside DashboardServiceImpl, getLevelId");
		DashboardLevelIdResponse dashboardLevelIdResponse = new DashboardLevelIdResponse();
//		List<Integer> levelIdList = new ArrayList<Integer>();

		int surveyLevelId = dashboardRepository.findSurveyLevelId(userLevelIdRequest.getSurveyId());

		int userLevelId = dashboardRepository.findUserLevelId(userLevelIdRequest.getUserId());
		if (StringUtils.isValidObj(userLevelId) && userLevelId != 0) {
			List<Integer> levelIdList = new ArrayList<Integer>();
			List<Integer> parentLevelId = new ArrayList<Integer>();
			List<Object[]> levels = new ArrayList<Object[]>();
			List<LevelIdNameModel> modelList = new ArrayList<LevelIdNameModel>();
			parentLevelId.add(userLevelId);
			if (userLevelId == surveyLevelId) {
				levels = dashboardRepository.findLevelNameByLevelId(parentLevelId);
				for (Object[] o : levels) {
					LevelIdNameModel model = new LevelIdNameModel();
					model.setLevelId(Integer.valueOf(o[0].toString()));
					model.setLevelName((String) o[1]);
					modelList.add(model);
					;
				}
				dashboardLevelIdResponse.setLevelList(modelList);

			} else {
				for (int i = 0; i < 36; i++) {
					if (userLevelId == surveyLevelId) {
						levelIdList.add(userLevelId);
						break;
					} else {

						parentLevelId = parentLevelId.stream().filter(a -> a <= surveyLevelId)
								.collect(Collectors.toList());
						if (parentLevelId.size() < 1) {
							break;
						}
						for (int j = 0; j < parentLevelId.size(); j++) {
							levelIdList.add(parentLevelId.get(j));
						}
						List<Integer> userChildLevelId = new ArrayList<Integer>();
						userChildLevelId = dashboardRepository.findUserChildLevelId(parentLevelId);
						parentLevelId = userChildLevelId;
						Collections.sort(parentLevelId);
					}
				}
			}
			levels = dashboardRepository.findLevelNameByLevelId(levelIdList);
			for (Object[] o : levels) {
				LevelIdNameModel model = new LevelIdNameModel();
				model.setLevelId(Integer.valueOf(o[0].toString()));
				model.setLevelName((String) o[1]);
				modelList.add(model);
			}
			dashboardLevelIdResponse.setLevelList(modelList);
			return dashboardLevelIdResponse;
		} else {
			// TODO
			System.out.println("error message");
			return dashboardLevelIdResponse;
		}

	}

	public String moveWebsiteData(Integer year_code) {

		String tableCopying = dashboardRepository.moveWebsiteTables(year_code);
		String result = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			Pattern pattern = Pattern.compile("\"success\" : (.*?), \"message\"");

			Matcher matcher = pattern.matcher(tableCopying);
			boolean success = false;
			if (matcher.find()) {
				// Extract the matched substring
				String extractedText = matcher.group(1);
				if (extractedText.equalsIgnoreCase("true")) {
					success = true;
				}
			}

			if (success) {

				try {

					try (Connection connection = DriverManager.getConnection(
							"jdbc:postgresql://107.155.65.84:5432/pgiw_db", WEBSITE_DATABASE_USER,
							WEBSITE_DATABASE_PASSWORD)) {

						CallableStatement callableStatement = connection
								.prepareCall("call get_refresh_website_tables_data()");

						boolean flag = callableStatement.execute();

						if (flag) {
							result = "Table data copied successfully.";
						}
//						result = callableStatement.getString(8);
						connection.close();
					} catch (SQLException e) {
						LOGGER.info(e.getMessage());
						return result = "Table data could not be copied. Exception: " + e.getMessage();
//						throw new SomethingWentWrongException(e.getMessage());
					}

					if (!StringUtils.isEmpty(result)) {
						return result;
					} else {
						return "Could Not Execute Procedure";
					}
				} catch (Exception e) {
					LOGGER.info(e.getMessage());
					return result = "Table data could not be copied. Exception: " + e.getMessage();
//					throw new SomethingWentWrongException(e.getMessage());
				}

			} else {
				return "Could not create tables on website servers";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return (e.getMessage());
		}
	}

	public static boolean containsElements(List<int[]> dataEntries, List<int[]> uniqueDataEntries) {
		for (int[] uniqueEntry : uniqueDataEntries) {
			boolean found = false;
			for (int[] dataEntry : dataEntries) {
				if (Arrays.equals(dataEntry, uniqueEntry)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

}
