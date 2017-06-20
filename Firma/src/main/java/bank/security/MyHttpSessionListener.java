package bank.security;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

@Component
public class MyHttpSessionListener implements HttpSessionListener {
	public void sessionCreated(HttpSessionEvent event) {
		event.getSession().setMaxInactiveInterval(30 * 60); // in seconds
	}

	public void sessionDestroyed(HttpSessionEvent event) {
	}
}
