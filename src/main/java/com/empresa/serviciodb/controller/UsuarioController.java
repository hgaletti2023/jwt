package com.empresa.serviciodb.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.empresa.serviciodb.services.UsuarioService;

@RestController
@RequestMapping("/cliente")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
	@GetMapping("/{idCliente}")
	public ResponseEntity<String> obtenerNombreCompletoCliente(@PathVariable int idCliente){
		
		try	{
			logger.info("--- bienvenido al metodo obtenerNombreCompletoCliente");
			logger.info("--- idCliente = {}", idCliente);
	

			var auth = SecurityContextHolder.getContext().getAuthentication();
			
			logger.info("--- datos del usuario = {}", auth.getPrincipal());
			logger.info("--- datos de los roles = {}", auth.getAuthorities());
			logger.info("--- esta autenticado? {}", auth.isAuthenticated());

			String nombreCompleto = usuarioService.obtenerNombreCompletoCliente(idCliente);
			if (nombreCompleto != null) 	{
				return ResponseEntity.ok(nombreCompleto);
			}
			else	{
				return ResponseEntity.notFound().build();
			}
		}
		catch (SQLException ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al obtener el nombre del cliente.");
		}
		
	}
	
}
