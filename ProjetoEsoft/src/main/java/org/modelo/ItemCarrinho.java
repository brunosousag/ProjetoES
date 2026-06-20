package org.modelo;

import java.io.Serializable;

public class ItemCarrinho implements Serializable {

    private static final long serialVersionUID = 2L;

    public enum Tipo {
        BILHETE,
        PRODUTO
    }

    private final Tipo tipo;
    private final String descricao;
    private final String detalhe;
    private final double precoUnitario;
    private int quantidade;

    // Campos específicos de bilhete (null para produtos)
    private final String jogoDescricao;   // ex: "Portugal vs Argentina"
    private final String bancadaNome;     // ex: "1º piso inferior Sul"

    public ItemCarrinho(Tipo tipo, String descricao, String detalhe,
                        double precoUnitario, int quantidade) {
        this(tipo, descricao, detalhe, precoUnitario, quantidade, null, null);
    }

    public ItemCarrinho(Tipo tipo, String descricao, String detalhe,
                        double precoUnitario, int quantidade,
                        String jogoDescricao, String bancadaNome) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.detalhe = detalhe;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
        this.jogoDescricao = jogoDescricao;
        this.bancadaNome = bancadaNome;
    }

    public Tipo getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public String getDetalhe() { return detalhe; }
    public double getPrecoUnitario() { return precoUnitario; }
    public int getQuantidade() { return quantidade; }
    public String getJogoDescricao() { return jogoDescricao; }
    public String getBancadaNome() { return bancadaNome; }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void incrementarQuantidade() {
        this.quantidade++;
    }

    public double getSubtotal() {
        return precoUnitario * quantidade;
    }
}
