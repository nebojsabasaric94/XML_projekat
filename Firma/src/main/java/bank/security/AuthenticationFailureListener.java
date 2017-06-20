package bank.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent>{
	
	@Autowired
    private LoginAttemptService loginAttemptService;
	WebAuthenticationDetails auth;
 
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        auth = (WebAuthenticationDetails) 
          e.getAuthentication().getDetails();
         
        loginAttemptService.loginFailed(auth.getRemoteAddress());
    }

	public WebAuthenticationDetails getAuth() {
		/*AuthenticationFailureBadCredentialsEvent e;
		auth = (WebAuthenticationDetails) 
		          e.getAuthentication().getDetails();*/
		return auth;
	}

	public void setAuth(WebAuthenticationDetails auth) {
		this.auth = auth;
	}
    
    

}
