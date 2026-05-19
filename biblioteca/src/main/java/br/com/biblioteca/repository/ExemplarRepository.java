package br.com.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biblioteca.model.Exemplar;

public interface ExemplarRepository extends JpaRepository<Exemplar, Long> {
    Optional<Exemplar> findByCodigo(int codigo);
    boolean existsByCodigo(int codigo);
}