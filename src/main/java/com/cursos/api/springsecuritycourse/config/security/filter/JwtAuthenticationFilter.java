package com.cursos.api.springsecuritycourse.config.security.filter;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cursos.api.springsecuritycourse.entity.User;
import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.service.UserService;
import com.cursos.api.springsecuritycourse.service.auth.JwtService;

@Component
public class JwtAuthenticationFilter extends  OncePerRequestFilter{

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//1. Obtener encabezado HTTP llamado Authorization
		
		String authorizationHeader = request.getHeader("Authorization");
		
		if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
			/*
			 * Si el token comienza con bearer o no se encuentra en el header, entonces vamos a hacer que seejecuten todos
			 * los demas filtros. Va a llegar un punto en el que un filtro va a necesitar el Authentication dentro del
			 * Security Context Holder y no lo va a encontrar va a dar error
			 */
			filterChain.doFilter(request, response);
			/*
			 * Cortamos la ejecucion con un return para que no siga con el resto de los pasos, 
			 * Al ser void retorna el control a quein mando a llamar al metodo actual.
			 */
			return;
			
		}
		//2. Obtener token JWT desde el encabezado pos 0 = Bearer pos 1 = jwt
		String jwt = authorizationHeader.split(" ")[1];
		
		/*3. Obtener el Subject/Username/Propietario desde el token, 
		*	 esta acccion a su vez valida el formato del token, firma y fecha de expiracion */
		String username = jwtService.extractUsername(jwt);
		
		//4. Setear objeto authentication dentro del security context holder
		User user = userService.findOneByUsername(username)
									.orElseThrow(()-> new ObjectNotFoundException("User not found. Username: " + username));
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
		/*
		 * puedo agregarle detalles al token
		 * WebAuthenticationDetails: recibe un request,
		 * tiene como paramrtros la IP y la sessionID si estuvieramos usando sessiones.
		 */
		authToken.setDetails(new WebAuthenticationDetails(request));
		
		SecurityContextHolder.getContext().setAuthentication(authToken);
		//5. Ejecutar el registro de filtros
		filterChain.doFilter(request, response);
	}

}
