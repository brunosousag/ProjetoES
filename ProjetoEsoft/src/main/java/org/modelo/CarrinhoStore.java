package org.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarrinhoStore {

    public interface CarrinhoListener {
        void onCarrinhoAlterado();
    }

    private static final CarrinhoStore INSTANCIA = new CarrinhoStore();

    private final List<ItemCarrinho> itens = new ArrayList<>();
    private final List<CarrinhoListener> listeners = new ArrayList<>();

    private CarrinhoStore() {
    }

    public static CarrinhoStore getInstance() {
        return INSTANCIA;
    }

    public List<ItemCarrinho> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public int getNumeroItens() {
        int total = 0;
        for (ItemCarrinho item : itens) {
            total += item.getQuantidade();
        }
        return total;
    }

    public boolean temBilhetes() {
        for (ItemCarrinho item : itens) {
            if (item.getTipo() == ItemCarrinho.Tipo.BILHETE) {
                return true;
            }
        }
        return false;
    }

    public double getTotal() {
        double total = 0.0;
        for (ItemCarrinho item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void adicionarBilhete(JogoCalendario jogo) {
        ItemCarrinho existente = encontrarBilhete(jogo);
        if (existente != null) {
            existente.incrementarQuantidade();
            notificarListeners();
            return;
        }

        itens.add(new ItemCarrinho(
                ItemCarrinho.Tipo.BILHETE,
                descricaoBilhete(jogo),
                detalheBilhete(jogo),
                jogo.getPrecoBilhete(),
                1
        ));
        notificarListeners();
    }

    public void removerUmBilhete(JogoCalendario jogo) {
        ItemCarrinho item = encontrarBilhete(jogo);
        if (item == null) {
            return;
        }
        if (item.getQuantidade() <= 1) {
            itens.remove(item);
        } else {
            item.setQuantidade(item.getQuantidade() - 1);
        }
        notificarListeners();
    }

    public int getQuantidadeBilhete(JogoCalendario jogo) {
        ItemCarrinho item = encontrarBilhete(jogo);
        return item == null ? 0 : item.getQuantidade();
    }

    private ItemCarrinho encontrarBilhete(JogoCalendario jogo) {
        String descricao = descricaoBilhete(jogo);
        String detalhe = detalheBilhete(jogo);
        for (ItemCarrinho item : itens) {
            if (item.getTipo() == ItemCarrinho.Tipo.BILHETE
                    && item.getDescricao().equals(descricao)
                    && item.getDetalhe().equals(detalhe)) {
                return item;
            }
        }
        return null;
    }

    private String descricaoBilhete(JogoCalendario jogo) {
        return jogo.getEquipaA() + " vs " + jogo.getEquipaB();
    }

    private String detalheBilhete(JogoCalendario jogo) {
        return jogo.getGrupo() + " · "
                + jogo.getDataFormatada() + " " + jogo.getHoraFormatada()
                + " · " + jogo.getEstadio();
    }

    public void adicionarProduto(Produto produto, String tamanho, int quantidade) {
        String descricao = produto.getNome();
        String detalhe = "Tamanho " + tamanho;

        for (ItemCarrinho item : itens) {
            if (item.getTipo() == ItemCarrinho.Tipo.PRODUTO
                    && item.getDescricao().equals(descricao)
                    && item.getDetalhe().equals(detalhe)) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                notificarListeners();
                return;
            }
        }

        itens.add(new ItemCarrinho(
                ItemCarrinho.Tipo.PRODUTO,
                descricao,
                detalhe,
                produto.getPreco(),
                quantidade
        ));
        notificarListeners();
    }

    public void removerItem(int indice) {
        if (indice < 0 || indice >= itens.size()) {
            return;
        }
        itens.remove(indice);
        notificarListeners();
    }

    public void limpar() {
        if (itens.isEmpty()) {
            return;
        }
        itens.clear();
        notificarListeners();
    }

    public void registarListener(CarrinhoListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removerListener(CarrinhoListener listener) {
        listeners.remove(listener);
    }

    private void notificarListeners() {
        for (CarrinhoListener listener : new ArrayList<>(listeners)) {
            listener.onCarrinhoAlterado();
        }
    }
}