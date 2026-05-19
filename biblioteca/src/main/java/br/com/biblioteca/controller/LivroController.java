package br.com.biblioteca.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.biblioteca.model.Livro;
import br.com.biblioteca.repository.LivroRepository;

@RestController
@RequestMapping("/api/livros")
@CrossOrigin(origins = "*")
public class LivroController {

    private final LivroRepository livroRepository;

    public LivroController(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @GetMapping
    public List<Livro> listar() {
        return livroRepository.findAll();
    }

    @GetMapping("/{codigoLivro}")
    public ResponseEntity<Livro> buscarPorCodigo(@PathVariable Long codigoLivro) {
        return livroRepository.findById(codigoLivro)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Livro livro) {

        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            return ResponseEntity.badRequest().body("Título é obrigatório.");
        }

        if (livro.getAutor() == null || livro.getAutor().isBlank()) {
            return ResponseEntity.badRequest().body("Autor é obrigatório.");
        }

        if (livro.getAnoDePublicacao() <= 0) {
            return ResponseEntity.badRequest().body("Ano de publicação inválido.");
        }

        Livro livroSalvo = livroRepository.save(livro);

        return ResponseEntity.ok(livroSalvo);
    }

    @PutMapping("/{codigoLivro}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long codigoLivro,
            @RequestBody Livro dadosAtualizados) {

        Livro livro = livroRepository.findById(codigoLivro).orElse(null);

        if (livro == null) {
            return ResponseEntity.notFound().build();
        }

        if (dadosAtualizados.getTitulo() == null || dadosAtualizados.getTitulo().isBlank()) {
            return ResponseEntity.badRequest().body("Título é obrigatório.");
        }

        if (dadosAtualizados.getAutor() == null || dadosAtualizados.getAutor().isBlank()) {
            return ResponseEntity.badRequest().body("Autor é obrigatório.");
        }

        if (dadosAtualizados.getAnoDePublicacao() <= 0) {
            return ResponseEntity.badRequest().body("Ano de publicação inválido.");
        }

        livro.setTitulo(dadosAtualizados.getTitulo());
        livro.setAutor(dadosAtualizados.getAutor());
        livro.setAnoDePublicacao(dadosAtualizados.getAnoDePublicacao());

        Livro livroAtualizado = livroRepository.save(livro);

        return ResponseEntity.ok(livroAtualizado);
    }

    @DeleteMapping("/{codigoLivro}")
    public ResponseEntity<Void> deletar(@PathVariable Long codigoLivro) {

        if (!livroRepository.existsById(codigoLivro)) {
            return ResponseEntity.notFound().build();
        }

        livroRepository.deleteById(codigoLivro);

        return ResponseEntity.noContent().build();
    }
}