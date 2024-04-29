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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.np.schoolpgi.aop.CheckSignature;
import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.LoginRepository;
import com.np.schoolpgi.dao.UserRepository;
import com.np.schoolpgi.dto.request.EncryptDataRequest;
import com.np.schoolpgi.dto.request.LoginRequest;
import com.np.schoolpgi.dto.request.TokenRefreshRequest;
import com.np.schoolpgi.dto.response.ErrorResponse;
import com.np.schoolpgi.dto.response.LoginResponse;
import com.np.schoolpgi.dto.response.ResponseData;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.exception.TokenRefreshException;
import com.np.schoolpgi.exception.ValidateRequestDtoUtil;
import com.np.schoolpgi.model.Login;
import com.np.schoolpgi.model.RefreshToken;
import com.np.schoolpgi.model.User;
import com.np.schoolpgi.service.LoginService;
import com.np.schoolpgi.service.RefreshTokenService;
import com.np.schoolpgi.util.AESEncryption;
import com.np.schoolpgi.util.JwtUtils;
import com.np.schoolpgi.util.UserDetailsImpl;

@RequestMapping("/np/app")
@RestController
@CrossOrigin(origins = "*")
public class AuthController {
	final static Logger LOGGER = LogManager.getLogger(AuthController.class);

	@Autowired
	private LoginService loginService;

	@Autowired
	private AESEncryption aesEncryption;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private ValidateRequestDtoUtil validateRequestDtoUtil;
	
	Set<ConstraintViolation<Object>> violations = null;

	@PostMapping("/login")
	//@CheckSignature
	public ResponseEntity<?> signin(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {

		try {
//			LOGGER.info("Before Decryption" + encryptedData);
			String decryptdeData = aesEncryption.decrypt(encryptedData.getData());
			LoginRequest loginRequest = new LoginRequest();
			loginRequest = mapper.readValue(decryptdeData, LoginRequest.class);
			violations = validateRequestDtoUtil.validateReqDto(loginRequest);
			if (!violations.isEmpty()) {
				LOGGER.error(violations);
				throw new ConstraintViolationException(violations);
			}
			return loginService.signin(loginRequest);
		}catch (ConstraintViolationException e) {
			LOGGER.error("#signin ConstraintViolationException " + violations);
			throw new ConstraintViolationException(violations);
		} catch (UsernameNotFoundException e) {
			LOGGER.error(e);
			throw new UsernameNotFoundException(e.getMessage());
		} catch (BadCredentialsException e) {
			LOGGER.error(e);
			throw new BadCredentialsException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	//@CheckSignature
	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@RequestHeader Map<String, String> requestHeader,
			@RequestBody EncryptDataRequest encryptedData, HttpServletRequest request, HttpServletResponse response) {
		TokenRefreshRequest requestRefreshToken=new TokenRefreshRequest();
		try {
			String decrypt = aesEncryption.decrypt(encryptedData.getData());
			requestRefreshToken = mapper.readValue(decrypt, TokenRefreshRequest.class);
			RefreshToken refreshTk = refreshTokenService.findByToken(requestRefreshToken.getRefreshToken());
			if(refreshTk==null) {
				LOGGER.error(requestRefreshToken.getRefreshToken()+" :: "+
						APPServiceCode.APP171.getStatusDesc());
				String jsonStr = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString(
								new ErrorResponse(
										false, 
										APPServiceCode.APP171.getStatusCode(),
										requestRefreshToken.getRefreshToken()+APPServiceCode.APP171.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
			refreshTokenService.verifyExpiration(refreshTk);
			Login login = loginRepository.findByUid(refreshTk.getUser().getUserId());
			UserDetailsImpl userDetailsImpl = new UserDetailsImpl(login);
			String accessToken = jwtUtils.generateToken(userDetailsImpl);
			
			User user = userRepository.findByUserId(login.getUid());
			
			
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new LoginResponse(true, APPServiceCode.APP039.getStatusDesc(), 
							accessToken,
							refreshTk.getToken(),
							user.getUserId(),
							user.getName(),
							user.getEmail(),
							login.getUsername(),
							user.getPhoneNo(),
							user.getInstanceId().getId(),
							user.getInstanceId().getInstanceName(),
							user.getLevelMaster().getId(),
							user.getLevelMaster().getLevelName(),
							user.getRoleId().getId(),
							user.getRoleId().getName(),
							user.getStatus()));
			
//			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
//					.writeValueAsString(
//							new ResponseValue(
//									true, APPServiceCode.APP001.getStatusDesc(),
//									new TokenRefreshResponse(accessToken,requestRefreshToken.getRefreshToken())));
			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		} catch (TokenRefreshException e) {
			LOGGER.error(requestRefreshToken.getRefreshToken()+" :: "+
					APPServiceCode.APP170.getStatusDesc()+" :: "+e);
			throw new TokenRefreshException(requestRefreshToken.getRefreshToken(),
					APPServiceCode.APP170.getStatusDesc());
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}
}
