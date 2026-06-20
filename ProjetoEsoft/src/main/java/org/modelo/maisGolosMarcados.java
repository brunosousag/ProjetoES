package org.modelo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class maisGolosMarcados extends BaseFrame {
    private JPanel panel1;
    private JPanel menuPrincipal;
    private JButton btnEquipas;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;
    private JLabel lblNomeCampeonato;
    private JList list1;

    private static final int TOP_N = 10;

    public maisGolosMarcados(String title) {
        super(title);

        setContentPane(panel1);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnEquipas = btnEquipas;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();
        carregarTopGolos();

        pack();
        setLocationRelativeTo(null);
    }

    private void carregarTopGolos() {
        ArrayList<Jogador> jogadores = RepositorioDados.carregarJogadores();

        jogadores.sort(Comparator.comparingInt(Jogador::getGolos).reversed());

        List<Jogador> top = jogadores.subList(0, Math.min(TOP_N, jogadores.size()));

        DefaultListModel<Jogador> modelo = new DefaultListModel<>();
        for (Jogador jogador : top) {
            modelo.addElement(jogador);
        }

        list1.setModel(modelo);
        list1.setCellRenderer(new JogadorRenderer());
        list1.setFixedCellHeight(30);
    }

    /**
     * Desenha cada linha com as mesmas 4 colunas dos cabeçalhos
     * (POS / Nome / Equipa / Golos), centradas, para ficarem alinhadas.
     */
    private static class JogadorRenderer extends JPanel implements ListCellRenderer<Jogador> {

        private final JLabel lblPos = criarLabel();
        private final JLabel lblNome = criarLabel();
        private final JLabel lblEquipa = criarLabel();
        private final JLabel lblGolos = criarLabel();

        JogadorRenderer() {
            setLayout(new GridLayout(1, 4));
            setOpaque(true);
            add(lblPos);
            add(lblNome);
            add(lblEquipa);
            add(lblGolos);
        }

        private static JLabel criarLabel() {
            return new JLabel("", SwingConstants.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends Jogador> list, Jogador jogador,
                int index, boolean isSelected, boolean cellHasFocus) {

            lblPos.setText(String.valueOf(index + 1));
            lblNome.setText(jogador.getNome());
            lblEquipa.setText(RepositorioDados.nomeEquipaPorId(jogador.getEquipaId()));
            lblGolos.setText(String.valueOf(jogador.getGolos()));

            Color fundo = isSelected ? list.getSelectionBackground() : list.getBackground();
            Color texto = isSelected ? list.getSelectionForeground() : list.getForeground();

            setBackground(fundo);
            lblPos.setForeground(texto);
            lblNome.setForeground(texto);
            lblEquipa.setForeground(texto);
            lblGolos.setForeground(texto);

            return this;
        }
    }
}
