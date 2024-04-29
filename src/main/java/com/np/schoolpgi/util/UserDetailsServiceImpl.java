package com.np.schoolpgi.util;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.dao.LoginRepository;
import com.np.schoolpgi.dao.UserRepository;
import com.np.schoolpgi.model.Login;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;
  
  @Autowired
  LoginRepository loginRepository;
  
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Login login = loginRepository.findByUsername(username);
    if(login==null) {
    	throw new UsernameNotFoundException("User Not Found with username: " + username);
    }
 
    return new UserDetailsImpl(login);
  }

}