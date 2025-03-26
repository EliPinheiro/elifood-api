package com.example.algamoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.algamoney.model.Lancamento;
import com.example.algamoney.repository.lancamento.LancamentoRepositoryQuery;
@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento ,Long> , LancamentoRepositoryQuery {

	

}
