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
    private JButton BtnComprar;
    private JCheckBox checkBox1;
    private JPanel listaGrupo;
    private JLabel lblNomeCampeonato;

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
        BtnComprar.addActionListener(this::btnComprarActionPerformed);

        popup.add(itemEquipas);
        popup.add(itemGrupos);
        popup.add(itemFases);

        btnGestao.addActionListener(e ->
                popup.show(btnGestao, 0, btnGestao.getHeight())
        );

        btnMerch.addActionListener(this::btnMerchActionPerformed);
    }

    private void btnMerchActionPerformed(ActionEvent actionEvent) {
        ComprarMerch merch = new ComprarMerch("Comprar Merch");
        merch.setVisible(true);
    }

    private void btnGrupoActionPerformed(ActionEvent actionEvent) {
        GerirGrupos janela = new GerirGrupos("Gerir Grupos");
        janela.setVisible(true);
    }

    private void btnComprarActionPerformed(ActionEvent actionEvent) {
        ComprarBilhete janela = new ComprarBilhete("Comprar");
        janela.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JanelaPrincipal("Campeonato Mundial 2026").setVisible(true);
        });
    }
}
