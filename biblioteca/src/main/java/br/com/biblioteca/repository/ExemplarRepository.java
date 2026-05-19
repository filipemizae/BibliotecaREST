package br.com.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biblioteca.model.Exemplar;

public interface ExemplarRepository extends JpaRepository<Exemplar, Long> {
}