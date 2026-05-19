package br.com.biblioteca.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigoEmprestimo;

    @ManyToOne
    @JoinColumn(name = "codigo_exemplar")
    @JsonIgnoreProperties("livro")
    private Exemplar exemplar;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties("livrosEmprestados")
    private Usuario usuario;

    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolvida;

    public Emprestimo() {
    }

    public Emprestimo(Exemplar exemplar, Usuario usuario, LocalDate dataEmprestimo) {
        this.exemplar = exemplar;
        this.usuario = usuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataEmprestimo.plusDays(15);
        this.dataDevolvida = null;

        if (this.exemplar != null) {
            this.exemplar.setDisponivel(false);
        }
    }

    public Long getCodigoEmprestimo() {
        return codigoEmprestimo;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolvida() {
        return dataDevolvida;
    }

    public void setCodigoEmprestimo(Long codigoEmprestimo) {
        this.codigoEmprestimo = codigoEmprestimo;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public void setDataDevolvida(LocalDate dataDevolvida) {
        this.dataDevolvida = dataDevolvida;
    }

    public void devolver() {
        this.dataDevolvida = LocalDate.now();

        if (this.exemplar != null) {
            this.exemplar.setDisponivel(true);
        }
    }

    public LocalDate prolongarDataDevEmprestimo(int dias) {
        this.dataPrevistaDevolucao = this.dataPrevistaDevolucao.plusDays(dias);
        return this.dataPrevistaDevolucao;
    }

    public LocalDate prolongarDataDevEmprestimo() {
        this.dataPrevistaDevolucao = this.dataPrevistaDevolucao.plusDays(7);
        return this.dataPrevistaDevolucao;
    }

    @Override
    public String toString() {
        return "Empréstimo: " + codigoEmprestimo +
                " | Usuário: " + usuario.getNome() +
                " | Exemplar: " + exemplar.getcodigoExemplar() +
                " | Data empréstimo: " + dataEmprestimo +
                " | Data prevista devolução: " + dataPrevistaDevolucao;
    }
}