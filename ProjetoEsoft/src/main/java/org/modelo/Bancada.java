package org.modelo;

import java.io.Serializable;

/**
 * Representa uma zona de bancada de um estádio (ex: "1º piso inferior Norte").
 *
 * Como simplificação do projeto, todos os estádios partilham o mesmo conjunto
 * de bancadas — basta um único ficheiro bancadas.dat. Os lugaresVendidos são
 * geridos globalmente por jogo (ver FinalizarCompra) e não persistem por
 * bancada+jogo: usamos o valor como aproximação de ocupação para mostrar a
 * cor na UI.
 */
public class Bancada implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Setor {
        NORTE, SUL, ESTE, OESTE, CAMAROTE, MOBILIDADE_REDUZIDA
    }

    public enum Piso {
        INFERIOR, SUPERIOR_2, SUPERIOR_3, CAMAROTE, MOBILIDADE_REDUZIDA
    }

    public enum Estado {
        DISPONIVEL, POUCOS_LUGARES, ESGOTADO
    }

    private final String nome;
    private final Setor setor;
    private final Piso piso;
    private final int capacidadeTotal;
    private int lugaresVendidos;
    private final double multiplicadorPreco;

    public Bancada(String nome, Setor setor, Piso piso,
                   int capacidadeTotal, double multiplicadorPreco) {
        this.nome = nome;
        this.setor = setor;
        this.piso = piso;
        this.capacidadeTotal = capacidadeTotal;
        this.lugaresVendidos = 0;
        this.multiplicadorPreco = multiplicadorPreco;
    }

    public String getNome() { return nome; }
    public Setor getSetor() { return setor; }
    public Piso getPiso() { return piso; }
    public int getCapacidadeTotal() { return capacidadeTotal; }
    public int getLugaresVendidos() { return lugaresVendidos; }
    public double getMultiplicadorPreco() { return multiplicadorPreco; }

    public int getLugaresDisponiveis() {
        return Math.max(0, capacidadeTotal - lugaresVendidos);
    }

    public void venderLugares(int quantidade) {
        this.lugaresVendidos = Math.min(capacidadeTotal, lugaresVendidos + quantidade);
    }

    public boolean isEsgotada() {
        return getLugaresDisponiveis() <= 0;
    }

    public boolean isPoucosLugares() {
        if (isEsgotada()) return false;
        return getLugaresDisponiveis() <= capacidadeTotal * 0.15;
    }

    public Estado getEstado() {
        if (isEsgotada()) return Estado.ESGOTADO;
        if (isPoucosLugares()) return Estado.POUCOS_LUGARES;
        return Estado.DISPONIVEL;
    }

    public double getPreco(double precoBase) {
        return precoBase * multiplicadorPreco;
    }

    @Override
    public String toString() {
        return nome;
    }
}
