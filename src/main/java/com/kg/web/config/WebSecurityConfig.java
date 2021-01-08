package com.kg.web.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.kg.web.service.IUserService;
import com.kg.web.util.PasswordUtil;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private IUserService service;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/licence", "/invalidLogin", "/onlogout", "/assets/**").permitAll().anyRequest()
				.authenticated().and().formLogin().successHandler(new LoggedInSuccessHandler()).loginPage("/login")
				.permitAll().and().logout().logoutSuccessHandler(new LoggedOutSuccessHandler()).permitAll()
				.addLogoutHandler(new CustomLogoutHandler());
//				.addFilterBefore(getBeforeAuthenticationFilter(), CustomAuthenticationFilter.class);
		// .addFilterBefore(new CustomFilter(),BasicAuthenticationFilter.class)
//		.addFilterBefore(getBeforeAuthenticationFilter(), CustomAuthenticationFilter.class)
	}

	@SuppressWarnings("deprecation")
	@Bean
	@Override
	public UserDetailsService userDetailsService() {

		List<com.kg.web.model.User> userDetail = service.getAllActiveUsers();

		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		if (userDetail != null && !userDetail.isEmpty()) {
			for (com.kg.web.model.User usr : userDetail) {
				UserDetails user = User.withDefaultPasswordEncoder().username(usr.getUser()).password(PasswordUtil.decrypt(usr.getPassword()))
						.roles(usr.getRole()).build();
				manager.createUser(user);
			}

		} else {
			UserDetails user = User.withDefaultPasswordEncoder().username("charles").password("password123%")
					.roles("ADMIN").build();
			manager.createUser(user);
			UserDetails user1 = User.withDefaultPasswordEncoder().username("admin").password("password1%")
					.roles("ADMIN").build();
			manager.createUser(user1);
		}

		return manager;
	}
	


}
