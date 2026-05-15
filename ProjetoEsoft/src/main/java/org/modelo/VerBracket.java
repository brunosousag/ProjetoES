package org.modelo;

import javax.swing.*;

public class VerBracket extends JFrame {
    private JPanel menuPrincipal;
    private JLabel lblNomeCampeonato;
    private JButton btnClassificacaoGeral;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;
    private JPanel janelaBracket;

    public VerBracket(String title){
        super(title);

        setContentPane(janelaBracket);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800,600);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VerBracket("Campeonato Mundial 2026").setVisible(true);
        });
    }
}
