package com.curso.ecommerce.service;

import java.util.List;
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
	public Usuario save(Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Usuario> findById(Integer id) {
		return usuRepo.findById(id);
	}

	@Override
	public void update(Usuario usuario) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Usuario> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
