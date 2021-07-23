package com.arelance.test.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arelance.test.api.entity.Department;
import com.arelance.test.api.repository.DepartmentRepository;

@Service
@Transactional
public class DepartmentService {

	/**
	 * Nos ayuda a cuando sea necesario se crea una instancia, 
	 * y no se necesite instanciar en el constructor de la clase
	 */	
	@Autowired
	DepartmentRepository departmentRepository;
	
	public List<Department> list(){
		return departmentRepository.findAll();
	}
	
	public void save(Department department) {
		departmentRepository.save(department);
	}
	
	public void delete(int id) {
		departmentRepository.deleteById(id);
	}
	
	
	public Optional<Department> getById(int id) {
		return departmentRepository.findById(id);
	}
	
	public Optional<Department> getByName(String name) {
		return departmentRepository.findByName(name);
	}
	
	public boolean existById(int id) {
		return departmentRepository.existsById(id);
	}
	
	public boolean existByName(String name) {
		return departmentRepository.existsByName(name);
	}
	
}
