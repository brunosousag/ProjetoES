package org.modelo;

public class ItemCarrinho {

    public enum Tipo {
        BILHETE,
        PRODUTO
    }

    private final Tipo tipo;
    private final String descricao;
    private final String detalhe;
    private final double precoUnitario;
    private int quantidade;

    public ItemCarrinho(Tipo tipo, String descricao, String detalhe, double precoUnitario, int quantidade) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.detalhe = detalhe;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    public Tipo getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public String getDetalhe() { return detalhe; }
    public double getPrecoUnitario() { return precoUnitario; }
    public int getQuantidade() { return quantidade; }

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