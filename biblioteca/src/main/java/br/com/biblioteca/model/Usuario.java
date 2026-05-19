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

    private int registro;
    private String email;
    private String senha;

    @Transient
    private ArrayList<Exemplar> livrosEmprestados = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String nome, String dataNascimento, String telefone, int registro, String senha) {
        super(nome, dataNascimento, telefone);
        this.registro = registro;
        this.email = registro + "@biblioteca.com";
        this.senha = senha;
        this.livrosEmprestados = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public int getRegistro() {
        return registro;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public ArrayList<Exemplar> getLivrosEmprestados() {
        return livrosEmprestados;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRegistro(int registro) {
        this.registro = registro;
        this.email = registro + "@biblioteca.com";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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
        return super.toString() + 
               " Registro: " + registro + 
               " Email: " + email;
    }
}