package br.com.biblioteca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Funcionario extends Pessoa {

    @Id
    private Long id;

    private String email;

    public Funcionario() {
    }

    public Funcionario(String nome, String dataNascimento, String telefone, int id) {
        super(nome, dataNascimento, telefone);
        this.id = (long) id;
        this.email = id + "@biblioteca.com";
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
        this.email = id + "@biblioteca.com";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return super.toString() + 
               " ID funcionário: " + id + 
               " Email: " + email;
    }
}