package com.np.schoolpgi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.LoginRepository;
import com.np.schoolpgi.dao.UserRepository;
import com.np.schoolpgi.dto.request.LoginRequest;
import com.np.schoolpgi.dto.response.ErrorResponse;
import com.np.schoolpgi.dto.response.LoginResponse;
import com.np.schoolpgi.dto.response.ResponseData;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.Login;
import com.np.schoolpgi.model.RefreshToken;
import com.np.schoolpgi.model.User;
import com.np.schoolpgi.service.LoginService;
import com.np.schoolpgi.service.RefreshTokenService;
import com.np.schoolpgi.util.AESEncryption;
import com.np.schoolpgi.util.JwtUtils;
import com.np.schoolpgi.util.UserDetailsImpl;

@Service
public class LoginServiceImpl implements LoginService {

	final static Logger LOGGER = LogManager.getLogger(LoginServiceImpl.class);

	@Autowired
	private AESEncryption aesEncryption;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	LoginRepository loginRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	ObjectMapper mapper = new ObjectMapper();

	@Override
	public ResponseEntity<?> signin(LoginRequest loginRequest) {

		try {
			String decodedPassword = loginRequest.getPassword();
			
			Login loginData = loginRepository.findByUsername(loginRequest.getUsername().toLowerCase());
			if (loginData == null) {
				LOGGER.error(APPServiceCode.APP006.getStatusDesc()+"--"+"This user does not exist in DB");
				throw new UsernameNotFoundException(APPServiceCode.APP006.getStatusDesc());
			} 

			if(loginData.getStatus()==false) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString(new ErrorResponse(false, APPServiceCode.APP176.getStatusCode(), APPServiceCode.APP176.getStatusDesc()));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr))); 
			}
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), decodedPassword));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			LOGGER.info("User has been logged in");
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			userDetails.setId(loginData.getUid());
			String jwt = jwtUtils.generateToken(userDetails);
			
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
			
			User user = userRepository.findByUserId(loginData.getUid());
			
			
			String jsonStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new LoginResponse(true, APPServiceCode.APP039.getStatusDesc(), 
							jwt,
							refreshToken.getToken(),
							user.getUserId(),
							user.getName(),
							user.getEmail(),
							loginData.getUsername(),
							user.getPhoneNo(),
							user.getInstanceId().getId(),
							user.getInstanceId().getInstanceName(),
							user.getLevelMaster().getId(),
							user.getLevelMaster().getLevelName(),
							user.getRoleId().getId(),
							user.getRoleId().getName(),
							user.getStatus()));

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
		}catch(UsernameNotFoundException e) {
			LOGGER.error(e);
			throw new UsernameNotFoundException(APPServiceCode.APP006.getStatusDesc());
		}catch(BadCredentialsException e) {
			LOGGER.error(e);
			throw new BadCredentialsException(APPServiceCode.APP007.getStatusDesc());
		}catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

}