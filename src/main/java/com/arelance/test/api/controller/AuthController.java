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
import org.springframework.web.bind.annotation.GetMapping;
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
import com.arelance.test.api.request.UserRequest;
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
	
	/**
	 * M??todo que permite crear una cuenta de usuario que a la vez es una entidad de empleado dentro de la empresa.
	 * Este endpoint est?? limitado a accederse con privilegios de Admin ya que se dise???? una mec??nica que a partir
	 * de un usuario administrador, se generen las cuentas de los empleados.
	 * Una contrase??a temporal se genera, provista como respuesta al administrador para que el usuario pueda loguearse,
	 * dentro de la cuenta el usuario pueda cambiar su contrase??a personalmente.
	 * @param signUpReq
	 * @param bindingResult
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/register")
	public ResponseEntity<?> newUser(@Valid @RequestBody SignUpUserRequest signUpReq, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<Message>(new Message("Datos inv??lidos o incompletos"), HttpStatus.BAD_REQUEST);
		}
		if (userService.existsByEmail(signUpReq.getEmail())) {
			return new ResponseEntity<Message>(new Message("El email ingresado ya existe"), HttpStatus.BAD_REQUEST);
		}
		if (!roleService.existsByRoleName(signUpReq.getRole())) {
			return new ResponseEntity<Message>(new Message("El rol ingresado no existe"), HttpStatus.BAD_REQUEST);
		}
		
		LocalDateTime now = LocalDateTime.now();
		
		User user = new User(signUpReq.getName(), signUpReq.getLast(), signUpReq.getAddress(), signUpReq.getDni(),
							 signUpReq.getEmail(),1,0,now,now);
		
		String pass = userService.generatePass(user);
		user.setPassword(passwordEncoder.encode(pass));
		user.setRole(roleService.getByRoleName(signUpReq.getRole()).get());

		userService.save(user);
		
		GenericObjectResponse response = new GenericObjectResponse("Usuario creado con ??xito", 
																new SignUpResponse(user.getEmail(),pass));
		return new ResponseEntity<GenericObjectResponse>(response, HttpStatus.CREATED);
	}

	/**
	 * M??todo para generar el inicio de sesi??n, aqu?? se genera el JWT que ser?? devuelto, seg??n el formato est??ndar.
	 * Tiene una validez de dos horas desde que se gener??.
	 * @param signInReq
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody SignInUserRequest signInReq, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<Message>(new Message("Datos inv??lidos o incompletos"), HttpStatus.BAD_REQUEST);
		}
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					signInReq.getEmail(), signInReq.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateToken(authentication);
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		
		JwtRequest jwtRequest = new JwtRequest(jwt, userDetails.getUsername(), userDetails.getAuthorities());
		
		return new ResponseEntity<JwtRequest>(jwtRequest,HttpStatus.OK);
		
	}
	
	/**
	 * Este m??todo permite actualizar la informaci??n de acceso ya sea cambiar la contrase??a o solamente actualizar el
	 * correo electr??nico, en este caso si la contrase??a se deja vac??a NO se actualizar??, por eso no se valida si viene vac??a
	 * Este endpoint lo puede acceder tanto un ADMIN como un CONSULTOR
	 * @param id
	 * @param signInReq
	 * @param bindingResult
	 * @return
	 */
	@PreAuthorize("hasAnyRole('ADMIN','CONSULTANT')")
	@PostMapping("/update/credentials/{id}")
	public ResponseEntity<?> changeCredentials(@PathVariable("id") int id, @Valid @RequestBody SignInUserRequest signInReq, BindingResult bindingResult) {
		if (bindingResult.hasFieldErrors("email")) {
			return new ResponseEntity<Message>(new Message("Email inv??lido o vac??o"), HttpStatus.BAD_REQUEST);
		}
		// Se comprueba que el id exista
		if (!userService.existsById(id)) {
			return new ResponseEntity<Message>(new Message("El usuario no existe"), HttpStatus.NOT_FOUND); 
		}
		LocalDateTime now = LocalDateTime.now();
		User user = userService.getById(id).get();
		user.setEmail(signInReq.getEmail());
		
		// Si la contrase??a ven??a vacia, no sufre cambios, si ven??a llena si se actualiza
		if (!signInReq.getPassword().equals("")) {
			user.setPassword(passwordEncoder.encode(signInReq.getPassword()));
		}
		user.setUpdated(now);
		userService.save(user);
			
		return new ResponseEntity<Message>(new Message("Credenciales actualizadas, vuelve a iniciar sesi??n"), HttpStatus.OK);
	}
	
	/***
	 * Este m??todo es para uso exclusivo del ADMIN, en el momento en que alg??n usuario haya perdido su contrase??a, desde
	 * un bot??n se puede utilizar esta petici??n GET, para volver a gener una nueva contrase??a.
	 * La contrase??a se genera a partir de la inicial del nombre del usuario, el apellido y tres n??meros aleatorios.
	 * al igual que la generada en el registro.s 
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/update/password/{id}")
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
			
		GenericObjectResponse response = new GenericObjectResponse("Contrase??a generada exitosamente", 
				new SignUpResponse(user.getEmail(),pass));
		return new ResponseEntity<GenericObjectResponse>(response, HttpStatus.CREATED);
	}
	
	
}
