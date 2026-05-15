package org.modelo;

import javax.swing.*;

public class GerirEquipas extends JFrame{
    private JPanel gerirEquipas;
    private JPanel menuPrincipal;
    private JLabel lblNomeCampeonato;
    private JButton btnClassificacaoGeral;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;
    private JLabel lblDadosEquipa;
    private JTextField txtFldNomeEquipa;
    private JButton btnAdicionarEquipa;
    private JTextField txtFldDescricao;
    private JTextField txtFldNacionalidade;
    private JLabel lblNacionalidade;
    private JLabel lblDescricao;
    private JLabel lblNomeEquipa;

    public GerirEquipas(String title) {
        super(title);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(gerirEquipas);
        pack();
        setLocationRelativeTo(null);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GerirEquipas("Campeonato Mundial 2026").setVisible(true);
        });
    }
}
