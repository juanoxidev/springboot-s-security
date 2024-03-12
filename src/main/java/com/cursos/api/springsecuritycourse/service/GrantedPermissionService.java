package com.cursos.api.springsecuritycourse.service;

import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cursos.api.springsecuritycourse.dto.SaveGrantedPermission;
import com.cursos.api.springsecuritycourse.dto.ShowGrantedPermission;


public interface GrantedPermissionService {

	Page<ShowGrantedPermission> findAll(Pageable pageable);

	ShowGrantedPermission deleteOneById(Long permissionId);

	ShowGrantedPermission createOne(SaveGrantedPermission savePermission);

	Optional<ShowGrantedPermission> findOneById(Long permissionId);
	
}
