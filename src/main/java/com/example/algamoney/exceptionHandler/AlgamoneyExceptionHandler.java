package com.example.algamoney.exceptionHandler;




import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;


@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		
		String messageUser = messageSource.getMessage("mensagem.invalida",null, LocaleContextHolder.getLocale());
		String messageDev = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
		//Jackson serializa automaticamente o objeto Erro para JSON.
		List<Erro> erros = Arrays.asList(new Erro(messageUser, messageDev));
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
		
	}
	

	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		
		List<Erro> erros =  criarListaDeErros(ex.getBindingResult());
		return handleExceptionInternal(ex, erros, headers, status, request);
	}
	
	private List<Erro> criarListaDeErros(BindingResult bindingResult){
		List<Erro> erros = new ArrayList<>();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String messageUser = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String messageDev = fieldError.toString();
			erros.add(new Erro(messageUser, messageDev));
		}
		
		
		return erros;
	}
	
	@ExceptionHandler({ EmptyResultDataAccessException.class })
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
		// pegando a causa raiz com a ajuda dda dependencia do commnos lang 3
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
	    String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null, LocaleContextHolder.getLocale());
	    String mensagemDesenvolvedor = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.toString();
	    List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
	    return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	
	
	
	@AllArgsConstructor
	@Getter
	public static class Erro {
		private String messageUser;
		private String messageDev;
	}
}
