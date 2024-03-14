package com.cursos.api.springsecuritycourse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/react")
public class PruebaReactController {

	@GetMapping("/hola")
	public ResponseEntity<String> bienvenido(){
		String respuesta = "Probando React c/Spring en el mismo puerto";
		return ResponseEntity.ok(respuesta);
		
	}
}
