package com.example.algamoney.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Pessoa {

	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long codigo;
	
	@NotNull
	private String nome;
	
	private boolean ativo;
	
	@Embedded
	private Endereco endereço;
	
	
	// para o jackson nao serializar por causa do nome
	@Transient
	@JsonIgnore
	public boolean isInativo() {
		return !this.ativo;
	}
}
