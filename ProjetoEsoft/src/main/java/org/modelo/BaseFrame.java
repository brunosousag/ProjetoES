package org.modelo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class BaseFrame extends JFrame {

    protected JButton btnGestao;
    protected JButton btnEquipas;
    protected JButton btnMerch;
    protected JButton btnCarrinho;

    private CarrinhoStore.CarrinhoListener carrinhoListener;

    public BaseFrame(String title) {
        super(title);
    }

    protected void configurarMenuGestao() {

        JPopupMenu popup = new JPopupMenu();

        JMenuItem itemGrupos = new JMenuItem("Grupos do torneio");
        JMenuItem itemFases = new JMenuItem("Brackets do torneio");
        JMenuItem itemHistorico = new JMenuItem("Histórico de Vendas");
        JMenuItem itemGolos = new JMenuItem("Melhores Marcadores");
        JMenuItem itemJogos = new JMenuItem("Gerir Resultados");

        itemHistorico.addActionListener(e ->
                WindowManager.abrirJanela(
                        this,
                        "hostoricoVendas",
                        "A janela de historico de vendas já está aberta!",
                        new HistoricoVendas("Campeonato Mundial 2026 - Histórico de vendas")
                )
        );

        itemGrupos.addActionListener(e ->
                WindowManager.abrirJanela(
                        this,
                        "gerirGrupos",
                        "A janela Gerir Torneio já está aberta!",
                        new GerirGrupos("Campeonato Mundial 2026 - Gerir Grupos")
                )
        );

        itemFases.addActionListener(e ->
                WindowManager.abrirJanela(
                        this,
                        "verBracket",
                        "A janela de ver Fases de Torneio ja está aberta!",
                        new FasesTorneio("Campeonato Mundial 2026 - Fases de Torneio")
                )
        );

        itemGolos.addActionListener(e ->
                WindowManager.abrirJanela(
                        this,
                        "maisGolosMarcados",
                        "A janela de Mais Golos Marcados já está aberta!",
                        new maisGolosMarcados("Campeonato Mundial 2026 - Mais Golos Marcados")
                )
        );

        itemJogos.addActionListener(e ->
                WindowManager.abrirJanela(
                        this,
                        "gerirJogos",
                        "A janela Registar Jogos já está aberta!",
                        new GerirJogos("Campeonato Mundial 2026 - Registar Jogos")
                )
        );

        popup.add(itemGrupos);
        popup.add(itemFases);
        popup.add(itemHistorico);
        popup.add(itemGolos);
        popup.add(itemJogos);

        btnGestao.addActionListener(e ->
                popup.show(btnGestao, 0, btnGestao.getHeight())
        );

        configurarMenuEquipas();

        btnMerch.addActionListener(this::abrirMerch);
        btnCarrinho.addActionListener(this::abrirCarrinho);

        sincronizarContadorCarrinho();
    }

    private void sincronizarContadorCarrinho() {
        atualizarTextoBtnCarrinho();

        carrinhoListener = this::atualizarTextoBtnCarrinho;
        CarrinhoStore.getInstance().registarListener(carrinhoListener);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                CarrinhoStore.getInstance().removerListener(carrinhoListener);
            }
        });
    }

    private void atualizarTextoBtnCarrinho() {
        if (btnCarrinho == null) return;
        int n = CarrinhoStore.getInstance().getNumeroItens();
        btnCarrinho.setText("🛒 " + n);
    }

    /** Dropdown do botão EQUIPAS (cabeçalho de todas as páginas). */
    protected void configurarMenuEquipas() {

        JPopupMenu popup = new JPopupMenu();

        JMenuItem itemAdicionar = new JMenuItem("Adicionar Equipas");
        JMenuItem itemMostrar = new JMenuItem("Mostrar Equipas");
        JMenuItem itemDeslocacao = new JMenuItem("Alterar Deslocação");
        JMenuItem itemAlojamento = new JMenuItem("Alterar Alojamento");

        itemAdicionar.addActionListener(e ->
                WindowManager.abrirJanela(
                        this,
                        "gerirEquipas",
                        "A janela Adicionar Equipas já está aberta!",
                        new GerirEquipas("Campeonato Mundial 2026 - Adicionar Equipas")
                )
        );

        itemMostrar.addActionListener(e ->
                WindowManager.abrirJanela(
                        this,
                        "mostrarEquipas",
                        "A janela Mostrar Equipas já está aberta!",
                        new MostrarEquipas("Campeonato Mundial 2026 - Mostrar Equipas")
                )
        );

        itemDeslocacao.addActionListener(e ->
                WindowManager.abrirJanela(
                        this,
                        "alterarDeslocacao",
                        "A janela Alterar Deslocação já está aberta!",
                        new AlterarDeslocacao("Campeonato Mundial 2026 - Alterar Deslocação")
                )
        );

        itemAlojamento.addActionListener(e ->
                WindowManager.abrirJanela(
                        this,
                        "alterarAlojamento",
                        "A janela Alterar Alojamento já está aberta!",
                        new AlterarAlojamento("Campeonato Mundial 2026 - Alterar Alojamento")
                )
        );

        popup.add(itemAdicionar);
        popup.add(itemMostrar);
        popup.add(itemDeslocacao);
        popup.add(itemAlojamento);

        btnEquipas.addActionListener(e ->
                popup.show(btnEquipas, 0, btnEquipas.getHeight())
        );
    }

    private void abrirMerch(ActionEvent e) {
        WindowManager.abrirJanela(
                this,
                "comprarMerch",
                "A janela Comprar Merch já está aberta!",
                new ComprarMerch("Campeonato Mundial 2026 - Comprar Merch")
        );
    }

    private void abrirCarrinho(ActionEvent e) {
        WindowManager.abrirJanela(
                this,
                "carrinho",
                "A janela Carrinho já está aberta!",
                new Carrinho("Campeonato Mundial 2026 - Carrinho")
        );
    }


}