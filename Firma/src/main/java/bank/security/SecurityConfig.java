package bank.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailServiceImpl userDetailsService;

	@Autowired
	AuthFailure authFailure;

	@Autowired
	AuthSuccess authSuccess;
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.httpBasic().
			and().
				authorizeRequests().antMatchers("/login.html", "/forgotPassword.html", "/forgotPasswordMail/*").permitAll().anyRequest().authenticated().
			and().
				formLogin().loginPage("/login.html").defaultSuccessUrl("/#/home", true).failureUrl("/login.html?error=true").permitAll().
			and().
				logout().logoutSuccessUrl("/login.html").
			and().
				sessionManagement().invalidSessionUrl("/").
			and().
				csrf().disable().exceptionHandling().
			and().
				headers().xssProtection();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	


}