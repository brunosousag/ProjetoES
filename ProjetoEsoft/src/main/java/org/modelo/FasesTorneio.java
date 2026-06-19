package org.modelo;

import javax.swing.*;

public class FasesTorneio extends BaseFrame {
    private JButton btnEquipas;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;
    private JPanel janelaBracket;

    public FasesTorneio(String title) {
        super(title);

        setContentPane(janelaBracket);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnEquipas = btnEquipas;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();

        pack();
        setLocationRelativeTo(null);
    }
}