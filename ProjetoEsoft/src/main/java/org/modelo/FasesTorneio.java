package org.modelo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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
        preencherLabelsOitavos();

        pack();
        setLocationRelativeTo(null);
    }

    private void preencherLabelsOitavos() {
        List<JogoCalendario> jogosOitavos = obterJogosOitavos();
        JLabel[][] labelsOitavos = {
                {lblOitavos_jogo1_equipa1, lblOitavos_jogo1_equipa2},
                {lblOitavos_jogo2_equipa1, lblOitavos_jogo2_equipa2},
                {lblOitavos_jogo3_equipa1, lblOitavos_jogo3_equipa2},
                {lblOitavos_jogo4_equipa1, lblOitavos_jogo4_equipa2},
                {lblOitavos_jogo5_equipa1, lblOitavos_jogo5_equipa2},
                {lblOitavos_jogo6_equipa1, lblOitavos_jogo6_equipa2},
                {lblOitavos_jogo7_equipa1, lblOitavos_jogo7_equipa2},
                {lblOitavos_jogo8_equipa1, lblOitavos_jogo8_equipa2}
        };

        for (int i = 0; i < labelsOitavos.length; i++) {
            if (i < jogosOitavos.size()) {
                JogoCalendario jogo = jogosOitavos.get(i);
                labelsOitavos[i][0].setText(jogo.getEquipaA());
                labelsOitavos[i][1].setText(jogo.getEquipaB());
            } else {
                labelsOitavos[i][0].setText("Por definir");
                labelsOitavos[i][1].setText("Por definir");
            }
        }
    }

    private List<JogoCalendario> obterJogosOitavos() {
        List<JogoCalendario> jogosOitavos = filtrarJogosOitavos(
                RepositorioDados.carregarJogosCalendario()
        );

        if (!jogosOitavos.isEmpty()) {
            return jogosOitavos;
        }

        return new LogicaTorneio().gerarOitavos();
    }

    private List<JogoCalendario> filtrarJogosOitavos(List<JogoCalendario> jogos) {
        List<JogoCalendario> jogosOitavos = new ArrayList<>();
        for (JogoCalendario jogo : jogos) {
            if (LogicaTorneio.FASE_OITAVOS.equals(jogo.getGrupo())) {
                jogosOitavos.add(jogo);
            }
        }
        return jogosOitavos;
    }
}
