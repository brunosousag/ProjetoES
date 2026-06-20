package org.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Uma venda finalizada — o que aparece no histórico de vendas.
 * Reúne os itens do carrinho no momento da compra + dados do cliente
 * e método de pagamento.
 */
public class Venda implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum MetodoPagamento {
        MULTIBANCO, DINHEIRO
    }

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final String numFatura;
    private final String nomeCliente;
    private final String nif;
    private final LocalDateTime dataHora;
    private final MetodoPagamento metodoPagamento;
    private final List<ItemCarrinho> itens;
    private final double total;

    public Venda(String numFatura, String nomeCliente, String nif,
                 LocalDateTime dataHora, MetodoPagamento metodoPagamento,
                 List<ItemCarrinho> itens, double total) {
        this.numFatura = numFatura;
        this.nomeCliente = nomeCliente;
        this.nif = nif;
        this.dataHora = dataHora;
        this.metodoPagamento = metodoPagamento;
        this.itens = new ArrayList<>(itens);
        this.total = total;
    }

    public String getNumFatura() { return numFatura; }
    public String getNomeCliente() { return nomeCliente; }
    public String getNif() { return nif; }
    public LocalDateTime getDataHora() { return dataHora; }
    public MetodoPagamento getMetodoPagamento() { return metodoPagamento; }
    public double getTotal() { return total; }

    public List<ItemCarrinho> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public String getDataFormatada() {
        return dataHora.toLocalDate().format(FORMATO_DATA);
    }

    public String getTotalFormatado() {
        return String.format("%.2f€", total).replace('.', ',');
    }
}
