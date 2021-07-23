package com.arelance.test.api.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arelance.test.api.entity.Department;
import com.arelance.test.api.entity.Role;
import com.arelance.test.api.entity.User;
import com.arelance.test.api.request.DepartmentRequest;
import com.arelance.test.api.request.RoleRequest;
import com.arelance.test.api.request.UserRequest;
import com.arelance.test.api.response.GenericListResponse;
import com.arelance.test.api.response.GenericObjectResponse;
import com.arelance.test.api.response.Message;
import com.arelance.test.api.service.RoleService;

@RestController
@RequestMapping("/role")
@CrossOrigin(origins = "*")
public class RoleController {
	
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	private static final String ROLE_CONSULTANT = "ROLE_CONSULTANT";
	
	@Autowired
	RoleService roleService;
	
	
	@GetMapping("/list")
	public ResponseEntity<GenericListResponse> list() {
		List<Role> list = roleService.list(); 
		
		GenericListResponse response = new GenericListResponse("Transacción exitosa", list);
		return new ResponseEntity<GenericListResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("/detail/{id}")
	public ResponseEntity<?> searchById(@PathVariable int id) {
		// Se comprueba que el id exista
		if (!roleService.existsById(id)) {
			return new ResponseEntity<Message>(new Message("No existe"), HttpStatus.NOT_FOUND); 
		} else {
			Role role= roleService.getById(id).get();
			
			GenericObjectResponse response = new GenericObjectResponse("Transacción exitosa", role);
			return new ResponseEntity<GenericObjectResponse>(response, HttpStatus.OK);			
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<?> create(@Valid @RequestBody RoleRequest roleRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<Message>(new Message("Datos inválidos o incompletos"), HttpStatus.BAD_REQUEST);
		}
		// Se le da el formato al rol (asumiendo que del cliente se envia sin el ROLE_)
		roleRequest.setRoleName("ROLE_"+roleRequest.getRoleName().toUpperCase());
		// Se comprueba que el nombre no exista
		if (roleService.existsByRoleName(roleRequest.getRoleName())) {
			return new ResponseEntity<Message>(new Message("Es rol ya existe"), HttpStatus.BAD_REQUEST);
		}
		// Obtenemos la fecha y hora actual
		LocalDateTime now = LocalDateTime.now();
		// Creamos el nuevo objeto
		Role role = new Role(
				roleRequest.getRoleName(),
				1, 
				now, 
				now);
		// Creamos el objeto
		roleService.save(role);
		
		return new ResponseEntity<Message>(new Message("Rol creado"), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable("id") int id, @Valid @RequestBody RoleRequest roleRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<Message>(new Message("Datos inválidos o vacíos"), HttpStatus.BAD_REQUEST);
		}
		// Se comprueba que el id exista
		if (!roleService.existsById(id)) {
			return new ResponseEntity<Message>(new Message("El usuario no existe"), HttpStatus.NOT_FOUND); 
		}
		// Se le da el formato al rol (asumiendo que del cliente se envia sin el ROLE_)
		roleRequest.setRoleName("ROLE_"+roleRequest.getRoleName().toUpperCase());
		// Se comprueba si el nombre YA existe, y si es así, se compara si el ID es diferente se devuelve un error
		if (roleService.existsByRoleName(roleRequest.getRoleName()) && roleService.getByRoleName(roleRequest.getRoleName()).get().getId() != id) {
			return new ResponseEntity<Message>(new Message("El Rol asignado ya existe"), HttpStatus.BAD_REQUEST);
		}
		// Obtenemos la fecha y hora actual 
		LocalDateTime now = LocalDateTime.now();
		// Obtenemos el objeto
		Role role = roleService.getById(id).get();
		
		// Validamos que no sea de los dos principales
		if (role.getRoleName().equals(ROLE_ADMIN) || role.getRoleName().equals(ROLE_CONSULTANT)) {
			return new ResponseEntity<Message>(new Message("No es posible modificar un rol por defecto"), HttpStatus.CONFLICT);
		}
		
		// Cambiamos por los nuevos datos
		role.setRoleName(roleRequest.getRoleName());
		
		role.setUpdated(now);
		// Actualizamos
		roleService.save(role);
		
		return new ResponseEntity<Message>(new Message("Rol actualizado"), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		// Se comprueba que el id exista
		if (!roleService.existsById(id)) {
			return new ResponseEntity<Message>(new Message("No existe"), HttpStatus.NOT_FOUND); 
		}
		Role role = roleService.getById(id).get();
		// Validamos que no sea de los dos principales
		if (role.getRoleName().equals(ROLE_ADMIN) || role.getRoleName().equals(ROLE_CONSULTANT)) {
			return new ResponseEntity<Message>(new Message("No es posible eliminar un rol por defecto"), HttpStatus.CONFLICT);
		}
		roleService.delete(id);
		
		return new ResponseEntity<Message>(new Message("Rol eliminado"), HttpStatus.OK);
	}
	
}
