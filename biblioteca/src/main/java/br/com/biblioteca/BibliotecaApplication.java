package br.com.biblioteca;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.Exemplar;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.Usuario;
import br.com.biblioteca.repository.EmprestimoRepository;
import br.com.biblioteca.repository.ExemplarRepository;
import br.com.biblioteca.repository.LivroRepository;
import br.com.biblioteca.repository.UsuarioRepository;

@SpringBootApplication
public class BibliotecaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApplication.class, args);
    }

    @Bean
    CommandLineRunner carregarDadosIniciais(
            UsuarioRepository usuarioRepository,
            LivroRepository livroRepository,
            ExemplarRepository exemplarRepository,
            EmprestimoRepository emprestimoRepository) {

        return args -> {

            Usuario usuario1 = new Usuario("Alex", "1995", "11912345678", 1, "123");
            Usuario usuario2 = new Usuario("Arthur", "2001", "11912345678", 2, "123");
            Usuario usuario3 = new Usuario("Camila", "1997", "11912345678", 3, "123");

            usuarioRepository.save(usuario1);
            usuarioRepository.save(usuario2);
            usuarioRepository.save(usuario3);

            Livro livro1 = new Livro("Dom Casmurro", "Machado de Assis", 1899);
            Livro livro2 = new Livro("O Pequeno Príncipe", "Antoine de Saint-Exupéry", 1943);
            Livro livro3 = new Livro("Dom Quixote", "Miguel de Cervantes", 1605);

            Exemplar exemplar1 = new Exemplar(true);
            Exemplar exemplar2 = new Exemplar(true);
            Exemplar exemplar3 = new Exemplar(true);
            Exemplar exemplar4 = new Exemplar(true);

            livro1.adicionarExemplar(exemplar1);
            livro1.adicionarExemplar(exemplar2);
            livro2.adicionarExemplar(exemplar3);
            livro2.adicionarExemplar(exemplar4);

            livroRepository.save(livro1);
            livroRepository.save(livro2);
            livroRepository.save(livro3);

            exemplarRepository.save(exemplar1);
            exemplarRepository.save(exemplar2);
            exemplarRepository.save(exemplar3);
            exemplarRepository.save(exemplar4);

            Emprestimo emprestimo1 = new Emprestimo(exemplar1, usuario2, LocalDate.now());
            Emprestimo emprestimo2 = new Emprestimo(exemplar2, usuario1, LocalDate.now());
            Emprestimo emprestimo3 = new Emprestimo(exemplar3, usuario3, LocalDate.now());

            exemplar1.setDisponivel(false);
            exemplar2.setDisponivel(false);
            exemplar3.setDisponivel(false);

            exemplarRepository.save(exemplar1);
            exemplarRepository.save(exemplar2);
            exemplarRepository.save(exemplar3);

            emprestimoRepository.save(emprestimo1);
            emprestimoRepository.save(emprestimo2);
            emprestimoRepository.save(emprestimo3);
        };
    }
}