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

    // componentes próprios da página
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JPanel Grupo;
    private JButton adicionarEquipaButton;
    private JButton modificarGrupoMudaEstadoButton;

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