package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Usuario;

public interface IUsuarioService {

	public Usuario save(Usuario usuario);

	public Optional<Usuario> findById(Integer id);

	public void update(Usuario usuario);

	public void delete(Integer id);

	public List<Usuario> findAll();

}
