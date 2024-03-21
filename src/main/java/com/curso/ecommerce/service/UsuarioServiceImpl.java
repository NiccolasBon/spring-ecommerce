package com.curso.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private IUsuarioRepository usuRepo;

	@Override
	public Optional<Usuario> findById(Integer id) {
		return usuRepo.findById(id);
	}

	@Override
	public Usuario save(Usuario usuario) {
		return usuRepo.save(usuario);
	}

	@Override
	public Optional<Usuario> findByEmail(String email) {
		return usuRepo.findByEmail(email);
	}

}
