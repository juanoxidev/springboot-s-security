package com.cursos.api.springsecuritycourse.config.security.filter;


import java.io.IOException;
import java.util.Date;
import java.util.Optional;

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

import com.cursos.api.springsecuritycourse.entity.security.JwtToken;
import com.cursos.api.springsecuritycourse.entity.security.User;
import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.repository.security.JwtTokenRepository;
import com.cursos.api.springsecuritycourse.service.UserService;
import com.cursos.api.springsecuritycourse.service.auth.JwtService;

@Component
public class JwtAuthenticationFilter extends  OncePerRequestFilter{

	@Autowired
	private JwtService jwtService;

	@Autowired
	private JwtTokenRepository jwtTokenRepository;

	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1. Extraer token jwt
		String jwt = jwtService.extractJwtFromRequest(request);

		if (jwt == null || !StringUtils.hasText(jwt)) {
			/*
			 * Si el token devuelto es null o no se encuentra en el header, entonces vamos a
			 * hacer que seejecuten todos los demas filtros. Va a llegar un punto en el que
			 * un filtro va a necesitar el Authentication como el AuthenticacionFilter
			 * dentro del Security Context Holder y no lo va a encontrar va a lanzar una
			 * excepcion
			 */
			filterChain.doFilter(request, response);
			/*
			 * Cortamos la ejecucion con un return para que no siga con el resto de los pasos, 
			 * Al ser void retorna el control a quein mando a llamar al metodo actual.
			 */
			return;

		}

		/*
		 * 
		 * 1.2 Obtener token no expirado y valido desde base de datos
		 */

		Optional<JwtToken> token = jwtTokenRepository.findByToken(jwt);

		boolean isValid = validateToken(token);

		if (!isValid) {
			filterChain.doFilter(request, response);
			return;
		}

		/*
		 * 2. Obtener el Subject/Username/Propietario desde el token, esta acccion a su
		 * vez valida el formato del token, firma y fecha de expiracion
		 */
		String username = jwtService.extractUsername(jwt);

		// 3. Setear objeto authentication dentro del security context holder
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
		// 4. Ejecutar el registro de filtros
		filterChain.doFilter(request, response);
	}

	private boolean validateToken(Optional<JwtToken> optionalJwtToken) {

		if (!optionalJwtToken.isPresent()) {

			System.out.println("El token no existe o no fue generado en nuestro sistema");

			return false;
		}

		// obtengo el token
		JwtToken token = optionalJwtToken.get();

		// guardo el momento exacto en el que estoy haciendo una request
		Date now = new Date(System.currentTimeMillis());

		// el token es valido? la fecha de expiracion es posterior a la fecha que estoy
		// haciendo la request?
		boolean isValid = token.isValid() && token.getExpiration().after(now);

		if (!isValid) {
			System.out.println("Token invalido");
			// lo invalido y lo guardo en bd
			updateTokenStatus(token);
		}

		return isValid;

	}

	private void updateTokenStatus(JwtToken token) {

		token.setValid(false);

		jwtTokenRepository.save(token);

	}

}
