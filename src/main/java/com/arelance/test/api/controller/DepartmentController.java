package com.arelance.test.api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.arelance.test.api.request.DepartmentRequest;
import com.arelance.test.api.response.Message;
import com.arelance.test.api.service.DepartmentService;

@RestController
@RequestMapping("/department")
@CrossOrigin(origins = "*")// "http://localhost:3000" para React 
public class DepartmentController {
	
	@Autowired
	DepartmentService departmentService;
	
	@GetMapping("/list")
	public ResponseEntity<List<Department>> list() {
		List<Department> list = departmentService.list();
		
		return new ResponseEntity<List<Department>>(list,HttpStatus.OK);
	}
	
	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") int id) {
		// Se comprueba que el id exista
		if (!departmentService.existById(id)) {
			return new ResponseEntity<Message>(new Message("No existe"), HttpStatus.NOT_FOUND); 
		} else {
			Department dep = departmentService.getById(id).get();
			return new ResponseEntity<Department>(dep, HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/detailname/{name}")
	public ResponseEntity<?> getDep(@PathVariable("name") String name) {
		// Se comprueba que el nombre no esté en blanco
		if (!departmentService.existByName(name)) {
			return new ResponseEntity<Message>(new Message("No existe"), HttpStatus.NOT_FOUND); 
		} else {
			Department dep = departmentService.getByName(name).get();
			return new ResponseEntity<Department>(dep, HttpStatus.OK);
		}
		
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody DepartmentRequest departmentRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<Message>(new Message("Datos inválidos o incompletos"), HttpStatus.BAD_REQUEST);
		}
		// Se comprueba que el nombre no exista
		if (departmentService.existByName(departmentRequest.getName())) {
			return new ResponseEntity<Message>(new Message("Ese nombre de department ya existe"), HttpStatus.BAD_REQUEST);
		}
		// Obtenemos la fecha y hora actual
		LocalDateTime now = LocalDateTime.now();
		// Creamos el nuevo objeto
		Department department = new Department(
				departmentRequest.getName(), 
				departmentRequest.getDescription(), 
				1, 
				now, 
				now);
		// Creamos el objeto
		departmentService.save(department);
		return new ResponseEntity<Message>(new Message("Departamento creado"), HttpStatus.OK);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody DepartmentRequest departmentRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<Message>(new Message("Datos inválidos o incompletos"), HttpStatus.BAD_REQUEST);
		}
		// Se comprueba que el id exista
		if (!departmentService.existById(id)) {
			return new ResponseEntity<Message>(new Message("El departamento no existe"), HttpStatus.NOT_FOUND); 
		}
		// Se comprueba que el nombre no esté en blanco
		if (departmentRequest.getName().isEmpty()) {
			return new ResponseEntity<Message>(new Message("El campo nombre es obligatorio"), HttpStatus.BAD_REQUEST);
		}
		// Se comprueba si el nombre YA existe, y si es así, se compara si el ID es diferente se devuelve un error
		if (departmentService.existByName(departmentRequest.getName()) && departmentService.getByName(departmentRequest.getName()).get().getId() != id) {
			return new ResponseEntity<Message>(new Message("Ese nombre de departamento ya existe"), HttpStatus.BAD_REQUEST);
		}
		// Obtenemos la fecha y hora actual 
		LocalDateTime now = LocalDateTime.now();
		
		// Obtenemos el objeto
		Department department = departmentService.getById(id).get();
		// Cambiamos por los nuevos datos
		department.setName(departmentRequest.getName());
		department.setDescription(departmentRequest.getDescription());
		department.setUpdated(now);
		// Actualizamos
		departmentService.save(department);
		
		return new ResponseEntity<Message>(new Message("Departamento actualizado"), HttpStatus.OK);
		
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		// Se comprueba que el id exista
		if (!departmentService.existById(id)) {
			return new ResponseEntity<Message>(new Message("No existe"), HttpStatus.NOT_FOUND); 
		}
		departmentService.delete(id);
		
		return new ResponseEntity<Message>(new Message("Departamento eliminado"), HttpStatus.OK);
	}
	
}
