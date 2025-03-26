package com.example.algamoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.algamoney.model.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa ,Long>{
	

}
