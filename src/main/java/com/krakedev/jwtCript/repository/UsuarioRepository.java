package com.krakedev.jwtCript.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krakedev.jwtCript.entidades.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
	Optional<Usuario> findByUsername(String username);
	
	boolean existsByUsername(String username);
	
	
}
