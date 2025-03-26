package com.example.algamoney.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//@EnableWebSecurity
public class SecurityConfigInMemory {
	
	@Bean
	public UserDetailsService userDetailsService() {
	    // Criação de usuários em memória para autenticação
	    UserDetails userDetailOne = User.withUsername("eli")
	            .password(passwordEncoder().encode("123")) // Define a senha criptografada
	            .build();

	    UserDetails userDetailTwo = User.withUsername("dalia")
	            .password(passwordEncoder().encode("321")) // Define a senha criptografada
	            .build();

	    UserDetails adm = User.withUsername("adm")
	            .password(passwordEncoder().encode("111")) // Define a senha criptografada
	            .build();

	    // Retorna um gerenciador de usuários baseado em memória, contendo os usuários criados acima
	    return new InMemoryUserDetailsManager(userDetailOne, userDetailTwo, adm);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
	    // Define o codificador de senhas como BCrypt
	    return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	    
	    // Desativa a proteção contra CSRF, pois geralmente não é necessária para APIs REST sem sessão
	    httpSecurity.csrf(csrfCustomizer -> csrfCustomizer.disable());

	    // Define as regras de autorização para as requisições HTTP
	    httpSecurity.authorizeHttpRequests(request ->
	        request.requestMatchers("/lancamentos").permitAll() // Permite acesso público à rota "/lancamentos"
	            .anyRequest().authenticated()); // Exige autenticação para qualquer outra requisição

	    // Habilita autenticação HTTP Basic (usuário/senha via cabeçalho de requisição)
	    httpSecurity.httpBasic(Customizer.withDefaults());

	    // Configura a gestão de sessões como STATELESS (sem estado), ideal para APIs REST
	    httpSecurity.sessionManagement(session ->
	        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

	    // Retorna a configuração da segurança construída
	    return httpSecurity.build();
	}


}
