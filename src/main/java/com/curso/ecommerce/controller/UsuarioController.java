package com.curso.ecommerce.controller;

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

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger log = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@GetMapping("/registro")
	public String create() {
		return "usuario/registro";
	}

	@PostMapping("/save")
	public String save(Usuario usuario) {
		log.info("Usuario registro: {}", usuario);
		usuario.setTipo("USER");
		usuarioService.save(usuario);
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}

	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession miSession) {
		log.info("Accesos: {}", usuario);

		Optional<Usuario> optionalUsuario = usuarioService.findByEmail(usuario.getEmail());

		if (optionalUsuario.isPresent()) {
			log.info("Usuario de bd: {}", optionalUsuario.get());
			miSession.setAttribute("idusuario", optionalUsuario.get().getId());
			if (optionalUsuario.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			} else {
				return "redirect:/";
			}
		} else {
			log.info("El usuario no existe");
		}

		return "redirect:/";
	}

	@GetMapping("/compras")
	public String obtenerCompras(HttpSession miSession, Model model) {
		model.addAttribute("miSession", miSession.getAttribute("idusuario"));
		Usuario usuario = usuarioService.findById(Integer.parseInt(miSession.getAttribute("idusuario").toString()))
				.get();

		List<Orden> listaOrdenes = ordenService.findByUsuario(usuario);

		model.addAttribute("listaOrdenes", listaOrdenes);

		return "usuario/compras";
	}

	@GetMapping("/detalle/{id}")
	public String mostrarDetalleCompra(@PathVariable Integer id, HttpSession miSession, Model model) {
		log.info("Id de la orden: {}", id);
		Optional<Orden> optionalOrden = ordenService.findById(id);

		model.addAttribute("listaDetalles", optionalOrden.get().getDetalles());

		// session
		model.addAttribute("miSession", miSession.getAttribute("idusuario"));
		return "usuario/detallecompra";
	}

}
