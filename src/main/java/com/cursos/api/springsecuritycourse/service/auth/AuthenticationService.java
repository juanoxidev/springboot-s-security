package com.cursos.api.springsecuritycourse.service.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cursos.api.springsecuritycourse.dto.RegisteredUser;
import com.cursos.api.springsecuritycourse.dto.SaveUser;
import com.cursos.api.springsecuritycourse.dto.auth.AuthenticationRequest;
import com.cursos.api.springsecuritycourse.dto.auth.AuthenticationResponse;
import com.cursos.api.springsecuritycourse.entity.security.User;
import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.service.UserService;




@Service
public class AuthenticationService {

	
	@Autowired 
	private UserService userService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public RegisteredUser registerOneCustomer(SaveUser newUser) {
		User user = userService.registerOneCustomer(newUser);
		RegisteredUser userDTO = new RegisteredUser();
		userDTO.setId(user.getId());
		userDTO.setName(user.getName());
		userDTO.setUsername(user.getUsername());
		userDTO.setRole(user.getRole().getName());
		String jwt = jwtService.generateToken(user, generateExtraClaims(user));
		userDTO.setJwt(jwt);
		return userDTO;
	}

	private Map<String, Object> generateExtraClaims(User user) {
		Map<String,Object> extraClaims = new HashMap<>();
		extraClaims.put("name", user.getName());
		extraClaims.put("role", user.getRole().getName());
		extraClaims.put("authorities", user.getAuthorities());
		return extraClaims;
		
	}

	public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
		/* 
		 * SecurityContextHolder tiene un contexto de seguridad que adentro tiene el authentication, 
			cuando no estamos logueados sive como input
			SecurityContextHolder.getContext().getAuthentication();
			buscamos una implementacion de ese authentication que me beneficie para mandarselo
			al authenticationManager. en este caso UsernamePasswordAuthenticationToken
			al que le pasamos el username y el passqord de la authenticationRequest
		 */		
		Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		/*
		 * el metodo authenticate va a buscar un proveedor que le resuelva a ese usernametoken 
		 * en este caso el Dao el cual configuramos en el AuthenticationProvider del Security 
		 * Bean Injector y en el HttpSecurityConfig
		 */
		
		/*
		 * 
		 */
		authenticationManager.authenticate(authentication);
		/*
		 * Obtenemos los detalles de este user, este es un optional que siempre va a estar si yo logre pasar a esta linea es 
		 * poque la autentificacion es exitosa. es decir que existe, en caso de no existir lanza una excepcion pidiendo
		 * que se autentifique correctamente.
		 */
		UserDetails user = userService.findOneByUsername(authenticationRequest.getUsername()).get();
		
		User userForRole = (User) user;
		/*
		 * Creo el token para enviarselo en el auth Response.
		 */
		String jwt = jwtService.generateToken(user, generateExtraClaims((User) user)); 
		
		AuthenticationResponse authResponse = new AuthenticationResponse();
		
		authResponse.setJwt(jwt);
		authResponse.setRole(userForRole.getRole().getName());
		
		return authResponse;
	}

/**
 * Valida que el formato del token sea correcto, pasar de base 64 debe devolver un json equivalente al header, al payload, valida que la firma coincida, que el token no haya expirado.
 * @param String jwt
 * @return boolean
 */
	public boolean validateToken(String jwt) {
		try {
		jwtService.extractUsername(jwt);
		return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

public User findLoggedInUser() {
	
	/*
	 * No tenemos que preguntar si es una instancia si solo manejamos un tipo de Authentificacion
	 * Ya sabemos cual es el tipo de authentificacion que implementamos, en caso de ser mas de una forma
	 * si hay que usar el instanceof.
	 */
	
	/*
	 * Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	 * A partir de JAVA16 se puede hacer el downcasting directamente en la declaracion de instance of 
	 * if (auth instanceof UsernamePasswordAuthenticationToken authToken) {}
	 */
	/* if (auth instanceof UsernamePasswordAuthenticationToken) {
	UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) auth;
	*/
		/*
		 * Cuando seteamos el UsernamePasswordAuthenticationToken en JwtAuthenticationFilter 
		 * indicamos que el principal era un String que contenia el username
		 */
	
		UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		String username = (String) authToken.getPrincipal();
		return userService.findOneByUsername(username)
				.orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));
	}
	


}
