package br.com.biblioteca.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigoLivro;

    private String titulo;
    private String autor;
    private int anoDePublicacao;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("livro")
    private List<Exemplar> exemplares = new ArrayList<>();

    public Livro() {
    }

    public Livro(String titulo, String autor, int anoDePublicacao) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoDePublicacao = anoDePublicacao;
        this.exemplares = new ArrayList<>();
    }

    public Long getCodigoLivro() {
        return codigoLivro;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnoDePublicacao() {
        return anoDePublicacao;
    }

    public List<Exemplar> getExemplares() {
        return exemplares;
    }

    public void setCodigoLivro(Long codigoLivro) {
        this.codigoLivro = codigoLivro;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setAnoDePublicacao(int anoDePublicacao) {
        this.anoDePublicacao = anoDePublicacao;
    }

    public void setExemplares(List<Exemplar> exemplares) {
        this.exemplares = exemplares;
    }

    public void adicionarExemplar(Exemplar exemplar) {
        exemplares.add(exemplar);
        exemplar.setLivro(this);
    }

    public void mostrarLivro() {
        System.out.println("Código do Livro: " + codigoLivro);
        System.out.println("Título: " + titulo);
        System.out.println("Autor: " + autor);
        System.out.println("Ano de Publicação: " + anoDePublicacao);
        System.out.println(">>>>>");
        System.out.println("Exemplares:");
        for (Exemplar exemplar : exemplares) {
            System.out.println("- Código do Exemplar: " + exemplar.getcodigoExemplar() + ", Disponível: " + exemplar.getDisponivel());
        }
    }
}