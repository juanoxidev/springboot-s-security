package com.cursos.api.springsecuritycourse.repository.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cursos.api.springsecuritycourse.entity.security.JwtToken;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

	Optional<JwtToken> findByToken(String jwt);

}
