package org.modelo;

import java.io.Serializable;

public class Deslocacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tipoTransporte;
    private String origem;
    private String destino;

    public Deslocacao(String tipoTransporte, String origem, String destino) {
        this.tipoTransporte = tipoTransporte;
        this.origem = origem;
        this.destino = destino;
    }

    public String getTipoTransporte() {
        return tipoTransporte;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setTipoTransporte(String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    @Override
    public String toString() {
        return tipoTransporte + ": " + origem + " → " + destino;
    }
}