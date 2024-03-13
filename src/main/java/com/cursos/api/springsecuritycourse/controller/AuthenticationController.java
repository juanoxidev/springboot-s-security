package com.cursos.api.springsecuritycourse.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cursos.api.springsecuritycourse.dto.LogoutResponse;
import com.cursos.api.springsecuritycourse.dto.auth.AuthenticationRequest;
import com.cursos.api.springsecuritycourse.dto.auth.AuthenticationResponse;
import com.cursos.api.springsecuritycourse.entity.security.User;
import com.cursos.api.springsecuritycourse.service.auth.AuthenticationService;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired 
	private AuthenticationService authenticationService;
	/**
	 * Valida el token jwt
	 * @param String
	 * @return Boolean
	 */

	@GetMapping("/validate-token")
	@PreAuthorize("permitAll")
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
	@PreAuthorize("permitAll")
	public ResponseEntity<AuthenticationResponse> authenticate (
			@RequestBody @Valid AuthenticationRequest authenticationRequest) {

		AuthenticationResponse rsp = authenticationService.login(authenticationRequest);

		return ResponseEntity.ok(rsp);
	}

	@PostMapping("/logout")
	public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {

		authenticationService.logout(request);

		return ResponseEntity.ok(new LogoutResponse("Logout exitoso"));
	}

	@PreAuthorize("hasAuthority('READ_MY_PROFILE')")
	@GetMapping("/profile")
	public ResponseEntity<User> findMyProfile(){
		User user = authenticationService.findLoggedInUser();
		return ResponseEntity.ok(user);
	}
}
