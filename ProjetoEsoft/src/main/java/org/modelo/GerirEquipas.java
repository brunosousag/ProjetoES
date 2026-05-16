package org.modelo;

import javax.swing.*;

public class GerirEquipas extends BaseFrame{
    private JPanel gerirEquipas;
    private JLabel lblDadosEquipa;
    private JTextField txtFldNomeEquipa;
    private JTextField txtFldDescricao;
    private JTextField txtFldNacionalidade;
    private JLabel lblNacionalidade;
    private JLabel lblDescricao;
    private JLabel lblNomeEquipa;
    private JPanel menuPrincipal;
    private JButton btnClassificacaoGeral;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;
    private JLabel lblNomeCampeonato;
    private JButton btnAdicionarEquipa;
    private JCheckBox checkBox1;
    private JButton gerirDeslocaçãoButton;
    private JButton gerirAlojamentoButton;
    private JComboBox comboBox1;

    public GerirEquipas(String title) {
        super(title);

        setContentPane(gerirEquipas);
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
