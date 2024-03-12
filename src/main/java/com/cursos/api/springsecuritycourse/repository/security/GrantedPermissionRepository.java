package com.cursos.api.springsecuritycourse.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cursos.api.springsecuritycourse.entity.security.GrantedPermission;

@Repository
public interface GrantedPermissionRepository extends JpaRepository<GrantedPermission, Long> {

}
