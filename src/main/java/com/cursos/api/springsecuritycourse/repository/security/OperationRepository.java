package com.cursos.api.springsecuritycourse.repository.security;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cursos.api.springsecuritycourse.entity.security.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long> {
	
	@Query("SELECT o FROM Operation o where o.permitAll = true")
	List<Operation> findByPublicAccess();

	Optional<Operation> findByName(String operation);

}
