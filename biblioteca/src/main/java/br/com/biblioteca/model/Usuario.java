package br.com.biblioteca.model;

import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Usuario extends Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Transient
    private ArrayList<Exemplar> livrosEmprestados = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String nome, String dataNascimento, String telefone) {
        super(nome, dataNascimento, telefone);
        this.livrosEmprestados = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        if (email == null && id != null) {
            email = id + "@biblioteca.com";
        }
        return email;
    }

    public ArrayList<Exemplar> getLivrosEmprestados() {
        return livrosEmprestados;
    }

    public void setId(Long id) {
        this.id = id;
        this.email = id + "@biblioteca.com";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLivrosEmprestados(ArrayList<Exemplar> livrosEmprestados) {
        this.livrosEmprestados = livrosEmprestados;
    }

    public void adicionarEmprestimo(Exemplar exemplar) {
        livrosEmprestados.add(exemplar);
    }

    public void removerEmprestimo(Exemplar exemplar) {
        livrosEmprestados.remove(exemplar);
    }

    @Override
    public String toString() {
        return super.toString() + " ID: " + id + " Email: " + getEmail();
    }
}