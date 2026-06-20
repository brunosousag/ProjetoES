package org.modelo;

import javax.swing.*;

public class FasesTorneio extends BaseFrame {
    private JButton btnEquipas;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;
    private JPanel janelaBracket;
    private JLabel lblOitavos_jogo1_equipa1;
    private JLabel lblOitavos_jogo1_equipa2;
    private JLabel lblOitavos_jogo2_equipa1;
    private JLabel lblOitavos_jogo2_equipa2;
    private JLabel lblOitavos_jogo3_equipa1;
    private JLabel lblOitavos_jogo3_equipa2;
    private JLabel lblOitavos_jogo4_equipa1;
    private JLabel lblOitavos_jogo4_equipa2;
    private JLabel lblOitavos_jogo5_equipa1;
    private JLabel lblOitavos_jogo5_equipa2;
    private JLabel lblOitavos_jogo6_equipa1;
    private JLabel lblOitavos_jogo6_equipa2;
    private JLabel lblOitavos_jogo7_equipa1;
    private JLabel lblOitavos_jogo7_equipa2;
    private JLabel lblOitavos_jogo8_equipa1;
    private JLabel lblOitavos_jogo8_equipa2;
    private JLabel lblQuartos_jogo1_equipa1;
    private JLabel lblQuartos_jogo1_equipa2;
    private JLabel lblQuartos_jogo2_equipa1;
    private JLabel lblQuartos_jogo2_equipa2;
    private JLabel lblQuartos_jogo3_equipa1;
    private JLabel lblQuartos_jogo3_equipa2;
    private JLabel lblQuartos_jogo4_equipa1;
    private JLabel lblQuartos_jogo4_equipa2;
    private JLabel lblMeia_jogo1_equipa1;
    private JLabel lblMeia_jogo1_equipa2;
    private JLabel lblMeia_jogo2_equipa1;
    private JLabel lblMeia_jogo2_equipa2;
    private JLabel lblFinal_jogo1_equipa1;
    private JLabel lblFinal_jogo1_equipa2;
    private JLabel lblVencedor;



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