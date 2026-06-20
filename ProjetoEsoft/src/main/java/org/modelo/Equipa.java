package org.modelo;

import java.io.Serializable;

public class Equipa implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String tipo;

    private Alojamento alojamento;
    private Deslocacao deslocacao;

    public Equipa(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
        this.alojamento = null;
        this.deslocacao = null;
    }

    public Equipa(String nome, String tipo,
                  Alojamento alojamento, Deslocacao deslocacao) {
        this.nome = nome;
        this.tipo = tipo;
        this.alojamento = alojamento;
        this.deslocacao = deslocacao;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
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

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        return nome;
    }
}
