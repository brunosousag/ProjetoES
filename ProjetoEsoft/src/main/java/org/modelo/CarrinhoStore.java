package org.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarrinhoStore {

    public interface CarrinhoListener {
        void onCarrinhoAlterado();
    }
    // ÚNICA instância — criada uma vez, guardada para sempre
    private static final CarrinhoStore INSTANCIA = new CarrinhoStore();

    private final List<ItemCarrinho> itens = new ArrayList<>();
    private final List<CarrinhoListener> listeners = new ArrayList<>();

    //Construtor PRIVADO — ninguém de fora pode fazer `new CarrinhoStore()`
    private CarrinhoStore() {
    }

    //Único acesso público à instância
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

    /**
     * Adiciona bilhetes para o jogo numa bancada específica.
     * Se já existir um item para o mesmo jogo+bancada, soma à quantidade.
     */
    public void adicionarBilhete(JogoCalendario jogo, Bancada bancada, int quantidade) {
        if (quantidade <= 0) return;

        String descricao = descricaoBilhete(jogo);
        String detalhe = detalheBilhete(jogo, bancada);
        double preco = bancada.getPreco(jogo.getPrecoBilhete());

        ItemCarrinho existente = encontrarBilhete(descricao, detalhe);
        if (existente != null) {
            existente.setQuantidade(existente.getQuantidade() + quantidade);
            notificarListeners();
            return;
        }

        itens.add(new ItemCarrinho(
                ItemCarrinho.Tipo.BILHETE,
                descricao,
                detalhe,
                preco,
                quantidade,
                descricao,
                bancada.getNome()
        ));
        notificarListeners();
    }

    public int getQuantidadeBilhete(JogoCalendario jogo) {
        int total = 0;
        String descricao = descricaoBilhete(jogo);
        for (ItemCarrinho item : itens) {
            if (item.getTipo() == ItemCarrinho.Tipo.BILHETE
                    && descricao.equals(item.getJogoDescricao())) {
                total += item.getQuantidade();
            }
        }
        return total;
    }

    /** Quantidade de bilhetes no carrinho para um jogo + bancada específicos. */
    public int getQuantidadeBilhete(JogoCalendario jogo, Bancada bancada) {
        int total = 0;
        String descricao = descricaoBilhete(jogo);
        String nomeBancada = bancada.getNome();
        for (ItemCarrinho item : itens) {
            if (item.getTipo() == ItemCarrinho.Tipo.BILHETE
                    && descricao.equals(item.getJogoDescricao())
                    && nomeBancada.equals(item.getBancadaNome())) {
                total += item.getQuantidade();
            }
        }
        return total;
    }

    private ItemCarrinho encontrarBilhete(String descricao, String detalhe) {
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

    private String detalheBilhete(JogoCalendario jogo, Bancada bancada) {
        return bancada.getNome() + " · "
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

    //Avisa todas as janelas registadas que o carrinho mudou — para que elas possam atualizar a UI automaticamente.
    private void notificarListeners() {
        for (CarrinhoListener listener : new ArrayList<>(listeners)) {
            listener.onCarrinhoAlterado();
        }
    }
}
