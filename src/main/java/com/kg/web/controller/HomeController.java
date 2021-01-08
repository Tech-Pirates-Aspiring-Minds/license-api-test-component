package com.kg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kg.web.model.Licence;
import com.kg.web.model.SessionDetail;
import com.kg.web.service.ILicenceService;
import com.kg.web.service.ISessionValidator;

@Controller
public class HomeController {

	@Autowired
	private ILicenceService licence;

	@Autowired
	private ISessionValidator validator;

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String home(HttpServletRequest request, HttpServletResponse response) {
		return "redirect:home";
	}

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response) {
		return "/login";
	}

	@RequestMapping(value = "/licence", method = RequestMethod.GET)
	public String licence(Model model) {
		model.addAttribute("message", "");
		model.addAttribute("licenceData", new Licence());
		return "/licence";
	}

	@RequestMapping(value = "/licence", method = RequestMethod.POST)
	public String register(@ModelAttribute Licence licenceData, Model model) {

		boolean valid = licence.register(licenceData);
		if (valid) {
			return "/login";
		} else {
			model.addAttribute("message", "Invalid Key");
			model.addAttribute("licenceData", new Licence());
			return "/licence";
		}
	}

	@RequestMapping(value = "/invalidLogin", method = RequestMethod.GET)
	public String invalidLogin(Model model) {
		model.addAttribute("message", "User sessions reached Maximum level!, We can't allow more than that. Please try after some time");
		return "/invalid";
	}

	@RequestMapping(value = "/validate", method = RequestMethod.GET)
	public String validateLimit(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(false);
		String os = (String) session.getAttribute("os");
		String browser = (String) session.getAttribute("browser");
		String ipAddress = (String) session.getAttribute("ip_address");
		session.removeAttribute("os");
		session.removeAttribute("browser");
		session.removeAttribute("ip_address");
		
		SessionDetail sessionDetail = validator.validateSessionCount();
		if (sessionDetail.isValid()) {
			validator.trackLogin(os, browser, ipAddress);
			return "redirect:home";
		}
		validator.logout(request, response);
		return "redirect:invalidLogin";
	}

	@RequestMapping(value = "/onlogout", method = RequestMethod.GET)
	public String onLogout(HttpServletRequest request, HttpServletResponse response) {
		validator.trackLogout();
		return "redirect:login";
	}
}
