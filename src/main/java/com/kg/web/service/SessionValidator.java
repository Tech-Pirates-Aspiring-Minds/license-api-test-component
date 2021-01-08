package com.kg.web.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import com.kg.web.model.AuditUserData;
import com.kg.web.model.SessionDetail;
import com.kg.web.util.KeyUtil;

@Service
public class SessionValidator implements ISessionValidator {

	public static Set<String> logoutSession = new HashSet<>();

	@Autowired
	private ILicenceService licence;

	@Value("${licence.url:#{null}}")
	private String url;

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	public SessionDetail validateSessionCount() {
		String key = licence.getKey();
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl = url + "/" + KeyUtil.getURIKey(key) + "/user";

		ResponseEntity<SessionDetail> response = restTemplate.getForEntity(resourceUrl, SessionDetail.class);
		if (response.getStatusCode().equals(HttpStatus.OK)) {
			return response.getBody();
		}
		SessionDetail detail = new SessionDetail();
		detail.setValid(true);
		return detail;
	}

	@Override
	public boolean trackLogout() {
		if (!logoutSession.isEmpty()) {
			Iterator<String> it = logoutSession.iterator();
			while (it.hasNext()) {
				String sessionId = it.next();

				String key = licence.getKey();

				AuditUserData reqData = new AuditUserData();
				reqData.setKey(key);
				reqData.setLogIn(false);
				reqData.setSessionId(sessionId);

				RestTemplate restTemplate = new RestTemplate();
				String resourceUrl = url + "/" + KeyUtil.getURIKey(key) + "/user";
				ResponseEntity<Boolean> response = restTemplate.postForEntity(resourceUrl, reqData, Boolean.class);
				if (response.getStatusCode().equals(HttpStatus.OK)) {
					System.out.println("removed session : " + sessionId);
				}

			}
		}

		logoutSession.clear();

		return true;
	}

	@Override
	public boolean trackLogin(String os, String browser, String ipAddress) {
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String sessionID = RequestContextHolder.currentRequestAttributes().getSessionId();

		String key = licence.getKey();

		AuditUserData reqData = new AuditUserData();
		reqData.setKey(key);
		reqData.setLogIn(true);
		reqData.setSessionId(sessionID);
		reqData.setUserId(authUser.getUsername());
		reqData.setBrowser(browser);
		reqData.setOs(os);
		reqData.setIpAddress(ipAddress);

		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl = url + "/" + KeyUtil.getURIKey(key) + "/user";
		ResponseEntity<Boolean> response = restTemplate.postForEntity(resourceUrl, reqData, Boolean.class);
		if (response.getStatusCode().equals(HttpStatus.OK)) {
			return response.getBody();
		}
		return false;
	}

}
