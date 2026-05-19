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

import br.com.biblioteca.model.Exemplar;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.repository.ExemplarRepository;
import br.com.biblioteca.repository.LivroRepository;

@RestController
@RequestMapping("/api/exemplares")
@CrossOrigin(origins = "*")
public class ExemplarController {

    private final ExemplarRepository exemplarRepo;
    private final LivroRepository livroRepo;

    public ExemplarController(ExemplarRepository exemplarRepo, LivroRepository livroRepo) {
        this.exemplarRepo = exemplarRepo;
        this.livroRepo = livroRepo;
    }

    @GetMapping
    public List<Exemplar> listar() {
        return exemplarRepo.findAll();
    }

    @GetMapping("/{codigoExemplar}")
    public ResponseEntity<Exemplar> buscarPorId(@PathVariable Long codigoExemplar) {
        return exemplarRepo.findById(codigoExemplar)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody ExemplarRequest req) {

        Livro livro = livroRepo.findById(req.codigoLivro()).orElse(null);

        if (livro == null) {
            return ResponseEntity.badRequest().body("Livro não encontrado.");
        }

        Exemplar exemplar = new Exemplar(true);
        exemplar.setLivro(livro);

        return ResponseEntity.ok(exemplarRepo.save(exemplar));
    }

    @PutMapping("/{codigoExemplar}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long codigoExemplar,
            @RequestBody ExemplarRequest req) {

        Exemplar exemplar = exemplarRepo.findById(codigoExemplar).orElse(null);

        if (exemplar == null) {
            return ResponseEntity.notFound().build();
        }

        Livro livro = livroRepo.findById(req.codigoLivro()).orElse(null);

        if (livro == null) {
            return ResponseEntity.badRequest().body("Livro não encontrado.");
        }

        exemplar.setDisponivel(req.disponivel());
        exemplar.setLivro(livro);

        return ResponseEntity.ok(exemplarRepo.save(exemplar));
    }

    @DeleteMapping("/{codigoExemplar}")
    public ResponseEntity<Void> apagar(@PathVariable Long codigoExemplar) {

        if (!exemplarRepo.existsById(codigoExemplar)) {
            return ResponseEntity.notFound().build();
        }

        exemplarRepo.deleteById(codigoExemplar);

        return ResponseEntity.noContent().build();
    }

    record ExemplarRequest(boolean disponivel, Long codigoLivro) {
    }
}