package org.modelo;

import java.io.Serializable;

/**
 * Um estádio onde se realizam jogos, com a respetiva capacidade.
 *
 * Guardado em ficheiro próprio (estadios.dat) para poder ser reutilizado
 * por várias classes (geração do calendário, venda de bilhetes, etc.).
 */
public class Estadio implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private int capacidade;
    private String cidade;
    private String estado;

    public Estadio(String nome, int capacidade, String cidade, String estado) {
        this.nome = nome;
        this.capacidade = capacidade;
        this.cidade = cidade;
        this.estado = estado;
    }

    public String getNome() {
        return nome;
    }
    public int getCapacidade() {
        return capacidade;
    }
    public String getCidade() {return cidade;}
    public String getEstado() {return estado;}
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }
    public void setCidade(String cidade) {this.cidade = cidade;}
    public void setEstado(String estado) {this.estado = estado;}

    @Override
    public String toString() {
        return nome + " (" + capacidade + " lugares )" + "(" + capacidade + " localizacao)";
    }
}