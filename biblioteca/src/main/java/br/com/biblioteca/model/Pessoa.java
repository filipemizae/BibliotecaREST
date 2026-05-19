package br.com.biblioteca.model;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Pessoa {

    private String nome;
    private String dataNascimento;
    private String telefone;

    public Pessoa(){
    }

    public Pessoa(String nome, String dataNascimento, String telefone) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setDataNascimento(String dataNascimento) {
    this.dataNascimento = dataNascimento;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + "  Data de nascimento: " + dataNascimento + "  Telefone: " + telefone;
    }
    
}
