package com.np.schoolpgi.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.dao.LoginRepository;
import com.np.schoolpgi.dao.UserRepository;
import com.np.schoolpgi.model.Login;
import com.np.schoolpgi.model.User;
import com.np.schoolpgi.service.ValidateOtpService;

@Service
public class ValidateOtpServiceImpl implements ValidateOtpService {

	@Autowired
	LoginRepository loginRepository;
	
	@Autowired
	UserRepository userRepository;
	
	long OTP_VALID_DURATION=300000;
	@Override
	public Integer validateOtp(String email, String otp) {

		
		User user = userRepository.findByEmailIgnoreCase(email);
		 if ( user == null )
	        {
	            return 0;
	        }
		 else
		 {
			 Login login=loginRepository.findByUid(user.getUserId());
			 Date otpRequestedTime = login.getForgetpasswordtokenat();
		      long otpRequestedTimeInMillis = otpRequestedTime.getTime();
		      long currentTimeInMillis = System.currentTimeMillis();
		      if(currentTimeInMillis<otpRequestedTimeInMillis+OTP_VALID_DURATION)
		      {
		    	 
		    	 if(login.getForgetpasswordtoken().equals( otp))
		    		 return 1;
		    	 
		    	 else
		    	  return 3;
		      }
		      else
		      {
		    	  login.setForgetpasswordtoken(null);
		    	  login.setForgetpasswordtokenat(null);
		    	  loginRepository.save(login);
		    	  return 2;
		      }
		     
		 }

	}

}
