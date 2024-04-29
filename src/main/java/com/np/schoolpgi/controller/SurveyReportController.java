package com.np.schoolpgi.controller;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.schoolpgi.constants.AppConstants;
import com.np.schoolpgi.dao.SurveyInstanceRequest;
import com.np.schoolpgi.dto.request.EncryptDataRequest;
import com.np.schoolpgi.dto.request.SurveyReportRequest;
import com.np.schoolpgi.dto.request.SurveyStatusRequest;
import com.np.schoolpgi.dto.request.SuveyReportBasedRequest;
import com.np.schoolpgi.dto.response.ResponseData;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.exception.ValidateRequestDtoUtil;
import com.np.schoolpgi.service.SurveyReportService;
import com.np.schoolpgi.util.AESEncryption;
import com.np.schoolpgi.util.CustomJsonParser;
import com.np.schoolpgi.util.StringUtils;

@RequestMapping("/np/app")
@RestController
@CrossOrigin(origins = "*")
public class SurveyReportController {

	final static Logger LOGGER = LogManager.getLogger(SurveyReportController.class);

	@Autowired
	private AESEncryption aesEncryption;

	@Autowired
	private CustomJsonParser customJsonParser;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private SurveyReportService servicReportService;

	Set<ConstraintViolation<Object>> violations = null;

	@Autowired
	private ValidateRequestDtoUtil validateRequestDtoUtil;

	@PostMapping("/assign-survey-report")
	public ResponseEntity<?> surveyReport1(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy") String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey,
			HttpServletRequest request, HttpServletResponse response) {
		try {

			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			SurveyReportRequest surveyReportRequest = mapper.readValue(decrypt, SurveyReportRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(surveyReportRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			String surveyReport1 = servicReportService.assignSurveyReport(pageNo, pageSize, sortDir, sortBy, searchKey,
					surveyReportRequest.getLoginId(), surveyReportRequest.getLevelId(),
					surveyReportRequest.getInstanceId());
			Object parse = customJsonParser.customJsonParserMethod().parse(surveyReport1);
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parse);

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@PostMapping("/survey-name-report")
	public ResponseEntity<?> surveyNameReport(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy") String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey,
			HttpServletRequest request, HttpServletResponse response) {
		try {

			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			SurveyReportRequest surveyReportRequest = mapper.readValue(decrypt, SurveyReportRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(surveyReportRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			String surveyReport2 = servicReportService.surveyNameReport(pageNo, pageSize, sortDir, sortBy, searchKey,
					surveyReportRequest.getSurveyId(), surveyReportRequest.getLoginId(),
					surveyReportRequest.getLevelId(), surveyReportRequest.getInstanceId());
			Object parse = customJsonParser.customJsonParserMethod().parse(surveyReport2);
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parse);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

//	@PostMapping("/getSurveyStatusList")
//	public String getSurveyStatusList(@RequestHeader Map<String, String> requestHeader,
//			@RequestBody SurveyStatusRequest surveyStatusRequest,
//			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
//			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
//			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
//			@RequestParam(value = "sortBy") String sortBy,
//			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey,
//			HttpServletRequest request, HttpServletResponse response) {
//
//		return servicReportService.getSurveyStatus(surveyStatusRequest, pageNo, pageSize, sortDir, sortBy, searchKey);
//	}

	@PostMapping("/getSurveyStatusList")
	public ResponseEntity<?> getSurveyStatusList(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy") String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			SurveyStatusRequest surveyStatusRequest = mapper.readValue(decrypt, SurveyStatusRequest.class);

			if (!StringUtils.isValidObj(surveyStatusRequest.getUserId()) && surveyStatusRequest.getUserId() == 0) {
				LOGGER.error("Login id is not valid");
				throw new ConstraintViolationException(violations);
			}

			String surveyStatusList = servicReportService.getSurveyStatus(surveyStatusRequest, pageNo, pageSize,
					sortDir, sortBy, searchKey);
			Object parse = customJsonParser.customJsonParserMethod().parse(surveyStatusList);
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parse);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@PostMapping("/getInstanceWiseList")
	public ResponseEntity<?> getInstanceWiseList(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			SurveyInstanceRequest surveyInstanceRequest = mapper.readValue(decrypt, SurveyInstanceRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(surveyInstanceRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String InstanceWiseList = servicReportService.getInstanceWiseList(surveyInstanceRequest, pageNo, pageSize,
					sortDir, sortBy, searchKey);
			Object parse = customJsonParser.customJsonParserMethod().parse(InstanceWiseList);
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parse);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@PostMapping("/getSurveyDataAccToReport")
	public ResponseEntity<?> getSurveyDataAccToReport(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			SuveyReportBasedRequest suveyReportBasedRequest = mapper.readValue(decrypt, SuveyReportBasedRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(suveyReportBasedRequest);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			String ReportBasedList = servicReportService.getSurveyDataAccToReport(suveyReportBasedRequest, pageNo,
					pageSize, sortDir, sortBy, searchKey);
			Object parse = customJsonParser.customJsonParserMethod().parse(ReportBasedList);
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parse);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

}
