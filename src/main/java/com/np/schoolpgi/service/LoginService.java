package com.np.schoolpgi.service;

import org.springframework.http.ResponseEntity;

import com.np.schoolpgi.dto.request.LoginRequest;

public interface LoginService {

	ResponseEntity<?> signin(LoginRequest loginRequest) throws Exception;
}
