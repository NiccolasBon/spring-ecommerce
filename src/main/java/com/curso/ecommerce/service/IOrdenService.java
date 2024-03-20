package com.curso.ecommerce.service;

import java.util.List;

import com.curso.ecommerce.model.Orden;

public interface IOrdenService {

	public Orden save(Orden orden);

	public List<Orden> findAll();

}
