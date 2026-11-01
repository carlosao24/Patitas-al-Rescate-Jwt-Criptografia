package com.krakedev.jwtCript.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.jwtCript.entidades.Usuario;
import com.krakedev.jwtCript.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	private final UsuarioRepository userRepo;

	public UsuarioService(UsuarioRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}
	
	public Usuario registrar(Usuario user) {
		if(userRepo.findByUsername(user.getUsername()).isPresent()) {
			throw new IllegalArgumentException("El usuario ya existe");
		}
		return userRepo.save(user);
	}
	
	public Optional<Usuario> login(String username, String password){
		Optional<Usuario> usuario = userRepo.findByUsername(username);
		if(usuario.isPresent() && usuario.get().getPassword().equals(password)) {
			return usuario;
		}
		return Optional.empty();
	}
	
}
