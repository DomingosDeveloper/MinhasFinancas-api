package com.ddevolper.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddevolper.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByEmail (String email);
	Optional<Usuario> findByNome (String Nome);
	Optional<Usuario> findByEmailAndNome (String email, String nome);
	Optional<Usuario> findById(long id);
	boolean existsByEmail(String email);
}
