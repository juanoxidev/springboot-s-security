package com.cursos.api.springsecuritycourse.service.imp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cursos.api.springsecuritycourse.dto.SaveUser;
import com.cursos.api.springsecuritycourse.entity.User;
import com.cursos.api.springsecuritycourse.exception.InvalidPasswordException;
import com.cursos.api.springsecuritycourse.repository.UserRepository;
import com.cursos.api.springsecuritycourse.service.UserService;

import util.Role;

@Service

public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User registerOneCustomer(SaveUser newUser) {
		validatePassword(newUser);
		User user = new User();
		
		user.setName(newUser.getName());
		user.setRole(Role.ROLE_CUSTOMER);
		user.setUsername(newUser.getUsername());
		user.setPassword(passwordEncoder.encode(newUser.getPassword()));
		return userRepository.save(user);
	}

	private void validatePassword(SaveUser newUser) {
		if (!StringUtils.hasText(newUser.getPassword()) || !StringUtils.hasText(newUser.getRepeatedPassword())) {
			throw new InvalidPasswordException("Passwords dont match");	
		}
		
		if (!newUser.getPassword().equals(newUser.getRepeatedPassword())) {
			throw new InvalidPasswordException("Passwords dont match");	
		}
	}

	@Override
	public Optional<User> findOneByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	

}
