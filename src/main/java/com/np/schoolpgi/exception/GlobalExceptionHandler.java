package com.np.schoolpgi.exception;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.schoolpgi.aop.SignatureValidationFailed;
import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dto.response.ResponseData;
import com.np.schoolpgi.util.AESEncryption;
import com.np.schoolpgi.util.ApiError;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private ObjectMapper mapper;

	final static Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);
	@Autowired
	private AESEncryption aesEncryption;

//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
//		LOGGER.error("globleExcpetionHandler ", ex);
//		ApiError errorDetails = new ApiError(HttpStatus.OK, ex.getMessage(), request.getDescription(false));
//		LOGGER.info("This is Global Exception Handler.");
//		LOGGER.info(ex.getMessage());
//		LOGGER.error(errorDetails.toString());
//		LOGGER.error(request.getDescription(false));
//		return ResponseEntity.ok(errorDetails);
//	}

	@ExceptionHandler(SignatureValidationFailed.class)
	public ResponseEntity<?> signatureValidationFailed(Exception ex, WebRequest request) throws Exception {
		LOGGER.error(ex.getMessage(), ex);
		ApiError errorDetails = new ApiError(
        		false,
        		HttpStatus.BAD_REQUEST,
        		"Signature Validation Failed",
        		request.getDescription(false));
		String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorDetails);
		return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
	}

	@ExceptionHandler(JsonGenerationException.class)
	public ResponseEntity<?> JsonGenerationException(JsonGenerationException ex, WebRequest request) throws Exception {
		LOGGER.error(ex.getMessage(), ex);
		ApiError errorDetails = new ApiError(
        		false,
        		HttpStatus.BAD_REQUEST,
        		ex.getMessage(),
        		request.getDescription(false));
		String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorDetails);
		return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
	}

	@ExceptionHandler(SomethingWentWrongException.class)
	public ResponseEntity<?> somethingWentWrongException(Exception ex, WebRequest request) throws Exception {
		LOGGER.error(ex);
		ApiError errorDetails = new ApiError(
        		false,
        		HttpStatus.INTERNAL_SERVER_ERROR,
        		"Something went wrong in DB.",
        		request.getDescription(false));
		String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorDetails);
		return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> UsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
		LOGGER.error(ex);
		ApiError errorDetails = new ApiError(
        		false,
        		HttpStatus.BAD_REQUEST,
        		APPServiceCode.APP006.getStatusDesc(),
        		request.getDescription(false));
		try {
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorDetails);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> BadCredentialsException(BadCredentialsException ex, WebRequest request) {
		LOGGER.error(ex);
		ApiError errorDetails = new ApiError(
        		false,
        		HttpStatus.BAD_REQUEST,
        		APPServiceCode.APP002.getStatusDesc(),
        		request.getDescription(false));
		try {
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorDetails);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<?> IOException(IOException ex, WebRequest request) {
		LOGGER.error(ex.getMessage(), ex);
		ApiError errorDetails = new ApiError(false, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),
				request.getDescription(false));
		try {
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorDetails);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
		LOGGER.error(ex.getMessage(), ex);
//		LocalDateTime datetime1 = LocalDateTime.now();

		ApiError errorDetails = new ApiError(false, HttpStatus.BAD_REQUEST, ex.getMessage(),
				request.getDescription(false));
		try {
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorDetails);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@ExceptionHandler(TokenRefreshException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<?> handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
		LOGGER.error(ex);
		ApiError errorDetails = new ApiError(false, HttpStatus.BAD_REQUEST, ex.getMessage(),
				request.getDescription(false));
		try {
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorDetails);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(Exception ex, WebRequest request){

		LOGGER.error(ex.getMessage(), ex);
		ApiError errorDetails = new ApiError(false, HttpStatus.BAD_REQUEST, APPServiceCode.APP302.getStatusDesc(),
				request.getDescription(false));
		try {
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorDetails);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			throw new SomethingWentWrongException(e.getMessage());
		}
   }
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> missingServletRequestParameterException(Exception ex, WebRequest request){
		
		LOGGER.error(ex.getMessage(), ex);
		ApiError errorDetails = new ApiError(false, HttpStatus.BAD_REQUEST, APPServiceCode.APP303.getStatusDesc(),
				request.getDescription(false));
		try {
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorDetails);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
	
	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<?> fileNotFoundException(Exception ex, WebRequest request){
		
		LOGGER.error(ex.getMessage(), ex);
		ApiError errorDetails = new ApiError(false, HttpStatus.BAD_REQUEST, APPServiceCode.APP303.getStatusDesc(),
				request.getDescription(false));
		try {
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorDetails);
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		} catch (Exception e) {
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

}