package com.example.algamoney.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.event.RecursoCriadoEvent;
import com.example.algamoney.exceptionHandler.AlgamoneyExceptionHandler.Erro;
import com.example.algamoney.model.Lancamento;
import com.example.algamoney.repository.LancamentoRepository;
import com.example.algamoney.repository.filter.LancamentoFilter;
import com.example.algamoney.repository.projection.ResumoLancamento;
import com.example.algamoney.resource.exception.PessoaInesxistenteOuInativaException;
import com.example.algamoney.resource.service.CategoriaService;
import com.example.algamoney.resource.service.LancamentoService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	
	// PAGEABLE COM CLASSE DE FILTRO
//	@GetMapping
//	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable){
//		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
//	}
	
	@GetMapping
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter,   Pageable pageable){
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
		
	}
	

	

	
	

	
	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> listar(@PathVariable Long codigo){
		
		Lancamento lancamento = lancamentoService.buscarOuFalhar(codigo);
		
		return ResponseEntity.status(HttpStatus.OK).body(lancamento);
		
		
	}
	
	@PostMapping
	public ResponseEntity<?> criar (@RequestBody @Valid Lancamento lancamento, HttpServletResponse response ){

		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		categoriaService.buscarOuFalhar(lancamento.getCategoria().getCodigo());
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamento.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamento);
	}
	
	
	
	
	@DeleteMapping("/{codigoId}")
	public ResponseEntity<Object> remover(@PathVariable Long codigoId){
		Lancamento lancamento = lancamentoService.buscarOuFalhar(codigoId);
		lancamentoRepository.deleteById(codigoId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		
	}
	
	
	
	
	
	
@ExceptionHandler(PessoaInesxistenteOuInativaException.class)
	public ResponseEntity<Object> handerPessoaInesxistenteOuInativaException(PessoaInesxistenteOuInativaException ex){
		 String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		    String mensagemDesenvolvedor = ex.toString();
		    List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
	}
}
