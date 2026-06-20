package org.modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FasesTorneio extends BaseFrame {

    // ------------------------------------------------------------------ campos usados pelo .form

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

    /**
     * Timer usado para atualizar a árvore automaticamente quando outro ecrã
     * guarda resultados. Assim, não é preciso fechar e voltar a abrir o bracket.
     */
    private Timer timerAtualizacaoBracket;
    private String assinaturaUltimaAtualizacao = "";

    public FasesTorneio(String title) {
        super(title);

        // No IntelliJ, o .form cria estes componentes automaticamente.
        // Este fallback serve para a classe não rebentar caso seja executada fora do GUI Designer.
        if (janelaBracket == null) {
            criarInterfaceFallback();
        }

        setContentPane(janelaBracket);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Como os botões são privados nesta classe por causa do .form, temos de os passar
        // para os campos protegidos da BaseFrame, que é onde está a lógica dos menus.
        super.btnGestao = btnGestao;
        super.btnEquipas = btnEquipas;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();
        atualizarBracket();
        assinaturaUltimaAtualizacao = criarAssinaturaJogos();
        iniciarAtualizacaoAutomatica();

        setPreferredSize(new Dimension(1180, 650));
        pack();
        setLocationRelativeTo(null);
    }

    // ------------------------------------------------------------------ atualização automática

    /**
     * Atualiza o bracket de forma automática enquanto a janela está aberta.
     *
     * A cada segundo verifica se o ficheiro de jogos mudou. Se mudou, significa
     * que outra janela, por exemplo o Registo de Jogos, guardou um resultado.
     * Nesse caso, a árvore é reconstruída visualmente.
     */
    private void iniciarAtualizacaoAutomatica() {
        if (timerAtualizacaoBracket != null && timerAtualizacaoBracket.isRunning()) {
            return;
        }

        timerAtualizacaoBracket = new Timer(1000, e -> atualizarBracketSeNecessario());
        timerAtualizacaoBracket.setRepeats(true);
        timerAtualizacaoBracket.start();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                pararAtualizacaoAutomatica();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                pararAtualizacaoAutomatica();
            }
        });

        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                atualizarBracketSeNecessario();
            }
        });
    }

    private void pararAtualizacaoAutomatica() {
        if (timerAtualizacaoBracket != null) {
            timerAtualizacaoBracket.stop();
        }
    }

    private void atualizarBracketSeNecessario() {
        String assinaturaAtual = criarAssinaturaJogos();
        if (!assinaturaAtual.equals(assinaturaUltimaAtualizacao)) {
            atualizarBracket();
            assinaturaUltimaAtualizacao = criarAssinaturaJogos();
        }
    }

    /**
     * Cria uma assinatura textual simples do estado atual dos jogos.
     * Não serve para segurança; serve apenas para perceber se houve alterações
     * nos resultados, equipas, vencedores ou marcadores.
     */
    private String criarAssinaturaJogos() {
        ArrayList<Jogo> jogos = RepositorioDados.carregarJogos();
        StringBuilder sb = new StringBuilder();

        for (Jogo jogo : jogos) {
            sb.append(jogo.getNumero()).append('|')
                    .append(jogo.getFase()).append('|')
                    .append(jogo.getEquipaA()).append('|')
                    .append(jogo.getEquipaB()).append('|')
                    .append(jogo.getGolosA()).append('|')
                    .append(jogo.getGolosB()).append('|')
                    .append(jogo.isTerminado()).append('|')
                    .append(jogo.getVencedorDesempate()).append('|')
                    .append(jogo.getMarcadores()).append(';');
        }

        return sb.toString();
    }

    // ------------------------------------------------------------------ atualização dinâmica da árvore

    private void atualizarBracket() {
        List<Jogo> jogos = new LogicaTorneio().garantirArvoreEliminatoria();
        Map<Integer, Jogo> porNumero = jogosPorNumero(jogos);

        // Oitavos de final: jogos 49 a 56.
        preencherJogo(porNumero.get(49), lblOitavos_jogo1_equipa1, lblOitavos_jogo1_equipa2);
        preencherJogo(porNumero.get(50), lblOitavos_jogo2_equipa1, lblOitavos_jogo2_equipa2);
        preencherJogo(porNumero.get(51), lblOitavos_jogo3_equipa1, lblOitavos_jogo3_equipa2);
        preencherJogo(porNumero.get(52), lblOitavos_jogo4_equipa1, lblOitavos_jogo4_equipa2);
        preencherJogo(porNumero.get(53), lblOitavos_jogo5_equipa1, lblOitavos_jogo5_equipa2);
        preencherJogo(porNumero.get(54), lblOitavos_jogo6_equipa1, lblOitavos_jogo6_equipa2);
        preencherJogo(porNumero.get(55), lblOitavos_jogo7_equipa1, lblOitavos_jogo7_equipa2);
        preencherJogo(porNumero.get(56), lblOitavos_jogo8_equipa1, lblOitavos_jogo8_equipa2);

        // Quartos de final: jogos 57 a 60.
        preencherJogo(porNumero.get(57), lblQuartos_jogo1_equipa1, lblQuartos_jogo1_equipa2);
        preencherJogo(porNumero.get(58), lblQuartos_jogo2_equipa1, lblQuartos_jogo2_equipa2);
        preencherJogo(porNumero.get(59), lblQuartos_jogo3_equipa1, lblQuartos_jogo3_equipa2);
        preencherJogo(porNumero.get(60), lblQuartos_jogo4_equipa1, lblQuartos_jogo4_equipa2);

        // Meias-finais: jogos 61 e 62.
        preencherJogo(porNumero.get(61), lblMeia_jogo1_equipa1, lblMeia_jogo1_equipa2);
        preencherJogo(porNumero.get(62), lblMeia_jogo2_equipa1, lblMeia_jogo2_equipa2);

        // Final: jogo 64.
        preencherJogo(porNumero.get(64), lblFinal_jogo1_equipa1, lblFinal_jogo1_equipa2);

        atualizarVencedor(porNumero.get(64));

        janelaBracket.revalidate();
        janelaBracket.repaint();
    }

    private Map<Integer, Jogo> jogosPorNumero(List<Jogo> jogos) {
        Map<Integer, Jogo> mapa = new HashMap<>();
        for (Jogo jogo : jogos) {
            if (jogo.getNumero() > 0) {
                mapa.put(jogo.getNumero(), jogo);
            }
        }
        return mapa;
    }

    private void preencherJogo(Jogo jogo, JLabel lblEquipaA, JLabel lblEquipaB) {
        if (lblEquipaA == null || lblEquipaB == null) {
            return;
        }

        if (jogo == null) {
            lblEquipaA.setText("Por definir");
            lblEquipaB.setText("Por definir");
            return;
        }

        lblEquipaA.setText(textoEquipa(jogo, true));
        lblEquipaB.setText(textoEquipa(jogo, false));
    }

    private String textoEquipa(Jogo jogo, boolean equipaA) {
        String equipa = equipaA ? jogo.getEquipaA() : jogo.getEquipaB();
        if (equipa == null || equipa.trim().isEmpty()) {
            equipa = "Por definir";
        }

        if (!jogo.isTerminado()) {
            return equipa;
        }

        int golos = equipaA ? jogo.getGolosA() : jogo.getGolosB();
        String vencedor = jogo.getVencedora();
        boolean ganhou = vencedor != null && vencedor.equals(equipa);

        return (ganhou ? "🏆 " : "") + equipa + "  " + golos;
    }

    private void atualizarVencedor(Jogo finalMundial) {
        if (lblVencedor == null) {
            return;
        }

        if (finalMundial != null && finalMundial.isTerminado() && finalMundial.getVencedora() != null) {
            lblVencedor.setText("🏆 " + finalMundial.getVencedora());
        } else {
            lblVencedor.setText("Por definir");
        }
    }

    // ------------------------------------------------------------------ fallback sem .form

    /**
     * Interface simples usada apenas se a classe for executada sem instrumentação do .form.
     * No teu projecto, o IntelliJ deve continuar a usar o FasesTorneio.form normalmente.
     */
    private void criarInterfaceFallback() {
        janelaBracket = new JPanel(new BorderLayout());
        janelaBracket.setBackground(new Color(245, 246, 248));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(15, 41, 91));
        header.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel titulo = new JLabel("🏆 CAMPEONATO MUNDIAL 2026");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        header.add(titulo, BorderLayout.WEST);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.setOpaque(false);
        btnGestao = criarBotaoMenu("TORNEIO", new Color(24, 38, 84));
        btnEquipas = criarBotaoMenu("EQUIPAS", new Color(30, 80, 120));
        btnMerch = criarBotaoMenu("MERCH", new Color(120, 70, 110));
        btnCarrinho = criarBotaoMenu("🛒 0", new Color(200, 90, 70));
        botoes.add(btnGestao);
        botoes.add(btnEquipas);
        botoes.add(btnMerch);
        botoes.add(btnCarrinho);
        header.add(botoes, BorderLayout.EAST);

        janelaBracket.add(header, BorderLayout.NORTH);

        JPanel corpo = new JPanel(new GridLayout(1, 5, 12, 0));
        corpo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        corpo.setBackground(new Color(245, 246, 248));

        JLabel[][] oitavos = criarLabels(8);
        JLabel[][] quartos = criarLabels(4);
        JLabel[][] meias = criarLabels(2);
        JLabel[][] finalLabels = criarLabels(1);

        atribuirLabelsOitavos(oitavos);
        atribuirLabelsQuartos(quartos);
        atribuirLabelsMeias(meias);
        lblFinal_jogo1_equipa1 = finalLabels[0][0];
        lblFinal_jogo1_equipa2 = finalLabels[0][1];
        lblVencedor = new JLabel("Por definir");
        lblVencedor.setFont(lblVencedor.getFont().deriveFont(Font.BOLD, 14f));

        corpo.add(criarColuna("Oitavos", oitavos));
        corpo.add(criarColuna("Quartos", quartos));
        corpo.add(criarColuna("Meia-final", meias));
        corpo.add(criarColuna("Final", finalLabels));

        JPanel colunaVencedor = new JPanel();
        colunaVencedor.setLayout(new BoxLayout(colunaVencedor, BoxLayout.Y_AXIS));
        colunaVencedor.setBackground(new Color(230, 230, 230));
        colunaVencedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel tituloVencedor = new JLabel("Vencedor");
        tituloVencedor.setFont(tituloVencedor.getFont().deriveFont(Font.BOLD, 15f));
        colunaVencedor.add(tituloVencedor);
        colunaVencedor.add(Box.createVerticalStrut(30));
        colunaVencedor.add(lblVencedor);
        corpo.add(colunaVencedor);

        janelaBracket.add(new JScrollPane(corpo), BorderLayout.CENTER);
    }

    private JButton criarBotaoMenu(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

    private JLabel[][] criarLabels(int quantidadeJogos) {
        JLabel[][] labels = new JLabel[quantidadeJogos][2];
        for (int i = 0; i < quantidadeJogos; i++) {
            labels[i][0] = new JLabel("Por definir");
            labels[i][1] = new JLabel("Por definir");
            labels[i][0].setFont(labels[i][0].getFont().deriveFont(Font.BOLD, 13f));
            labels[i][1].setFont(labels[i][1].getFont().deriveFont(Font.BOLD, 13f));
        }
        return labels;
    }

    private JPanel criarColuna(String titulo, JLabel[][] jogos) {
        JPanel coluna = new JPanel();
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));
        coluna.setBackground(new Color(230, 230, 230));
        coluna.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 15f));
        coluna.add(lblTitulo);
        coluna.add(Box.createVerticalStrut(10));

        for (JLabel[] jogo : jogos) {
            coluna.add(jogo[0]);
            coluna.add(jogo[1]);
            coluna.add(Box.createVerticalStrut(16));
        }
        coluna.add(Box.createVerticalGlue());
        return coluna;
    }

    private void atribuirLabelsOitavos(JLabel[][] labels) {
        lblOitavos_jogo1_equipa1 = labels[0][0];
        lblOitavos_jogo1_equipa2 = labels[0][1];
        lblOitavos_jogo2_equipa1 = labels[1][0];
        lblOitavos_jogo2_equipa2 = labels[1][1];
        lblOitavos_jogo3_equipa1 = labels[2][0];
        lblOitavos_jogo3_equipa2 = labels[2][1];
        lblOitavos_jogo4_equipa1 = labels[3][0];
        lblOitavos_jogo4_equipa2 = labels[3][1];
        lblOitavos_jogo5_equipa1 = labels[4][0];
        lblOitavos_jogo5_equipa2 = labels[4][1];
        lblOitavos_jogo6_equipa1 = labels[5][0];
        lblOitavos_jogo6_equipa2 = labels[5][1];
        lblOitavos_jogo7_equipa1 = labels[6][0];
        lblOitavos_jogo7_equipa2 = labels[6][1];
        lblOitavos_jogo8_equipa1 = labels[7][0];
        lblOitavos_jogo8_equipa2 = labels[7][1];
    }

    private void atribuirLabelsQuartos(JLabel[][] labels) {
        lblQuartos_jogo1_equipa1 = labels[0][0];
        lblQuartos_jogo1_equipa2 = labels[0][1];
        lblQuartos_jogo2_equipa1 = labels[1][0];
        lblQuartos_jogo2_equipa2 = labels[1][1];
        lblQuartos_jogo3_equipa1 = labels[2][0];
        lblQuartos_jogo3_equipa2 = labels[2][1];
        lblQuartos_jogo4_equipa1 = labels[3][0];
        lblQuartos_jogo4_equipa2 = labels[3][1];
    }

    private void atribuirLabelsMeias(JLabel[][] labels) {
        lblMeia_jogo1_equipa1 = labels[0][0];
        lblMeia_jogo1_equipa2 = labels[0][1];
        lblMeia_jogo2_equipa1 = labels[1][0];
        lblMeia_jogo2_equipa2 = labels[1][1];
    }
}
