package com.krakedev.jwtCript.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.krakedev.jwtCript.entidades.Usuario;
import com.krakedev.jwtCript.service.UsuarioService;
import com.krakedev.jwtCript.utils.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UsuarioService service;

	public AuthController(UsuarioService service) {
		super();
		this.service = service;
	}

	@PostMapping("/registrar")
	public ResponseEntity<?> registrar(@RequestBody Usuario user) {
		try {
			Usuario userNuevo = service.registrar(user);
//			userNuevo.setPassword(null);
			return ResponseEntity.status(HttpStatus.CREATED).body(userNuevo);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar Usuario");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Usuario datosLogin) {
		Optional<Usuario> usuario = service.login(datosLogin.getUsername(), datosLogin.getPassword());
		if (usuario.isPresent()) {
			Usuario usuarioAutenticado = usuario.get();
			// usuarioAutenticado.setPassword(null);
			String token = JwtUtil.generarToken(usuarioAutenticado.getUsername(), usuarioAutenticado.getRol());

			return ResponseEntity.ok(java.util.Map.of("token", token, "tipo", "Bearer", "username",
					usuarioAutenticado.getUsername(), "rol", usuarioAutenticado.getRol()));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");

	}

	@GetMapping("/perfil")
	public ResponseEntity<?> verPerfil(@RequestHeader(value = "Authorization", required = false) String authHeader) {
		try {
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("Acceso Denegado: Debes proveer un token Bearer valido en la cabecera Authorization");

			}

			String token = authHeader.substring(7);

			DecodedJWT datosToken = JwtUtil.validarToken(token);

			if (datosToken == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado token denegado o expirado");
			}

			String usuario = datosToken.getSubject();
			String rol = datosToken.getClaim("rol").asString();

			String mensaje;

			if ("ADMIN".equals(rol)) {
				mensaje = "Bienvenido voluntario " + usuario + ". Tienes acceso de administrador.";
			} else if ("USER".equals(rol)) {
				mensaje = "Biemvenido adoptante " + usuario + ". Puedes consultar tu perfil";
			} else {
				mensaje = "Bienvenido " + usuario;
			}

			return ResponseEntity.ok(java.util.Map.of("username", usuario, "rol", rol, "mensaje", mensaje));
		} catch (com.auth0.jwt.exceptions.JWTVerificationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalido o no autorizado");
		}

	}

}
