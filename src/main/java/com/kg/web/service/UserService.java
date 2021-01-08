package com.kg.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kg.web.model.User;
import com.kg.web.repository.UserRepository;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository repository;

	@Override
	public List<User> getAllActiveUsers() {
		return repository.findByStatus(1);
	}

}
