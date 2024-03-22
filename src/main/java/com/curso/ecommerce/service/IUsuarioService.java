package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Usuario;

public interface IUsuarioService {

	public Optional<Usuario> findById(Integer id);
	public Usuario save(Usuario usuario);
	public Optional<Usuario> findByEmail(String email);
	public List<Usuario> findAll();

}
