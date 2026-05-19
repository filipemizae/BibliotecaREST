package br.com.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biblioteca.model.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByCodigo(int codigo);
    boolean existsByCodigo(int codigo);
}