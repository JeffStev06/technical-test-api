package com.arelance.test.api.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arelance.test.api.entity.Role;
import com.arelance.test.api.repository.RoleRepository;

@Service
@Transactional
public class RoleService {
	
	@Autowired
	RoleRepository roleRepository;
	
	public List<Role> list(){
		return roleRepository.findAll();
	}
	
	public Optional<Role> getById(int id) {
		return roleRepository.findById(id);
	}
	
	public Optional<Role> getByRoleName(String roleName) {
		return roleRepository.findByRoleName(roleName);
	}
	
	public boolean existsByRoleName(String roleName) {
		return roleRepository.existsByRoleName(roleName);
	}
	
	public boolean existsById(int id) {
		return roleRepository.existsById(id);
	}
	
	public void save(Role role) {
		roleRepository.save(role);
	}

	public void delete(int id) {
		roleRepository.deleteById(id);
	}
	
}
