package org.modelo;

import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class BaseFrame extends JFrame {

    protected JButton btnGestao;
    protected JButton btnClassificacaoGeral;
    protected JButton btnMerch;
    protected JButton btnCarrinho;

    public BaseFrame(String title) {
        super(title);
    }

    protected void configurarMenuGestao() {

        JPopupMenu popup = new JPopupMenu();

        JMenuItem itemEquipas = new JMenuItem("Gerir Equipas");
        JMenuItem itemGrupos = new JMenuItem("Gerir torneio");
        JMenuItem itemFases = new JMenuItem("Gerir Fases do Torneio");
        JMenuItem itemHistorico = new JMenuItem("Histórico de Vendas");
        JMenuItem itemGolos = new JMenuItem("Mais Golos Marcados");
        JMenuItem itemJogos = new JMenuItem("Registar Jogos");

        itemHistorico.addActionListener(e ->
                WindowManager.abrirJanela(
                        this,
                        "hostoricoVendas",
                        "A janela de historico de vendas já está aberta!",
                        new HistoricoVendas("Campeonato Mundial 2026 - Histórico de vendas")
                )
        );

        itemEquipas.addActionListener(e ->
                WindowManager.abrirJanela(
                        this,
                        "gerirEquipas",
                        "A janela Gerir Equipas já está aberta!",
                        new GerirEquipas("Campeonato Mundial 2026 - Gerir Equipas")
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

        popup.add(itemEquipas);
        popup.add(itemGrupos);
        popup.add(itemFases);
        popup.add(itemHistorico);
        popup.add(itemGolos);
        popup.add(itemJogos);

        btnGestao.addActionListener(e ->
                popup.show(btnGestao, 0, btnGestao.getHeight())
        );

        //btnClassificacaoGeral.addActionListener(this::abrirClassificacao);
        btnMerch.addActionListener(this::abrirMerch);
        //btnCarrinho.addActionListener(this::abrirCarrinho);
    }

//    private void abrirClassificacao(ActionEvent e) {
//        WindowManager.abrirJanela(
//                this,
//                "classificacaoGeral",
//                "A janela Classificação Geral já está aberta!",
//                new ClassificacaoGeral("Campeonato Mundial 2026 - Classificação Geral")
//        );
//    }

    private void abrirMerch(ActionEvent e) {
        WindowManager.abrirJanela(
                this,
                "comprarMerch",
                "A janela Comprar Merch já está aberta!",
                new ComprarMerch("Campeonato Mundial 2026 - Comprar Merch")
        );
    }

//    private void abrirCarrinho(ActionEvent e) {
//        WindowManager.abrirJanela(
//                this,
//                "carrinho",
//                "A janela Carrinho já está aberta!",
//                new Carrinho("Campeonato Mundial 2026 - Carrinho")
//        );
//    }


}