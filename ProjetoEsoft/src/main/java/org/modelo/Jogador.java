package org.modelo;

import java.io.Serializable;

public class Jogador implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String equipa;
    private int golos;

    public Jogador(String nome, String equipa, int golos) {
        this.nome = nome;
        this.equipa = equipa;
        this.golos = golos;
    }

    public String getNome() {
        return nome;
    }

    public String getEquipa() {
        return equipa;
    }

    public int getGolos() {
        return golos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEquipa(String equipa) {
        this.equipa = equipa;
    }

    public void setGolos(int golos) {
        this.golos = golos;
    }

    @Override
    public String toString() {
        return nome + " (" + equipa + ") - " + golos + " golos";
    }
}
