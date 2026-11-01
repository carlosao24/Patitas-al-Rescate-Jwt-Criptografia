package com.krakedev.jwtCript.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.jwtCript.entidades.Usuario;
import com.krakedev.jwtCript.service.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final UsuarioService service;

	public AuthController(UsuarioService service) {
		super();
		this.service = service;
	}
	
	
	@PostMapping("/registrar")
	public ResponseEntity<?> registrar(@RequestBody Usuario user){
		try {
			Usuario userNuevo = service.registrar(user);
//			userNuevo.setPassword(null);
			return ResponseEntity.status(HttpStatus.CREATED).body(userNuevo);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar Usuario");
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Usuario datosLogin){
		Optional<Usuario> usuario = service.login(datosLogin.getUsername(), datosLogin.getPassword());
		if(usuario.isPresent()) {
			Usuario usuarioAutenticado = usuario.get();
			//usuarioAutenticado.setPassword(null);
			return ResponseEntity.ok(usuarioAutenticado);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
		
	}

}
