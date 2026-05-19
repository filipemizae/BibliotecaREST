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

import br.com.biblioteca.model.Usuario;
import br.com.biblioteca.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository repo;

    public UsuarioController(UsuarioRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Usuario> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return repo.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {

        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            return ResponseEntity.badRequest().body("Nome é obrigatório.");
        }

        if (usuario.getTelefone() == null || usuario.getTelefone().isBlank()) {
            return ResponseEntity.badRequest().body("Telefone é obrigatório.");
        }

        if (usuario.getRegistro() <= 0) {
            return ResponseEntity.badRequest().body("Registro inválido.");
        }

        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            return ResponseEntity.badRequest().body("Senha é obrigatória.");
        }

        if (repo.findByRegistro(usuario.getRegistro()).isPresent()) {
            return ResponseEntity.badRequest().body("Já existe um usuário com esse registro!");
        }

        usuario.setRegistro(usuario.getRegistro());

        return ResponseEntity.ok(repo.save(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Usuario dados) {

        Usuario usuario = repo.findById(id).orElse(null);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        if (dados.getNome() == null || dados.getNome().isBlank()) {
            return ResponseEntity.badRequest().body("Nome é obrigatório.");
        }

        if (dados.getTelefone() == null || dados.getTelefone().isBlank()) {
            return ResponseEntity.badRequest().body("Telefone é obrigatório.");
        }

        usuario.setNome(dados.getNome());
        usuario.setDataNascimento(dados.getDataNascimento());
        usuario.setTelefone(dados.getTelefone());

        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            usuario.setSenha(dados.getSenha());
        }

        return ResponseEntity.ok(repo.save(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        Usuario usuario = repo.findByRegistro(req.registro()).orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(401).body("Registro ou senha inválidos.");
        }

        if (usuario.getSenha() == null || !usuario.getSenha().equals(req.senha())) {
            return ResponseEntity.status(401).body("Registro ou senha inválidos.");
        }

        return ResponseEntity.ok(usuario);
    }

    record LoginRequest(int registro, String senha) {
    }
}