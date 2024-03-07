package com.cursos.api.springsecuritycourse.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cursos.api.springsecuritycourse.dto.auth.AuthenticationRequest;
import com.cursos.api.springsecuritycourse.dto.auth.AuthenticationResponse;
import com.cursos.api.springsecuritycourse.entity.User;
import com.cursos.api.springsecuritycourse.service.auth.AuthenticationService;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired 
	private AuthenticationService authenticationService;
/**
 * Validamos si el token jwt es valido
 * @param String jwt
 * @return Boolean
 */
	@GetMapping("/validate-token")
	public ResponseEntity<Boolean> validate (@RequestParam String jwt){
		boolean isTokenValid = authenticationService.validateToken(jwt);
		return ResponseEntity.ok(isTokenValid);
	}
	/**
	 * Es el controlador que autentifica al usuario y en caso de poder loguearse devuelve el jwt
	 * @param AuthenticationRequest
	 * @return AuthenticationResponse
	 */
	@PostMapping ("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate (
			@RequestBody @Valid AuthenticationRequest authenticationRequest) {
		
		AuthenticationResponse rsp = authenticationService.login(authenticationRequest);
		
		return ResponseEntity.ok(rsp);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<User> findMyProfile(){
		User user = authenticationService.findLoggedInUser();
		return ResponseEntity.ok(user);
	}
}
