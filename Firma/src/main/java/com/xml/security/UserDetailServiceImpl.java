package com.xml.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.xml.user.CustomUserDetails;
import com.xml.user.User;
import com.xml.user.UserService;
import com.xml.userbadpassword.UserBadPassword;
import com.xml.userbadpassword.UserBadPasswordService;

@Component
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserService userService;

	
    @Autowired
    private LoginAttemptService loginAttemptService;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserBadPasswordService userBadPasswordService;
	
	private UserBadPassword badPassword;

	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		

		String ip = getClientIP();
		User user = userService.findByUsernameAndCheckIp(username, ip);
		
		if(user == null){
			
			
			if (loginAttemptService.isBlocked(ip)) {
	            throw new RuntimeException("blocked");
	        }
		} else {
			badPassword = userBadPasswordService.findByUserId(user);
			if(badPassword == null){
				badPassword = new UserBadPassword();
				badPassword.setUser(user);
				//int att = badPassword.getAttempts();
				badPassword.setAttempts(1);
				badPassword.setAccessLocked(false);
				userBadPasswordService.save(badPassword);
			} else {
				if(userBadPasswordService.checkAccessLocked(badPassword.getId())){
					throw new RuntimeException("blocked");
				}
				int att = badPassword.getAttempts();
				badPassword.setAttempts(att + 1);
				if(badPassword.getAttempts() >= 3){
					badPassword.setAccessLocked(true);
				}
				badPassword.setUsername(user.getUsername());
				userBadPasswordService.save(badPassword);
			}
			
		}
		
		return new CustomUserDetails(user);
	}
	
	private String getClientIP() {
		String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}

	public UserBadPassword getBadPassword() {
		return badPassword;
	}
	
	

}

