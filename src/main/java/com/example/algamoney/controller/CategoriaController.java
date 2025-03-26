package com.example.algamoney.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.event.RecursoCriadoEvent;
import com.example.algamoney.model.Categoria;
import com.example.algamoney.repository.CategoriaRepository;
import com.example.algamoney.resource.service.CategoriaService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private CategoriaService categoriaService;
	
	
	@GetMapping
	public List<Categoria> listar(){

		return categoriaRepository.findAll();
	}
	
	@PostMapping
	public ResponseEntity<?> criar(@RequestBody @Valid Categoria entity, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(entity);
		
//		// é a uri que levara ao recurso criado
//		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{codigo}")
//				.buildAndExpand(categoriaSalva.getCodigo()).toUri();
//		response.setHeader("Location", uri.toASCIIString());
		
		
		//disparando um evento
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
		
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarPeloCodigo(@PathVariable("id") Long id) {
//	    Optional<Categoria> categoria = categoriaRepository.findById(codigo);
//	    
//	    if (categoria.isPresent()) {
//	        return ResponseEntity.status(HttpStatus.OK).body(categoria.get());
//	    }
//	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso inexistente");
		return ResponseEntity.status(HttpStatus.OK).body(categoriaService.buscarOuFalhar(id));
				}
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Categoria categoria){
		Categoria categoriaSalva = categoriaService.atualizar(id, categoria);
		return ResponseEntity.ok(categoriaSalva);
		
		
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Long id){
		Categoria categoria = categoriaService.buscarOuFalhar(id);
		categoriaRepository.deleteById(categoria.getCodigo());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		
		
	}
	

}
