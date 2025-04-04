package com.example.algamoney.resource.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.model.Categoria;
import com.example.algamoney.model.Pessoa;
import com.example.algamoney.repository.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Categoria atualizar(Long id, Categoria categoria) {
		Categoria categoriaSalva = categoriaRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		
		
		
		BeanUtils.copyProperties(categoria, categoriaSalva, "id");
		return categoriaRepository.save(categoriaSalva);
		
	}
	
	
	
	
	public Categoria buscarOuFalhar(Long id) {
		return categoriaRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
	}

}

