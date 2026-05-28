package org.modelo;

import java.io.Serializable;
import java.util.HashMap;

public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private double preco;
    private HashMap<String, Integer> stockPorTamanho;

    public Produto(String nome, double preco, HashMap<String, Integer> stockPorTamanho) {
        this.nome = nome;
        this.preco = preco;
        this.stockPorTamanho = stockPorTamanho;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public HashMap<String, Integer> getStockPorTamanho() {
        return stockPorTamanho;
    }

    public int getStockDoTamanho(String tamanho) {
        return stockPorTamanho.getOrDefault(tamanho, 0);
    }

    public boolean temStockNoTamanho(String tamanho) {
        return getStockDoTamanho(tamanho) > 0;
    }

    public boolean temAlgumStock() {
        for (int stock : stockPorTamanho.values()) {
            if (stock > 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return nome + " - " + String.format("%.2f€", preco);
    }
}