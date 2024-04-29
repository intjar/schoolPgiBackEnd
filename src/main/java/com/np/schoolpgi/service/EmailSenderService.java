package com.np.schoolpgi.service;

public interface EmailSenderService {

	Integer sendOTP( String email );
	Integer sendPassword(String email, String password, String name, String username, String role, String level);
}
