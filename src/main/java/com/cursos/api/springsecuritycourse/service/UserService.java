package com.cursos.api.springsecuritycourse.service;



import java.util.Optional;

import com.cursos.api.springsecuritycourse.dto.SaveUser;
import com.cursos.api.springsecuritycourse.entity.User;

public interface UserService {

	User registerOneCustomer(SaveUser newUser);

	Optional<User> findOneByUsername (String username);
	
}
