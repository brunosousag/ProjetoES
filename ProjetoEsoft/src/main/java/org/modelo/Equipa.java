package org.modelo;

import java.io.Serializable;

public class Equipa implements Serializable {
    private String nome;
    private String descricao;
    private String nacionalidade;

    public Equipa(String nome, String descricao, String nacionalidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.nacionalidade = nacionalidade;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getNacionalidade() { return nacionalidade; }

    @Override
    public String toString() {
        return nome + " (" + nacionalidade + ")";
    }
}
