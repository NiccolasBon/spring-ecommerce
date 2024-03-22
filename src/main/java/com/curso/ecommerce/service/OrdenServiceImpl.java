package com.curso.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {

	@Autowired
	private IOrdenRepository ordenRepo;

	@Override
	public Orden save(Orden orden) {
		return ordenRepo.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		return ordenRepo.findAll();
	}

	@Override
	public String generarNumOrden() {
		int numero = 0;
		String numeroConcat = "";

		List<Orden> listaOrdenes = this.findAll();

		List<Integer> listaNumeros = new ArrayList<Integer>();

		listaOrdenes.stream().forEach(o -> listaNumeros.add(Integer.parseInt(o.getNumero())));

		if (listaOrdenes.isEmpty()) {
			numero = 1;
		} else {
			numero = listaNumeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		if(numero < 10) { //
			numeroConcat = "000000000" + String.valueOf(numero);
		} else if(numero < 100) {
			numeroConcat = "00000000" + String.valueOf(numero);
		} else if(numero < 1000) {
			numeroConcat = "0000000" + String.valueOf(numero);
		} else if(numero < 10000) {
			numeroConcat = "000000" + String.valueOf(numero);
		}
		
		return numeroConcat;
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		
		return ordenRepo.findByUsuario(usuario);
	}

}
