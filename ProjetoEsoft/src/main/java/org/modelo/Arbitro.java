package org.modelo;

import java.io.Serializable;

public class Arbitro implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private int equipaId;

    public Arbitro(String nome, int equipaId) {
        this.nome = nome;
        this.equipaId = equipaId;
    }

    public String getNome() {
        return nome;
    }

    public int getEquipaId() {
        return equipaId;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEquipaId(int equipaId) {
        this.equipaId = equipaId;
    }

    @Override
    public String toString() {
        return nome;
    }
}
