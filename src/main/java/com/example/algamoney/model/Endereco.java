package com.example.algamoney.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Endereco {
	
	private String logradouro;
	
	private String complemento;
	
	private String bairro;
	
	private String cep;
	
	private String cidade;
	 
	private String estado;
	

}
