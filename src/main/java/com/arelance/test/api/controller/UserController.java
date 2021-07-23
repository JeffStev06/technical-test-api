package com.arelance.test.api.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arelance.test.api.entity.Department;
import com.arelance.test.api.entity.User;
import com.arelance.test.api.request.UserRequest;
import com.arelance.test.api.response.GenericListResponse;
import com.arelance.test.api.response.GenericObjectResponse;
import com.arelance.test.api.response.Message;
import com.arelance.test.api.service.DepartmentService;
import com.arelance.test.api.service.UserService;

@RestController
@RequestMapping("/employee")
@CrossOrigin(origins = "*")
public class UserController {
	
	private final static Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;
	@Autowired
	DepartmentService departmentService;
	
	@GetMapping("/list")
	public ResponseEntity<GenericListResponse> list() {
		List<User> list = userService.list();
		for (User u :list) {
			u.setPassword("");
		}
		GenericListResponse response = new GenericListResponse("Transacción exitosa", list);
		return new ResponseEntity<GenericListResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("/detail/{id}")
	public ResponseEntity<?> searchById(@PathVariable int id) {
		// Se comprueba que el id exista
		if (!userService.existsById(id)) {
			return new ResponseEntity<Message>(new Message("No existe"), HttpStatus.NOT_FOUND); 
		} else {
			User user = userService.getById(id).get();
			user.setPassword("");
			
			GenericObjectResponse response = new GenericObjectResponse("Transacción exitosa", user);
			return new ResponseEntity<GenericObjectResponse>(response, HttpStatus.OK);			
		}
	}
	
	@GetMapping("/search")
	public ResponseEntity<GenericListResponse> searchByName() {
		List<User> list = userService.list();
		// Tomar en cuenta:
		// Contains %Nombre%+%Apellido% Contains Departamento y Contains Rol
		GenericListResponse response = new GenericListResponse("Transacción exitosa", list);
		return new ResponseEntity<GenericListResponse>(response, HttpStatus.OK);
	}
	
	// Obtiene una lista de usuarios que NO pertenecen al departamento solicitado en el parámetro
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("available/department/{id}")
	public ResponseEntity<?> availableUsersToAssign(@PathVariable int id) {
		if (!departmentService.existById(id)) {
			return new ResponseEntity<Message>(new Message("El departamento no existe"), HttpStatus.NOT_FOUND); 
		}
		Department department = departmentService.getById(id).get();
		// Obtenemos todos los usuarios
		List<User> lstUsers = userService.list();
		// Le removemos los que ya tiene el departamento
		lstUsers.removeAll(department.getUsers());
		
		GenericListResponse response = new GenericListResponse("Transacción exitosa", lstUsers);
		return new ResponseEntity<GenericListResponse>(response, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable("id") int id, @Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<Message>(new Message("Datos inválidos o vacíos"), HttpStatus.BAD_REQUEST);
		}
		// Se comprueba que el id exista
		if (!userService.existsById(id)) {
			return new ResponseEntity<Message>(new Message("El usuario no existe"), HttpStatus.NOT_FOUND); 
		}
		// Se comprueba si el nombre YA existe, y si es así, se compara si el ID es diferente se devuelve un error
		if (userService.existsByEmail(userRequest.getEmail()) && userService.getByEmail(userRequest.getEmail()).get().getId() != id) {
			return new ResponseEntity<Message>(new Message("El correo ingresado ya está asignado a alguien más"), HttpStatus.BAD_REQUEST);
		}
		// Obtenemos la fecha y hora actual 
		LocalDateTime now = LocalDateTime.now();
		
		// Obtenemos el objeto
		User user = userService.getById(id).get();
		// Cambiamos por los nuevos datos
		user.setName(userRequest.getName());
		user.setLast(userRequest.getLast());
		user.setAddress(userRequest.getAddress());
		user.setDni(userRequest.getDni());
		user.setEmail(userRequest.getEmail());
		
		user.setUpdated(now);
		// Actualizamos
		userService.save(user);
		
		return new ResponseEntity<Message>(new Message("Usuario actualizado"), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/update/status/{id}")
	public ResponseEntity<?> changeStatus(@PathVariable("id") int id) {
		// Se comprueba que el id exista
		if (!userService.existsById(id)) {
			return new ResponseEntity<Message>(new Message("El usuario no existe"), HttpStatus.NOT_FOUND); 
		}
		User user = userService.getById(id).get();
		String message = "";
		if (user.getActive() == 0) {
			user.setActive(1);
			message = "Usuario activado exitosamente";
		} else {
			user.setActive(0);
			message = "Usuario desactivado exitosamente";
		}
		userService.save(user);
		
		return new ResponseEntity<Message>(new Message(message), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		// Se comprueba que el id exista
		if (!userService.existsById(id)) {
			return new ResponseEntity<Message>(new Message("No existe"), HttpStatus.NOT_FOUND); 
		}
		userService.delete(id);
		
		return new ResponseEntity<Message>(new Message("Usuario eliminado"), HttpStatus.OK);
	}
	
}
