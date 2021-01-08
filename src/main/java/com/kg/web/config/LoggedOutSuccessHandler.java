package com.kg.web.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Component("loggedOutSuccessHandler")
public class LoggedOutSuccessHandler implements LogoutSuccessHandler {
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		if (session != null) {
			session.removeAttribute("user");
		}
		
        response.setStatus(HttpServletResponse.SC_OK);
        
        //since we have created our custom success handler, its up to us to where
        //we will redirect the user after successfully login
        response.sendRedirect("onlogout");
	}

}