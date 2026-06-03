package org.modelo;

import javax.swing.*;

public class GerirGrupos extends BaseFrame {
    private JPanel janelaGrupos;

    // componentes do menu/cabeçalho
    private JPanel menuPrincipal;
    private JLabel lblNomeCampeonato;
    private JButton btnGestao;
    private JButton btnClassificacaoGeral;
    private JButton btnMerch;
    private JButton btnCarrinho;

    private JPanel Grupo;
    private JButton guardarButton;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JSpinner spinner1;
    private JTextPane textPane1;

    public GerirGrupos(String title) {
        super(title);

        setContentPane(janelaGrupos);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnClassificacaoGeral = btnClassificacaoGeral;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();

        pack();
        setLocationRelativeTo(null);
    }
}