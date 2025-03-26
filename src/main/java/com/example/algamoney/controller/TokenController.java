package com.example.algamoney.controller;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.model.Role;
import com.example.algamoney.repository.UsuarioRepository;

@RestController
public class TokenController {
	
	@Autowired
	private JwtEncoder jwtEncoder;
	
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
		var usuario = usuarioRepository.findByuserName(loginRequest.userName());
		
		 if (usuario.isEmpty() || !usuario.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
			 throw new BadCredentialsException("Usuário inválido");
		 }
		 
		 var now = Instant.now();
		 var expiresIn = 3000L;
		 
		 
		 var scopes = usuario.get().getRoles()
				 .stream()
				 .map(Role::getRoleNome)
				 .collect(Collectors.joining(" "));
		 
		 var clains = JwtClaimsSet.builder()
				 .issuer("AlgaMoney")
				 .subject(usuario.get().getCodigo().toString())
				 .issuedAt(now)
				 .expiresAt(now.plusSeconds(expiresIn))
				 .claim("scopes", scopes)
				 .build();
		 
		 var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(clains)).getTokenValue();
		 
		 return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
	}

}
