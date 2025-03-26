package com.example.algamoney.resource.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.model.Lancamento;
import com.example.algamoney.model.Pessoa;
import com.example.algamoney.repository.LancamentoRepository;
import com.example.algamoney.repository.PessoaRepository;
import com.example.algamoney.resource.exception.PessoaInesxistenteOuInativaException;

import jakarta.validation.Valid;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	

	
	public Lancamento buscarOuFalhar(Long id) {
	    return lancamentoRepository.findById(id)
	        .orElseThrow(() -> new EmptyResultDataAccessException(1));
	}



	public Lancamento salvar(@Valid Lancamento lancamento) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());
		if (pessoa.isEmpty() || pessoa.get().isInativo()) {
			throw new PessoaInesxistenteOuInativaException();
		}
		
		
		return lancamentoRepository.save(lancamento);
	}

}

