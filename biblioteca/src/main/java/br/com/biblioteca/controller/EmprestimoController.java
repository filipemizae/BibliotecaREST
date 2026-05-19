package br.com.biblioteca.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.Exemplar;
import br.com.biblioteca.model.Usuario;
import br.com.biblioteca.repository.EmprestimoRepository;
import br.com.biblioteca.repository.ExemplarRepository;
import br.com.biblioteca.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/emprestimos")
@CrossOrigin(origins = "*")
public class EmprestimoController {

    private final EmprestimoRepository emprestimoRepo;
    private final ExemplarRepository exemplarRepo;
    private final UsuarioRepository usuarioRepo;

    public EmprestimoController(
            EmprestimoRepository emprestimoRepo,
            ExemplarRepository exemplarRepo,
            UsuarioRepository usuarioRepo) {

        this.emprestimoRepo = emprestimoRepo;
        this.exemplarRepo = exemplarRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping
    public List<Emprestimo> listar() {
        return emprestimoRepo.findAll();
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Emprestimo>> listarPorUsuario(@PathVariable Long id) {
        if (!usuarioRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(emprestimoRepo.findByUsuarioId(id));
    }

    @GetMapping("/usuario/{id}/ativos")
    public ResponseEntity<List<Emprestimo>> listarAtivosPorUsuario(@PathVariable Long id) {
        if (!usuarioRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(emprestimoRepo.findByUsuarioIdAndDataDevolvidaIsNull(id));
    }

    @PostMapping
    public ResponseEntity<?> registrarEmprestimo(@RequestBody EmprestimoRequest req) {

        Exemplar exemplar = exemplarRepo.findById(req.codigoExemplar()).orElse(null);

        if (exemplar == null) {
            return ResponseEntity.badRequest().body("Exemplar não encontrado.");
        }

        if (!exemplar.getDisponivel()){
            return ResponseEntity.badRequest().body("Este exemplar não está disponível.");
        }

        Usuario usuario = usuarioRepo.findByRegistro(req.registroUsuario()).orElse(null);

        if (usuario == null) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        exemplar.setDisponivel(false);
        exemplarRepo.save(exemplar);

        Emprestimo emprestimo = new Emprestimo(exemplar, usuario, LocalDate.now());

        return ResponseEntity.ok(emprestimoRepo.save(emprestimo));
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolverLivro(@PathVariable Long id) {

        Emprestimo emprestimo = emprestimoRepo.findById(id).orElse(null);

        if (emprestimo == null) {
            return ResponseEntity.notFound().build();
        }

        if (emprestimo.getDataDevolvida() != null) {
            return ResponseEntity.badRequest().body("Este empréstimo já foi devolvido.");
        }

        emprestimo.setDataDevolvida(LocalDate.now());
        emprestimo.getExemplar().setDisponivel(true);

        exemplarRepo.save(emprestimo.getExemplar());
        emprestimoRepo.save(emprestimo);

        return ResponseEntity.ok(emprestimo);
    }

    @PutMapping("/{id}/prolongar")
    public ResponseEntity<?> prolongarEmprestimo(
            @PathVariable Long id,
            @RequestParam(defaultValue = "7") int dias) {

        Emprestimo emprestimo = emprestimoRepo.findById(id).orElse(null);

        if (emprestimo == null) {
            return ResponseEntity.notFound().build();
        }

        if (emprestimo.getDataDevolvida() != null) {
            return ResponseEntity.badRequest().body("Não é possível prolongar um empréstimo já devolvido.");
        }

        emprestimo.prolongarDataDevEmprestimo(dias);

        return ResponseEntity.ok(emprestimoRepo.save(emprestimo));
    }

    record EmprestimoRequest(Long codigoExemplar, int registroUsuario) {
}
}