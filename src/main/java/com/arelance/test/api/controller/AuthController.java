package com.arelance.test.api.controller;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arelance.test.api.security.jwt.JwtProvider;
import com.arelance.test.api.security.request.JwtRequest;
import com.arelance.test.api.security.request.SignInUserRequest;
import com.arelance.test.api.security.request.SignUpUserRequest;
import com.arelance.test.api.service.DepartmentService;
import com.arelance.test.api.service.RoleService;
import com.arelance.test.api.service.UserService;
import com.arelance.test.api.entity.Department;
import com.arelance.test.api.entity.User;
import com.arelance.test.api.response.GenericObjectResponse;
import com.arelance.test.api.response.Message;
import com.arelance.test.api.response.SignUpResponse;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
	
	private final static Logger log = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	JwtProvider jwtProvider;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/register")
	public ResponseEntity<?> newUser(@Valid @RequestBody SignUpUserRequest signUpReq, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<Message>(new Message("Datos inválidos o incompletos"), HttpStatus.BAD_REQUEST);
		}
		if (userService.existsByEmail(signUpReq.getEmail())) {
			return new ResponseEntity<Message>(new Message("El email ingresado ya existe"), HttpStatus.BAD_REQUEST);
		}
		if (!roleService.existsByRoleName(signUpReq.getRole())) {
			return new ResponseEntity<Message>(new Message("El rol ingresado no existe"), HttpStatus.BAD_REQUEST);
		}
		Set<Department> lstDepartments = new HashSet<>();
		for (int departmentId :signUpReq.getDepartments()) {
			Department dep = departmentService.getById(departmentId).get();
			lstDepartments.add(dep);
		}
		
		LocalDateTime now = LocalDateTime.now();
		
		User user = new User(signUpReq.getName(), signUpReq.getLast(), signUpReq.getAddress(), signUpReq.getDni(),
							 signUpReq.getEmail(),1,0, lstDepartments,now,now);
		
		String pass = userService.generatePass(user);
		user.setPassword(passwordEncoder.encode(pass));
		user.setRole(roleService.getByRoleName(signUpReq.getRole()).get());

		userService.save(user);
		
		GenericObjectResponse response = new GenericObjectResponse("Usuario creado con éxito", 
																new SignUpResponse(user.getEmail(),pass));
		return new ResponseEntity<GenericObjectResponse>(response, HttpStatus.CREATED);
	}

	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody SignInUserRequest signInReq, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<Message>(new Message("Datos inválidos o incompletos"), HttpStatus.BAD_REQUEST);
		}
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					signInReq.getEmail(), signInReq.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateToken(authentication);
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		
		JwtRequest jwtRequest = new JwtRequest(jwt, userDetails.getUsername(), userDetails.getAuthorities());
		
		return new ResponseEntity<JwtRequest>(jwtRequest,HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasRole('ADMIN') || hasRole('CONSULTANT')")
	//@PreAuthorize("hasAnyRole('ADMIN','CONSULTANT')")
	@PostMapping("/update/credentials/{id}")
	public ResponseEntity<?> changeCredentials(@Valid @RequestBody SignInUserRequest signInReq,@PathVariable("id") int id, BindingResult bindingResult) {
		if (bindingResult.hasFieldErrors("email")) {
			return new ResponseEntity<Message>(new Message("Email inválido o vacío"), HttpStatus.BAD_REQUEST);
		}
		// Se comprueba que el id exista
		if (!userService.existsById(id)) {
			return new ResponseEntity<Message>(new Message("El usuario no existe"), HttpStatus.NOT_FOUND); 
		}
		LocalDateTime now = LocalDateTime.now();
		User user = userService.getById(id).get();
		user.setEmail(signInReq.getEmail());
		
		// Si la contraseña venía vacia, no sufre cambios, si venía llena si se actualiza
		if (!signInReq.getPassword().equals("")) {
			user.setPassword(passwordEncoder.encode(signInReq.getPassword()));
		}
		user.setUpdated(now);
		userService.save(user);
			
		return new ResponseEntity<Message>(new Message("Credenciales actualizadas, vuelve a iniciar sesión"), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/update/password/{id}")
	public ResponseEntity<?> changePassword(@PathVariable("id") int id) {
		// Se comprueba que el id exista
		if (!userService.existsById(id)) {
			return new ResponseEntity<Message>(new Message("El usuario no existe"), HttpStatus.NOT_FOUND); 
		}
		LocalDateTime now = LocalDateTime.now();
		User user = userService.getById(id).get();
		
		String pass = userService.generatePass(user);
		user.setPassword(passwordEncoder.encode(pass));
		user.setUpdated(now);
		userService.save(user);
			
		GenericObjectResponse response = new GenericObjectResponse("Contraseña generada exitosamente", 
				new SignUpResponse(user.getEmail(),pass));
		return new ResponseEntity<GenericObjectResponse>(response, HttpStatus.CREATED);
	}
	
	
}
