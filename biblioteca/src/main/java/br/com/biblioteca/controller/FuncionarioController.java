package br.com.biblioteca.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.biblioteca.model.Funcionario;
import br.com.biblioteca.repository.FuncionarioRepository;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioController(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @GetMapping
    public List<Funcionario> listar() {
        return funcionarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable Long id) {
        return funcionarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Funcionario funcionario) {

        if (funcionario.getNome() == null || funcionario.getNome().isBlank()) {
            return ResponseEntity.badRequest().body("Nome é obrigatório.");
        }

        if (funcionario.getTelefone() == null || funcionario.getTelefone().isBlank()) {
            return ResponseEntity.badRequest().body("Telefone é obrigatório.");
        }

        if (funcionario.getId() == null || funcionario.getId() <= 0) {
            return ResponseEntity.badRequest().body("ID do funcionário inválido.");
        }

        if (funcionarioRepository.existsById(funcionario.getId())) {
            return ResponseEntity.badRequest().body("Já existe funcionário com esse ID.");
        }

        funcionario.setId(funcionario.getId());

        return ResponseEntity.ok(funcionarioRepository.save(funcionario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Funcionario dados) {

        Funcionario funcionario = funcionarioRepository.findById(id).orElse(null);

        if (funcionario == null) {
            return ResponseEntity.notFound().build();
        }

        if (dados.getNome() == null || dados.getNome().isBlank()) {
            return ResponseEntity.badRequest().body("Nome é obrigatório.");
        }

        if (dados.getTelefone() == null || dados.getTelefone().isBlank()) {
            return ResponseEntity.badRequest().body("Telefone é obrigatório.");
        }

        funcionario.setNome(dados.getNome());
        funcionario.setDataNascimento(dados.getDataNascimento());
        funcionario.setTelefone(dados.getTelefone());

        return ResponseEntity.ok(funcionarioRepository.save(funcionario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        if (!funcionarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        funcionarioRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}