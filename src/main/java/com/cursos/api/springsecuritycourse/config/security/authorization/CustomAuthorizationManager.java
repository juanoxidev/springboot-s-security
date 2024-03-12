package com.cursos.api.springsecuritycourse.config.security.authorization;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.cursos.api.springsecuritycourse.entity.security.Operation;
import com.cursos.api.springsecuritycourse.entity.security.User;
import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.repository.security.OperationRepository;
import com.cursos.api.springsecuritycourse.service.UserService;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

	@Autowired 
	private OperationRepository operationRepository;
	
	@Autowired
	private UserService userService;
	
	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {
		HttpServletRequest request = requestContext.getRequest();
		
		//1 .extraemos la url de la request para hacer validaciones sobre ella
		String url = extract(request);
		
		String httpMethod = request.getMethod();
		
		//2. validamos si la url es publica o privada
		
		boolean isPublic = isPublic(url, httpMethod);
		
		// si es publico retornamos una authorizacion true
		if (isPublic) return new AuthorizationDecision(isPublic);
		
		// al no ser publico nos fijamos si tiene los permisos necesarios
		boolean isGranted = isGranted(url, httpMethod, authentication.get());
		
		return new AuthorizationDecision(isGranted);
	}

	/*
	 * Config para Metodos Privados
	 */
	private boolean isGranted(String url, String httpMethod, Authentication authentication) {
		// si no se autentico o no es una instancia de UsernamePasswordAuthenticationToken lanzo una excepcion
		if (authentication == null || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
			throw new AuthenticationCredentialsNotFoundException("User not logged in");
		}
		
		List<Operation> operations = obtainedOperations(authentication);
		
		// Itera sobre la lista y se fija si hay coincidencia.
		boolean isGranted = operations.stream().anyMatch(getOperationPredicate(url, httpMethod));
		
		return isGranted;
	}
	
	// Itera sobre la lista y se fija si hay coincidencia entre el endpoint y los permisos del usuario
	private Predicate<? super Operation> getOperationPredicate(String url, String httpMethod) {
		return operation -> {
			
			String basePath = operation.getModule().getBasePath();
			
			Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
			
			Matcher matcher = pattern.matcher(url);
			
			return matcher.matches() && operation.getHttpMethod().equals(httpMethod); 
			};
	}
	
	private List<Operation> obtainedOperations(Authentication authentication) {
		
		UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
		
		String username = (String) authToken.getPrincipal();
		
		User user = userService.findOneByUsername(username)
					.orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));
		
		// retorno la lista de operaciones autorizadas a hacer del usuario logueado
		return user.getRole().getPermissions().stream()
				.map(grantedPermission -> grantedPermission.getOperation())
				.collect(Collectors.toList());
	}

	/*
	 * Config para Metodos Publicos
	 */

	private boolean isPublic(String url, String httpMethod) {
		// obtener todos los endppoints publicos desde base de datos
		List<Operation> publicAccessEndPoints = operationRepository
												.findByPublicAccess();
		
		// Itera sobre la lista y se fija si hay coincidencia.
		boolean isPublic = publicAccessEndPoints.stream().anyMatch(getOperationPredicate(url, httpMethod));
		
		System.out.println("IS PUBLIC: " + isPublic);
		
		return isPublic;
	}

	private String extract(HttpServletRequest request) {
		
		// extraemos el context path de nuestra app : /api/v1
		String contextPath = request.getContextPath();
		
		// nos retorna el contextpath + pathbase + pathmapping 
		String url = request.getRequestURI();
		
		// a la url le sacamos el path context
		url = url.replace(contextPath, "");
		
		System.out.println(url);
		
		return url;
	}

}
