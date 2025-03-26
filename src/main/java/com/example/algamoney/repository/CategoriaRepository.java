package com.example.algamoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import com.example.algamoney.model.Categoria;
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria ,Long>{
	

}
