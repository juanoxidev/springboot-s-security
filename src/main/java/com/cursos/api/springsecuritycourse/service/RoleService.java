package com.cursos.api.springsecuritycourse.service;

import java.util.Optional;

import com.cursos.api.springsecuritycourse.entity.security.Role;

public interface RoleService {

	Optional<Role> findDefaultRole();

}
