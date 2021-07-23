package com.arelance.test.api.controller;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		
		GenericListResponse response = new GenericListResponse("Transacción exitosa", list);
		return new ResponseEntity<GenericListResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("/search")
	public ResponseEntity<GenericListResponse> searchByName() {
		List<User> list = userService.list(); 
		
		GenericListResponse response = new GenericListResponse("Transacción exitosa", list);
		return new ResponseEntity<GenericListResponse>(response, HttpStatus.OK);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable("id") int id, @Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<Message>(new Message("Datos inválidos o vacíos"), HttpStatus.BAD_REQUEST);
		}
		// Se comprueba que el id exista
		if (!userService.existsById(id)) {
			return new ResponseEntity<Message>(new Message("El usuario no existe"), HttpStatus.NOT_FOUND); 
		}
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<Message>(new Message("Datos inválidos o vacíos"), HttpStatus.BAD_REQUEST);
		}
		// Se comprueba si el nombre YA existe, y si es así, se compara si el ID es diferente se devuelve un error
		if (userService.existsByEmail(userRequest.getEmail()) && userService.getByEmail(userRequest.getEmail()).get().getId() != id) {
			return new ResponseEntity<Message>(new Message("El correo ingresado ya está asignado a alguien más"), HttpStatus.BAD_REQUEST);
		}
		// Obtenemos la fecha y hora actual 
		LocalDateTime now = LocalDateTime.now();
		Set<Department> lstDepartments = new HashSet<>();
		
		// Obtenemos el objeto
		User user = userService.getById(id).get();
		// Cambiamos por los nuevos datos
		user.setName(userRequest.getName());
		user.setLast(userRequest.getLast());
		user.setAddress(userRequest.getAddress());
		user.setDni(userRequest.getDni());
		for (int departmentId :userRequest.getDepartments()) {
			Department dep = departmentService.getById(departmentId).get();
			lstDepartments.add(dep);
		}
		user.setDepartments(lstDepartments);
		user.setUpdated(now);
		// Actualizamos
		userService.save(user);
		
		return new ResponseEntity<Message>(new Message("Usuario actualizado"), HttpStatus.OK);
	}
	
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
		
		return new ResponseEntity<Message>(new Message(message), HttpStatus.OK);
	}
	
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
