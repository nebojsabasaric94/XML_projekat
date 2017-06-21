package com.xml.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.xml.userbadpassword.UserBadPasswordService;

@Component
public class AuthSuccess implements ApplicationListener<AuthenticationSuccessEvent> {
	
	@Autowired
	private LoginAttemptService loginAttemptService;
	
	@Autowired
	UserDetailServiceImpl userDetails;
	
	@Autowired
	private UserBadPasswordService userBadPasswordService;

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent e) {
		 final WebAuthenticationDetails auth = (WebAuthenticationDetails) e.getAuthentication().getDetails();
	        if (auth != null) {
	            loginAttemptService.loginSucceeded(auth.getRemoteAddress());
	        }
		
		userBadPasswordService.delete(userDetails.getBadPassword().getId());
		
		
	}
	
	
}

