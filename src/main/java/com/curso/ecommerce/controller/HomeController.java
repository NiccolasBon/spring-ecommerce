package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IDetalleOrdenService;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IProductoService;
import com.curso.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	// almacenar los detalles de la orden
	private List<DetalleOrden> listaDetalles = new ArrayList<DetalleOrden>();

	// datos de la orden
	private Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model, HttpSession miSession) {

		model.addAttribute("listaProductos", productoService.findAll());
		log.info("Session del usuario: {}", miSession.getAttribute("idusuario"));
		
		//session
		model.addAttribute("miSession", miSession.getAttribute("idusuario"));

		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		log.info("Id enviado como parametro {}", id);

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

		log.info("Producto añadido: {}", producto);
		log.info("Cantidad: {}", cantidad);

		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);

		// Validar que el producto no se añada dos veces
		Integer idProducto = producto.getId();
		boolean ingresado = listaDetalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);

		if (!ingresado) {
			listaDetalles.add(detalleOrden);
		}

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
		// poner la nueva lista con los productos restantes
		listaDetalles = ordenesNuevas;

		double sumaTotal = 0;
		sumaTotal = listaDetalles.stream().mapToDouble(dt -> dt.getTotal()).sum(); // funcion lambda

		orden.setTotal(sumaTotal);
		model.addAttribute("cart", listaDetalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}

	@GetMapping("/getCart")
	public String getCart(Model model, HttpSession miSession) {

		model.addAttribute("cart", listaDetalles);
		model.addAttribute("orden", orden);
		model.addAttribute("miSession", miSession.getAttribute("idusuario"));

		return "usuario/carrito";
	}

	@GetMapping("/order")
	public String showOrder(Model model, HttpSession miSession) {

		Usuario usuario = usuarioService.findById(Integer.parseInt(miSession.getAttribute("idusuario").toString()))
				.get();

		model.addAttribute("cart", listaDetalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", usuario);

		return "usuario/resumenorden";
	}

	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession miSession) {
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumOrden());

		// Usuario
		Usuario usuario = usuarioService.findById(Integer.parseInt(miSession.getAttribute("idusuario").toString()))
				.get();

		orden.setUsuario(usuario);
		ordenService.save(orden);

		// guardar detalles
		for (DetalleOrden detalle : listaDetalles) {
			detalle.setOrden(orden);
			detalleOrdenService.save(detalle);
		}

		// limpiar lista y orden
		orden = new Orden();
		listaDetalles.clear();

		return "redirect:/";
	}

	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model) {
		log.info("Nombre del producto: {}", nombre);
		List<Producto> listaProductos = productoService.findAll().stream()
				.filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase())).collect(Collectors.toList());

		model.addAttribute("listaProductos", listaProductos);
		return "usuario/home";
	}

}
