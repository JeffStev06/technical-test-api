package com.arelance.test.api.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arelance.test.api.entity.User;
import com.arelance.test.api.repository.UserRepository;
import com.arelance.test.api.security.request.SignUpUserRequest;

@Service
@Transactional
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public List<User> list(){
		return userRepository.findAll();
	}
	
	public Optional<User> getById(int id) {
		return userRepository.findById(id);
	}
	
	public Optional<User> getByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public boolean existsById(int id) {
		return userRepository.existsById(id);
	}
	
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	public void save(User user) {
		userRepository.save(user);
	}
	
	public void delete(int id) {
		userRepository.deleteById(id);
	}
	
	public String generatePass(User user) {
		String pass = "";
		int num = (int)Math.round((Math.random()*((999-100)+1))+100);;		
		pass = user.getName().substring(0,1).toLowerCase() +
			   user.getLast().toLowerCase() + num;
		return pass;
	}
}
