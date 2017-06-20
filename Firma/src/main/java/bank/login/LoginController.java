package bank.login;

import java.security.Principal;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mifmif.common.regex.Generex;

import bank.user.User;
import bank.user.UserService;

@RestController
@RequestMapping
public class LoginController {

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	UserService userService;

	@Autowired
	private JavaMailSender javaMailSender;

	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5, new SecureRandom());

	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}

	@GetMapping("/forgotPasswordMail/{credentials}")
	public void forgotPasswordMail(@PathVariable String credentials) {
		// UserDetails user =
		// userDetailsService.loadUserByUsername(credentials.getUsername());
		User user = userService.findByUsername(credentials);

		Generex regex = new Generex("([0-9]{1,}[a-z]{1,}[A-Z]{1,}){3,}");
		String newPassword = regex.random();
		// System.out.println("Nova sifra " + newPassword);
		user.setPassword(encoder.encode(newPassword));
		userService.save(user);
		try {
			SimpleMailMessage email = new SimpleMailMessage();
			email.setTo("choda.94@gmail.com");// umesto ovoga guest.mail..ako
												// neces da testiras
			email.setFrom("isarestorani2@gmail.com");
			email.setSubject("New password");
			email.setText("Your new password is " + newPassword);
			logger.info("User " + user.getUsername() + " forgot password and his password is changed.");
			javaMailSender.send(email);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
