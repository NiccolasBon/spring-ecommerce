package com.curso.ecommerce.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IProductoService;
import com.curso.ecommerce.service.IUsuarioService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {

	private final Logger log = LoggerFactory.getLogger(AdministradorController.class);

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@GetMapping("")
	public String home(Model model) {

		List<Producto> listaProductos = productoService.findAll();
		model.addAttribute("listaProductos", listaProductos);

		return "administrador/home";
	}

	@GetMapping("/usuarios")
	public String mostrarUsuarios(Model model) {
		List<Usuario> listaUsuarios = usuarioService.findAll();

		model.addAttribute("listaUsuarios", listaUsuarios);

		return "administrador/usuarios";
	}

	@GetMapping("/ordenes")
	public String mostrarOrdenes(Model model) {
		List<Orden> listaOrdenes = ordenService.findAll();
		model.addAttribute("listaOrdenes", listaOrdenes);
		return "administrador/ordenes";
	}

	@GetMapping("/detalle/{id}")
	public String mostrarDetalle(Model model, @PathVariable Integer id) {
		log.info("Id de la orden: {}", id);
		Orden orden = ordenService.findById(id).get();

		model.addAttribute("listaDetalles", orden.getDetalles());

		return "administrador/detalleorden";
	}

}
