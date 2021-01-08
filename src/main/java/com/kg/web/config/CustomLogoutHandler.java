package com.kg.web.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.kg.web.service.SessionValidator;

@Component("CustomLogoutHandler")
public class CustomLogoutHandler implements LogoutHandler {
	
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String sessionID = RequestContextHolder.currentRequestAttributes().getSessionId();
		System.out.println("logout handler : " + sessionID);
		SessionValidator.logoutSession.add(sessionID);
    }
}
