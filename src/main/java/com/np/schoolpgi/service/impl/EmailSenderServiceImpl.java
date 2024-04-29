package com.np.schoolpgi.service.impl;

import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.LoginRepository;
import com.np.schoolpgi.dao.UserRepository;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.Login;
import com.np.schoolpgi.model.User;
import com.np.schoolpgi.service.EmailSenderService;

@Service
@Configuration
@EnableAsync
public class EmailSenderServiceImpl implements EmailSenderService{

	final static Logger LOGGER = LogManager.getLogger(EmailSenderServiceImpl.class);

	  @Autowired
	    private LoginRepository loginRepository;
	  
	  @Autowired
	  private UserRepository userRepository;
	  
	    Random random = new Random( 1000 );
	    @Value("${spring.mail.username}")
	    private String sender;
	    @Value("${spring.mail.password}")
	    private String emailPassword;
	    
	  
	    @Transactional
	    @Override
	    public Integer sendOTP( String email )
	    {
	        User user = userRepository.findByEmailIgnoreCase( email );
	        if ( user == null )
	        {
	            return 0;
	        }
	        Integer otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
	        try
	        {
	        	
	        	
	        	Login login=loginRepository.findByUid(user.getUserId());
	        	if(login==null)
	        	{
	        		return 2; 
	        	}
	        	if(login.getStatus()==false)
	        	{
	        		return 4;
	        	}
	            // Setting up necessary details
	        	Integer otpSent=sendOtpAsync(email,otp);
	        	
	        	//Integer otpSent=1234;
	        	System.out.println(otpSent);
	        	
	        	if(otpSent!=1)
	        	{
	        		return 3;
	        	}
	            login.setForgetpasswordtoken(otp.toString());
		        login.setForgetpasswordtokenat( new Date() );
		        loginRepository.save(login);
		        return 1;
	        }
	        catch ( Exception e )
	        {
	        	LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
	            throw new SomethingWentWrongException(e.getMessage());
	        }
	       
	    }
	    
	    @Async
	    private Integer sendOtpAsync(String email,Integer otp)
	    {
	    	try {
	    		String host = "smtp.gmail.com";
	            String to = email;
	            String from = sender;
	            String subject = "OTP from School PGI";
	            String messageText = "One Time Password for reset password is "+otp.toString();
	            Properties props = System.getProperties();
	            props.put("mail.host", host);
	            props.put("mail.transport.protocol", "smtp");
	            props.put("mail.smtp.port", "25");
	            props.put("mail.smtp.auth", "true");

	            // Gmail requires TLS, your server may not
	            props.put("mail.smtp.starttls.enable", "true");
	            Session mailSession = Session.getDefaultInstance(props, null);
	            Message msg = new MimeMessage(mailSession);
	            msg.setFrom(new InternetAddress(from));
	            InternetAddress[] address = {new InternetAddress(to)};
	            msg.setRecipients(Message.RecipientType.TO, address);
	            msg.setSubject(subject);
	            msg.setSentDate(new Date());
	            msg.setText(messageText);

	            Transport transport = mailSession.getTransport("smtp");
	            
	            transport.connect(host,from ,emailPassword);
	            //transport.connect();
	            transport.sendMessage(msg, address);

	            transport.close();

//	            mailMessage.setFrom(sender);
//	            mailMessage.setSubject( "OTP from School PGI");
//	            mailMessage.setTo( email );
//	            mailMessage.setText( "One Time Password for reset password is "+otp.toString() );
//	            javaMailSender.send( mailMessage );
	           
		        return 1;
			} catch (Exception e) {
				// TODO: handle exception
				return 2;
			}
			
	    }
	    
		@Override
		public Integer sendPassword(String email, String password, String name, String username, String role, String level) {
			// TODO Auto-generated method stub
		        try
		        {
		            // Setting up necessary details
		            String host = "smtp.gmail.com";
		            String to = email;
		            String from = sender;
		            String subject = "User Credentials for login in SPGI.";
		            String messageText = "Dear "+name +",\r\n"
		            		+ "\r\n"
		            		+ "Your account has been created on SCHOOL PGI PORTAL. You can login using username "+username +" and password "+password+"\r\n"
		            		+"You have been assigned "+ role +" role at "+level+" level on the SCHOOL PGI portal. You can access this role once you log into the system."+"\r\n\n"
		            		+ "Thank you, \nSPGI team";
		            Properties props = System.getProperties();
		            props.put("mail.host", host);
		            props.put("mail.transport.protocol", "smtp");
		            props.put("mail.smtp.port", "25");
		            props.put("mail.smtp.auth", "true");

		            // Gmail requires TLS, your server may not
		            props.put("mail.smtp.starttls.enable", "true");
		            Session mailSession = Session.getDefaultInstance(props, null);
		            Message msg = new MimeMessage(mailSession);
		            msg.setFrom(new InternetAddress(from));
		            InternetAddress[] address = {new InternetAddress(to)};
		            msg.setRecipients(Message.RecipientType.TO, address);
		            msg.setSubject(subject);
		            msg.setSentDate(new Date());
		            msg.setText(messageText);

		            Transport transport = mailSession.getTransport("smtp");
		            
		            transport.connect(host,from ,emailPassword);
		            //transport.connect();
		            transport.sendMessage(msg, address);

		            transport.close();

//		            mailMessage.setFrom(sender);
//		            mailMessage.setSubject( "OTP from School PGI");
//		            mailMessage.setTo( email );
//		            mailMessage.setText( "One Time Password for reset password is "+otp.toString() );
//		            javaMailSender.send( mailMessage );
			        return 1;
		        }
		        catch ( Exception e )
		        {
		        	LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
		        	return 2;


		        }
			
		}

}
