package com.cursos.api.springsecuritycourse.entity;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import util.Role;
/*
 * 
 */
@Entity
@Data
@Table(name = "\"user\"")

public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true)
	private String username;
	
	private String name;
	
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	

	/*
	 * Devuelve una coleccion con los permisos concedidos segun el rol que tengo
	 * Necesito convertir los permisos del rol, em una coleccion de objetos que implementen GrantedAuhority
	 * Ej.SimpleGrantedAuthority
	 */
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (role == null) return null;
		
		if (role.getPermissions() == null) return null;
		//repasar stream y lambda java
		return role.getPermissions().stream()
		.map(each -> each.name())
		.map(each -> new SimpleGrantedAuthority(each))
		.collect(Collectors.toList());
/*			
 * 		Otra forma de hacerlo con 1 solo map
//		return role.getPermissions().stream()
//		.map(each -> {
//			String permission = each.name();
//			return new SimpleGrantedAuthority(permission);
//		})
//		.collect(Collectors.toList());
 * 
 */
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
