package com.cursos.api.springsecuritycourse.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.repository.security.UserRepository;

@Configuration
public class SecurityBeanInjector {
	
	@Autowired
	private UserRepository userRepository; 

	/* 
	 * Inyectamos el AuthenticationConfiguration como parametro del metodo que tiene el Bean.
	 * AuthenticationConfiguration tiene el AuthenticationManager el cual posee 
	 * el metodo Authenticate que recibe por parametro un Authentication tiene dentro principal, credenciales y los autorithies  (cuando estoy logeado) 
	 * o solo va a tener el principal (usuario) y la contrasenia (credentials) (cuando estoy en el proceso de autentificacion) con eso va a intentar hacer el logeo mediante una estrategia.
	 * El providerManager es la implementacion del AuthenticationManager, recorre en un ciclo for todas las estrategias de login hasta llegar a uno que matchee (metodo supports) con el tipo de
	 * logeo que intento hacer, a traves del metodo authenticate. 
	 * authenticate (Authentication authentication).
	 */
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	/*
	 * Creamos nuestra estrategia de autentificacion, el Authetication Provider basico, en este caso usaremos el 
	 * DaoAuthenticationProvider que necesita un PasswordEncoder, ya que las pass en la bd estan encriptadas y cuando el 
	 * proceso de ejecucion se ejecute voy a recibir la contrasenia desde el frontEnd sin encriptar.
	 * PasswordEncoder tienen un metodo match para saber si una contrasenia coincide con otra contrasenia (esta ultima encriptada)
	 * Ademas necesita del UserDetailsService que tiene un metodo loadUserByUsername, el cual recibe un string con el nombre de usuario 
	 * y va a la BD para obtenerlo para comparar la contrasenia enviada desde el frontend y la guardada.
	 */
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationStrategy = new DaoAuthenticationProvider();
		authenticationStrategy.setPasswordEncoder(passwordEncoder());
		authenticationStrategy.setUserDetailsService(userDetailsService());
		return authenticationStrategy;
	}
	
	/*
	 * La implementacion mas usada es BCryptPasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		
		return (username) -> {
			return userRepository.findByUsername(username)
					.orElseThrow(() -> new ObjectNotFoundException("User not found with username: " + username));
		};
/*
 * Otra forma de hacerlo		
 */
//		return new UserDetailsService() {
//			
//			@Override
//			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//				return null;
//			}
//		};
	}
	
}
