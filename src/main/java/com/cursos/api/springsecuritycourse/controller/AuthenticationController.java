package com.cursos.api.springsecuritycourse.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired 
	private AuthenticationService authenticationService;

	@ApiOperation(value = "Acá nombramos la operación", notes = "Podemos incluir una descripción más detallada que será útil al cliente")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente"),
			@ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
			@ApiResponse(code = 500, message = "Error inesperado del sistema") })
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

	@CrossOrigin
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
