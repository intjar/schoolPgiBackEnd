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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.schoolpgi.constants.EventListEnum;
import com.np.schoolpgi.dto.request.EncryptDataRequest;
import com.np.schoolpgi.dto.request.NotifySurveyRequest;
import com.np.schoolpgi.dto.response.ResponseData;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.exception.ValidateRequestDtoUtil;
import com.np.schoolpgi.service.AssignUnAssignSurveyService;
import com.np.schoolpgi.service.EventLogService;
import com.np.schoolpgi.util.AESEncryption;
import com.np.schoolpgi.util.CustomJsonParser;

@RequestMapping("/np/app")
@RestController
@CrossOrigin(origins = "*")
public class SurveyAssignUnAssignController {

	final static Logger LOGGER = LogManager.getLogger(HomeController.class);

	@Autowired
	private AESEncryption aesEncryption;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private CustomJsonParser customJsonParser;

	@Autowired
	private ValidateRequestDtoUtil validateRequestDtoUtil;

	@Autowired
	private AssignUnAssignSurveyService service;

	Set<ConstraintViolation<Object>> violations = null;

	@Autowired
	private EventLogService eventLogService;

	@PostMapping("/assign-unassign-survey")
	public ResponseEntity<?> assignUnAssignSurvey(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());

			String assignUnAssignSurvey = service.assignUnAssignSurvey(decryptedData);

			Object parse = customJsonParser.customJsonParserMethod().parse(assignUnAssignSurvey);

			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parse);

			JSONArray jsonArray = (JSONArray) customJsonParser.customJsonParserMethod().parse(decryptedData);
			
			JSONObject json = (JSONObject) jsonArray.get(0);
			
			

			if (json.containsKey("lg_id") && json.containsKey("assign_type")) {
				Long loginId = (Long) json.get("lg_id");

				String assignType = (String) json.get("assign_type");

				if (assignType.equalsIgnoreCase("AST")) {
					eventLogService.saveEventLogs(EventListEnum.AST_ASSIGN_UNASSIGN_SURVEY.getEventId(),
							EventListEnum.UST_ASSIGN_UNASSIGN_SURVEY.getEventName(), loginId.intValue(), decryptedData, jsonStr);
				} else if (assignType.equalsIgnoreCase("UST")) {
					eventLogService.saveEventLogs(EventListEnum.RE_UPDATE_SURVEY_REVIEW_APPROVE.getEventId(),
							EventListEnum.RE_UPDATE_SURVEY_REVIEW_APPROVE.getEventName(), loginId.intValue(), decryptedData,
							jsonStr);
				}
			}

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@PostMapping("/notifySurvey")
	public ResponseEntity<?> notifySurvey(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());

			NotifySurveyRequest req = mapper.readValue(decryptedData, NotifySurveyRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(req);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			String resp = service.notifySurvey(req.getLoginId(), req.getSurveyId());
			// NotifySurveyRequest

			Object parse = customJsonParser.customJsonParserMethod().parse(resp);

			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parse);
			eventLogService.saveEventLogs(EventListEnum.NOTIFY_SURVEY.getEventId(),
					EventListEnum.NOTIFY_SURVEY.getEventName(), req.getLoginId().intValue(), decryptedData, jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}
	
	@PostMapping("/notifyTPDSurvey")
	public ResponseEntity<?> notifyTPDSurvey(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());

			NotifySurveyRequest req = mapper.readValue(decryptedData, NotifySurveyRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(req);
			if (!violations.isEmpty()) {
				LOGGER.info(violations);
				throw new ConstraintViolationException(violations);
			}

			String resp = service.notifyTPDSurvey(req.getLoginId(), req.getSurveyId());
			// NotifySurveyRequest

			Object parse = customJsonParser.customJsonParserMethod().parse(resp);

			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parse);
			eventLogService.saveEventLogs(EventListEnum.NOTIFY_SURVEY.getEventId(),
					EventListEnum.NOTIFY_SURVEY.getEventName(), req.getLoginId().intValue(), decryptedData, jsonStr);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

}
