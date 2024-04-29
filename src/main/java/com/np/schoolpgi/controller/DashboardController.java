package com.np.schoolpgi.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.UserLevelIdRequest;
import com.np.schoolpgi.dto.request.EncryptDataRequest;
import com.np.schoolpgi.dto.request.ViewReqById;
import com.np.schoolpgi.dto.request.WebsiteRequest;
import com.np.schoolpgi.dto.response.DashboardLevelIdResponse;
import com.np.schoolpgi.dto.response.DashboardResponse;
import com.np.schoolpgi.dto.response.DashboardUserListResponse;
import com.np.schoolpgi.dto.response.ErrorResponse;
import com.np.schoolpgi.dto.response.ResponseData;
import com.np.schoolpgi.dto.response.ResponseValue;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.SurveyMaster;
import com.np.schoolpgi.service.DashboardService;
import com.np.schoolpgi.util.AESEncryption;
import com.np.schoolpgi.util.StringUtils;

@RequestMapping("/np/app")
@RestController
@CrossOrigin(origins = "*")
public class DashboardController {

	final static Logger LOGGER = LogManager.getLogger(DashboardController.class);

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private AESEncryption aesEncryption;

	@Autowired
	private ObjectMapper mapper;

	// @CheckSignature
	@PostMapping("/dashboardCount")
	public ResponseEntity<?> getDashboardDetails(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			LOGGER.info("Inside DashboardController, getDashboardDetails");
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			ViewReqById viewReqById = mapper.readValue(decryptedData, ViewReqById.class);
			List<SurveyMaster> surveyList = dashboardService.getSurveyList(viewReqById);
			
			
			DashboardResponse dashboardResponse = dashboardService.getDashboardCount(surveyList, viewReqById.getId());
			if (dashboardResponse == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), dashboardResponse));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// @CheckSignature
	@PostMapping("/dashboardUserList")
	public ResponseEntity<?> getDashboardUserList(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			LOGGER.info("Inside DashboardController, getDashboardUserList");

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			ViewReqById viewReqById = mapper.readValue(decryptedData, ViewReqById.class);

			DashboardUserListResponse dashboardResponse = dashboardService.getSurveyDetails(viewReqById);

			if (dashboardResponse == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), dashboardResponse));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
//			return null;
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	// @CheckSignature
//	@PostMapping("/dashboardUserList")
//	public ResponseEntity<?> getSurveyList(@RequestHeader Map<String, String> requestHeader,
//			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
//		try {
//			LOGGER.info("Inside DashboardController, getDashboardUserList");
//
//			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
//			String surveyId = mapper.readValue(decryptedData, String.class);
//
//			DashboardUserListResponse dashboardResponse = dashboardService.getSurveyDetails(surveyId);
//
//			if (dashboardResponse == null) {
//				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
//						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
//				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
//			}
//			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
//					new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), dashboardResponse));
//			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
////			return null;
//		} catch (Exception e) {
//			LOGGER.error(e);
//			throw new SomethingWentWrongException(e.getMessage());
//		} 
//	}

	@PostMapping("/getLevelId")
	public ResponseEntity<?> getLevelId(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			LOGGER.info("Inside DashboardController, getLevelId");

			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			UserLevelIdRequest userLevelIdRequest = mapper.readValue(decryptedData, UserLevelIdRequest.class);

			DashboardLevelIdResponse dashboardLevelIdResponse = dashboardService.getLevelId(userLevelIdRequest);

			if (dashboardLevelIdResponse == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP506.getStatusCode(), APPServiceCode.APP506.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), dashboardLevelIdResponse));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
	
	@PostMapping("/moveWebsiteData")
	public ResponseEntity<?> moveWebsiteData(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response){
		try {
			LOGGER.info("Inside DashboardController, moveWebsiteData");
			String decryptedData = aesEncryption.decrypt(encryptedData.getData());
			WebsiteRequest websiteRequest = mapper.readValue(decryptedData, WebsiteRequest.class);
			String websiteDataResponse = dashboardService.moveWebsiteData(websiteRequest.getYear_code()) ;
			
			if (StringUtils.isEmpty(websiteDataResponse)) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP518.getStatusCode(), APPServiceCode.APP518.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), websiteDataResponse));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			
		}catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

}
