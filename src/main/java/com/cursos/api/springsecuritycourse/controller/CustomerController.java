package com.cursos.api.springsecuritycourse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cursos.api.springsecuritycourse.dto.RegisteredUser;
import com.cursos.api.springsecuritycourse.dto.SaveUser;
import com.cursos.api.springsecuritycourse.service.auth.AuthenticationService;

import javax.validation.Valid;


@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired 
	private AuthenticationService authenticationService;
	
	@PostMapping
	public ResponseEntity<RegisteredUser> registerOne(@RequestBody @Valid SaveUser newUser) {
		// lo guarda en la bd, si no lo logra guardar va a devolver una excepcion, nunca es nulo.
		RegisteredUser registeredUser = authenticationService.registerOneCustomer(newUser);
		return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
	}
}
