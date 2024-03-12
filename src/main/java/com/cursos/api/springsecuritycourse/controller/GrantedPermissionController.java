package com.cursos.api.springsecuritycourse.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cursos.api.springsecuritycourse.dto.SaveGrantedPermission;
import com.cursos.api.springsecuritycourse.dto.ShowGrantedPermission;
import com.cursos.api.springsecuritycourse.service.GrantedPermissionService;

@RestController
@RequestMapping("/gpermission")
public class GrantedPermissionController {
// gestor de roles y sus permisos 
	
	@Autowired
	private GrantedPermissionService grantedPermissionService;

	@GetMapping()
	public ResponseEntity<Page<ShowGrantedPermission>> findAll(Pageable pageable) {
		Page<ShowGrantedPermission> permissions = grantedPermissionService.findAll(pageable);

		if (permissions.hasContent()) {
			return ResponseEntity.ok(permissions);
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping("/{permissionId}")
	public ResponseEntity<ShowGrantedPermission> findOneByID(@PathVariable Long permissionId) {
		Optional<ShowGrantedPermission> gpermission = grantedPermissionService.findOneById(permissionId);

		if (gpermission.isPresent()) {
			return ResponseEntity.ok(gpermission.get());
		}

		return ResponseEntity.notFound().build();

	}

	@PostMapping()
	public ResponseEntity<ShowGrantedPermission> createOne(@RequestBody @Valid SaveGrantedPermission savePermission) {
		
		ShowGrantedPermission gpermission = grantedPermissionService.createOne(savePermission);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(gpermission);
	}

	@DeleteMapping("/{permissionId}")
	public ResponseEntity<ShowGrantedPermission> deleteOneById(@PathVariable Long permissionId) {
		
		ShowGrantedPermission gpermission = grantedPermissionService.deleteOneById(permissionId);
		
		return ResponseEntity.ok(gpermission);
	}

}
