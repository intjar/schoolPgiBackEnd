package com.np.schoolpgi.controller;

import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.constants.AppConstants;
import com.np.schoolpgi.constants.EventListEnum;
import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.request.EncryptDataRequest;
import com.np.schoolpgi.dto.request.ViewReqById;
import com.np.schoolpgi.dto.response.ErrorResponse;
import com.np.schoolpgi.dto.response.MessageResponse;
import com.np.schoolpgi.dto.response.ResponseData;
import com.np.schoolpgi.dto.response.ResponseValue;
import com.np.schoolpgi.dto.response.ResponseWithPagination;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.exception.ValidateRequestDtoUtil;
import com.np.schoolpgi.model.MediaCategory;
import com.np.schoolpgi.model.MediaUpload;
import com.np.schoolpgi.service.EventLogService;
import com.np.schoolpgi.service.MediaUploadService;
import com.np.schoolpgi.util.AESEncryption;

@RequestMapping("/np/app")
@RestController
@CrossOrigin(origins = "*")
public class MediaUploadController {

	final static Logger LOGGER = LogManager.getLogger(MediaUploadController.class);

//	private static String IMAGE = "C://Users//NP//Downloads//shopping.jpeg";

	@Autowired
	private MediaUploadService mediaUploadService;

	@Autowired
	private ValidateRequestDtoUtil validateRequestDtoUtil;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private AESEncryption aesEncryption;

	Set<ConstraintViolation<Object>> violations = null;

	@Autowired
	private EventLogService eventLogService;

	@PostMapping("/mediaUpload")
	public ResponseEntity<?> mediaUpload(@RequestHeader Map<String, String> requestHeader,
			@RequestParam(name = "files") MultipartFile[] files,
			@RequestParam(name = "categoryId") Integer categoryId,
			@RequestParam(name = "loginId") Integer loginId,HttpServletRequest request, HttpServletResponse response) {
		try {
			
			MediaUpload mdeiaUpload = mediaUploadService.mdeiaUpload(files, categoryId,loginId);
			if(mdeiaUpload==null) {
				String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false, APPServiceCode.APP301.getStatusCode(), APPServiceCode.APP301.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString))); 
			}
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResponseValue(true, APPServiceCode.APP300.getStatusDesc(), mdeiaUpload));
			
			eventLogService.saveEventLogs(EventListEnum.MEDIA_UPLOAD.getEventId(), EventListEnum.MEDIA_UPLOAD.getEventName(), loginId, "Media Upload", jsonString);
			
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.error("#mediaUpload ConstraintViolationException " + e);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
	
//	@PostMapping("/mediaUpload")
//	public ResponseEntity<?> mediaUpload(@RequestBody EncryptDataRequest encryptedData) {
//		try {
//			
//			String decrypt = aesEncryption.decrypt(encryptedData.getData());
//			
//			/*
//			 * 
//			 * File file = new File(IMAGE); BufferedImage bImage = ImageIO.read(file);
//			 * ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			 * ImageIO.write(bImage, "jpeg", bos); byte[] data = bos.toByteArray();
//			 * LOGGER.info("# Arrays.toString(data) " + Arrays.toString(data));
//			 * List<MediaUploadRequest> mediaUploadRequest = new
//			 * ArrayList<MediaUploadRequest>(); MediaUploadRequest media = new
//			 * MediaUploadRequest(); BeanUtils.copyProperties(req, media);
//			 * mediaUploadRequest.add(media);
//			 * 
//			 * MediaUploadRequest mediaUploadRequest2 = new MediaUploadRequest();
//			 * mediaUploadRequest2.setCategoryId(1);
//			 * mediaUploadRequest2.setCategoryName("Survey");
//			 * mediaUploadRequest2.setFile(data);
//			 * mediaUploadRequest2.setFileName(file.getName());
//			 * mediaUploadRequest2.setFileType(".jpeg");
//			 * mediaUploadRequest2.setCreatedBy(1);
//			 * mediaUploadRequest.add(mediaUploadRequest2);
//			 * 
//			 */
//			
//			Gson gson = new Gson();
//			MediaUploadRequest mediaReq[] = gson.fromJson(decrypt, MediaUploadRequest[].class);
//			List<MediaUploadRequest> asList = Arrays.asList(mediaReq);
//			for (MediaUploadRequest mediaUploadRequest : asList) {
//				violations = validateRequestDtoUtil.validateReqDto(mediaUploadRequest);
//				if (!violations.isEmpty()) {
//					LOGGER.error(violations);
//					throw new ConstraintViolationException(violations);
//				}
//			}
//			ResponseValue mdeiaUpload = mediaUploadService.mdeiaUpload(asList);
//			
//			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mdeiaUpload);
//			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
//		} catch (ConstraintViolationException e) {
//			LOGGER.error("#mediaUpload ConstraintViolationException " + e);
//			throw new ConstraintViolationException(violations);
//		} catch (Exception e) {
//			LOGGER.error(e);
//			throw new SomethingWentWrongException(e.getMessage());
//		}
//	}

	@PostMapping("/mediaCategory")
	public ResponseEntity<?> getMediaCategory(@RequestHeader Map<String, String> requestHeader,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			List<MediaCategory> mediaCategory = mediaUploadService.getMediaCategory();
			if (mediaCategory == null) {
				String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP996.getStatusCode(), APPServiceCode.APP996.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
			}
			String jsonString = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), mediaCategory));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@PostMapping("/deleteFile")
	public ResponseEntity<?> deleteUploadedFile(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			DeleteRequestDto req = mapper.readValue(decrypt, DeleteRequestDto.class);

			violations = validateRequestDtoUtil.validateReqDto(req);
			if (!violations.isEmpty()) {
				LOGGER.error(violations);
				throw new ConstraintViolationException(violations);
			}
			mediaUploadService.deleteMedia(req);
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
					APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));
			
			eventLogService.saveEventLogs(EventListEnum.DELETE_FILE.getEventId(), EventListEnum.DELETE_FILE.getEventName(), req.getLoggedInUserId(), decrypt, jsonString);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (ConstraintViolationException e) {
			LOGGER.error("#mediaUpload ConstraintViolationException " + e);
			throw new ConstraintViolationException(violations);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@PostMapping("/files")
	public ResponseEntity<?> getUploadedFile(
			@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "searchKey", defaultValue = AppConstants.DEFAULT_SEARCH_KEY, required = false) String searchKey,
            HttpServletRequest request, HttpServletResponse response){
		try {
			
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			ViewReqById req = mapper.readValue(decrypt, ViewReqById.class);

			violations = validateRequestDtoUtil.validateReqDto(req);
			if (!violations.isEmpty()) {
				LOGGER.error(violations);
				throw new ConstraintViolationException(violations);
			}
			
			ResponseWithPagination uploadedFile = mediaUploadService.getUploadedFile(req.getId(),pageNo, pageSize, sortDir, sortBy,searchKey);
			if (uploadedFile.getResult() == null) {
				String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP996.getStatusCode(), APPServiceCode.APP996.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
			}
			String jsonString = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), uploadedFile));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonString)));
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

}
