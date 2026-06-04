package org.modelo;

import java.io.Serializable;

public class Equipa implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String descricao;
    private String nacionalidade;

    private Alojamento alojamento;
    private Deslocacao deslocacao;

    public Equipa(String nome, String descricao, String nacionalidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.nacionalidade = nacionalidade;
        this.alojamento = null;
        this.deslocacao = null;
    }

    public Equipa(String nome, String descricao, String nacionalidade,
                  Alojamento alojamento, Deslocacao deslocacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.nacionalidade = nacionalidade;
        this.alojamento = alojamento;
        this.deslocacao = deslocacao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public Alojamento getAlojamento() {
        return alojamento;
    }

    public Deslocacao getDeslocacao() {
        return deslocacao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public void setAlojamento(Alojamento alojamento) {
        this.alojamento = alojamento;
    }

    public void setDeslocacao(Deslocacao deslocacao) {
        this.deslocacao = deslocacao;
    }

    public boolean temAlojamento() {
        return alojamento != null;
    }

    public boolean temDeslocacao() {
        return deslocacao != null;
    }

    @Override
    public String toString() {
        return nome + " (" + nacionalidade + ")";
    }
}