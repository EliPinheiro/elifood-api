package com.example.algamoney.event;

import org.springframework.context.ApplicationEvent;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;


@Getter
public class RecursoCriadoEvent extends ApplicationEvent{

	
	private static final long serialVersionUID = 1L;
	
	private HttpServletResponse response;
	private Long id;

	public RecursoCriadoEvent(Object source, HttpServletResponse response, long id) {
		super(source);
		this.id = id;
		this.response = response;
		
	}

}
