package org.modelo;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class JanelaPrincipal extends JFrame {

    private JPanel janelaPrincipal;
    private JPanel menuPrincipal;
    private JButton btnClassificacaoGeral;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;
    private JPanel jogoListado;
    private JButton btnComprar;
    private JCheckBox adicionarAoCarrinhoCheckBox;
    private JPanel listaGrupo;
    private JPanel totalMerch;

    //verificação das abas
    private GerirGrupos gerirGruposAberto;
    private ComprarBilhete comprarBilheteAberto;
    private ComprarMerch comprarMerchAberto;

    //CALMA!!!!!!!!! NAO TIRA ISSO AQ
    //private VerBracket verBracketAberto;
    //private ClassificacaoGeral classificacaoGeralAberta;
    //private Carrinho carrinhoAberto;


    public JanelaPrincipal(String title) {
        super(title);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(janelaPrincipal);
        pack();
        setLocationRelativeTo(null);

        configurarMenuGestao();
    }

    private void configurarMenuGestao() {
        JPopupMenu popup = new JPopupMenu();

        JMenuItem itemEquipas = new JMenuItem("Gerir Equipas");
        JMenuItem itemGrupos  = new JMenuItem("Gerir Grupos");
        JMenuItem itemFases   = new JMenuItem("Gerir Fases do Torneio");

        itemEquipas.addActionListener(e -> System.out.println("Abrir gestão de Equipas"));
        itemGrupos.addActionListener(this::btnGrupoActionPerformed);
        itemFases.addActionListener(e -> System.out.println("Abrir gestão de Fases"));
        btnComprar.addActionListener(this::btnComprarActionPerformed);

        //CALMA
        //verFases.addActionListener(this::btnBracketActionPerformed);
        //btnClassificacaoGeral.addActionListener(this::btnClassificacaoGeralActionPerformed);
        //btnCarrinho.addActionListener(this::btnCarrinhoActionPerformed);

        popup.add(itemEquipas);
        popup.add(itemGrupos);
        popup.add(itemFases);

        btnGestao.addActionListener(e ->
                popup.show(btnGestao, 0, btnGestao.getHeight())
        );

        btnMerch.addActionListener(this::btnMerchActionPerformed);
    }

    private void btnMerchActionPerformed(ActionEvent actionEvent) {

        if (comprarMerchAberto != null && comprarMerchAberto.isVisible()) {
            JOptionPane.showMessageDialog(this, "A janela Comprar Merch já está aberta!");
            comprarMerchAberto.toFront();
            return;
        }

        comprarMerchAberto = new ComprarMerch("Campeonato Mundial 2026 - Comprar Merch");

        comprarMerchAberto.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                comprarMerchAberto = null;
            }
        });

        comprarMerchAberto.setVisible(true);
    }

    private void btnGrupoActionPerformed(ActionEvent actionEvent) {

        if (gerirGruposAberto != null && gerirGruposAberto.isVisible()) {
            JOptionPane.showMessageDialog(this, "A janela Gerir Grupos já está aberta!");
            gerirGruposAberto.toFront();
            return;
        }

        gerirGruposAberto = new GerirGrupos("Campeonato Mundial 2026 - Gerir Grupos");

        gerirGruposAberto.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                gerirGruposAberto = null;
            }
        });

        gerirGruposAberto.setVisible(true);
    }

    private void btnComprarActionPerformed(ActionEvent actionEvent) {

        if (comprarBilheteAberto != null && comprarBilheteAberto.isVisible()) {
            JOptionPane.showMessageDialog(this, "A janela Comprar Bilhete já está aberta!");
            comprarBilheteAberto.toFront();
            return;
        }

        comprarBilheteAberto = new ComprarBilhete("Campeonato Mundial 2026 - Comprar bilhete");

        comprarBilheteAberto.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                comprarBilheteAberto = null;
            }
        });

        comprarBilheteAberto.setVisible(true);
    }

    //CLMA
//    private void btnBracketActionPerformed(ActionEvent actionEvent) {
//
//        if (verBracketAberto != null && verBracketAberto.isVisible()) {
//            JOptionPane.showMessageDialog(
//                    this,
//                    "A janela Ver Bracket já está aberta!"
//            );
//
//            verBracketAberto.toFront();
//            return;
//        }
//
//        verBracketAberto = new VerBracket("Campeonato Mundial 2026 - Ver Bracket");
//
//        verBracketAberto.addWindowListener(new java.awt.event.WindowAdapter() {
//            @Override
//            public void windowClosed(java.awt.event.WindowEvent e) {
//                verBracketAberto = null;
//            }
//        });
//
//        verBracketAberto.setVisible(true);
//    }

//    private void btnClassificacaoGeralActionPerformed(ActionEvent actionEvent) {
//
//        if (classificacaoGeralAberta != null && classificacaoGeralAberta.isVisible()) {
//
//            JOptionPane.showMessageDialog(
//                    this,
//                    "A janela Classificação Geral já está aberta!"
//            );
//
//            classificacaoGeralAberta.toFront();
//            classificacaoGeralAberta.requestFocus();
//            return;
//        }
//
//        classificacaoGeralAberta =
//                new ClassificacaoGeral("Campeonato Mundial 2026 - Classificação Geral");
//
//        classificacaoGeralAberta.addWindowListener(new java.awt.event.WindowAdapter() {
//            @Override
//            public void windowClosed(java.awt.event.WindowEvent e) {
//                classificacaoGeralAberta = null;
//            }
//        });
//
//        classificacaoGeralAberta.setVisible(true);
//    }

//    private void btnCarrinhoActionPerformed(ActionEvent actionEvent) {
//
//        if (carrinhoAberto != null && carrinhoAberto.isVisible()) {
//
//            JOptionPane.showMessageDialog(
//                    this,
//                    "A janela Carrinho já está aberta!"
//            );
//
//            carrinhoAberto.toFront();
//            carrinhoAberto.requestFocus();
//            return;
//        }
//
//        carrinhoAberto =
//                new Carrinho("Campeonato Mundial 2026 - Carrinho");
//
//        carrinhoAberto.addWindowListener(new java.awt.event.WindowAdapter() {
//            @Override
//            public void windowClosed(java.awt.event.WindowEvent e) {
//                carrinhoAberto = null;
//            }
//        });
//
//        carrinhoAberto.setVisible(true);
//    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JanelaPrincipal("Campeonato Mundial 2026").setVisible(true);
        });
    }

    //carrinho - Carrinho
}
