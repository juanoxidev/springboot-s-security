package com.cursos.api.springsecuritycourse.service.imp;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cursos.api.springsecuritycourse.dto.SaveGrantedPermission;
import com.cursos.api.springsecuritycourse.dto.ShowGrantedPermission;
import com.cursos.api.springsecuritycourse.entity.security.GrantedPermission;
import com.cursos.api.springsecuritycourse.entity.security.Operation;
import com.cursos.api.springsecuritycourse.entity.security.Role;
import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.repository.security.GrantedPermissionRepository;
import com.cursos.api.springsecuritycourse.repository.security.OperationRepository;
import com.cursos.api.springsecuritycourse.repository.security.RoleRepository;
import com.cursos.api.springsecuritycourse.service.GrantedPermissionService;

@Service
public class GrantedPermissionServiceImpl implements GrantedPermissionService {

	@Autowired
	private GrantedPermissionRepository grantedPermissionRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private OperationRepository operationRepository;

	@Override
	public Page<ShowGrantedPermission> findAll(Pageable pageable) {
		return grantedPermissionRepository.findAll(pageable).map(this::mapEntityToShowDto);
	}

	private ShowGrantedPermission mapEntityToShowDto(GrantedPermission grantedPermission) {
		if (grantedPermission == null)
			return null;

		ShowGrantedPermission showDto = new ShowGrantedPermission();
		showDto.setId(grantedPermission.getId());
		showDto.setRole(grantedPermission.getRole().getName());
		showDto.setOperation(grantedPermission.getOperation().getName());
		showDto.setHttpMethod(grantedPermission.getOperation().getHttpMethod());
		showDto.setModule(grantedPermission.getOperation().getModule().getName());

		return showDto;
	}

	@Override
	public Optional<ShowGrantedPermission> findOneById(Long permissionId) {
		return grantedPermissionRepository.findById(permissionId).map(this::mapEntityToShowDto);
	}

	@Override
	public ShowGrantedPermission createOne(SaveGrantedPermission saveGrantedPermission) {

		GrantedPermission newPermission = new GrantedPermission();

		Operation operation = operationRepository.findByName(saveGrantedPermission.getOperation())
				.orElseThrow(() -> new ObjectNotFoundException(
						"Operation not found. Operation: " + saveGrantedPermission.getOperation()));

		newPermission.setOperation(operation);

		Role role = roleRepository.findByName(saveGrantedPermission.getRole()).orElseThrow(
				() -> new ObjectNotFoundException("Role not found. Role: " + saveGrantedPermission.getRole()));
		newPermission.setRole(role);

		grantedPermissionRepository.save(newPermission);
		return this.mapEntityToShowDto(newPermission);
	}

	@Override
	public ShowGrantedPermission deleteOneById(Long permissionId) {
		GrantedPermission permission = grantedPermissionRepository.findById(permissionId)
				.orElseThrow(() -> new ObjectNotFoundException("Permission not found. Permission: " + permissionId));

		grantedPermissionRepository.delete(permission);

		return this.mapEntityToShowDto(permission);
	}

}
