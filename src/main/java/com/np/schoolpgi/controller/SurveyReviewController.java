package com.np.schoolpgi.controller;

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
import com.np.schoolpgi.constants.EventListEnum;
import com.np.schoolpgi.dto.request.EncryptDataRequest;
import com.np.schoolpgi.dto.request.RemarksRequestDto;
import com.np.schoolpgi.dto.request.ReviewDataListReqDto;
import com.np.schoolpgi.dto.response.ResponseData;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.exception.ValidateRequestDtoUtil;
import com.np.schoolpgi.service.EventLogService;
import com.np.schoolpgi.service.SurveyDeoRemarksService;
import com.np.schoolpgi.util.AESEncryption;
import com.np.schoolpgi.util.CustomJsonParser;

@RequestMapping("/np/app")
@RestController
@CrossOrigin(origins = "*")
public class SurveyReviewController {

	final static Logger LOGGER = LogManager.getLogger(SurveyReviewController.class);

	@Autowired
	private AESEncryption aesEncryption;

	Set<ConstraintViolation<Object>> violations = null;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private SurveyDeoRemarksService surveyDeoRemarksService;

	@Autowired
	private ValidateRequestDtoUtil validateRequestDtoUtil;

	@Autowired
	private CustomJsonParser customJsonParser;

	@Autowired
	private EventLogService eventLogService;

	@PostMapping("/getRemarks")
	public ResponseEntity<?> getRemarks(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			RemarksRequestDto req = mapper.readValue(decrypt, RemarksRequestDto.class);

			violations = validateRequestDtoUtil.validateReqDto(req);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			String remarks = surveyDeoRemarksService.getRemarks(req);

			Object jsonParsed = customJsonParser.customJsonParserMethod().parse(remarks);

			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonParsed);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.info("#getRemarks ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@PostMapping("/surveyDataListReview")
	public ResponseEntity<?> getSurveyDataListReview(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			ReviewDataListReqDto req = mapper.readValue(decrypt, ReviewDataListReqDto.class);

			violations = validateRequestDtoUtil.validateReqDto(req);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}
			System.out.println("dfijsiojf");

			String jsonRespString = surveyDeoRemarksService.getSurveyDataListReview(pageNo, pageSize, sortDir, sortBy,
					searchKey, req);
           System.out.println(jsonRespString);
			Object jsonParsed = customJsonParser.customJsonParserMethod().parse(jsonRespString);

			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonParsed);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.info("#reviewDataList ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@PostMapping("/surveyDataListReviewDetails")
	public ResponseEntity<?> getSurveyDataListReviewDetails(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			RemarksRequestDto req = mapper.readValue(decrypt, RemarksRequestDto.class);

			violations = validateRequestDtoUtil.validateReqDto(req);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			String jsonRespString = surveyDeoRemarksService.getSurveyDataListReviewDetails(req);

			Object jsonParsed = customJsonParser.customJsonParserMethod().parse(jsonRespString);

			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonParsed);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.info("#getRemarks ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@PostMapping("/updSurveyReviewApprove")
	public ResponseEntity<?> updSurveyReviewApprove(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {

			String decrypt = aesEncryption.decrypt(encryptedData.getData());

			String jsonRespString = surveyDeoRemarksService.updSurveyReviewApprove(decrypt);

			Object jsonParsed = customJsonParser.customJsonParserMethod().parse(jsonRespString);
			// login_id

			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonParsed);

//			JSONArray json = new JSONArray();
			JSONArray jsonArray = (JSONArray) customJsonParser.customJsonParserMethod().parse(decrypt);

			/*
			 * RV - Reviewed
			 * 
			 * RE - Request to edit for review
			 * 
			 * AP - Approved
			 * 
			 * AE - Request to edit for approve
			 */

			JSONObject json = (JSONObject) jsonArray.get(0);

			if (json.containsKey("login_id") && json.containsKey("event_code")) {

				Long loginId = (Long) json.get("login_id");

				String eventCode = (String) json.get("event_code");

				if (eventCode.equalsIgnoreCase("RV")) {
					eventLogService.saveEventLogs(EventListEnum.RV_UPDATE_SURVEY_REVIEW_APPROVE.getEventId(),
							EventListEnum.RV_UPDATE_SURVEY_REVIEW_APPROVE.getEventName(), loginId.intValue(), decrypt,
							jsonString);
				} else if (eventCode.equalsIgnoreCase("RE")) {
					eventLogService.saveEventLogs(EventListEnum.RE_UPDATE_SURVEY_REVIEW_APPROVE.getEventId(),
							EventListEnum.RE_UPDATE_SURVEY_REVIEW_APPROVE.getEventName(), loginId.intValue(), decrypt,
							jsonString);
				} else if (eventCode.equalsIgnoreCase("AP")) {
					eventLogService.saveEventLogs(EventListEnum.AP_UPDATE_SURVEY_REVIEW_APPROVE.getEventId(),
							EventListEnum.AP_UPDATE_SURVEY_REVIEW_APPROVE.getEventName(), loginId.intValue(), decrypt,
							jsonString);
				} else if (eventCode.equalsIgnoreCase("AE")) {
					eventLogService.saveEventLogs(EventListEnum.AE_UPDATE_SURVEY_REVIEW_APPROVE.getEventId(),
							EventListEnum.AE_UPDATE_SURVEY_REVIEW_APPROVE.getEventName(), loginId.intValue(), decrypt,
							jsonString);
				}
			}

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.info("#getRemarks ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

}
