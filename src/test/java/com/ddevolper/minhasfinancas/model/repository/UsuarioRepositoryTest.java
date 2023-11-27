package com.ddevolper.minhasfinancas.model.repository;


import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ddevolper.minhasfinancas.model.entity.Usuario;
import com.ddevolper.minhasfinancas.model.repository.UsuarioRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)

public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@org.junit.Test
	public void deveVerificarExistenciaDeUmEmail() {
		//cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//ação/execução
		boolean result = repository.existsByEmail("usuario.email@email.com");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@org.junit.Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComEmail() {
		//cenário
		repository.deleteAll();
		
		//ação
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//verificacao
		Assertions.assertThat(result).isFalse();

	}
	
	@org.junit.Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//cenario
		Usuario usuario = criarUsuario();
		
		//acao
		Usuario usuarioSalvo = repository.save(usuario);
		
		//verificacao
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@org.junit.Test
	public void deveBuscarUmUsuarioPorEmail() {
		//cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario); 
		
		//verificacao
		Optional<Usuario> result = repository.findByEmail("usuario.email@email.com");
		Assertions.assertThat(result.isPresent()).isTrue();
		}
	
	@org.junit.Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
		
		//verificacao
		Optional<Usuario> result = repository.findByEmail("usuario.email@email.com");
		Assertions.assertThat(result.isPresent()).isFalse();
		}
	
	public static Usuario criarUsuario() {
		return Usuario.builder()
				.nome("usuario")
				.email("usuario.email@email.com")
				.senha("123")
				.build();
	}
}
