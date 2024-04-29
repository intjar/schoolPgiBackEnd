package com.np.schoolpgi.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.dao.LoginRepository;
import com.np.schoolpgi.dao.UserRepository;
import com.np.schoolpgi.dto.request.UpdatePasswordRequest;
import com.np.schoolpgi.model.Login;
import com.np.schoolpgi.model.User;
import com.np.schoolpgi.service.ResetPasswordService;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

	@Autowired
	LoginRepository loginRepository;
	
	@Autowired
	UserRepository userRepository;
    
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public Integer resetpassword(String email, String resetPassword, String confirmPassword) {

		User user = this.userRepository.findByEmailIgnoreCase(email);
		if (user == null) {
			return 0;
		} else {
			Login login=loginRepository.findByUid(user.getUserId());
			if(login==null)
			{
				return 3;
			}
			if (resetPassword.equals(confirmPassword)) {
				
				String encryptPassword=passwordEncoder.encode(confirmPassword);
				login.setPassword(encryptPassword);
				login.setForgetpasswordtoken(null);
				login.setForgetpasswordtokenat(null);
				login.setUpdatedAt(new Date());
				loginRepository.save(login);
				return 1;
			} else {
				return 2;
			}

		}

	}

	@Override
	public Integer updateChangePassword(UpdatePasswordRequest updatePasswordRequest) {
		// TODO Auto-generated method stub
		Login login=loginRepository.findByUid(updatePasswordRequest.getLoggedInUserId());
		if(login==null)
		{
			return 0;
		}
		
		PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
		boolean passwordMatch=passwordEncoder.matches(updatePasswordRequest.getCurrentpassword(),login.getPassword());
		if(passwordMatch)
		{
			
              if (updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword())) {
				
				String encryptPassword=passwordEncoder.encode(updatePasswordRequest.getNewPassword());
				login.setPassword(encryptPassword);
				login.setUpdatedAt(new Date());
				login.setUpdatedBy(updatePasswordRequest.getLoggedInUserId());
				loginRepository.save(login);
				return 1;
			}
              else {
				return 2;
			}
		}
		else 
			return 3;
				
	}

}
