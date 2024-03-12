package com.cursos.api.springsecuritycourse.repository.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cursos.api.springsecuritycourse.entity.security.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(String defaultRole);

}
