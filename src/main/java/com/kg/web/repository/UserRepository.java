package com.kg.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kg.web.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	List<User> findByStatus(int status);

}
