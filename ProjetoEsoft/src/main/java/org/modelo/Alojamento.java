package org.modelo;

import java.io.Serializable;

public class Alojamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nomeHotel;
    private String morada;

    public Alojamento(String nomeHotel, String morada) {
        this.nomeHotel = nomeHotel;
        this.morada = morada;
    }

    public String getNomeHotel() {
        return nomeHotel;
    }

    public String getMorada() {
        return morada;
    }

    public void setNomeHotel(String nomeHotel) {
        this.nomeHotel = nomeHotel;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    @Override
    public String toString() {
        return nomeHotel + " - " + morada;
    }
}
