package org.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class JogoCalendario implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("HH:mm");

    private String grupo;
    private String equipaA;
    private String equipaB;
    private LocalDate data;
    private LocalTime hora;
    private String estadio;
    private int capacidadeTotal;
    private int bilhetesVendidos;
    private double precoBilhete;

    public JogoCalendario(
            String grupo,
            String equipaA,
            String equipaB,
            LocalDate data,
            LocalTime hora,
            String estadio,
            int capacidadeTotal,
            int bilhetesVendidos,
            double precoBilhete
    ) {
        this.grupo = grupo;
        this.equipaA = equipaA;
        this.equipaB = equipaB;
        this.data = data;
        this.hora = hora;
        this.estadio = estadio;
        this.capacidadeTotal = capacidadeTotal;
        this.bilhetesVendidos = bilhetesVendidos;
        this.precoBilhete = precoBilhete;
    }

    public String getGrupo() { return grupo; }
    public String getEquipaA() { return equipaA; }
    public String getEquipaB() { return equipaB; }
    public LocalDate getData() { return data; }
    public LocalTime getHora() { return hora; }
    public String getEstadio() { return estadio; }
    public int getCapacidadeTotal() { return capacidadeTotal; }
    public int getBilhetesVendidos() { return bilhetesVendidos; }
    public double getPrecoBilhete() { return precoBilhete; }

    public void setBilhetesVendidos(int bilhetesVendidos) {
        this.bilhetesVendidos = bilhetesVendidos;
    }

    public LocalDateTime getDataHora() {
        return LocalDateTime.of(data, hora);
    }

    public String getDataFormatada() {
        return data.format(FORMATO_DATA);
    }

    public String getHoraFormatada() {
        return hora.format(FORMATO_HORA);
    }

    public int getBilhetesDisponiveis() {
        return Math.max(0, capacidadeTotal - bilhetesVendidos);
    }

    public boolean isEsgotado() {
        return getBilhetesDisponiveis() <= 0;
    }

    public boolean isHoje() {
        return data.equals(LocalDate.now());
    }

    public boolean isAnterior() {
        return data.isBefore(LocalDate.now());
    }

    public String getDescricaoCompleta() {
        return equipaA + " vs " + equipaB + " (" + getDataFormatada() + " " + getHoraFormatada() + ")";
    }

    @Override
    public String toString() {
        return getDescricaoCompleta();
    }
}