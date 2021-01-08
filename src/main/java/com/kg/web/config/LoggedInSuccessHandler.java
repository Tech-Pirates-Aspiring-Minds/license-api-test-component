package com.kg.web.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Component("loggedInSuccessHandler")
@Configuration
public class LoggedInSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		HttpSession session = request.getSession(false);

		// HttpSession session = request.getSession();
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		session.setAttribute("username", authUser.getUsername());
		session.setAttribute("authorities", authentication.getAuthorities());

		String sessionID = RequestContextHolder.currentRequestAttributes().getSessionId();
		 String userAgent = request.getHeader("User-Agent");

		System.out.println("login success handler : " + sessionID);
		// set our response to OK status
		UserAgent detail = getUserAgentDetail(userAgent);
		response.setStatus(HttpServletResponse.SC_OK);
		String ipAddress = request.getParameter("client_detail");
				
//		response.sendRedirect("home");
		session.setAttribute("os", detail.os);
		session.setAttribute("browser", detail.browser);
		session.setAttribute("ip_address", ipAddress);
		response.sendRedirect("validate");

	}
	
	class UserAgent{
		String os;
		String browser;
	}
	
	private UserAgent getUserAgentDetail(String userAgent) {
        String  user            =   userAgent.toLowerCase();

        String os = "";
        String browser = "";

        System.out.println("User Agent for the request is===>"+userAgent);
        //=================OS=======================
         if (userAgent.toLowerCase().indexOf("windows") >= 0 )
         {
             os = "Windows";
         } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
         {
             os = "Mac";
         } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
         {
             os = "Unix";
         } else if(userAgent.toLowerCase().indexOf("android") >= 0)
         {
             os = "Android";
         } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
         {
             os = "IPhone";
         }else{
             os = "UnKnown, More-Info: "+userAgent;
         }
         //===============Browser===========================
        if (user.contains("msie"))
        {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera"))
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            else if(user.contains("opr"))
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
        } else if (user.contains("chrome"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
        {
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            browser = "Netscape-?";

        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
        } else
        {
            browser = "UnKnown, More-Info: "+userAgent;
        }
        System.out.println("Operating System======>"+os);
        System.out.println("Browser Name==========>"+browser);
        
        UserAgent agentDetail = new UserAgent();
        
        agentDetail.browser = browser;
        agentDetail.os = os;
        return agentDetail;
	}
	
}
