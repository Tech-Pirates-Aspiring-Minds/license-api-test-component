package com.kg.web.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kg.web.model.SessionDetail;

public interface ISessionValidator {
	
	SessionDetail validateSessionCount();
	
	void logout(HttpServletRequest request, HttpServletResponse response);
	
	boolean trackLogin(String os, String browser, String ipAddress);

	boolean trackLogout();

}
