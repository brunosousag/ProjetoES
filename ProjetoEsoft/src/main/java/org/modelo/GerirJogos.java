package org.modelo;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registo de jogos da fase de grupos.
 *
 * Agora o utilizador escolhe primeiro o grupo. Depois disso, as equipas
 * disponíveis passam a ser apenas as equipas desse grupo. Os marcadores
 * são escolhidos através de uma checkbox por jogador; o spinner indica
 * quantos golos esse jogador marcou na partida.
 */
public class GerirJogos extends BaseFrame {

    /** Uma linha de marcador: jogador escolhido + número de golos. */
    private static class LinhaGolo {
        final String jogador;
        final JCheckBox checkBox;
        final JSpinner spinner;

        LinhaGolo(String jogador, JCheckBox checkBox, JSpinner spinner) {
            this.jogador = jogador;
            this.checkBox = checkBox;
            this.spinner = spinner;
        }
    }

    private final JComboBox<String> cmbGrupo = new JComboBox<>();
    private final JComboBox<String> cmbEquipaA = new JComboBox<>();
    private final JComboBox<String> cmbEquipaB = new JComboBox<>();

    // uma coluna de jogadores por equipa, cada jogador com checkbox + spinner de golos
    private final JPanel colunaA = criarColuna();
    private final JPanel colunaB = criarColuna();
    private final JScrollPane scrollA = new JScrollPane(colunaA);
    private final JScrollPane scrollB = new JScrollPane(colunaB);
    private final TitledBorder bordaA = BorderFactory.createTitledBorder("Equipa A");
    private final TitledBorder bordaB = BorderFactory.createTitledBorder("Equipa B");
    private final List<LinhaGolo> linhasA = new ArrayList<>();
    private final List<LinhaGolo> linhasB = new ArrayList<>();

    private final JLabel lblResultado = new JLabel(" ");

    private final DefaultListModel<Jogo> jogosModel = new DefaultListModel<>();
    private final JList<Jogo> listaJogos = new JList<>(jogosModel);

    private ArrayList<Jogo> jogos = new ArrayList<>();
    private ArrayList<Grupo> grupos = new ArrayList<>();
    private Jogo jogoEmEdicao = null;
    private boolean carregando = false;   // suprime listeners durante carregamentos

    public GerirJogos(String title) {
        super(title);

        JPanel root = new JPanel(new BorderLayout());
        root.add(construirCabecalho(), BorderLayout.NORTH);
        root.add(construirCorpo(), BorderLayout.CENTER);
        setContentPane(root);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        configurarMenuGestao();
        carregarDados();
        ligarEventos();
        novoJogo();

        setPreferredSize(new Dimension(960, 660));
        pack();
        setLocationRelativeTo(null);
    }

    private static JPanel criarColuna() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        return p;
    }

    // ------------------------------------------------------------------ cabeçalho

    private JPanel construirCabecalho() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(15, 41, 91));
        barra.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel titulo = new JLabel("🏆 CAMPEONATO MUNDIAL 2026");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));

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

        barra.add(titulo, BorderLayout.WEST);
        barra.add(botoes, BorderLayout.EAST);
        return barra;
    }

    private JButton criarBotaoMenu(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

    // ------------------------------------------------------------------ corpo

    private JComponent construirCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 12));
        corpo.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel titulo = new JLabel("Fase de grupos — Registo de jogos");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        corpo.add(titulo, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                construirPainelLista(), construirPainelEditor());
        split.setResizeWeight(0.30);
        split.setBorder(null);
        corpo.add(split, BorderLayout.CENTER);
        return corpo;
    }

    private JComponent construirPainelLista() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBorder(BorderFactory.createTitledBorder("Jogos registados"));
        painel.setPreferredSize(new Dimension(290, 10));

        listaJogos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        painel.add(new JScrollPane(listaJogos), BorderLayout.CENTER);

        JButton btnNovo = new JButton("+ Novo jogo");
        btnNovo.addActionListener(e -> { listaJogos.clearSelection(); novoJogo(); });
        painel.add(btnNovo, BorderLayout.SOUTH);
        return painel;
    }

    private JComponent construirPainelEditor() {
        JPanel painel = new JPanel(new BorderLayout(0, 10));
        painel.setBorder(BorderFactory.createTitledBorder("Detalhe do jogo"));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.fill = GridBagConstraints.HORIZONTAL;
        addLinha(form, g, 0, "Grupo:", cmbGrupo);
        addLinha(form, g, 1, "Equipa A:", cmbEquipaA);
        addLinha(form, g, 2, "Equipa B:", cmbEquipaB);

        JLabel dica = new JLabel("Marcadores — seleciona apenas quem marcou e indica quantos golos fez");
        dica.setForeground(new Color(90, 90, 90));

        JPanel topo = new JPanel(new BorderLayout(0, 8));
        topo.add(form, BorderLayout.CENTER);
        topo.add(dica, BorderLayout.SOUTH);
        painel.add(topo, BorderLayout.NORTH);

        scrollA.setBorder(bordaA);
        scrollB.setBorder(bordaB);
        JPanel colunas = new JPanel(new GridLayout(1, 2, 12, 0));
        colunas.add(scrollA);
        colunas.add(scrollB);
        painel.add(colunas, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        lblResultado.setFont(lblResultado.getFont().deriveFont(Font.BOLD, 15f));
        JButton btnGuardar = new JButton("Guardar jogo");
        btnGuardar.addActionListener(e -> guardarJogo());
        rodape.add(lblResultado, BorderLayout.WEST);
        rodape.add(btnGuardar, BorderLayout.EAST);
        painel.add(rodape, BorderLayout.SOUTH);
        return painel;
    }

    private void addLinha(JPanel form, GridBagConstraints g, int row, String etiqueta, JComponent campo) {
        g.gridy = row;
        g.gridx = 0; g.weightx = 0;
        form.add(new JLabel(etiqueta), g);
        g.gridx = 1; g.weightx = 1;
        form.add(campo, g);
    }

    // ------------------------------------------------------------------ dados / eventos

    private void carregarDados() {
        jogos = RepositorioDados.carregarJogos();
        grupos = RepositorioDados.carregarGrupos();

        for (Grupo grupo : grupos) {
            cmbGrupo.addItem(grupo.getNome());
        }

        // Mostra nesta janela apenas os jogos cujo campo fase corresponde a um grupo.
        for (Jogo jogo : jogos) {
            if (grupoExiste(jogo.getFase())) {
                jogosModel.addElement(jogo);
            }
        }

        atualizarEquipasDoGrupo(null, null);
    }

    private void ligarEventos() {
        cmbGrupo.addActionListener(e -> {
            if (!carregando) {
                atualizarEquipasDoGrupo(null, null);
                preencherColunas(null);
            }
        });

        cmbEquipaA.addActionListener(e -> { if (!carregando) preencherColunas(null); });
        cmbEquipaB.addActionListener(e -> { if (!carregando) preencherColunas(null); });

        listaJogos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listaJogos.getSelectedValue() != null) {
                carregarJogoNoEditor(listaJogos.getSelectedValue());
            }
        });
    }

    private void novoJogo() {
        jogoEmEdicao = null;
        carregando = true;
        if (cmbGrupo.getItemCount() > 0) cmbGrupo.setSelectedIndex(0);
        carregando = false;

        atualizarEquipasDoGrupo(null, null);
        preencherColunas(null);
    }

    private void carregarJogoNoEditor(Jogo jogo) {
        jogoEmEdicao = jogo;
        carregando = true;
        cmbGrupo.setSelectedItem(jogo.getFase());
        carregando = false;

        atualizarEquipasDoGrupo(jogo.getEquipaA(), jogo.getEquipaB());
        preencherColunas(jogo);
    }

    /** Atualiza as equipas disponíveis com base no grupo selecionado. */
    private void atualizarEquipasDoGrupo(String equipaASelecionada, String equipaBSelecionada) {
        carregando = true;
        cmbEquipaA.removeAllItems();
        cmbEquipaB.removeAllItems();

        Grupo grupo = grupoSelecionado();
        if (grupo != null) {
            for (String equipa : grupo.getEquipas()) {
                cmbEquipaA.addItem(equipa);
                cmbEquipaB.addItem(equipa);
            }
        }

        if (equipaASelecionada != null) {
            cmbEquipaA.setSelectedItem(equipaASelecionada);
        } else if (cmbEquipaA.getItemCount() > 0) {
            cmbEquipaA.setSelectedIndex(0);
        }

        if (equipaBSelecionada != null) {
            cmbEquipaB.setSelectedItem(equipaBSelecionada);
        } else if (cmbEquipaB.getItemCount() > 1) {
            cmbEquipaB.setSelectedIndex(1);
        } else if (cmbEquipaB.getItemCount() > 0) {
            cmbEquipaB.setSelectedIndex(0);
        }

        carregando = false;
    }

    private Grupo grupoSelecionado() {
        String nomeGrupo = (String) cmbGrupo.getSelectedItem();
        if (nomeGrupo == null) return null;

        for (Grupo grupo : grupos) {
            if (grupo.getNome().equals(nomeGrupo)) {
                return grupo;
            }
        }
        return null;
    }

    private boolean grupoExiste(String nomeGrupo) {
        if (nomeGrupo == null) return false;
        for (Grupo grupo : grupos) {
            if (grupo.getNome().equals(nomeGrupo)) {
                return true;
            }
        }
        return false;
    }

    /** Preenche cada coluna com os jogadores da respetiva equipa. */
    private void preencherColunas(Jogo jogo) {
        carregando = true;
        String a = (String) cmbEquipaA.getSelectedItem();
        String b = (String) cmbEquipaB.getSelectedItem();

        bordaA.setTitle(a == null ? "Equipa A" : a);
        bordaB.setTitle(b == null ? "Equipa B" : b);

        preencherColuna(colunaA, linhasA, a, jogo);
        preencherColuna(colunaB, linhasB, b, jogo);

        carregando = false;
        scrollA.repaint();
        scrollB.repaint();
        recalcularResultado();
    }

    private void preencherColuna(JPanel coluna, List<LinhaGolo> linhas, String equipa, Jogo jogo) {
        coluna.removeAll();
        linhas.clear();

        if (equipa != null) {
            for (Jogador jogador : RepositorioDados.jogadoresDaEquipa(equipa)) {
                int golos = (jogo != null && jogo.getMarcadores().containsKey(jogador.getNome()))
                        ? jogo.getMarcadores().get(jogador.getNome()) : 0;
                coluna.add(criarLinhaJogador(jogador.getNome(), golos, linhas));
            }
        }
        coluna.add(Box.createVerticalGlue());
        coluna.revalidate();
        coluna.repaint();
    }

    private JPanel criarLinhaJogador(String nome, int golos, List<LinhaGolo> linhas) {
        JPanel linha = new JPanel(new BorderLayout(8, 0));
        linha.setOpaque(false);
        linha.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        linha.setAlignmentX(Component.LEFT_ALIGNMENT);

        boolean marcou = golos > 0;
        JCheckBox checkBox = new JCheckBox(nome, marcou);
        checkBox.setOpaque(false);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(golos, 0, 99, 1));
        spinner.setEnabled(marcou);
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setColumns(2);
        }
        spinner.setMaximumSize(new Dimension(64, 28));
        spinner.setPreferredSize(new Dimension(64, 28));

        checkBox.addActionListener(e -> {
            boolean selecionado = checkBox.isSelected();
            spinner.setEnabled(selecionado);

            if (selecionado && (Integer) spinner.getValue() == 0) {
                spinner.setValue(1);
            } else if (!selecionado) {
                spinner.setValue(0);
            }

            if (!carregando) recalcularResultado();
        });

        spinner.addChangeListener(e -> {
            if (carregando) return;

            int valor = (Integer) spinner.getValue();
            if (valor > 0 && !checkBox.isSelected()) {
                checkBox.setSelected(true);
                spinner.setEnabled(true);
            } else if (valor == 0 && checkBox.isSelected()) {
                checkBox.setSelected(false);
                spinner.setEnabled(false);
            }

            recalcularResultado();
        });

        linha.add(checkBox, BorderLayout.CENTER);
        linha.add(spinner, BorderLayout.EAST);

        linhas.add(new LinhaGolo(nome, checkBox, spinner));
        return linha;
    }

    private void recalcularResultado() {
        String a = (String) cmbEquipaA.getSelectedItem();
        String b = (String) cmbEquipaB.getSelectedItem();
        int ga = somaGolos(linhasA);
        int gb = somaGolos(linhasB);

        String txt = nome(a) + "   " + ga + " – " + gb + "   " + nome(b);
        if (a == null || b == null) txt = "Escolhe um grupo com pelo menos duas equipas.";
        else if (ga > gb)       txt += "      🏆 " + a;
        else if (gb > ga)  txt += "      🏆 " + b;
        else               txt += "      (empate — decidido nos penáltis)";
        lblResultado.setText(txt);
    }

    private String nome(String s) { return s == null ? "?" : s; }

    private int somaGolos(List<LinhaGolo> linhas) {
        int total = 0;
        for (LinhaGolo linha : linhas) {
            if (linha.checkBox.isSelected()) {
                total += (Integer) linha.spinner.getValue();
            }
        }
        return total;
    }

    // ------------------------------------------------------------------ guardar

    private void guardarJogo() {
        String grupo = (String) cmbGrupo.getSelectedItem();
        String a = (String) cmbEquipaA.getSelectedItem();
        String b = (String) cmbEquipaB.getSelectedItem();

        if (grupo == null) {
            JOptionPane.showMessageDialog(this, "Escolhe um grupo.");
            return;
        }

        if (a == null || b == null || a.equals(b)) {
            JOptionPane.showMessageDialog(this, "Escolhe duas equipas diferentes do mesmo grupo.");
            return;
        }

        Map<String, Integer> marcadores = new LinkedHashMap<>();
        recolherMarcadores(linhasA, marcadores);
        recolherMarcadores(linhasB, marcadores);

        int ga = somaGolos(linhasA);
        int gb = somaGolos(linhasB);

        Jogo jogo = (jogoEmEdicao != null)
                ? jogoEmEdicao
                : new Jogo(grupo, a, b);
        jogo.setFase(grupo);          // o campo "fase" passa a guardar o grupo nesta janela
        jogo.setEquipaA(a);
        jogo.setEquipaB(b);
        jogo.setGolosA(ga);
        jogo.setGolosB(gb);
        jogo.setMarcadores(marcadores);
        jogo.setTerminado(true);

        if (ga == gb) {
            String venc = escolherDesempate(a, b);
            if (venc == null) return;   // cancelou
            jogo.setVencedorDesempate(venc);
        } else {
            jogo.setVencedorDesempate(null);
        }

        if (jogoEmEdicao == null) {
            jogos.add(jogo);
            jogosModel.addElement(jogo);
        } else {
            jogosModel.set(jogosModel.indexOf(jogo), jogo);  // força atualização do texto
        }
        RepositorioDados.guardarJogos(jogos);
        listaJogos.repaint();

        jogoEmEdicao = jogo;
        JOptionPane.showMessageDialog(this, "Jogo guardado:\n" + jogo);
    }

    private void recolherMarcadores(List<LinhaGolo> linhas, Map<String, Integer> dest) {
        for (LinhaGolo linha : linhas) {
            int golos = (Integer) linha.spinner.getValue();
            if (linha.checkBox.isSelected() && golos > 0) {
                dest.put(linha.jogador, golos);
            }
        }
    }

    private String escolherDesempate(String a, String b) {
        Object[] opcoes = { a, b };
        int r = JOptionPane.showOptionDialog(this,
                "Empate entre " + a + " e " + b + ".\nQuem avança (penáltis)?",
                "Desempate", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opcoes, opcoes[0]);
        if (r == 0) return a;
        if (r == 1) return b;
        return null;
    }
}
