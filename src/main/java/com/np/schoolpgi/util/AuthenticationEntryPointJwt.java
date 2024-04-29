package com.np.schoolpgi.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.schoolpgi.dto.response.ResponseData;

@Component
public class AuthenticationEntryPointJwt implements AuthenticationEntryPoint {

	final static Logger LOGGER = LogManager.getLogger(AuthenticationEntryPointJwt.class);
  
  @Autowired
  AESEncryption aesEncryption;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
	  LOGGER.error("Forbidden error: {}", authException.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    final Map<String, Object> body = new HashMap<>();
    body.put("success", false);
    body.put("status", HttpServletResponse.SC_FORBIDDEN);
    body.put("error", "Unauthorized");
    body.put("errorMessage", authException.getMessage());
    body.put("path", request.getServletPath());
    
    

    final ObjectMapper mapper = new ObjectMapper();
    ResponseData responseData = new ResponseData();
    
    JSONObject jsonBody = new JSONObject(body);
    
    try {
    	 responseData = new ResponseData(aesEncryption.encrypt(jsonBody));
    }catch (Exception e) {
    	LOGGER.error(e);
	}
   
    mapper.writeValue(response.getOutputStream(), responseData);

  }

}
