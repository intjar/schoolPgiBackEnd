package com.np.schoolpgi.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.InstanceMasterRepository;
import com.np.schoolpgi.dao.LevelMasterRepository;
import com.np.schoolpgi.dao.LoginRepository;
import com.np.schoolpgi.dao.RoleRepository;
import com.np.schoolpgi.dao.SurveyNotificationRepository;
import com.np.schoolpgi.dao.SurveyUserMappingRepo;
import com.np.schoolpgi.dao.UserRepository;
import com.np.schoolpgi.dto.request.DeleteUpdateUserRequest;
import com.np.schoolpgi.dto.request.UserCreateRequest;
import com.np.schoolpgi.dto.request.ViewListRequest;
import com.np.schoolpgi.dto.response.ResponseWithPagination;
import com.np.schoolpgi.dto.response.UserDetailResponse;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.InstanceMaster;
import com.np.schoolpgi.model.LevelMaster;
import com.np.schoolpgi.model.Login;
import com.np.schoolpgi.model.Roles;
import com.np.schoolpgi.model.SurveyUserMapping;
import com.np.schoolpgi.model.User;
import com.np.schoolpgi.service.UserService;
import com.np.schoolpgi.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

	final static Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	LoginRepository loginRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	LevelMasterRepository levelMasterRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	InstanceMasterRepository instanceMasterRepository;

	@Autowired
	private SurveyUserMappingRepo surveyUserMappingRepo;

	@Autowired
	private EmailSenderServiceImpl emailSenderServiceImpl;

	@Autowired
	private SurveyNotificationRepository surveyNotificationRepository;

	@Transactional
	@Override
	public Integer createUser(UserCreateRequest user) {

		try {
			Login login = loginRepository.findByUsername(user.getUsername());
			if (login == null) {
				User userDetailSearch = userRepository.findByEmailIgnoreCase(user.getEmail());
				if (userDetailSearch == null) {
					Optional<Roles> role = roleRepository.findById(user.getRoleId());
					if (!role.isPresent()) {
						return 3;
					}
					Optional<LevelMaster> level = levelMasterRepository.findById(user.getLevelId());
					if (!level.isPresent()) {
						return 4;
					}
					Optional<InstanceMaster> instance = instanceMasterRepository.findById(user.getInstanceId());
					if (!instance.isPresent()) {
						return 5;
					}

					User userDetail = new User();
					userDetail.setCreatedAt(new Date());
					userDetail.setEmail(user.getEmail());
					userDetail.setCreatedBy(user.getLoggedInUserId());
					userDetail.setRoleId(role.get());
					userDetail.setName(user.getName());
					userDetail.setPhoneNo(user.getMobileNo());
					userDetail.setLevelMaster(level.get());
					userDetail.setStatus(user.getStatus());
					userDetail.setInstanceId(instance.get());
					Login logInUser = new Login();
					logInUser.setCreatedAt(new Date());
					logInUser.setUsername(user.getUsername());
					//String randompassword = getAlphaNumericString(8);
					String randompassword = "User@123";
					String encryptPassword = passwordEncoder.encode(randompassword);
					logInUser.setPassword(encryptPassword);

//					Integer sendEmail = emailSenderServiceImpl.sendPassword(user.getEmail(), randompassword,
//							user.getName(), user.getUsername(), role.get().getName(), level.get().getLevelName());
//
//					if (sendEmail != 1 || sendEmail == null) {
//						return 6;
//					}

					User savedUser = userRepository.save(userDetail);
					logInUser.setUid(savedUser.getUserId());
					logInUser.setCreatedBy(user.getLoggedInUserId());
					logInUser.setStatus(user.getStatus());

					loginRepository.save(logInUser);

					SurveyUserMapping surveyUserMapping = new SurveyUserMapping();
					surveyUserMapping.setUserId(Long.parseLong(savedUser.getUserId().toString()));
					surveyUserMapping.setCreatedAt(new Date());
					surveyUserMapping.setCreatedBy((Long.parseLong(savedUser.getCreatedBy().toString())));
					surveyUserMapping.setInstanceId(savedUser.getInstanceId().getId());
					surveyUserMapping.setLevelId(savedUser.getLevelMaster().getId());
					surveyUserMappingRepo.save(surveyUserMapping);

					return 0;
				} else
					return 2;
			} else {
				return 1;
			}
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public ResponseWithPagination usersList(ViewListRequest viewListRequest, int pageNo, int pageSize, String sortDir,
			String sortBy, String searchKey) {

		try {
			ResponseWithPagination responseWithPagination = new ResponseWithPagination();

//			Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
//					: Sort.by(sortBy).ascending();
			
			Sort sort;
			if (sortByExistsInUserMaster(sortBy)) {
				sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
						: Sort.by(sortBy).ascending();
			}else {
				sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by("u_id").descending()
						: Sort.by("u_id").ascending();
			}
			
			Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
			List<User> users = null;
			Page<User> pageableData = null;
			if (viewListRequest.isChildInstance() == true && viewListRequest.getLoggedInUserId() > 0) {
				pageableData = userRepository.findByCreatedBy(viewListRequest.getLoggedInUserId(), pageable, searchKey);

				users = pageableData.getContent();
			} else {
				pageableData = userRepository.findAllUser(pageable, searchKey);
				users = pageableData.getContent();
			}
			if (users == null) {
				return null;
			}

			else {
				List<UserDetailResponse> userDetails = new ArrayList<>();
				for (User u : users) {
					UserDetailResponse userDetailsResponse = new UserDetailResponse();
					userDetailsResponse.setRole(u.getRoleId().getName());
					userDetailsResponse.setRoleId(u.getRoleId().getId());
					userDetailsResponse.setLevel(u.getLevelMaster().getLevelName());
					userDetailsResponse.setLevelId(u.getLevelMaster().getId());
					userDetailsResponse.setInstance(u.getInstanceId().getInstanceName());
					userDetailsResponse.setInstanceId(u.getInstanceId().getId());
					userDetailsResponse.setName(u.getName());
					userDetailsResponse.setEmailId(u.getEmail());
					userDetailsResponse.setUId(u.getUserId());
					userDetailsResponse.setNumber(u.getPhoneNo());
					userDetailsResponse.setStatus(u.getStatus());
					Login login = loginRepository.findByUid(u.getUserId());
					if (login != null) {
						userDetailsResponse.setUserName(login.getUsername());
					} 

					List<SurveyUserMapping> existUser = surveyUserMappingRepo.findByUserId(u.getUserId().longValue());
					/*
					for (SurveyUserMapping sur : existUser) {
						if (StringUtils.isEmpty(sur.getApproverSurveyIds()) 
								&& StringUtils.isEmpty(sur.getReviewerSurveyIds())
								&& StringUtils.isEmpty(sur.getViewerSurveyIds())
								&& StringUtils.isEmpty(sur.getDeoSurveyIds()))

							userDetailsResponse.setIsEditable(true);
						else {

							List<String> finalList = new ArrayList<>();
							if (StringUtils.isValidObj(sur.getApproverSurveyIds())) {
								finalList.addAll(Arrays.asList(sur.getApproverSurveyIds().split(",")));
							}
							if (StringUtils.isValidObj(sur.getReviewerSurveyIds())) {
								finalList.addAll(Arrays.asList(sur.getReviewerSurveyIds().split(",")));
							}
							if (StringUtils.isValidObj(sur.getViewerSurveyIds())) {
								finalList.addAll(Arrays.asList(sur.getViewerSurveyIds().split(",")));
							}
							if (StringUtils.isValidObj(sur.getDeoSurveyIds())) {
								finalList.addAll(Arrays.asList(sur.getDeoSurveyIds().split(",")));
							}
							
							List<Integer> surveyNotofications = surveyNotificationRepository
									.findNotificationByStatus("A");
							
							System.out.println("User Id "+u.getUserId()+"----"+finalList +"    ---------   "+ surveyNotofications);

							Boolean flag = true;
							for (String s : finalList) {
								System.out.println("++++++   "+s);
								if(!surveyNotofications.contains(Integer.parseInt(s)))
								{
									flag = false;
									break;
								}
//								if (surveyNotofications.contains(Integer.parseInt(s))) {
//									flag = true;
//								}

							}
							if (flag == true) {
								userDetailsResponse.setIsEditable(true);
							} else {
								userDetailsResponse.setIsEditable(false);
							}
						}
					}
					*/
					
					for (SurveyUserMapping sur : existUser) {
						if (StringUtils.isEmpty(sur.getApproverSurveyIds()) 
								&& StringUtils.isEmpty(sur.getReviewerSurveyIds())
								&& StringUtils.isEmpty(sur.getViewerSurveyIds())
								&& StringUtils.isEmpty(sur.getDeoSurveyIds()))

							userDetailsResponse.setIsEditable(true);
						else {
							userDetailsResponse.setIsEditable(false);
						}
					
					userDetails.add(userDetailsResponse);
					}
				}

				if (userDetails.isEmpty())
					return null;
//				Collections.sort(userDetails, new UserRegViewComparator());

				responseWithPagination.setHttpStatus(HttpStatus.OK);
				responseWithPagination.setLast(pageableData.isLast());
				responseWithPagination.setMessage(APPServiceCode.APP001.getStatusDesc());
				responseWithPagination.setMessageCode(APPServiceCode.APP001.getStatusCode());
				responseWithPagination.setPageNo(pageableData.getNumber());
				responseWithPagination.setPageSize(pageableData.getSize());
				responseWithPagination.setResult(userDetails);
				responseWithPagination.setSuccess(true);
				responseWithPagination.setTotalElements(pageableData.getTotalElements());
				responseWithPagination.setTotalPages(pageableData.getTotalPages());
				return responseWithPagination;
			}
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}
	
	private boolean sortByExistsInUserMaster(String sortBy) {
		try {
			Field[] fields = User.class.getDeclaredFields();
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
	public Integer deleteUser(Integer uId, Integer updatedBy) {
		// TODO Auto-generated method stub
		Optional<User> user = userRepository.findById(uId);
		if (user.isPresent()) {
			User removedUser = user.get();
			removedUser.setStatus(false);
			removedUser.setUpdatedBy(updatedBy);
			removedUser.setUpdatedAt(new Date());
			userRepository.save(removedUser);
			Login login = loginRepository.findByUid(removedUser.getUserId());
			login.setStatus(false);
			login.setUpdatedBy(updatedBy);
			login.setUpdatedAt(new Date());
			loginRepository.save(login);
			return 1;

		} else {
			return 0;
		}

	}

	@Transactional
	@Override
	public Integer updateUser(DeleteUpdateUserRequest update) {

		if (update.getId() == update.getLoggedInUserId()
				&& (update.getIsprofile() == null || update.getIsprofile() == false)) {
			return 2;
		}

		Optional<User> user = userRepository.findById(update.getId());
		if (user.isPresent()) {
			User emailUser = userRepository.findByEmailIgnoreCaseNotId(update.getEmail(), update.getId());
			if (emailUser != null) {
				return 4;
			}
			Optional<Roles> role = roleRepository.findById(update.getRoleId());
			if (!role.isPresent()) {
				return 6;
			}
			Optional<LevelMaster> level = levelMasterRepository.findById(update.getLevelId());
			if (!level.isPresent()) {
				return 7;
			}
			Optional<InstanceMaster> instance = instanceMasterRepository.findById(update.getInstanceId());
			if (!instance.isPresent()) {
				return 8;
			}
			Login loginUsername = loginRepository.findByUsernameAndUidNot(update.getUsername(), update.getId());
			if (loginUsername != null) {
				return 5;
			}

			Login login = loginRepository.findByUid(update.getId());
			if (login != null) {
				login.setUsername(update.getUsername());
				login.setUpdatedAt(new Date());
				login.setUpdatedBy(update.getLoggedInUserId());
				login.setStatus(update.getStatus());
				User updatedUser = user.get();
				updatedUser.setStatus(update.getStatus());
				updatedUser.setEmail(update.getEmail());
				updatedUser.setInstanceId(instance.get());
				updatedUser.setLevelMaster(level.get());
				updatedUser.setName(update.getName());
				updatedUser.setPhoneNo(update.getMobileNo());
				updatedUser.setRoleId(role.get());
				updatedUser.setUpdatedAt(new Date());
				updatedUser.setUpdatedBy(update.getLoggedInUserId());
				userRepository.save(updatedUser);
				loginRepository.save(login);
				return 1;
			}
			return 3;

		} else {
			return 0;
		}
	}

	private String getAlphaNumericString(int n) {

		// choose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {

			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());

			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}

}
