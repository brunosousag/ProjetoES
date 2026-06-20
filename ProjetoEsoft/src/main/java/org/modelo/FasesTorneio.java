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
    private JLabel lblTerceiro_jogo1_equipa1;
    private JLabel lblTerceiro_jogo1_equipa2;
    private JLabel lblVencedor;

    /**
     * Timer usado para atualizar a árvore automaticamente quando outro ecrã
     * guarda resultados. Assim, não é preciso fechar e voltar a abrir o bracket.
     */
    private Timer timerAtualizacaoBracket;
    private String assinaturaUltimaAtualizacao = "";

    public FasesTorneio(String title) {
        super(title);

        // Monta a interface de forma programática para manter o visual
        // consistente mesmo quando o .form é simplificado.
        criarInterfaceFallback();

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

        // 3.º lugar: jogo 63.
        preencherJogo(porNumero.get(63), lblTerceiro_jogo1_equipa1, lblTerceiro_jogo1_equipa2);

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
        janelaBracket.setBackground(new Color(241, 244, 248));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(-15783589));
        header.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));

        JPanel tituloArea = new JPanel();
        tituloArea.setOpaque(false);
        tituloArea.setLayout(new BoxLayout(tituloArea, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Bracket do Mundial 2026");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 19f));

        JLabel subtitulo = new JLabel("Fases eliminatórias em vista simples");
        subtitulo.setForeground(new Color(182, 190, 202));
        subtitulo.setFont(subtitulo.getFont().deriveFont(Font.PLAIN, 12f));

        tituloArea.add(titulo);
        tituloArea.add(Box.createVerticalStrut(4));
        tituloArea.add(subtitulo);
        header.add(tituloArea, BorderLayout.WEST);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.setOpaque(false);
        btnGestao = criarBotaoMenu("TORNEIO", new Color(-14635993));
        btnEquipas = criarBotaoMenu("EQUIPAS", new Color(-13734167));
        btnMerch = criarBotaoMenu("MERCH", new Color(-7319319));
        btnCarrinho = criarBotaoMenu("CARRINHO", new Color(-1457045));
        botoes.add(btnGestao);
        botoes.add(btnEquipas);
        botoes.add(btnMerch);
        botoes.add(btnCarrinho);
        header.add(botoes, BorderLayout.EAST);

        janelaBracket.add(header, BorderLayout.NORTH);

        JLabel[][] oitavos = criarLabels(8);
        JLabel[][] quartos = criarLabels(4);
        JLabel[][] meias = criarLabels(2);
        JLabel[][] finalLabels = criarLabels(1);

        atribuirLabelsOitavos(oitavos);
        atribuirLabelsQuartos(quartos);
        atribuirLabelsMeias(meias);
        lblFinal_jogo1_equipa1 = finalLabels[0][0];
        lblFinal_jogo1_equipa2 = finalLabels[0][1];
        JLabel[][] terceiroLabels = criarLabels(1);
        lblTerceiro_jogo1_equipa1 = terceiroLabels[0][0];
        lblTerceiro_jogo1_equipa2 = terceiroLabels[0][1];
        lblVencedor = new JLabel("Por definir");
        lblVencedor.setFont(lblVencedor.getFont().deriveFont(Font.BOLD, 15f));
        lblVencedor.setForeground(new Color(53, 44, 18));

        JPanel corpo = new JPanel(new GridLayout(1, 5, 16, 0));
        corpo.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        corpo.setBackground(new Color(241, 244, 248));

        corpo.add(criarColuna("Oitavos de Final", 49, oitavos, new Color(42, 93, 138), 10, 0, 0));
        corpo.add(criarColuna("Quartos de Final", 57, quartos, new Color(69, 119, 96), 18, 72, 72));
        corpo.add(criarColuna("Meias-Finais", 61, meias, new Color(164, 117, 61), 34, 164, 164));
        corpo.add(criarColuna("Final", 64, finalLabels, new Color(180, 68, 68), 0, 268, 268));
        corpo.add(criarColunaVencedor("Vencedor", lblVencedor, "3.º classificado", terceiroLabels, 18));

        JScrollPane scroll = new JScrollPane(corpo);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(new Color(241, 244, 248));

        janelaBracket.add(scroll, BorderLayout.CENTER);
    }

    private JButton criarBotaoMenu(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 12f));
        return b;
    }

    private JLabel[][] criarLabels(int quantidadeJogos) {
        JLabel[][] labels = new JLabel[quantidadeJogos][2];
        for (int i = 0; i < quantidadeJogos; i++) {
            labels[i][0] = new JLabel("Por definir");
            labels[i][1] = new JLabel("Por definir");
            estilizarLinhaPartida(labels[i][0]);
            estilizarLinhaPartida(labels[i][1]);
        }
        return labels;
    }

    private void estilizarLinhaPartida(JLabel label) {
        label.setOpaque(false);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));
        label.setForeground(new Color(27, 33, 41));
        label.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
    }

    private JPanel criarCardPartida(String rotuloTopo, JLabel equipaA, JLabel equipaB, Color acento) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(220, 78));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, acento),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(214, 221, 230)),
                        BorderFactory.createEmptyBorder(8, 12, 10, 12)
                )
        ));

        JLabel lblNumero = new JLabel(rotuloTopo);
        lblNumero.setForeground(acento.darker());
        lblNumero.setFont(lblNumero.getFont().deriveFont(Font.BOLD, 11f));
        card.add(lblNumero, BorderLayout.NORTH);

        JPanel linhas = new JPanel(new GridLayout(2, 1, 0, 2));
        linhas.setOpaque(false);
        linhas.add(equipaA);
        linhas.add(equipaB);
        card.add(linhas, BorderLayout.CENTER);
        return card;
    }

    private JPanel criarColuna(String titulo, int numeroInicial, JLabel[][] jogos, Color acento, int espacoEntreCards, int espacoTopo, int espacoBase) {
        JPanel coluna = new JPanel();
        coluna.setOpaque(false);
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(new Color(28, 32, 40));
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 15f));
        coluna.add(lblTitulo);
        coluna.add(Box.createVerticalStrut(12));

        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.add(Box.createVerticalGlue());
        if (espacoTopo > 0) {
            conteudo.add(Box.createVerticalStrut(espacoTopo));
        }

        int numeroAtual = numeroInicial;
        for (JLabel[] jogo : jogos) {
            conteudo.add(criarCardPartida("Jogo " + numeroAtual, jogo[0], jogo[1], acento));
            conteudo.add(Box.createVerticalStrut(espacoEntreCards));
            numeroAtual++;
        }
        if (espacoBase > 0) {
            conteudo.add(Box.createVerticalStrut(espacoBase));
        }
        conteudo.add(Box.createVerticalGlue());
        coluna.add(conteudo);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(248, 250, 252));
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 235)),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));
        wrapper.add(coluna, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel criarColunaVencedor(String titulo, JLabel vencedor, int espacoTopo) {
        JPanel coluna = new JPanel();
        coluna.setOpaque(false);
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(new Color(28, 32, 40));
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 15f));
        coluna.add(lblTitulo);
        coluna.add(Box.createVerticalStrut(12));

        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.add(Box.createVerticalGlue());
        if (espacoTopo > 0) {
            conteudo.add(Box.createVerticalStrut(espacoTopo));
        }

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255, 245, 214));
        card.setPreferredSize(new Dimension(220, 64));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(201, 155, 49)),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(232, 212, 157)),
                        BorderFactory.createEmptyBorder(16, 14, 16, 14)
                )
        ));

        JLabel trofeu = new JLabel("Troféu");
        trofeu.setForeground(new Color(129, 95, 18));
        trofeu.setFont(trofeu.getFont().deriveFont(Font.BOLD, 12f));
        card.add(trofeu, BorderLayout.NORTH);
        card.add(vencedor, BorderLayout.CENTER);

        conteudo.add(card);
        conteudo.add(Box.createVerticalGlue());
        coluna.add(conteudo);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(248, 250, 252));
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 235)),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));
        wrapper.add(coluna, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel criarColunaVencedor(String titulo, JLabel vencedor, String rotuloTerceiro, JLabel[][] terceiro, int espacoTopo) {
        JPanel coluna = new JPanel();
        coluna.setOpaque(false);
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(new Color(28, 32, 40));
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 15f));
        coluna.add(lblTitulo);
        coluna.add(Box.createVerticalStrut(12));

        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.add(Box.createVerticalGlue());
        if (espacoTopo > 0) {
            conteudo.add(Box.createVerticalStrut(espacoTopo));
        }

        JPanel cardCampeao = new JPanel(new BorderLayout());
        cardCampeao.setBackground(new Color(255, 245, 214));
        cardCampeao.setPreferredSize(new Dimension(220, 58));
        cardCampeao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        cardCampeao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(201, 155, 49)),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(232, 212, 157)),
                        BorderFactory.createEmptyBorder(10, 14, 10, 14)
                )
        ));

        JLabel trofeu = new JLabel("Campeão");
        trofeu.setForeground(new Color(129, 95, 18));
        trofeu.setFont(trofeu.getFont().deriveFont(Font.BOLD, 12f));
        cardCampeao.add(trofeu, BorderLayout.NORTH);
        cardCampeao.add(vencedor, BorderLayout.CENTER);

        conteudo.add(cardCampeao);
        conteudo.add(Box.createVerticalStrut(16));

        JPanel cardTerceiro = criarCardPartida(rotuloTerceiro, terceiro[0][0], terceiro[0][1], new Color(129, 95, 18));
        cardTerceiro.setPreferredSize(new Dimension(220, 66));
        cardTerceiro.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        conteudo.add(cardTerceiro);
        conteudo.add(Box.createVerticalGlue());
        coluna.add(conteudo);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(248, 250, 252));
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 235)),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));
        wrapper.add(coluna, BorderLayout.CENTER);
        return wrapper;
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
