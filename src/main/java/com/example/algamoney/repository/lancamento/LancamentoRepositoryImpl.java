package com.example.algamoney.repository.lancamento;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.algamoney.model.Lancamento;
import com.example.algamoney.repository.filter.LancamentoFilter;
import com.example.algamoney.repository.projection.ResumoLancamento;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
	    // Cria o CriteriaBuilder para a construção da consulta.
	    CriteriaBuilder builder = manager.getCriteriaBuilder();

	    // Cria a consulta para o tipo 'Lancamento' (entidade que será consultada).
	    CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);

	    // Define a raiz da consulta (a entidade 'Lancamento' no caso).
	    Root<Lancamento> root = criteria.from(Lancamento.class);

	    // Cria as restrições (filtros) para a consulta com base no 'lancamentoFilter'.
	    Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);

	    // Aplica as restrições (filtros) à consulta.
	    criteria.where(predicates);

	    // Cria a consulta executável a partir do CriteriaQuery.
	    TypedQuery<Lancamento> query = manager.createQuery(criteria);

	    // Aplica as restrições de paginação à consulta (limitando os resultados).
	    adicionarRestricoesDePaginacao(query, pageable);

	    // Executa a consulta e retorna uma página contendo os resultados.
	    // A consulta retorna os resultados, 'pageable' contém as informações de paginação,
	    // e 'total' fornece a quantidade total de registros para calcular o número total de páginas.
	    return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	/**
	 * Aplica restrições de paginação à consulta (limitando a quantidade de resultados retornados).
	 * @param query A consulta executável para aplicar as restrições de paginação.
	 * @param pageable O objeto Pageable que contém informações sobre a página atual e o tamanho da página.
	 */
	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
	    // Obtém o número da página atual a partir do objeto Pageable.
	    int paginaAtual = pageable.getPageNumber();

	    // Obtém o número de registros por página a partir do objeto Pageable.
	    int totalRegistrosPorPagina = pageable.getPageSize();

	    // Calcula o primeiro registro a ser retornado na página.
	    int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

	    // Define a primeira posição do registro a ser retornado e a quantidade máxima de registros por página.
	    query.setFirstResult(primeiroRegistroDaPagina);
	    query.setMaxResults(totalRegistrosPorPagina);
	}

	/**
	 * Conta o total de registros que atendem ao filtro (não aplica paginação).
	 * @param lancamentoFilter O filtro que será usado para contar o total de registros.
	 * @return O total de registros que atendem aos critérios do filtro.
	 */
	private Long total(LancamentoFilter lancamentoFilter) {
	    // Cria o CriteriaBuilder para a construção da consulta.
	    CriteriaBuilder builder = manager.getCriteriaBuilder();

	    // Cria a consulta para contar o número total de registros (não seleciona todos os campos).
	    CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

	    // Define a raiz da consulta (a entidade 'Lancamento' no caso).
	    Root<Lancamento> root = criteria.from(Lancamento.class);

	    // Cria as restrições (filtros) para a consulta com base no 'lancamentoFilter'.
	    Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);

	    // Aplica as restrições (filtros) à consulta.
	    criteria.where(predicates);

	    // Seleciona a contagem dos registros que atendem aos critérios.
	    criteria.select(builder.count(root));

	    // Executa a consulta e retorna o total de registros.
	    return manager.createQuery(criteria).getSingleResult();
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
                                       Root<Lancamento> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
            predicates.add(builder.like(
                    builder.lower(root.get("descricao")), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
        }

        if (lancamentoFilter.getDataVencimentoDe() != null) {
            predicates.add(
                    builder.greaterThanOrEqualTo(root.get("dataVencimento"), lancamentoFilter.getDataVencimentoDe()));
        }

        if (lancamentoFilter.getDataVencimentoAte() != null) {
            predicates.add(
                    builder.lessThanOrEqualTo(root.get("dataVencimento"), lancamentoFilter.getDataVencimentoAte()));
        }

        return predicates.toArray(new Predicate[0]);
    }
	
	



}

	

