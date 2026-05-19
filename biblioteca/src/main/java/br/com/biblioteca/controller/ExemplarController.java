package br.com.biblioteca.controller;

import br.com.biblioteca.model.Exemplar;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.repository.ExemplarRepository;
import br.com.biblioteca.repository.LivroRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<Exemplar> buscarPorId(@PathVariable Long id) {
        return exemplarRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody ExemplarRequest req) {

        if (exemplarRepo.existsByCodigo(req.codigo())) {
            return ResponseEntity.badRequest().body("Já existe exemplar com esse código.");
        }

        Livro livro = livroRepo.findById(req.livroId()).orElse(null);

        if (livro == null) {
            return ResponseEntity.badRequest().body("Livro não encontrado.");
        }

        Exemplar exemplar = new Exemplar(req.codigo(), true, livro);

        return ResponseEntity.ok(exemplarRepo.save(exemplar));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ExemplarRequest req) {

        Exemplar exemplar = exemplarRepo.findById(id).orElse(null);

        if (exemplar == null) {
            return ResponseEntity.notFound().build();
        }

        Livro livro = livroRepo.findById(req.livroId()).orElse(null);

        if (livro == null) {
            return ResponseEntity.badRequest().body("Livro não encontrado.");
        }

        exemplar.setCodigo(req.codigo());
        exemplar.setDisponivel(req.disponivel());
        exemplar.setLivro(livro);

        return ResponseEntity.ok(exemplarRepo.save(exemplar));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagar(@PathVariable Long id) {

        if (!exemplarRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        exemplarRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    record ExemplarRequest(int codigo, boolean disponivel, Long livroId) {
    }
}