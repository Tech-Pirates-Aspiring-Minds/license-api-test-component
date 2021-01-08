package com.kg.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kg.web.service.ILicenceService;

@Component
//@Order(1)
public class TransactionFilter implements Filter {

	@Autowired
	private ILicenceService licence;

	@Value("${licence.url:#{null}}")
	private String url;

	@Override
	public void doFilter(ServletRequest request, javax.servlet.ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String uri = req.getRequestURI();
//		System.out.println(uri);
		if (licence.isValid() || uri.contains("licence") || uri.startsWith("/assets")) {
			chain.doFilter(request, response);
		} else {
			uri = "/licence";
			RequestDispatcher dispatcher = request.getRequestDispatcher(uri);
			dispatcher.forward(request, response);
		}

	}

}