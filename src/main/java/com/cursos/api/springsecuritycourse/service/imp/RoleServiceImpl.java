package com.cursos.api.springsecuritycourse.service.imp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cursos.api.springsecuritycourse.entity.security.Role;
import com.cursos.api.springsecuritycourse.repository.security.RoleRepository;
import com.cursos.api.springsecuritycourse.service.RoleService;


@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	@Value("${security.default.role}")
	private String defaultRole;
	
	@Override
	public Optional<Role> findDefaultRole() {
		return roleRepository.findByName(defaultRole);
	}

}
