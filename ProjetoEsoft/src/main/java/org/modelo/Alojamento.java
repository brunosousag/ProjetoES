package org.modelo;

import java.io.Serializable;

public class Alojamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nomeHotel;
    private String morada;
    private int numeroQuartos;

    public Alojamento(String nomeHotel, String morada, int numeroQuartos) {
        this.nomeHotel = nomeHotel;
        this.morada = morada;
        this.numeroQuartos = numeroQuartos;
    }

    public String getNomeHotel() {
        return nomeHotel;
    }

    public String getMorada() {
        return morada;
    }

    public int getNumeroQuartos() {
        return numeroQuartos;
    }

    public void setNomeHotel(String nomeHotel) {
        this.nomeHotel = nomeHotel;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public void setNumeroQuartos(int numeroQuartos) {
        this.numeroQuartos = numeroQuartos;
    }

    @Override
    public String toString() {
        return nomeHotel + " - " + morada;
    }
}
