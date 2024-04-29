package com.np.schoolpgi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.np.schoolpgi.aop.CheckSignature;
import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.constants.AppConstants;
import com.np.schoolpgi.constants.EventListEnum;
import com.np.schoolpgi.dao.SurveyIdRequest;
import com.np.schoolpgi.dto.request.AddInstanceRequest;
import com.np.schoolpgi.dto.request.AssignSurveyReqDto;
import com.np.schoolpgi.dto.request.CreateSurveyRequest;
import com.np.schoolpgi.dto.request.DataSourceRequest;
import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.DeleteSchoolInstanceReqDto;
import com.np.schoolpgi.dto.request.DeleteSurveyRequest;
import com.np.schoolpgi.dto.request.DeleteUpdateInstanceRequest;
import com.np.schoolpgi.dto.request.DeleteUpdateUserRequest;
import com.np.schoolpgi.dto.request.DomainMasterRequest;
import com.np.schoolpgi.dto.request.DownloadExcelRequest;
import com.np.schoolpgi.dto.request.EncryptDataRequest;
import com.np.schoolpgi.dto.request.EventLogRequest;
import com.np.schoolpgi.dto.request.ForgetPasswordRequest;
import com.np.schoolpgi.dto.request.InstancesWithSchoolRequest;
import com.np.schoolpgi.dto.request.LevelMasterRequest;
import com.np.schoolpgi.dto.request.ManageSettingRequest;
import com.np.schoolpgi.dto.request.NotificationFlagChangeRequest;
import com.np.schoolpgi.dto.request.QuestDropDownForSurveyMapQuest;
import com.np.schoolpgi.dto.request.QuestionMasterRequest;
import com.np.schoolpgi.dto.request.ResetPasswordRequest;
import com.np.schoolpgi.dto.request.RoleLinkMappingUpdateRequest;
import com.np.schoolpgi.dto.request.RoleReqDto;
import com.np.schoolpgi.dto.request.STDResponse;
import com.np.schoolpgi.dto.request.SchoolMasterRequest;
import com.np.schoolpgi.dto.request.SubDomainRequest;
import com.np.schoolpgi.dto.request.SubQuestionRequest;
import com.np.schoolpgi.dto.request.SurveyCloneRequest;
import com.np.schoolpgi.dto.request.SurveyDataEntryListRequest;
import com.np.schoolpgi.dto.request.UpdatePasswordRequest;
import com.np.schoolpgi.dto.request.UserCreateRequest;
import com.np.schoolpgi.dto.request.ValidateOtpRequest;
import com.np.schoolpgi.dto.request.ViewByListIds;
import com.np.schoolpgi.dto.request.ViewLevelReq;
import com.np.schoolpgi.dto.request.ViewListRequest;
import com.np.schoolpgi.dto.request.ViewReqById;
import com.np.schoolpgi.dto.request.ViewSubQuestTypeReq;
import com.np.schoolpgi.dto.request.ViewSurMapSubQuestReq;
import com.np.schoolpgi.dto.response.AssignedSurvey;
import com.np.schoolpgi.dto.response.BlockInstancResponse;
import com.np.schoolpgi.dto.response.DownloadSurveyExcelResponse;
import com.np.schoolpgi.dto.response.ErrorResponse;
import com.np.schoolpgi.dto.response.InstancesResponse;
import com.np.schoolpgi.dto.response.MessageResponse;
import com.np.schoolpgi.dto.response.QuestionSubQuestionResponse;
import com.np.schoolpgi.dto.response.ResponseData;
import com.np.schoolpgi.dto.response.ResponseValue;
import com.np.schoolpgi.dto.response.ResponseWithPagination;
import com.np.schoolpgi.dto.response.SurvMapSubQuestResponse;
import com.np.schoolpgi.dto.response.SurveyErrorReponse;
import com.np.schoolpgi.dto.response.UserDetailsResponse;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.exception.ValidateRequestDtoUtil;
import com.np.schoolpgi.model.DataSource;
import com.np.schoolpgi.model.DomainMaster;
import com.np.schoolpgi.model.EventLog;
import com.np.schoolpgi.model.InstanceMaster;
import com.np.schoolpgi.model.LevelMaster;
import com.np.schoolpgi.model.NotificationData;
import com.np.schoolpgi.model.QuestionMaster;
import com.np.schoolpgi.model.QuestionTypeMaster;
import com.np.schoolpgi.model.Roles;
import com.np.schoolpgi.model.SubDomain;
import com.np.schoolpgi.model.SubQuestion;
import com.np.schoolpgi.model.SurveyMapQuestion;
import com.np.schoolpgi.model.YearMaster;
import com.np.schoolpgi.service.DataSourceService;
import com.np.schoolpgi.service.DocumentUploadDownloadService;
import com.np.schoolpgi.service.DomainService;
import com.np.schoolpgi.service.EmailSenderService;
import com.np.schoolpgi.service.EventLogService;
import com.np.schoolpgi.service.InstanceMasterService;
import com.np.schoolpgi.service.LevelMasterService;
import com.np.schoolpgi.service.LinkListService;
import com.np.schoolpgi.service.ManageSettingService;
import com.np.schoolpgi.service.NotificationDataService;
import com.np.schoolpgi.service.QuestionMasterService;
import com.np.schoolpgi.service.QuestionTypeMasterService;
import com.np.schoolpgi.service.ResetPasswordService;
import com.np.schoolpgi.service.RoleService;
import com.np.schoolpgi.service.SchoolService;
import com.np.schoolpgi.service.SubDomainService;
import com.np.schoolpgi.service.SubQuestionService;
import com.np.schoolpgi.service.SurveyMapQuestionService;
import com.np.schoolpgi.service.SurveyMasterService;
import com.np.schoolpgi.service.SurveyUserMappingService;
import com.np.schoolpgi.service.UserService;
import com.np.schoolpgi.service.ValidateOtpService;
import com.np.schoolpgi.service.YearMasterService;
import com.np.schoolpgi.util.AESEncryption;
import com.np.schoolpgi.util.CustomJsonParser;
import com.np.schoolpgi.util.StringUtils;

@RequestMapping("/np/app")
@RestController
@CrossOrigin(origins = "*")
public class HomeController {

	final static Logger LOGGER = LogManager.getLogger(HomeController.class);

	@Autowired
	private AESEncryption aesEncryption;
	@Autowired
	EmailSenderService emailSenderService;
	@Autowired
	ValidateOtpService validateOtpService;
	@Autowired
	ResetPasswordService resetPasswordService;
	@Autowired
	LinkListService linkListService;

	@Value("${thirdpartydatasource.excels}")
	private String excelFolder;
	
	@Autowired
	ManageSettingService manageSettingService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private CustomJsonParser customJsonParser;

	@Autowired
	private LevelMasterService levelMasterService;

	@Autowired
	private SchoolService schoolService;

	@Autowired
	private DomainService domainService;

	@Autowired
	private SubDomainService subDomainService;

	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private InstanceMasterService instanceMasterService;

	@Autowired
	private UserService userService;

	@Autowired
	private EventLogService eventLogService;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private NotificationDataService notificationDataService;

	@Autowired
	private QuestionTypeMasterService questionTypeMasterService;

	@Autowired
	private QuestionMasterService questionMasterService;

	@Autowired
	private ValidateRequestDtoUtil validateRequestDtoUtil;

	@Autowired
	private SubQuestionService subQuestionService;

	@Autowired
	private SurveyUserMappingService surveyUserMappingService;

	@Autowired
	private YearMasterService yearMasterService;

	@Autowired
	private SurveyMasterService surveyMasterService;

	@Autowired
	private SurveyMapQuestionService surveyMapQuestionService;

	@Autowired
	DocumentUploadDownloadService documentUploadDownloadService;

	Set<ConstraintViolation<Object>> violations = null;

	// -----------SEND EMAIL ,VALIDATE OTP AND RESET PASSWORD-----

	// @CheckSignature
	@PostMapping("/sendOTP")
	public ResponseEntity<?> forgotPassword(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			System.out.println(new Date());
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			ForgetPasswordRequest forgetPasswordRequest = mapper.readValue(decrypt, ForgetPasswordRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(forgetPasswordRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			Integer status = emailSenderService.sendOTP(forgetPasswordRequest.getEmail());
			System.out.println("*****************status=="+status);
			
			if (status == 0) {

				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP040.getStatusCode(), APPServiceCode.APP040.getStatusDesc()));

				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			} else if (status == 1) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP041.getStatusCode(), APPServiceCode.APP041.getStatusDesc()));
				System.out.println(new Date());
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			} else if (status == 3) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP017.getStatusCode(), APPServiceCode.APP017.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

			} else if (status == 4) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP173.getStatusCode(), APPServiceCode.APP173.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

			}

			else {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP012.getStatusCode(), APPServiceCode.APP012.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}

		} catch (ConstraintViolationException e) {
			LOGGER.info("#createrole ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/validateotp")
	public ResponseEntity<?> validateOtp(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			ValidateOtpRequest validateOtpRequest = mapper.readValue(decrypt, ValidateOtpRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(validateOtpRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			Integer status = validateOtpService.validateOtp(validateOtpRequest.getEmail(), validateOtpRequest.getOtp());
			if (status == 0) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP040.getStatusCode(), APPServiceCode.APP040.getStatusDesc()));

				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			} else if (status == 1) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP011.getStatusCode(), APPServiceCode.APP011.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

			} else if (status == 2) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP010.getStatusCode(), APPServiceCode.APP010.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

			} else if (status == 3) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP021.getStatusCode(), APPServiceCode.APP021.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}

			else {

				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP012.getStatusCode(), APPServiceCode.APP012.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
		} catch (ConstraintViolationException e) {
			LOGGER.info("#validateOtp ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/resetpassword")
	public ResponseEntity<?> resetPassword(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
			resetPasswordRequest = mapper.readValue(decrypt, ResetPasswordRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(resetPasswordRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
//			if (resetPasswordRequest.getEmail() == null) {
//				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
//						APPServiceCode.APP017.getStatusCode(), APPServiceCode.APP017.getStatusDesc()));
//				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
//			}
			Integer status = resetPasswordService.resetpassword(resetPasswordRequest.getEmail(),
					resetPasswordRequest.getResetPassword(), resetPasswordRequest.getConfirmPassword());
			if (status == 0) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP017.getStatusCode(), APPServiceCode.APP017.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

			} else if (status == 1) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP009.getStatusCode(), APPServiceCode.APP009.getStatusDesc()));

				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

			} else if (status == 2) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP030.getStatusCode(), APPServiceCode.APP030.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

			} else if (status == 3) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP015.getStatusCode(), APPServiceCode.APP015.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

			}

			else {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP503.getStatusCode(), APPServiceCode.APP503.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}

		} catch (ConstraintViolationException e) {
			LOGGER.info("#resetPassword ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/updatepassword")
	public ResponseEntity<?> updatepassword(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
			updatePasswordRequest = mapper.readValue(decrypt, UpdatePasswordRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(updatePasswordRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
//			if (resetPasswordRequest.getEmail() == null) {
//				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
//						APPServiceCode.APP017.getStatusCode(), APPServiceCode.APP017.getStatusDesc()));
//				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
//			}
			String jsonStr = "";
			Integer status = resetPasswordService.updateChangePassword(updatePasswordRequest);
			if (status == 0) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP015.getStatusCode(), APPServiceCode.APP015.getStatusDesc()));

			} else if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP009.getStatusCode(), APPServiceCode.APP009.getStatusDesc()));

			} else if (status == 2) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP030.getStatusCode(), APPServiceCode.APP030.getStatusDesc()));

			} else if (status == 3) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP061.getStatusCode(), APPServiceCode.APP061.getStatusDesc()));

			}

			else {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP503.getStatusCode(), APPServiceCode.APP503.getStatusDesc()));
			}
			eventLogService.saveEventLogs(EventListEnum.UPDATE_PASSWORD.getEventId(),
					EventListEnum.UPDATE_PASSWORD.getEventName(), updatePasswordRequest.getLoggedInUserId(), decrypt,
					jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#updatepassword ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}
	// --------------------ROLE MASTER-----------

	// @CheckSignature
	@PostMapping("/createrole")
	public ResponseEntity<?> createRole(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			RoleReqDto roleRequest = mapper.readValue(decrypt, RoleReqDto.class);

			violations = validateRequestDtoUtil.validateReqDto(roleRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = roleService.createRole(roleRequest);
			if (status == 2) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP200.getStatusCode(), APPServiceCode.APP200.getStatusDesc()));

			} else if (status == 3) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP201.getStatusCode(), APPServiceCode.APP201.getStatusDesc()));

			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			eventLogService.saveEventLogs(EventListEnum.CREATE_ROLE.getEventId(),
					EventListEnum.CREATE_ROLE.getEventName(), roleRequest.getLoggedInUserId(), decrypt, jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (ConstraintViolationException e) {
			LOGGER.error("#createrole ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/viewRole")
	public ResponseEntity<?> viewRole(@RequestHeader Map<String, String> requestHeader, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "sortByColumn", defaultValue = AppConstants.QUERY_SORT_BY_COLUMN_NAME_ID, required = false) String sortByColumn,
			@RequestParam(value = "sortDirection", defaultValue = AppConstants.sortDirectionDESC, required = false) String sortDirection) {
		try {
			List<Roles> roles = roleService.viewRole(sortByColumn, sortDirection);
			if (roles == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), roles));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());

			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/updateRole")
	public ResponseEntity<?> updateRole(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());

			RoleReqDto roleRequest = mapper.readValue(decryptedData, RoleReqDto.class);

			// Validating RequestDto, it can give ConstraintViolationException
			violations = validateRequestDtoUtil.validateReqDto(roleRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = roleService.updateRole(roleRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else if (status == 2) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP200.getStatusCode(), APPServiceCode.APP200.getStatusDesc()));
			} else if (status == 3) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP201.getStatusCode(), APPServiceCode.APP201.getStatusDesc()));
			} else if (status == 4) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
			} else if (status == 6) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP177.getStatusCode(), APPServiceCode.APP177.getStatusDesc()));
			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP511.getStatusCode(), APPServiceCode.APP511.getStatusDesc()));
			eventLogService.saveEventLogs(EventListEnum.UPDATE_ROLE.getEventId(),
					EventListEnum.UPDATE_ROLE.getEventName(), roleRequest.getLoggedInUserId(), decryptedData, jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (ConstraintViolationException e) {
			LOGGER.info("#updateRole ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		}

		catch (Exception e) {

			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());

			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/deleteRole")
	public ResponseEntity<?> deleteRole(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			DeleteRequestDto roleRequest = mapper.readValue(decryptedData, DeleteRequestDto.class);
			violations = validateRequestDtoUtil.validateReqDto(roleRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = roleService.deleteRole(roleRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));

			} else if (status == 2) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
			} else if (status == 4) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP177.getStatusCode(), APPServiceCode.APP177.getStatusDesc()));
			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP511.getStatusCode(), APPServiceCode.APP511.getStatusDesc()));
			eventLogService.saveEventLogs(EventListEnum.DELETE_ROLE.getEventId(),
					EventListEnum.DELETE_ROLE.getEventName(), roleRequest.getLoggedInUserId(), decryptedData, jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteRole ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + e.getMessage());

			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// -----------------MANAGE SETTINGS-------------------

	// @CheckSignature
	@PostMapping("/managesetting")
	public ResponseEntity<?> managemenus(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			ManageSettingRequest manageSettingRequest = new ManageSettingRequest();

			manageSettingRequest = mapper.readValue(decrypt, ManageSettingRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(manageSettingRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			return manageSettingService.manageStting(manageSettingRequest.getRoleid());
		} catch (ConstraintViolationException e) {
			LOGGER.info("#managesetting ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());

		}

	}

	// @CheckSignature
	@PostMapping("/savesetting")
	public ResponseEntity<?> saveMenus(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {
			String encryptData = aesEncryption.decrypt(encryptedData.getData());
			ObjectMapper mapper = new ObjectMapper();

			RoleLinkMappingUpdateRequest manageSettingRequest = new RoleLinkMappingUpdateRequest();
			manageSettingRequest = mapper.readValue(encryptData, RoleLinkMappingUpdateRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(manageSettingRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = manageSettingService.manageSttingUpdateLinks(manageSettingRequest);
			eventLogService.saveEventLogs(EventListEnum.SAVE_SETTING.getEventId(),
					EventListEnum.SAVE_SETTING.getEventName(), manageSettingRequest.getLoggedInUserId(), encryptData,
					jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.error("#savesetting ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());

		}

	}

	// @CheckSignature
	@PostMapping("/getmenulist")
	public ResponseEntity<?> getMenuList(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {
			String encryptData = aesEncryption.decrypt(encryptedData.getData());
			ObjectMapper mapper = new ObjectMapper();

			ViewReqById viewReqById = new ViewReqById();
			viewReqById = mapper.readValue(encryptData, ViewReqById.class);
			violations = validateRequestDtoUtil.validateReqDto(viewReqById);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			return manageSettingService.getRoleLinks(viewReqById);

		} catch (ConstraintViolationException e) {
			LOGGER.error("#getmenulist ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());

		}

	}

	// ------------------LEVEL MASTER-----------

	// @CheckSignature
	@PostMapping("/createLevel")
	public ResponseEntity<?> createdLevel(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			LevelMasterRequest levelRequest = mapper.readValue(decryptedData, LevelMasterRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(levelRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = levelMasterService.createdLevel(levelRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));

			} else if (status == 2) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));

			} else if (status == 3) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP200.getStatusCode(), APPServiceCode.APP200.getStatusDesc()));

			} else if (status == 4) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP201.getStatusCode(), APPServiceCode.APP201.getStatusDesc()));

			} else if (status == 6) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP179.getStatusCode(), APPServiceCode.APP179.getStatusDesc()));

			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));

			if (StringUtils.isValidObj(levelRequest.getId())) {
				if (levelRequest.getId() > 0)
					eventLogService.saveEventLogs(EventListEnum.UPDATE_LEVEL.getEventId(),
							EventListEnum.UPDATE_LEVEL.getEventName(), levelRequest.getLoggedInUserId(), decryptedData,
							jsonStr);
			}

			else
				eventLogService.saveEventLogs(EventListEnum.CREATE_LEVEL.getEventId(),
						EventListEnum.CREATE_LEVEL.getEventName(), levelRequest.getLoggedInUserId(), decryptedData,
						jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		}

		catch (ConstraintViolationException e) {
			LOGGER.error("#createLevel ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		}

		catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// View Level Master Using Stored Procedure
	// @CheckSignature
	@PostMapping("/viewLevel")
	public ResponseEntity<?> viewLevelMaster(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			ViewLevelReq levelRequest = mapper.readValue(decryptedData, ViewLevelReq.class);
			violations = validateRequestDtoUtil.validateReqDto(levelRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			String levels = levelMasterService.viewLevelMaster(levelRequest.getIsDropDown());

			if (levels == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}

			String substring = levels.substring(1, levels.length() - 1);
			Object parseData = customJsonParser.customJsonParserMethod().parse(substring);
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parseData);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (ConstraintViolationException e) {
			LOGGER.error("#createLevel ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/deleteLevel")
	public ResponseEntity<?> deleteLevel(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			DeleteRequestDto levelRequest = mapper.readValue(decryptedData, DeleteRequestDto.class);
			violations = validateRequestDtoUtil.validateReqDto(levelRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = levelMasterService.deleteLevel(levelRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else if (status == 2) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));

			} else if (status == 4) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP179.getStatusCode(), APPServiceCode.APP179.getStatusDesc()));
			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP511.getStatusCode(), APPServiceCode.APP511.getStatusDesc()));
			eventLogService.saveEventLogs(EventListEnum.DELETE_LEVEL.getEventId(),
					EventListEnum.DELETE_LEVEL.getEventName(), levelRequest.getLoggedInUserId(), decryptedData,
					jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.error("#deleteLevel ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/childLevel")
	public ResponseEntity<?> childLevel(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			ViewReqById levelRequest = mapper.readValue(decryptedData, ViewReqById.class);
			violations = validateRequestDtoUtil.validateReqDto(levelRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			List<LevelMaster> levels = levelMasterService.childLevels(levelRequest.getId());
			if (levels == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}

			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), levels));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.error("#deleteLevel ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// ------------------SCHOOL MASTER-----------

	// @CheckSignature
	@PostMapping("/viewBlockLevelInstance")
	public ResponseEntity<?> viewBlockLevelInstance(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			ViewByListIds vieByListIds = mapper.readValue(decrypt, ViewByListIds.class);

			violations = validateRequestDtoUtil.validateReqDto(vieByListIds);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			BlockInstancResponse viewBlockLevelInstance = schoolService.viewBlockLevelInstance(vieByListIds);
			List<InstanceMaster> result = viewBlockLevelInstance.getResult();
			Object appServiceCode = viewBlockLevelInstance.getAppServiceCode();
			Boolean success = viewBlockLevelInstance.getSuccess();
			if (success) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), result));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			if (success == false && appServiceCode == APPServiceCode.APP996) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP996.getStatusCode(), APPServiceCode.APP996.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
					APPServiceCode.APP507.getStatusCode(), APPServiceCode.APP507.getStatusDesc()));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (ConstraintViolationException e) {
			LOGGER.error(e.getMessage());
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/createSchool")
	public ResponseEntity<?> createSchool(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			SchoolMasterRequest schoolRequest = mapper.readValue(decryptedData, SchoolMasterRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(schoolRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = schoolService.createSchool(schoolRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else if (status == 2) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));

			} else if (status == 3) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP508.getStatusCode(), APPServiceCode.APP508.getStatusDesc()));

			} else if (status == 5) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP509.getStatusCode(), APPServiceCode.APP509.getStatusDesc()));

			} else if (status == 6) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP510.getStatusCode(), APPServiceCode.APP510.getStatusDesc()));
			} else if (status == 7) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP175.getStatusCode(), APPServiceCode.APP175.getStatusDesc()));
			} else if (status == 8) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP178.getStatusCode(), APPServiceCode.APP178.getStatusDesc()));

			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));

			if (StringUtils.isValidObj(schoolRequest.getId())) {
				if (schoolRequest.getId() > 0) {
					eventLogService.saveEventLogs(EventListEnum.UPDATE_SCHOOL.getEventId(),
							EventListEnum.UPDATE_SCHOOL.getEventName(), schoolRequest.getLoggedInUserId(),
							decryptedData, jsonStr);
				}
			} else {
				eventLogService.saveEventLogs(EventListEnum.CREATE_SCHOOL.getEventId(),
						EventListEnum.CREATE_SCHOOL.getEventName(), schoolRequest.getLoggedInUserId(), decryptedData,
						jsonStr);
			}

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (ConstraintViolationException e) {
			LOGGER.error("#createSchool ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/viewSchool")
	public ResponseEntity<?> viewSchool(@RequestHeader Map<String, String> requestHeader, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey) {
		try {
			ResponseWithPagination schools = schoolService.viewSchool(pageNo, pageSize, sortDir, sortBy, searchKey);
			if (schools == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), schools));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/deleteSchool")
	public ResponseEntity<?> deleteSchool(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			DeleteSchoolInstanceReqDto schoolRequest = mapper.readValue(decryptedData,
					DeleteSchoolInstanceReqDto.class);
			violations = validateRequestDtoUtil.validateReqDto(schoolRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = schoolService.deleteSchool(schoolRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else if (status == 2) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));

			} else if (status == 4) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP510.getStatusCode(), APPServiceCode.APP510.getStatusDesc()));
			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP511.getStatusCode(), APPServiceCode.APP511.getStatusDesc()));
			eventLogService.saveEventLogs(EventListEnum.DELETE_SCHOOL.getEventId(),
					EventListEnum.DELETE_SCHOOL.getEventName(), schoolRequest.getLoggedInUserId(), decryptedData,
					jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteSchool ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		}

		catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// ------------------DOMAIN MASTER-------------------

	// @CheckSignature
	@PostMapping("/createDomain")
	public ResponseEntity<?> createDomain(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			DomainMasterRequest domainRequest = mapper.readValue(decryptedData, DomainMasterRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(domainRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = domainService.createDomain(domainRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else if (status == 2) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));

			} else if (status == 3) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP200.getStatusCode(), APPServiceCode.APP200.getStatusDesc()));

			} else if (status == 4) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP201.getStatusCode(), APPServiceCode.APP201.getStatusDesc()));

			} else if (status == 6) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP540.getStatusCode(), APPServiceCode.APP540.getStatusDesc()));
			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));

			if (StringUtils.isValidObj(domainRequest.getId())) {
				if (domainRequest.getId() > 0) {
					eventLogService.saveEventLogs(EventListEnum.UPDATE_DOMAIN.getEventId(),
							EventListEnum.UPDATE_DOMAIN.getEventName(), domainRequest.getLoggedInUserId(),
							decryptedData, jsonStr);
				}
			}
			eventLogService.saveEventLogs(EventListEnum.CREATE_DOMAIN.getEventId(),
					EventListEnum.CREATE_DOMAIN.getEventName(), domainRequest.getLoggedInUserId(), decryptedData,
					jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		}

		catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/viewDomain")
	public ResponseEntity<?> viewDomain(@RequestHeader Map<String, String> requestHeader, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "sortByColumn", defaultValue = AppConstants.QUERY_SORT_BY_COLUMN_NAME_ID, required = false) String sortByColumn,
			@RequestParam(value = "sortDirection", defaultValue = AppConstants.sortDirectionDESC, required = false) String sortDirection) {
		try {
			List<DomainMaster> domains = domainService.viewDomain(sortByColumn, sortDirection);
			if (domains == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), domains));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/deleteDomain")
	public ResponseEntity<?> deleteDomain(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			DeleteRequestDto domainRequest = mapper.readValue(decryptedData, DeleteRequestDto.class);
			violations = validateRequestDtoUtil.validateReqDto(domainRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = domainService.deleteDomain(domainRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else if (status == 2) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));

			} else if (status == 4) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP540.getStatusCode(), APPServiceCode.APP540.getStatusDesc()));
			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP511.getStatusCode(), APPServiceCode.APP511.getStatusDesc()));
			eventLogService.saveEventLogs(EventListEnum.DELETE_DOMAIN.getEventId(),
					EventListEnum.DELETE_DOMAIN.getEventName(), domainRequest.getLoggedInUserId(), decryptedData,
					jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		}

		catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// ---------------INSTANCE MASTER------------

	// @CheckSignature
	@PostMapping("/createInstance")
	public ResponseEntity<?> createInstance(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			AddInstanceRequest addInstanceRequest = mapper.readValue(decryptedData, AddInstanceRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(addInstanceRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			Integer status = instanceMasterService.addInstance(addInstanceRequest);
			String jsonStr = "";
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));

			} else if (status == 2) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP201.getStatusCode(), APPServiceCode.APP201.getStatusDesc()));

			} else if (status == 3) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP200.getStatusCode(), APPServiceCode.APP200.getStatusDesc()));
			}

			else if (status == 0) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
			} else {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP505.getStatusCode(), APPServiceCode.APP505.getStatusDesc()));

			}
			eventLogService.saveEventLogs(EventListEnum.CREATE_INSTANCE.getEventId(),
					EventListEnum.CREATE_INSTANCE.getEventName(), addInstanceRequest.getLoggedInUserId(), decryptedData,
					jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception ex) {

			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + ex.getMessage());
			throw new SomethingWentWrongException(ex.getMessage());

		}

	}

	// @CheckSignature
	@PostMapping("/viewinstances")
	public ResponseEntity<?> viewinstances(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			InstancesWithSchoolRequest instancesWithSchoolRequest = mapper.readValue(decryptedData,
					InstancesWithSchoolRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(instancesWithSchoolRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			ResponseWithPagination instances = instanceMasterService.getInstances(instancesWithSchoolRequest, pageNo,
					pageSize, sortDir, sortBy, searchKey);
			if (instances == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new InstancesResponse(true, APPServiceCode.APP001.getStatusDesc(), instances));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (ConstraintViolationException e) {
			LOGGER.error("#viewinstances ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception ex) {
			LOGGER.error(ex);
			throw new SomethingWentWrongException(ex.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/deleteInstance")
	public ResponseEntity<?> deleteInstance(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			DeleteUpdateInstanceRequest instanceRequest = mapper.readValue(decryptedData,
					DeleteUpdateInstanceRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(instanceRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			Boolean status = instanceMasterService.deleteInstance(instanceRequest.getId(),
					instanceRequest.getLoggedInUserId());
			String jsonStr = "";
			if (!status) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP109.getStatusCode(), APPServiceCode.APP109.getStatusDesc()));
			}
			jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
					APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			eventLogService.saveEventLogs(EventListEnum.DELETE_INSTANCE.getEventId(),
					EventListEnum.DELETE_INSTANCE.getEventName(), instanceRequest.getLoggedInUserId(), decryptedData,
					jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		}

		catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + e.getMessage());
			e.printStackTrace();
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/updateInstance")
	public ResponseEntity<?> updateInstance(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			DeleteUpdateInstanceRequest instanceRequest = mapper.readValue(decryptedData,
					DeleteUpdateInstanceRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(instanceRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = instanceMasterService.updateInstance(instanceRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));

			} else if (status == 2) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP201.getStatusCode(), APPServiceCode.APP201.getStatusDesc()));

			} else if (status == 3) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP200.getStatusCode(), APPServiceCode.APP200.getStatusDesc()));
			}

			else if (status == 0) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
			} else {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP505.getStatusCode(), APPServiceCode.APP505.getStatusDesc()));
			}
			eventLogService.saveEventLogs(EventListEnum.UPDATE_INSTANCE.getEventId(),
					EventListEnum.UPDATE_INSTANCE.getEventName(), instanceRequest.getLoggedInUserId(), decryptedData,
					jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		}

		catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + e.getMessage());
			e.printStackTrace();
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// ----------------------SUB DOMAIN-------------

	// @CheckSignature
	@PostMapping("/createSubDomain")
	public ResponseEntity<?> createSubDomain(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			SubDomainRequest subDomainRequest = mapper.readValue(decryptedData, SubDomainRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(subDomainRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = subDomainService.createSubDomain(subDomainRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else if (status == 2) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));

			} else if (status == 3) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP118.getStatusCode(), APPServiceCode.APP118.getStatusDesc()));

			} else if (status == 4) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP119.getStatusCode(), APPServiceCode.APP119.getStatusDesc()));

			} else if (status == 6) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP541.getStatusCode(), APPServiceCode.APP541.getStatusDesc()));
			} else {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP201.getStatusCode(), APPServiceCode.APP201.getStatusDesc()));
			}

			if (StringUtils.isValidObj(subDomainRequest.getId())) {
				if (subDomainRequest.getId() > 0) {
					eventLogService.saveEventLogs(EventListEnum.UPDATE_SUB_DOMAIN.getEventId(),
							EventListEnum.UPDATE_SUB_DOMAIN.getEventName(), subDomainRequest.getLoggedInUserId(),
							decryptedData, jsonStr);
				}
			}
			eventLogService.saveEventLogs(EventListEnum.CREATE_SUB_DOMAIN.getEventId(),
					EventListEnum.CREATE_SUB_DOMAIN.getEventName(), subDomainRequest.getLoggedInUserId(), decryptedData,
					jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#SubDomain ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/viewSubDomain")
	public ResponseEntity<?> viewSubDomain(@RequestHeader Map<String, String> requestHeader, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "sortByColumn", defaultValue = AppConstants.QUERY_SORT_BY_COLUMN_NAME_ID, required = false) String sortByColumn,
			@RequestParam(value = "sortDirection", defaultValue = AppConstants.sortDirectionDESC, required = false) String sortDirection) {
		try {
			List<SubDomain> subDomains = subDomainService.viewSubDomain(sortByColumn, sortDirection);
			if (subDomains == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), subDomains));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/deleteSubDomain")
	public ResponseEntity<?> deleteSubDomain(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			DeleteRequestDto subDomainRequest = mapper.readValue(decryptedData, DeleteRequestDto.class);

			violations = validateRequestDtoUtil.validateReqDto(subDomainRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = subDomainService.deleteSubDomain(subDomainRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else if (status == 2) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));

			} else if (status == 4) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP541.getStatusCode(), APPServiceCode.APP541.getStatusDesc()));
			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP511.getStatusCode(), APPServiceCode.APP511.getStatusDesc()));
			eventLogService.saveEventLogs(EventListEnum.DELETE_SUB_DOMAIN.getEventId(),
					EventListEnum.DELETE_SUB_DOMAIN.getEventName(), subDomainRequest.getLoggedInUserId(), decryptedData,
					jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteSubDomain ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// ------------------DATA SOURCE-----------------
	// Create And Update APIs are same.

	// @CheckSignature
	@PostMapping("/createDataSource")
	public ResponseEntity<?> createDataSource(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			DataSourceRequest dataRequest = mapper.readValue(decryptedData, DataSourceRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(dataRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			Integer status = dataSourceService.createDataSource(dataRequest);
			String jsonStr = "";
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else if (status == 2) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));

			} else if (status == 3) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP200.getStatusCode(), APPServiceCode.APP200.getStatusDesc()));

			} else if (status == 4) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP201.getStatusCode(), APPServiceCode.APP201.getStatusDesc()));

			} else if (status == 5) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP512.getStatusCode(), APPServiceCode.APP512.getStatusDesc()));

			} else {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP513.getStatusCode(), APPServiceCode.APP513.getStatusDesc()));

			}
			if (StringUtils.isValidObj(dataRequest.getId())) {
				if (dataRequest.getId() > 0)
					eventLogService.saveEventLogs(EventListEnum.UPDATE_DATA_SOURCE.getEventId(),
							EventListEnum.UPDATE_DATA_SOURCE.getEventName(), dataRequest.getLoggedInUserId(),
							decryptedData, jsonStr);
			} else
				eventLogService.saveEventLogs(EventListEnum.CREATE_DATA_SOURCE.getEventId(),
						EventListEnum.CREATE_DATA_SOURCE.getEventName(), dataRequest.getLoggedInUserId(), decryptedData,
						jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#DataSource ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/viewDataSources")
	public ResponseEntity<?> viewDataSource(@RequestHeader Map<String, String> requestHeader,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "sortByColumn", defaultValue = AppConstants.QUERY_SORT_BY_COLUMN_NAME_ID, required = false) String sortByColumn,
			@RequestParam(value = "sortDirection", defaultValue = AppConstants.sortDirectionDESC, required = false) String sortDirection) {
		try {
			List<DataSource> dataSources = dataSourceService.viewDataSource(sortByColumn, sortDirection);
			if (dataSources == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), dataSources));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/deleteDataSource")
	public ResponseEntity<?> deleteDataSources(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			DeleteRequestDto dataRequest = mapper.readValue(decryptedData, DeleteRequestDto.class);

			violations = validateRequestDtoUtil.validateReqDto(dataRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			Integer status = dataSourceService.deleteDataSource(dataRequest);
			String jsonStr = "";
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else if (status == 2) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP507.getStatusCode(), APPServiceCode.APP507.getStatusDesc()));

			eventLogService.saveEventLogs(EventListEnum.DELETE_DATA_SOURCE.getEventId(),
					EventListEnum.DELETE_DATA_SOURCE.getEventName(), dataRequest.getLoggedInUserId(), decryptedData,
					jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#DataSource ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// ------------------QUESTION MASTER------------------

	// @CheckSignature
	@PostMapping("/viewQuestionType")
	public ResponseEntity<?> viewQuestionTypeMaster(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "sortByColumn", defaultValue = AppConstants.QUERY_SORT_BY_COLUMN_NAME_ID, required = false) String sortByColumn,
			@RequestParam(value = "sortDirection", defaultValue = AppConstants.sortDirectionDESC, required = false) String sortDirection) {
		try {
			// String str
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			ViewSubQuestTypeReq dataRequest = mapper.readValue(decryptedData, ViewSubQuestTypeReq.class);
			List<QuestionTypeMaster> queTypeList = questionTypeMasterService
					.viewQuestionTypeMaster(dataRequest.getType(), sortByColumn, sortDirection);

			if (queTypeList == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), queTypeList));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/createQuestion")
	public ResponseEntity<?> createQuestionMaster(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());

			QuestionMasterRequest questRequest = mapper.readValue(decryptedData, QuestionMasterRequest.class);

			// Validating RequestDto, it can give ConstraintViolationException
			violations = validateRequestDtoUtil.validateReqDto(questRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			Integer status = questionMasterService.createQuestionMaster(questRequest);
			String jsonStr = "";
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP201.getStatusCode(), APPServiceCode.APP201.getStatusDesc()));
			} else if (status == 2) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP200.getStatusCode(), APPServiceCode.APP200.getStatusDesc()));

			} else if (status == 3) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else if (status == 5) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP516.getStatusCode(), APPServiceCode.APP516.getStatusDesc()));

			} else {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
			}
			if (StringUtils.isValidObj(questRequest.getId())) {
				if (questRequest.getId() > 0)
					eventLogService.saveEventLogs(EventListEnum.UPDATE_QUESTION.getEventId(),
							EventListEnum.UPDATE_QUESTION.getEventName(), questRequest.getLoggedInUserId(),
							decryptedData, jsonStr);
			} else
				eventLogService.saveEventLogs(EventListEnum.CREATE_QUESTION.getEventId(),
						EventListEnum.CREATE_QUESTION.getEventName(), questRequest.getLoggedInUserId(), decryptedData,
						jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#QuestionMaster ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/viewQuestions")
	public ResponseEntity<?> viewQuestionMaster(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());

			QuestDropDownForSurveyMapQuest questRequest = mapper.readValue(decryptedData,
					QuestDropDownForSurveyMapQuest.class);

			// Validating RequestDto, it can give ConstraintViolationException
			violations = validateRequestDtoUtil.validateReqDto(questRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			List<QuestionMaster> questList = questionMasterService.viewQuestionMaster(questRequest);
			if (questList == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), questList));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@PostMapping("/viewQuestionssubquestion")
	public ResponseEntity<?> viewQuestionSubQuestionMaster(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());

			ViewReqById questRequest = mapper.readValue(decryptedData, ViewReqById.class);

			// Validating RequestDto, it can give ConstraintViolationException
			violations = validateRequestDtoUtil.validateReqDto(questRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			List<QuestionSubQuestionResponse> questList = questionMasterService
					.viewQuestionSubquestionMaster(questRequest);
			if (questList == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), questList));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/deleteQuestion")
	public ResponseEntity<?> deleteQuestion(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			DeleteRequestDto dataRequest = mapper.readValue(decryptedData, DeleteRequestDto.class);
			// Validating RequestDto, it can give ConstraintViolationException
			violations = validateRequestDtoUtil.validateReqDto(dataRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = questionMasterService.deleteQuestionMaster(dataRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP507.getStatusCode(), APPServiceCode.APP507.getStatusDesc()));

			eventLogService.saveEventLogs(EventListEnum.DELETE_QUESTION.getEventId(),
					EventListEnum.DELETE_QUESTION.getEventName(), dataRequest.getLoggedInUserId(), decryptedData,
					jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// -----------SUB QUESTION MASTER---------

	// @CheckSignature
	@PostMapping("/createSubQuestion")
	public ResponseEntity<?> createSubQuestion(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());

			SubQuestionRequest subQuestRequest = mapper.readValue(decryptedData, SubQuestionRequest.class);

			// Validating RequestDto, it can give ConstraintViolationException
			violations = validateRequestDtoUtil.validateReqDto(subQuestRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = subQuestionService.createSubQuestion(subQuestRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else if (status == 2) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));

			} else if (status == 3) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP118.getStatusCode(), APPServiceCode.APP118.getStatusDesc()));

			} else if (status == 4) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP119.getStatusCode(), APPServiceCode.APP119.getStatusDesc()));
			} else if (status == 6) {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP516.getStatusCode(), APPServiceCode.APP516.getStatusDesc()));
			} else {

				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP201.getStatusCode(), APPServiceCode.APP201.getStatusDesc()));
			}
			if (StringUtils.isValidObj(subQuestRequest.getId())) {

				if (subQuestRequest.getId() > 0)
					eventLogService.saveEventLogs(EventListEnum.UPDATE_SUB_QUESTION.getEventId(),
							EventListEnum.UPDATE_SUB_QUESTION.getEventName(), subQuestRequest.getLoggedInUserId(),
							decryptedData, jsonStr);
			}

			else
				eventLogService.saveEventLogs(EventListEnum.CREATE_SUB_QUESTION.getEventId(),
						EventListEnum.CREATE_SUB_QUESTION.getEventName(), subQuestRequest.getLoggedInUserId(),
						decryptedData, jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (ConstraintViolationException e) {
			LOGGER.info("#SubQuestionMaster ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/viewSubQuestions")
	public ResponseEntity<?> viewSubQuestion(@RequestHeader Map<String, String> requestHeader,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "sortByColumn", defaultValue = AppConstants.QUERY_SORT_BY_COLUMN_NAME_ID, required = false) String sortByColumn,
			@RequestParam(value = "sortDirection", defaultValue = AppConstants.sortDirectionDESC, required = false) String sortDirection) {
		try {
			List<SubQuestion> questList = subQuestionService.viewSubQuestionMaster(sortByColumn, sortDirection);
			if (questList == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), questList));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/deleteSubQuestion")
	public ResponseEntity<?> deleteSubQuestion(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			DeleteRequestDto dataRequest = mapper.readValue(decryptedData, DeleteRequestDto.class);
			// Validating RequestDto, it can give ConstraintViolationException
			violations = validateRequestDtoUtil.validateReqDto(dataRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = subQuestionService.deleteSubQuestion(dataRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP507.getStatusCode(), APPServiceCode.APP507.getStatusDesc()));

			eventLogService.saveEventLogs(EventListEnum.DELETE_SUB_QUESTION.getEventId(),
					EventListEnum.DELETE_SUB_QUESTION.getEventName(), dataRequest.getLoggedInUserId(), decryptedData,
					jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteSubQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// ----------------USER REGISTRATION------
	// @CheckSignature
	@PostMapping("/createUser")
	public ResponseEntity<?> createUser(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			UserCreateRequest userCreateReaquest = mapper.readValue(decryptedData, UserCreateRequest.class);

			violations = validateRequestDtoUtil.validateReqDto(userCreateReaquest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			String jsonStr = "";
			Integer status = userService.createUser(userCreateReaquest);

			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP013.getStatusCode(), APPServiceCode.APP013.getStatusDesc()));
			} else if (status == 2) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP108.getStatusCode(), APPServiceCode.APP108.getStatusDesc()));
			} else if (status == 0) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP014.getStatusCode(), APPServiceCode.APP014.getStatusDesc()));

			} else if (status == 4) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP096.getStatusCode(), APPServiceCode.APP096.getStatusDesc()));

			} else if (status == 3) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP109.getStatusCode(), APPServiceCode.APP109.getStatusDesc()));

			} else if (status == 5) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP510.getStatusCode(), APPServiceCode.APP510.getStatusDesc()));

			} else if (status == 6) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP017.getStatusCode(), APPServiceCode.APP017.getStatusDesc()));

			}

			else {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP503.getStatusCode(), APPServiceCode.APP503.getStatusDesc()));
			}

			eventLogService.saveEventLogs(EventListEnum.CREATE_USER.getEventId(),
					EventListEnum.CREATE_USER.getEventName(), userCreateReaquest.getLoggedInUserId(), decryptedData,
					jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		}

		catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/viewusers")
	public ResponseEntity<?> viewUsers(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			ViewListRequest viewListRequest = new ViewListRequest();
			viewListRequest = mapper.readValue(decryptedData, ViewListRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(viewListRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			if (viewListRequest.isChildInstance() == true
					&& (viewListRequest.getLoggedInUserId() < 0 || viewListRequest.getLoggedInUserId() == null)) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP174.getStatusCode(), APPServiceCode.APP174.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			ResponseWithPagination users = userService.usersList(viewListRequest, pageNo, pageSize, sortDir, sortBy,
					searchKey);
			if (users == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(users);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception ex) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + ex.getMessage());
			ex.printStackTrace();
			throw new SomethingWentWrongException(ex.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/deleteUser")
	public ResponseEntity<?> deleteUser(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());

			DeleteUpdateUserRequest userRequest = mapper.readValue(decryptedData, DeleteUpdateUserRequest.class);

			violations = validateRequestDtoUtil.validateReqDto(userRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			if (userRequest.getId() == userRequest.getLoggedInUserId()) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP503.getStatusCode(), APPServiceCode.APP503.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			Integer status = userService.deleteUser(userRequest.getId(), userRequest.getLoggedInUserId());
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));

			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP503.getStatusCode(), APPServiceCode.APP503.getStatusDesc()));

			eventLogService.saveEventLogs(EventListEnum.DELETE_USER.getEventId(),
					EventListEnum.DELETE_USER.getEventName(), userRequest.getLoggedInUserId(), decryptedData, jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + e.getMessage());
			e.printStackTrace();
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/updateUser")
	public ResponseEntity<?> updateUser(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());

			DeleteUpdateUserRequest userRequest = mapper.readValue(decryptedData, DeleteUpdateUserRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(userRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = userService.updateUser(userRequest);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));

			} else if (status == 2) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP503.getStatusCode(), APPServiceCode.APP503.getStatusDesc()));
			} else if (status == 3) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP503.getStatusCode(), APPServiceCode.APP503.getStatusDesc()));
			} else if (status == 4) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP108.getStatusCode(), APPServiceCode.APP108.getStatusDesc()));
			} else if (status == 5) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP121.getStatusCode(), APPServiceCode.APP121.getStatusDesc()));
			} else if (status == 6) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP096.getStatusCode(), APPServiceCode.APP096.getStatusDesc()));

			} else if (status == 7) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP109.getStatusCode(), APPServiceCode.APP109.getStatusDesc()));

			} else if (status == 8) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP510.getStatusCode(), APPServiceCode.APP510.getStatusDesc()));

			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP503.getStatusCode(), APPServiceCode.APP503.getStatusDesc()));

			eventLogService.saveEventLogs(EventListEnum.UPDATE_USER.getEventId(),
					EventListEnum.UPDATE_USER.getEventName(), userRequest.getLoggedInUserId(), decryptedData, jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + e.getMessage());
			e.printStackTrace();
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// ------------------Event Logs------------------

	// @CheckSignature
	@PostMapping("/vieweventlogs")
	public ResponseEntity<?> eventLogs(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			EventLogRequest userRequest = mapper.readValue(decryptedData, EventLogRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(userRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			List<EventLog> users = eventLogService.getEventLogs(userRequest.getLoggedInUserId());
			if (users == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new UserDetailsResponse(true, APPServiceCode.APP001.getStatusDesc(), users));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception ex) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + ex.getMessage());
			ex.printStackTrace();
			throw new SomethingWentWrongException(ex.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/viewNotifications")
	public ResponseEntity<?> viewnotifications(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			EventLogRequest userRequest = mapper.readValue(decryptedData, EventLogRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(userRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			List<NotificationData> users = notificationDataService.getNotifications(userRequest.getLoggedInUserId());
			if (users == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), users));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception ex) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + ex.getMessage());
			ex.printStackTrace();
			throw new SomethingWentWrongException(ex.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/markNotificationAsReadUnread")
	public ResponseEntity<?> notificationRead(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());

			NotificationFlagChangeRequest userRequest = mapper.readValue(decryptedData,
					NotificationFlagChangeRequest.class);

			violations = validateRequestDtoUtil.validateReqDto(userRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			Integer status = notificationDataService.updateNotificationToReadUnread(userRequest);
			if (status == 1) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
					APPServiceCode.APP503.getStatusCode(), APPServiceCode.APP503.getStatusDesc()));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + e.getMessage());
			e.printStackTrace();
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

//	@PostMapping("/markNotificationAsUnread")
//	public ResponseEntity<?> notificationUnread(@RequestBody EncryptDataRequest encryptedData) {
//
//		try {
//
//			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
//
//			DeleteRequestDto userRequest = mapper.readValue(decryptedData, DeleteRequestDto.class);
//
//			violations = validateRequestDtoUtil.validateReqDto(userRequest);
//			if (!violations.isEmpty()) {
//				LOGGER.info(violations);
//				throw new ConstraintViolationException(violations);
//			}
//
//			Integer status = notificationDataService.updateNotificationToUnRead(
//					Integer.valueOf(userRequest.getId().intValue()), userRequest.getLoggedInUserId());
//			if (status == 1) {
//				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
//						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
//				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
//
//			}
//			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
//					APPServiceCode.APP503.getStatusCode(), APPServiceCode.APP503.getStatusDesc()));
//			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
//
//		} catch (ConstraintViolationException e) {
//			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
//			throw new ConstraintViolationException(violations);
//		} catch (Exception e) {
//			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + e.getMessage());
//			e.printStackTrace();
//			throw new SomethingWentWrongException(e.getMessage());
//		}
//
//	}
	// --------User Survey Mapping-------------
	// Not Used
	@PostMapping("/surveyUserMapping")
	public ResponseEntity<?> surveyUserMapping(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			LOGGER.info(decryptedData);
			String surveyUserMapping = surveyUserMappingService.surveyUserMapping(decryptedData);
			String jsonStr = "";
			if (surveyUserMapping == null) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
			} else {
				String substring = surveyUserMapping.substring(1, surveyUserMapping.length() - 1);
				Object parseData = customJsonParser.customJsonParserMethod().parse(substring);
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parseData);
			}

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (Exception ex) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + ex.getMessage());
			ex.printStackTrace();
			throw new SomethingWentWrongException(ex.getMessage());
		}
	}

	// -----------------Year---------------

	// @CheckSignature
	@PostMapping("/yearsList")
	public ResponseEntity<?> yearsList(@RequestHeader Map<String, String> requestHeader, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			List<YearMaster> yearMasters = yearMasterService.getYearList();
			if (yearMasters == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), yearMasters));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception ex) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + ex.getMessage());
			ex.printStackTrace();
			throw new SomethingWentWrongException(ex.getMessage());
		}
	}

	// -----------------Survey Master-----------------

	// @CheckSignature
	@PostMapping("/createsurvey")
	public ResponseEntity<?> createSurvey(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			CreateSurveyRequest createSurveyRequest = mapper.readValue(decryptedData, CreateSurveyRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(createSurveyRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			Integer status = surveyMasterService.createSurvey(createSurveyRequest);
			String jsonStr = "";

			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP014.getStatusCode(), APPServiceCode.APP014.getStatusDesc()));
			} else if (status == 2) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP126.getStatusCode(), APPServiceCode.APP126.getStatusDesc()));
			} else if (status == 4) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP126.getStatusCode(), APPServiceCode.APP126.getStatusDesc()));
			} else {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP503.getStatusCode(), APPServiceCode.APP503.getStatusDesc()));
			}
			eventLogService.saveEventLogs(EventListEnum.CREATE_SURVEY.getEventId(),
					EventListEnum.CREATE_SURVEY.getEventName(), createSurveyRequest.getLoggedInUserId(), decryptedData,
					jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception ex) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + ex.getMessage());
			ex.printStackTrace();
			throw new SomethingWentWrongException(ex.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/updatesurvey")
	public ResponseEntity<?> updatesurvey(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			CreateSurveyRequest createSurveyRequest = mapper.readValue(decryptedData, CreateSurveyRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(createSurveyRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = surveyMasterService.updateSurvey(createSurveyRequest);

			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP014.getStatusCode(), APPServiceCode.APP014.getStatusDesc()));
			} else if (status == 2) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP126.getStatusCode(), APPServiceCode.APP126.getStatusDesc()));
			} else if (status == 4) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP126.getStatusCode(), APPServiceCode.APP126.getStatusDesc()));
			} else

			{
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP503.getStatusCode(), APPServiceCode.APP503.getStatusDesc()));
			}

			eventLogService.saveEventLogs(EventListEnum.UPDATE_SURVEY.getEventId(),
					EventListEnum.UPDATE_SURVEY.getEventName(), createSurveyRequest.getLoggedInUserId(), decryptedData,
					jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception ex) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + ex.getMessage());
			ex.printStackTrace();
			throw new SomethingWentWrongException(ex.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/surveylist")
	public ResponseEntity<?> surveylist(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			ViewReqById viewReqById = mapper.readValue(decryptedData, ViewReqById.class);
			ResponseWithPagination surveyMasters = surveyMasterService.surveyList(viewReqById, pageNo, pageSize,

					sortDir, sortBy, searchKey);
			if (surveyMasters == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), surveyMasters));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception ex) {
			LOGGER.error(ex);
			throw new SomethingWentWrongException(ex.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/deletesurvey")
	public ResponseEntity<?> deletesurvey(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());

			DeleteSurveyRequest deletesurvey = mapper.readValue(decryptedData, DeleteSurveyRequest.class);

			violations = validateRequestDtoUtil.validateReqDto(deletesurvey);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String jsonStr = "";
			Integer status = surveyMasterService.deleteSurvey(deletesurvey);
			if (status == 1) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
						APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));

			} else
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP503.getStatusCode(), APPServiceCode.APP503.getStatusDesc()));
			eventLogService.saveEventLogs(EventListEnum.DELETE_SURVEY.getEventId(),
					EventListEnum.DELETE_SURVEY.getEventName(), deletesurvey.getLoggedInUserId(), decryptedData,
					jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + e.getMessage());
			e.printStackTrace();
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/userSurveyList")
	public ResponseEntity<?> usersurveylist(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());

			ViewReqById usersurveyrequest = mapper.readValue(decryptedData, ViewReqById.class);

			violations = validateRequestDtoUtil.validateReqDto(usersurveyrequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			List<AssignedSurvey> assignedSurveys = surveyUserMappingService
					.assignedSurveyList(Long.parseLong(usersurveyrequest.getId().toString()));
			if (assignedSurveys == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), assignedSurveys));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception ex) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc() + " " + ex.getMessage());
			ex.printStackTrace();
			throw new SomethingWentWrongException(ex.getMessage());
		}
	}

	// ------SurveyMapQuestion------

	// @CheckSignature
	@PostMapping("/createSurveyMapQuest")
	public ResponseEntity<?> createSurveyMapQuestion(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			SurveyMapQuestion surveyMapQuestion = mapper.readValue(decrypt, SurveyMapQuestion.class);

			violations = validateRequestDtoUtil.validateReqDto(surveyMapQuestion);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			STDResponse stdResponse = surveyMapQuestionService.createSurveyMapQuest(surveyMapQuestion);

			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(stdResponse);

			eventLogService.saveEventLogs(EventListEnum.CREATE_SURVEY_MAP_QUEST.getEventId(),
					EventListEnum.CREATE_SURVEY_MAP_QUEST.getEventName(), surveyMapQuestion.getCreatedBy(), decrypt,
					jsonStr);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (ConstraintViolationException e) {
			LOGGER.info("#createSurveyMapQuest ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/viewSurveyMapQuest")
	public ResponseEntity<?> viewSurveyMapQuestion(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey) {

		try {

			System.out.println("#searchKey -" + searchKey);
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			ViewReqById surveyMapQuestionReq = mapper.readValue(decrypt, ViewReqById.class);

			violations = validateRequestDtoUtil.validateReqDto(surveyMapQuestionReq);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			ResponseWithPagination stdResponse = surveyMapQuestionService.viewSurveyMapQuestion(surveyMapQuestionReq,
					pageNo, pageSize, sortDir, sortBy, searchKey);
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(stdResponse);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.info("#viewSurveyMapQuest ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	// @CheckSignature
	@PostMapping("/surveySubQuestsByQuestId")
	public ResponseEntity<?> surveySubQuestsByQuestId(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			ViewSurMapSubQuestReq req = mapper.readValue(decrypt, ViewSurMapSubQuestReq.class);

			violations = validateRequestDtoUtil.validateReqDto(req);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			List<SurvMapSubQuestResponse> questList = surveyMapQuestionService.viewSurveyMapSubQuestByQuestId(req);
			if (questList.isEmpty()) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), questList));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/updateSurveyMapQuestion")
	public ResponseEntity<?> updateSurveyMapQuestion(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			SurveyMapQuestion surveyMapQuestionReq = mapper.readValue(decrypt, SurveyMapQuestion.class);

			violations = validateRequestDtoUtil.validateReqDto(surveyMapQuestionReq);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			STDResponse stdResponse = surveyMapQuestionService.updateSurveyMapQuestion(surveyMapQuestionReq);
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(stdResponse);
			eventLogService.saveEventLogs(EventListEnum.UPDATE_SURVEY_MAP_QUEST.getEventId(),
					EventListEnum.UPDATE_SURVEY_MAP_QUEST.getEventName(), surveyMapQuestionReq.getCreatedBy(), decrypt,
					jsonString);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.info("#updateSurveyMapQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/deleteSurveyMapQuestion")
	public ResponseEntity<?> deleteSurveyMapQuestion(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			DeleteRequestDto surveyMapQuestionReq = mapper.readValue(decrypt, DeleteRequestDto.class);

			violations = validateRequestDtoUtil.validateReqDto(surveyMapQuestionReq);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			STDResponse stdResponse = surveyMapQuestionService.deleteSurveyMapQuestion(surveyMapQuestionReq);
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(stdResponse);
			eventLogService.saveEventLogs(EventListEnum.DELETE_SURVEY_MAP_QUEST.getEventId(),
					EventListEnum.DELETE_SURVEY_MAP_QUEST.getEventName(), surveyMapQuestionReq.getLoggedInUserId(),
					decrypt, jsonString);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.info("#deleteSurveyMapQuestion ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// --------------View And Updated Assigned Survey Summary------------
	// Using Stored Procedure(SP)
	// @CheckSignature
	@PostMapping("/getUpdSurveySummary")
	public ResponseEntity<?> getUpdSurveySummary(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy") String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			AssignSurveyReqDto assignSurveyReqDto = mapper.readValue(decrypt, AssignSurveyReqDto.class);

			violations = validateRequestDtoUtil.validateReqDto(assignSurveyReqDto);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			String updSurveySummary = null;
			if (StringUtils.isValidObj(assignSurveyReqDto.getUpdatedData())) {
				updSurveySummary = surveyUserMappingService.getUpdSurveySummary(pageNo, pageSize, sortDir, sortBy,
						searchKey, assignSurveyReqDto.getLoginId(), assignSurveyReqDto.getSurveyId(),
						assignSurveyReqDto.getAction(), assignSurveyReqDto.getUpdatedData().toString());
			} else {
				updSurveySummary = surveyUserMappingService.getUpdSurveySummary(pageNo, pageSize, sortDir, sortBy,
						searchKey, assignSurveyReqDto.getLoginId(), assignSurveyReqDto.getSurveyId(),
						assignSurveyReqDto.getAction(), null);
			}

			Object parseData = customJsonParser.customJsonParserMethod().parse(updSurveySummary);
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parseData);
//			if (StringUtils.isValidObj(assignSurveyReqDto.getUpdatedData())) {
//				eventLogService.saveEventLogs(EventListEnum.GET_UPD_SURVEY_SUMMARY_ACTION_U.getEventId(),
//						EventListEnum.GET_UPD_SURVEY_SUMMARY_ACTION_U.getEventName(),
//						assignSurveyReqDto.getLoginId().intValue(), decrypt, jsonString);
//			} else {
//				eventLogService.saveEventLogs(EventListEnum.GET_UPD_SURVEY_SUMMARY_ACTION_G.getEventId(),
//						EventListEnum.GET_UPD_SURVEY_SUMMARY_ACTION_G.getEventName(),
//						assignSurveyReqDto.getLoginId().intValue(), decrypt, jsonString);
//			}
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.error("#getUpdSurveySummary ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// -------------View Survey Data Entry BySurId-----------
	// Using Stored Procedure(SP)
	// @CheckSignature
	@PostMapping("/surveyDataEntryById")
	public ResponseEntity<?> surveyDataEntryById(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			SurveyDataEntryListRequest req = mapper.readValue(decrypt, SurveyDataEntryListRequest.class);

			violations = validateRequestDtoUtil.validateReqDto(req);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			String surveyDataEntryById = surveyMasterService.surveyDataEntryById(req);

			Object parseData = customJsonParser.customJsonParserMethod().parse(surveyDataEntryById);
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parseData);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.error("#surveyDataEntryById ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// -------------View Survey Data List For DEO-----------
	// Using Stored Procedure(SP)
	// @CheckSignature
	@PostMapping("/surveyDeoList")
	public ResponseEntity<?> getSurveyListForDeo(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			SurveyDataEntryListRequest req = mapper.readValue(decrypt, SurveyDataEntryListRequest.class);

			violations = validateRequestDtoUtil.validateReqDto(req);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String surveyDataEntryById = surveyMasterService.getSurveyListForDeo(req, pageNo, pageSize, sortDir, sortBy,
					searchKey);

			Object parseData = customJsonParser.customJsonParserMethod().parse(surveyDataEntryById);
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parseData);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.error("#surveyDeoList ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// -------------Insert Survey Data Entry(Save Answer Data)----------
	// Using Stored Procedure(SP)
	// @CheckSignature
	@PostMapping("/insertSurveyDataEntry")
	public ResponseEntity<?> insertSurveyDataEntry(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			String surveyDataEntryById = surveyMasterService.insertSurveyDataEntry(decrypt);

			Object parseData = customJsonParser.customJsonParserMethod().parse(surveyDataEntryById);
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parseData);

			JSONArray jsonArray = (JSONArray) customJsonParser.customJsonParserMethod().parse(decrypt);

			JSONObject json = (JSONObject) jsonArray.get(0);

			if (json.containsKey("result_head")) {
				JSONArray jsonArr = (JSONArray) json.get("result_head");
				JSONObject js = (JSONObject) jsonArr.get(0);
				if (js.containsKey("login_id") && js.containsKey("event_name")) {

					Long loginId = (Long) js.get("login_id");

					String eventName = (String) js.get("event_name");

					if (eventName.equalsIgnoreCase("S")) {
						eventLogService.saveEventLogs(EventListEnum.S_INSERT_SURVEY_DATA_ENTRY.getEventId(),
								EventListEnum.S_INSERT_SURVEY_DATA_ENTRY.getEventName(), loginId.intValue(), decrypt,
								jsonString);
					} else if (eventName.equalsIgnoreCase("C")) {
						eventLogService.saveEventLogs(EventListEnum.C_INSERT_SURVEY_DATA_ENTRY.getEventId(),
								EventListEnum.C_INSERT_SURVEY_DATA_ENTRY.getEventName(), loginId.intValue(), decrypt,
								jsonString);
					}
				}

			}

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// -------------Clone Survey----------
	// @CheckSignature
	@PostMapping("/cloneSurvey")
	public ResponseEntity<?> cloneSurvey(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			SurveyCloneRequest req = mapper.readValue(decrypt, SurveyCloneRequest.class);

			violations = validateRequestDtoUtil.validateReqDto(req);
			if (!violations.isEmpty()) {
				LOGGER.error(violations);
				throw new ConstraintViolationException(violations);
			}
			Integer cloneSurvey = surveyMasterService.cloneSurvey(req);

//			Object parseData = customJsonParser.customJsonParserMethod().parse(surveyDataEntryById);

			String jsonString = null;

			if (cloneSurvey == 0) {
				jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP126.getStatusCode(), APPServiceCode.APP126.getStatusDesc()));
			} else if (cloneSurvey == 1) {
				jsonString = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), null));
			}

			eventLogService.saveEventLogs(EventListEnum.CLONE_SURVEY.getEventId(),
					EventListEnum.CLONE_SURVEY.getEventName(), req.getSurveyMaster().getLoggedInUserId().intValue(),
					decrypt, jsonString);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.error("#cloneSurvey ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	
	// -------------Download and Upload Survey Excel----------

	@PostMapping("/downloadExcel")
    public ResponseEntity<?> downloadExcel(
            @RequestHeader Map<String, String> requestHeader,
            @RequestBody EncryptDataRequest encryptedData,
            HttpServletRequest request, HttpServletResponse response
    ) throws Exception {
        String decrypt = aesEncryption.decrypt(encryptedData.getData());
        System.out.println("*****************decrypt=" + decrypt);

        @SuppressWarnings("unchecked")
        Map<String, String> responseMap = mapper.readValue(decrypt, Map.class);
        String fileName = responseMap.get("file_name");
        System.out.println("*****************fileName=" + fileName);

        
        
        
        
        Path filePath = Paths.get(excelFolder+"download/" + fileName);
        
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }
        File file = filePath.toFile();
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamResource resource = new InputStreamResource(fileInputStream);

        byte[] fileBytes = Files.readAllBytes(file.toPath());
        
        String jsonString = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(new ResponseValue(false, APPServiceCode.APP300.getStatusDesc(), fileBytes));
		return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
    }

	
	

	// @CheckSignature
	@PostMapping("/downloadsurveyexcel")
	public ResponseEntity<?> downloadSurvey(
			@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			DownloadExcelRequest req = mapper.readValue(decrypt, DownloadExcelRequest.class);

			violations = validateRequestDtoUtil.validateReqDto(req);
			if (!violations.isEmpty()) {
				LOGGER.error(violations);
				throw new ConstraintViolationException(violations);
			}
			DownloadSurveyExcelResponse downloadSurExlResp = documentUploadDownloadService.downloadSurveyExcel(req);

	       
			
			
			//			if (filepath == null) {
//				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
//						APPServiceCode.APP504.getStatusCode(), APPServiceCode.APP504.getStatusDesc()));
//				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
//			}

//			Object parseData = customJsonParser.customJsonParserMethod().parse(surveyDataEntryById);
//			DownloadSurveyExcelResponse downloadSurveyExcelResponse = new DownloadSurveyExcelResponse();
//			downloadSurveyExcelResponse.setSurveyId(req.getSurveyId());
//			downloadSurveyExcelResponse.setFileurl(filepath);
//			downloadSurveyExcelResponse.setSuccess(true);
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(downloadSurExlResp);

			eventLogService.saveEventLogs(EventListEnum.DOWNLOAD_SURVEY_EXCEL.getEventId(),
					EventListEnum.DOWNLOAD_SURVEY_EXCEL.getEventName(), req.getLoggedInUserId().intValue(), decrypt,
					jsonString);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.error("#cloneSurvey ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error("-----------Exception Occured While Download Survey Excel --------" + e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/uploadsurveyexcel")
	public ResponseEntity<?> uploadExcel(@RequestHeader Map<String, String> requestHeader, HttpServletRequest request,
			HttpServletResponse response, @RequestParam(name = "file") MultipartFile file,
			@RequestParam(name = "surveyId") Integer surveyId, @RequestParam(name = "loginUserid") Integer loginuserId,
			@RequestParam(name = "isUpload") boolean isUpload, @RequestParam("instanceId") Integer instanceId) {
		try {
			List<SurveyErrorReponse> result = documentUploadDownloadService.uploadSurveyExcel(file, surveyId,
					loginuserId, instanceId, isUpload);
			if (!result.isEmpty()) {
				if (isUpload == true) {
					boolean hasError = true;
					for (SurveyErrorReponse errorResponse : result) {
						if (errorResponse.getError() != null) {
							hasError = false;
						}
					}
					if (hasError == true) {
						String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
								new ResponseValue(true, APPServiceCode.APP300.getStatusDesc(), result));
						return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
					} else {
						String jsonString = mapper.writerWithDefaultPrettyPrinter()
								.writeValueAsString(new ResponseValue(false,
										APPServiceCode.APP303.getStatusDesc() + " Please check your answers!", result));
						return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
					}
				}
				String jsonString = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString(new ResponseValue(true, APPServiceCode.APP306.getStatusDesc(), result));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
			}

			String jsonString = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(false, APPServiceCode.APP305.getStatusDesc(), result));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.error("#cloneSurvey ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@PostMapping("/surveyProcedureCall")
	public ResponseEntity<?> surveyProcedureCall(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			SurveyIdRequest req = mapper.readValue(decrypt, SurveyIdRequest.class);

			if (StringUtils.isValidObj(req)) {
				if (!StringUtils.isValidObj(req.getSurveyId())) {
					return (ResponseEntity<?>) ResponseEntity.badRequest();
				}
			} else {
				return (ResponseEntity<?>) ResponseEntity.badRequest();
			}

			String procedureResponse = surveyMasterService.surveyProcedureCall(req, pageNo, pageSize, sortDir, sortBy, searchKey);
			Object parseData = customJsonParser.customJsonParserMethod().parse(procedureResponse);
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parseData);
			
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.error("#surveyDataEntryById ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(APPServiceCode.APP505.getStatusDesc() + "," + e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
}
