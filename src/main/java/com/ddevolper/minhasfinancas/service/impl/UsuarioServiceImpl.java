package com.ddevolper.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddevolper.minhasfinancas.exception.ErroAutenticacao;
import com.ddevolper.minhasfinancas.exception.RegraNegocioException;
import com.ddevolper.minhasfinancas.model.entity.Usuario;
import com.ddevolper.minhasfinancas.service.UsuarioService;
import com.ddevolper.minhasfinancas.model.repository.UsuarioRepository;
@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	private UsuarioRepository repository;
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
		throw new ErroAutenticacao("Senha incorreta.");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if (existe) {
			throw new RegraNegocioException("Já existe um usuario com esse e-mail.");
		}
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		
		return repository.findById(id);
	}

}
