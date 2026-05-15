package org.modelo;

import javax.swing.*;

public class HistoricoVendas extends JFrame {
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

    public HistoricoVendas(String title) {
        super(title);

        setContentPane(HistoricoVendas);

        UITheme.applyTheme(HistoricoVendas);
        UITheme.styleHeader(menuPrincipal);
        UITheme.styleTitleLabel(lblNomeCampeonato);

        UITheme.setBackground(ListaVendas, UITheme.BG_CARD);
        ListaVendas.setBorder(UITheme.cardBorder());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }
}
