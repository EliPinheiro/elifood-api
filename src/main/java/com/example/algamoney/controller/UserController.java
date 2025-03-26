package com.example.algamoney.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.algamoney.model.CreateUserDto;
import com.example.algamoney.model.Role;
import com.example.algamoney.model.Usuario;
import com.example.algamoney.repository.RoleRepository;
import com.example.algamoney.repository.UsuarioRepository;



@RestController
public class UserController {

	@Autowired
	private UsuarioRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	  @Transactional
	    @PostMapping("/usuarios")
	    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {

	        var basicRole = roleRepository.findByRoleNome(Role.Values.BASIC.name());

	        
	        System.out.println(basicRole);
	        var userFromDb = userRepository.findByuserName(dto.userName());
	        if (userFromDb.isPresent()) {
	            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
	        }

	        var usuario = new Usuario();
	        usuario.setUserName(dto.userName());
	        usuario.setSenha(passwordEncoder.encode(dto.senha()));
	        usuario.setRoles(Set.of(basicRole.get()));

	        userRepository.save(usuario);

	        return ResponseEntity.ok().build();
	    }

	  @GetMapping("/usuarios")
	  @PreAuthorize("hasRole('ROLE_ADMIN')")
	  public ResponseEntity<List<Usuario>> listarUsuarios(){
		  var usuarios = userRepository.findAll();
		  return ResponseEntity.ok(usuarios);
	  }
	
}
