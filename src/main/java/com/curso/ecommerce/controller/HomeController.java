package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger LOG = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private ProductoService productoService;

	// almacenar los detalles de la orden
	private List<DetalleOrden> listaDetalles = new ArrayList<DetalleOrden>();

	// datos de la orden
	private Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model) {

		model.addAttribute("listaProductos", productoService.findAll());

		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		LOG.info("Id enviado como parametro {}", id);

		Producto producto = new Producto();
		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();

		model.addAttribute("producto", producto);

		return "usuario/productohome";
	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {

		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;

		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();

		LOG.info("Producto añadido: {}", producto);
		LOG.info("Cantidad: {}", cantidad);

		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);

		listaDetalles.add(detalleOrden);

		sumaTotal = listaDetalles.stream().mapToDouble(dt -> dt.getTotal()).sum(); // funcion lambda

		orden.setTotal(sumaTotal);
		model.addAttribute("cart", listaDetalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}

	// quitar un producto del carrito
	@GetMapping("/delete/card/{id}")
	public String deleteProducto(@PathVariable Integer id, Model model) {

		// lista nueva de productos
		List<DetalleOrden> ordenesNuevas = new ArrayList<DetalleOrden>();

		for (DetalleOrden detalleOrden : listaDetalles) {
			if (detalleOrden.getProducto().getId() != id) {
				ordenesNuevas.add(detalleOrden);
			}
		}
		//poner la nueva lista con los productos restantes
		listaDetalles = ordenesNuevas;
		
		double sumaTotal = 0;	
		sumaTotal = listaDetalles.stream().mapToDouble(dt -> dt.getTotal()).sum(); // funcion lambda
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", listaDetalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}

}
