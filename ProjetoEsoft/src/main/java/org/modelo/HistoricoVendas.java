package org.modelo;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class HistoricoVendas extends BaseFrame {

    private JPanel menuPrincipal;
    private JLabel lblNomeCampeonato;
    private JButton btnClassificacaoGeral;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;

    private JPanel HistoricoVendas;
    private JPanel HistoricoTitulo;
    private JPanel TituloVendas;
    private JPanel ListaVendas;

    private JList list1;
    private JList list2;
    private JList list3;

    private JButton imprimirButton;

    public HistoricoVendas(String title) {
        super(title);

        setContentPane(HistoricoVendas);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnClassificacaoGeral = btnClassificacaoGeral;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();

        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        list1.setCellRenderer(renderer);
        list2.setCellRenderer(renderer);
        list3.setCellRenderer(renderer);

        pack();
        setLocationRelativeTo(null);
    }
}