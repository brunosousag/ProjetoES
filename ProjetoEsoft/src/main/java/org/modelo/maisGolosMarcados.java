package org.modelo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private static final int INTERVALO_ATUALIZACAO_MS = 1000;

    private Timer timerAtualizacao;
    private String ultimaAssinatura = "";

    public maisGolosMarcados(String title) {
        super(title);

        setContentPane(panel1);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnEquipas = btnEquipas;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();
        configurarLista();
        carregarTopGolos(true);
        iniciarAtualizacaoAutomatica();

        pack();
        setLocationRelativeTo(null);
    }

    private void configurarLista() {
        list1.setCellRenderer(new MarcadorRenderer());
        list1.setFixedCellHeight(34);
        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Atualiza automaticamente a tabela dos melhores marcadores.
     *
     * A fonte correta dos golos passa a ser o ficheiro de jogos, porque é nesse
     * ficheiro que o ecrã GerirJogos guarda os marcadores escolhidos para cada
     * partida. Assim, a estatística reflete todos os jogos já registados, de
     * todas as equipas, e não fica presa aos valores antigos do ficheiro de jogadores.
     */
    private void iniciarAtualizacaoAutomatica() {
        timerAtualizacao = new Timer(INTERVALO_ATUALIZACAO_MS, e -> carregarTopGolos(false));
        timerAtualizacao.setRepeats(true);
        timerAtualizacao.start();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                pararAtualizacaoAutomatica();
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                pararAtualizacaoAutomatica();
            }
        });
    }

    private void pararAtualizacaoAutomatica() {
        if (timerAtualizacao != null) {
            timerAtualizacao.stop();
            timerAtualizacao = null;
        }
    }

    private void carregarTopGolos(boolean forcarAtualizacao) {
        ArrayList<Jogo> jogos = RepositorioDados.carregarJogos();
        String assinaturaAtual = criarAssinaturaJogos(jogos);

        if (!forcarAtualizacao && Objects.equals(assinaturaAtual, ultimaAssinatura)) {
            return;
        }

        ultimaAssinatura = assinaturaAtual;

        List<MarcadorEstatistica> top = calcularMelhoresMarcadores(jogos);

        DefaultListModel<MarcadorEstatistica> modelo = new DefaultListModel<>();
        for (MarcadorEstatistica marcador : top) {
            modelo.addElement(marcador);
        }

        list1.setModel(modelo);
    }

    private List<MarcadorEstatistica> calcularMelhoresMarcadores(ArrayList<Jogo> jogos) {
        Map<String, MarcadorEstatistica> estatisticas = new LinkedHashMap<>();

        for (Jogo jogo : jogos) {
            if (jogo == null || !jogo.isTerminado() || jogo.getMarcadores().isEmpty()) {
                continue;
            }

            for (Map.Entry<String, Integer> entrada : jogo.getMarcadores().entrySet()) {
                String nomeJogador = entrada.getKey();
                int golos = entrada.getValue() == null ? 0 : entrada.getValue();

                if (nomeJogador == null || nomeJogador.isBlank() || golos <= 0) {
                    continue;
                }

                String equipa = descobrirEquipaDoMarcador(nomeJogador, jogo);
                String chave = nomeJogador + "|" + equipa;

                MarcadorEstatistica existente = estatisticas.get(chave);
                if (existente == null) {
                    estatisticas.put(chave, new MarcadorEstatistica(nomeJogador, equipa, golos));
                } else {
                    existente.adicionarGolos(golos);
                }
            }
        }

        return estatisticas.values()
                .stream()
                .sorted(
                        Comparator.comparingInt(MarcadorEstatistica::getGolos).reversed()
                                .thenComparing(MarcadorEstatistica::getNome)
                                .thenComparing(MarcadorEstatistica::getEquipa)
                )
                .limit(TOP_N)
                .collect(Collectors.toList());
    }

    private String descobrirEquipaDoMarcador(String nomeJogador, Jogo jogo) {
        if (jogadorPertenceAEquipa(nomeJogador, jogo.getEquipaA())) {
            return jogo.getEquipaA();
        }

        if (jogadorPertenceAEquipa(nomeJogador, jogo.getEquipaB())) {
            return jogo.getEquipaB();
        }

        for (Jogador jogador : RepositorioDados.carregarJogadores()) {
            if (jogador.getNome().equals(nomeJogador)) {
                String equipa = RepositorioDados.nomeEquipaPorId(jogador.getEquipaId());
                return equipa == null || equipa.isBlank() ? "-" : equipa;
            }
        }

        return "-";
    }

    private boolean jogadorPertenceAEquipa(String nomeJogador, String nomeEquipa) {
        if (nomeJogador == null || nomeEquipa == null || nomeEquipa.isBlank()) {
            return false;
        }

        for (Jogador jogador : RepositorioDados.jogadoresDaEquipa(nomeEquipa)) {
            if (nomeJogador.equals(jogador.getNome())) {
                return true;
            }
        }

        return false;
    }

    private String criarAssinaturaJogos(ArrayList<Jogo> jogos) {
        StringBuilder sb = new StringBuilder();

        for (Jogo jogo : jogos) {
            if (jogo == null) continue;

            sb.append(jogo.getNumero()).append('|')
                    .append(jogo.getFase()).append('|')
                    .append(jogo.getEquipaA()).append('|')
                    .append(jogo.getEquipaB()).append('|')
                    .append(jogo.getGolosA()).append('|')
                    .append(jogo.getGolosB()).append('|')
                    .append(jogo.isTerminado()).append('|');

            for (Map.Entry<String, Integer> entrada : jogo.getMarcadores().entrySet()) {
                sb.append(entrada.getKey()).append(':').append(entrada.getValue()).append(';');
            }

            sb.append('\n');
        }

        return sb.toString();
    }

    private static class MarcadorEstatistica {
        private final String nome;
        private final String equipa;
        private int golos;

        MarcadorEstatistica(String nome, String equipa, int golos) {
            this.nome = nome;
            this.equipa = equipa;
            this.golos = golos;
        }

        String getNome() {
            return nome;
        }

        String getEquipa() {
            return equipa;
        }

        int getGolos() {
            return golos;
        }

        void adicionarGolos(int golos) {
            this.golos += golos;
        }
    }

    /**
     * Desenha cada linha com as mesmas 4 colunas dos cabeçalhos
     * (POS / Nome / Equipa / Golos), centradas, para ficarem alinhadas.
     */
    private static class MarcadorRenderer extends JPanel implements ListCellRenderer<MarcadorEstatistica> {

        private final JLabel lblPos = criarLabel();
        private final JLabel lblNome = criarLabel();
        private final JLabel lblEquipa = criarLabel();
        private final JLabel lblGolos = criarLabel();

        MarcadorRenderer() {
            setLayout(new GridLayout(1, 4));
            setOpaque(true);
            add(lblPos);
            add(lblNome);
            add(lblEquipa);
            add(lblGolos);
        }

        private static JLabel criarLabel() {
            JLabel label = new JLabel("", SwingConstants.CENTER);
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            return label;
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends MarcadorEstatistica> list,
                MarcadorEstatistica marcador,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            lblPos.setText(String.valueOf(index + 1));
            lblNome.setText(marcador.getNome());
            lblEquipa.setText(marcador.getEquipa());
            lblGolos.setText(String.valueOf(marcador.getGolos()));

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
