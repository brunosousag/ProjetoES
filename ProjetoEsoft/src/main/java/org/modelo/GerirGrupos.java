package org.modelo;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GerirGrupos extends BaseFrame {
    private JPanel janelaGrupos;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JPanel Grupo;
    private JButton adicionarEquipaButton;
    private JButton modificarGrupoMudaEstadoButton;


    public GerirGrupos(String title) {
        super(title);

        setContentPane(janelaGrupos);

        // ligar os botões da janela à BaseFrame
        super.btnGestao = btnGestao;
        super.btnClassificacaoGeral = btnClassificacaoGeral;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        // ativa o menu
        configurarMenuGestao();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
    }

    private void configurarMenu() {
        btnMerch.addActionListener(this::btnMerchActionPerformed);
    }

    private void btnMerchActionPerformed(ActionEvent e) {
        WindowManager.abrirJanela(
                this,
                "comprarMerch",
                "A janela Comprar Merch já está aberta!",
                new ComprarMerch("Campeonato Mundial 2026 - Comprar Merch")
        );
    }

}
