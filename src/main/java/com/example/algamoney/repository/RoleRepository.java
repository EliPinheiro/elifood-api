 package com.example.algamoney.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.algamoney.model.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role ,Long>{
	   Optional<Role> findByRoleCodigo(Long roleCodigo); // Método para buscar por roleCodigo

	    Optional<Role> findByRoleNome(String roleNome); // Método para buscar por roleNome
	}
