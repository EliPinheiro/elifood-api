package com.example.algamoney.model;

import jakarta.persistence.Column;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "tb_roles")
public class Role {
	


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_codigo")
	private Long roleCodigo;
	
	private String roleNome;
	
	
	 public enum Values {

	        ADMIN(1L),
	        BASIC(2L);

	        long roleCodigo;

	        Values(long roleId) {
	            this.roleCodigo = roleId;
	        }

	        public long getRoleId() {
	            return roleCodigo;
	        }
	    }

}
