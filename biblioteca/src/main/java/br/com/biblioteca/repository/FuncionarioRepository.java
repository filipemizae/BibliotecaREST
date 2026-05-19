package br.com.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.biblioteca.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}