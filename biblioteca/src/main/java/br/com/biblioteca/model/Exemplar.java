package br.com.biblioteca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigoExemplar;

    private boolean disponivel = true;

    @ManyToOne
    @JoinColumn(name = "codigo_livro")
    private Livro livro;

    public Exemplar() {
    }
    public Exemplar(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public void mostrarExemplar(){
        System.out.println("Código do exemplar: " + codigoExemplar);
        System.out.println("Disponível: " + disponivel);
    }
    
    public Long getcodigoExemplar(){
        return codigoExemplar; 
    }

    public boolean getDisponivel(){
        return disponivel;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setCodigoExemplar(Long codigoExemplar) {
        this.codigoExemplar = codigoExemplar;
    }

    public void setDisponivel(boolean disponivel){
        this.disponivel = disponivel;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    
}
