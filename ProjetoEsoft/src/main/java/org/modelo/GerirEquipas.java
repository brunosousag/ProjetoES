package org.modelo;

import javax.swing.*;
import java.io.Serializable;

public class GerirEquipas extends BaseFrame {
    private transient JPanel gerirEquipas;
    private JLabel lblDadosEquipa;
    private JTextField txtFldNomeEquipa;
    private JTextField txtFldDescricao;
    private JTextField txtFldNacionalidade;
    private transient JLabel lblNacionalidade;
    private transient JLabel lblDescricao;
    private transient JLabel lblNomeEquipa;
    private transient JPanel menuPrincipal;
    private transient JButton btnClassificacaoGeral;
    private transient JButton btnCarrinho;
    private transient JButton btnMerch;
    private transient JButton btnGestao;
    private transient JLabel lblNomeCampeonato;
    private transient JButton btnAdicionarEquipa;
    private transient JCheckBox checkBox1;
    private transient JButton gerirDeslocaçãoButton;
    private transient JButton gerirAlojamentoButton;
    private transient JComboBox comboBox1;

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

