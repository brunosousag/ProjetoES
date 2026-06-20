package org.modelo;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registo de resultados.
 *
 * - Na fase de grupos, o utilizador escolhe o grupo e a Equipa B é escolhida
 *   automaticamente para impedir A contra A.
 * - Nas eliminatórias, os jogos são gerados automaticamente pela árvore do
 *   torneio. O utilizador apenas seleciona o jogo existente e regista o resultado.
 *   Depois de guardar, o vencedor passa automaticamente para o jogo seguinte.
 */
public class GerirJogos extends BaseFrame {

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

    private final JPanel colunaA = criarColuna();
    private final JPanel colunaB = criarColuna();
    private final JScrollPane scrollA = new JScrollPane(colunaA);
    private final JScrollPane scrollB = new JScrollPane(colunaB);
    private final TitledBorder bordaA = BorderFactory.createTitledBorder("Equipa A");
    private final TitledBorder bordaB = BorderFactory.createTitledBorder("Equipa B");
    private final List<LinhaGolo> linhasA = new ArrayList<>();
    private final List<LinhaGolo> linhasB = new ArrayList<>();

    private final JLabel lblResultado = new JLabel(" ");
    private final JLabel lblInstrucao = new JLabel("Marcadores — seleciona apenas quem marcou e indica quantos golos fez");

    private final DefaultListModel<Jogo> jogosModel = new DefaultListModel<>();
    private final JList<Jogo> listaJogos = new JList<>(jogosModel);

    private ArrayList<Jogo> jogos = new ArrayList<>();
    private ArrayList<Grupo> grupos = new ArrayList<>();
    private Jogo jogoEmEdicao = null;
    private boolean carregando = false;
    private final Integer numeroJogoInicial;

    public GerirJogos(String title) {
        this(title, null);
    }

    public GerirJogos(String title, Integer numeroJogoInicial) {
        super(title);
        this.numeroJogoInicial = numeroJogoInicial;

        JPanel root = new JPanel(new BorderLayout());
        root.add(construirCabecalho(), BorderLayout.NORTH);
        root.add(construirCorpo(), BorderLayout.CENTER);
        setContentPane(root);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cmbEquipaB.setEnabled(false);
        cmbEquipaB.setFocusable(false);
        cmbEquipaB.setToolTipText("Escolhida automaticamente. Na fase de grupos nunca pode ser igual à Equipa A.");

        configurarMenuGestao();
        carregarDados();
        ligarEventos();
        novoJogo();
        selecionarJogoInicial();

        setPreferredSize(new Dimension(1040, 700));
        pack();
        setLocationRelativeTo(null);
    }

    private void selecionarJogoInicial() {
        if (numeroJogoInicial == null) {
            return;
        }

        for (int i = 0; i < jogosModel.size(); i++) {
            Jogo jogo = jogosModel.get(i);
            if (jogo.getNumero() == numeroJogoInicial) {
                listaJogos.setSelectedIndex(i);
                listaJogos.ensureIndexIsVisible(i);
                carregarJogoNoEditor(jogo);
                return;
            }
        }
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

        JLabel titulo = new JLabel("Registo de resultados — grupos e eliminatórias automáticas");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        corpo.add(titulo, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                construirPainelLista(), construirPainelEditor());
        split.setResizeWeight(0.32);
        split.setBorder(null);
        corpo.add(split, BorderLayout.CENTER);
        return corpo;
    }

    private JComponent construirPainelLista() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBorder(BorderFactory.createTitledBorder("Jogos registados / gerados"));
        painel.setPreferredSize(new Dimension(340, 10));

        listaJogos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        painel.add(new JScrollPane(listaJogos), BorderLayout.CENTER);

        JButton btnNovo = new JButton("+ Novo jogo de grupo");
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
        addLinha(form, g, 0, "Grupo/Fase:", cmbGrupo);
        addLinha(form, g, 1, "Equipa A:", cmbEquipaA);
        addLinha(form, g, 2, "Equipa B:", cmbEquipaB);

        lblInstrucao.setForeground(new Color(90, 90, 90));

        JPanel topo = new JPanel(new BorderLayout(0, 8));
        topo.add(form, BorderLayout.CENTER);
        topo.add(lblInstrucao, BorderLayout.SOUTH);
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
        JButton btnGuardar = new JButton("Guardar resultado");
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
        new LogicaTorneio().garantirArvoreEliminatoria();

        jogos = RepositorioDados.carregarJogos();
        grupos = RepositorioDados.carregarGrupos();

        cmbGrupo.removeAllItems();
        for (Grupo grupo : grupos) {
            cmbGrupo.addItem(grupo.getNome());
        }

        recarregarModeloJogos(null);
        atualizarEquipasDoGrupo(null, null);
    }

    private void ligarEventos() {
        cmbGrupo.addActionListener(e -> {
            if (!carregando) {
                if (LogicaTorneio.isFaseEliminatoria((String) cmbGrupo.getSelectedItem())) {
                    JOptionPane.showMessageDialog(this,
                            "Os jogos eliminatórios são criados automaticamente. Seleciona um jogo da lista para registar o resultado.");
                    novoJogo();
                    return;
                }
                atualizarEquipasDoGrupo(null, null);
                preencherColunas(null);
            }
        });

        cmbEquipaA.addActionListener(e -> {
            if (!carregando) {
                atualizarEquipaBAutomaticamente();
                preencherColunas(null);
            }
        });

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
        removerFasesEliminatoriasDaCombo();
        cmbGrupo.setEnabled(true);
        cmbEquipaA.setEnabled(true);
        cmbEquipaB.setEnabled(false);
        if (cmbGrupo.getItemCount() > 0) cmbGrupo.setSelectedIndex(0);
        carregando = false;

        atualizarEquipasDoGrupo(null, null);
        preencherColunas(null);
    }

    private void carregarJogoNoEditor(Jogo jogo) {
        jogoEmEdicao = jogo;

        if (LogicaTorneio.isFaseEliminatoria(jogo.getFase())) {
            carregarJogoEliminatorio(jogo);
            return;
        }

        carregando = true;
        removerFasesEliminatoriasDaCombo();
        cmbGrupo.setEnabled(true);
        cmbEquipaA.setEnabled(true);
        cmbEquipaB.setEnabled(false);
        cmbGrupo.setSelectedItem(jogo.getFase());
        carregando = false;

        atualizarEquipasDoGrupo(jogo.getEquipaA(), jogo.getEquipaB());
        preencherColunas(jogo);
    }

    private void carregarJogoEliminatorio(Jogo jogo) {
        carregando = true;
        garantirItemComboGrupo(jogo.getFase());
        cmbGrupo.setSelectedItem(jogo.getFase());
        cmbGrupo.setEnabled(false);

        cmbEquipaA.removeAllItems();
        cmbEquipaB.removeAllItems();
        cmbEquipaA.addItem(jogo.getEquipaA());
        cmbEquipaB.addItem(jogo.getEquipaB());
        cmbEquipaA.setEnabled(false);
        cmbEquipaB.setEnabled(false);
        carregando = false;

        lblInstrucao.setText("Eliminatória: regista o resultado. Se empatar, escolhe quem passou nos penáltis.");
        preencherColunas(jogo);
    }

    private void garantirItemComboGrupo(String item) {
        for (int i = 0; i < cmbGrupo.getItemCount(); i++) {
            if (item.equals(cmbGrupo.getItemAt(i))) return;
        }
        cmbGrupo.addItem(item);
    }

    private void removerFasesEliminatoriasDaCombo() {
        for (int i = cmbGrupo.getItemCount() - 1; i >= 0; i--) {
            if (LogicaTorneio.isFaseEliminatoria(cmbGrupo.getItemAt(i))) {
                cmbGrupo.removeItemAt(i);
            }
        }
        lblInstrucao.setText("Marcadores — seleciona apenas quem marcou e indica quantos golos fez");
    }

    /** Atualiza as equipas disponíveis com base no grupo selecionado. */
    private void atualizarEquipasDoGrupo(String equipaASelecionada, String equipaBSelecionada) {
        if (LogicaTorneio.isFaseEliminatoria((String) cmbGrupo.getSelectedItem())) {
            return;
        }

        carregando = true;
        cmbEquipaA.removeAllItems();
        cmbEquipaB.removeAllItems();

        Grupo grupo = grupoSelecionado();
        if (grupo != null) {
            for (String equipa : grupo.getEquipas()) {
                cmbEquipaA.addItem(equipa);
            }
        }

        if (equipaASelecionada != null) {
            cmbEquipaA.setSelectedItem(equipaASelecionada);
        } else if (cmbEquipaA.getItemCount() > 0) {
            cmbEquipaA.setSelectedIndex(0);
        }

        preencherComboEquipaBSemEquipaA(equipaBSelecionada);
        carregando = false;
    }

    private void atualizarEquipaBAutomaticamente() {
        boolean estadoAnterior = carregando;
        carregando = true;
        preencherComboEquipaBSemEquipaA(null);
        carregando = estadoAnterior;
    }

    private void preencherComboEquipaBSemEquipaA(String equipaBSelecionada) {
        String equipaA = (String) cmbEquipaA.getSelectedItem();
        Grupo grupo = grupoSelecionado();

        cmbEquipaB.removeAllItems();
        if (grupo != null) {
            for (String equipa : grupo.getEquipas()) {
                if (!equipa.equals(equipaA)) {
                    cmbEquipaB.addItem(equipa);
                }
            }
        }

        if (equipaBSelecionada != null) {
            for (int i = 0; i < cmbEquipaB.getItemCount(); i++) {
                if (equipaBSelecionada.equals(cmbEquipaB.getItemAt(i))) {
                    cmbEquipaB.setSelectedIndex(i);
                    return;
                }
            }
        }

        if (cmbEquipaB.getItemCount() > 0) {
            cmbEquipaB.setSelectedIndex(0);
        }
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

        if (equipa != null && !LogicaTorneio.isPlaceholder(equipa)) {
            for (Jogador jogador : RepositorioDados.jogadoresDaEquipa(equipa)) {
                int golos = (jogo != null && jogo.getMarcadores().containsKey(jogador.getNome()))
                        ? jogo.getMarcadores().get(jogador.getNome()) : 0;
                coluna.add(criarLinhaJogador(jogador.getNome(), golos, linhas));
            }
        } else {
            JLabel lbl = new JLabel("Equipa ainda não definida no bracket.");
            lbl.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            coluna.add(lbl);
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
        spinner.setEnabled(true);
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setColumns(2);
        }
        spinner.setMaximumSize(new Dimension(64, 28));
        spinner.setPreferredSize(new Dimension(64, 28));

        checkBox.addActionListener(e -> {
            boolean selecionado = checkBox.isSelected();

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
            } else if (valor == 0 && checkBox.isSelected()) {
                checkBox.setSelected(false);
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
        if (a == null || b == null) {
            txt = "Escolhe um grupo com pelo menos duas equipas.";
        } else if (LogicaTorneio.isPlaceholder(a) || LogicaTorneio.isPlaceholder(b)) {
            txt = "Este jogo ainda depende de vencedores anteriores.";
        } else if (ga > gb) {
            txt += "      🏆 " + a;
        } else if (gb > ga) {
            txt += "      🏆 " + b;
        }
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
        String faseOuGrupo = (String) cmbGrupo.getSelectedItem();
        String a = (String) cmbEquipaA.getSelectedItem();
        String b = (String) cmbEquipaB.getSelectedItem();

        if (faseOuGrupo == null) {
            JOptionPane.showMessageDialog(this, "Escolhe um grupo ou seleciona um jogo da lista.");
            return;
        }

        if (LogicaTorneio.isFaseEliminatoria(faseOuGrupo)) {
            guardarJogoEliminatorio(faseOuGrupo, a, b);
        } else {
            guardarJogoGrupo(faseOuGrupo, a, b);
        }
    }

    private void guardarJogoGrupo(String grupo, String a, String b) {
        if (a == null || b == null || a.equals(b)) {
            JOptionPane.showMessageDialog(this, "Escolhe duas equipas diferentes do mesmo grupo.");
            return;
        }

        Map<String, Integer> marcadores = new LinkedHashMap<>();
        recolherMarcadores(linhasA, marcadores);
        recolherMarcadores(linhasB, marcadores);

        int ga = somaGolos(linhasA);
        int gb = somaGolos(linhasB);

        Jogo jogo = (jogoEmEdicao != null && !LogicaTorneio.isFaseEliminatoria(jogoEmEdicao.getFase()))
                ? jogoEmEdicao
                : new Jogo(grupo, a, b);

        jogo.setNumero(0);
        jogo.setFase(grupo);
        jogo.setEquipaA(a);
        jogo.setEquipaB(b);
        jogo.setGolosA(ga);
        jogo.setGolosB(gb);
        jogo.setMarcadores(marcadores);
        jogo.setTerminado(true);
        jogo.setVencedorDesempate(null);

        if (!jogos.contains(jogo)) {
            jogos.add(jogo);
        }

        RepositorioDados.guardarJogos(jogos);
        new LogicaTorneio().garantirArvoreEliminatoria();
        recarregarModeloJogos(jogo);

        JOptionPane.showMessageDialog(this,
                "Resultado de grupo guardado.\nA árvore eliminatória foi atualizada automaticamente.");
    }

    private void guardarJogoEliminatorio(String fase, String a, String b) {
        if (jogoEmEdicao == null || jogoEmEdicao.getNumero() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleciona primeiro um jogo eliminatório da lista. Estes jogos não são criados manualmente.");
            return;
        }

        if (LogicaTorneio.isPlaceholder(a) || LogicaTorneio.isPlaceholder(b)) {
            JOptionPane.showMessageDialog(this,
                    "Este jogo ainda não pode ser registado porque depende de vencedores anteriores.");
            return;
        }

        Map<String, Integer> marcadores = new LinkedHashMap<>();
        recolherMarcadores(linhasA, marcadores);
        recolherMarcadores(linhasB, marcadores);

        int ga = somaGolos(linhasA);
        int gb = somaGolos(linhasB);

        String vencedorDesempate = null;
        if (ga == gb) {
            Object vencedor = JOptionPane.showInputDialog(
                    this,
                    "O jogo ficou empatado. Quem passou nos penáltis?",
                    "Desempate",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{a, b},
                    a
            );
            if (vencedor == null) return;
            vencedorDesempate = vencedor.toString();
        }

        jogoEmEdicao.setFase(fase);
        jogoEmEdicao.setEquipaA(a);
        jogoEmEdicao.setEquipaB(b);
        jogoEmEdicao.setGolosA(ga);
        jogoEmEdicao.setGolosB(gb);
        jogoEmEdicao.setMarcadores(marcadores);
        jogoEmEdicao.setVencedorDesempate(vencedorDesempate);
        jogoEmEdicao.setTerminado(true);

        RepositorioDados.guardarJogos(jogos);
        new LogicaTorneio().garantirArvoreEliminatoria();
        recarregarModeloJogos(jogoEmEdicao);

        JOptionPane.showMessageDialog(this,
                "Resultado eliminatório guardado.\nO vencedor avançou automaticamente no bracket.");
    }

    private void recolherMarcadores(List<LinhaGolo> linhas, Map<String, Integer> dest) {
        for (LinhaGolo linha : linhas) {
            int golos = (Integer) linha.spinner.getValue();
            if (linha.checkBox.isSelected() && golos > 0) {
                dest.put(linha.jogador, golos);
            }
        }
    }

    private void recarregarModeloJogos(Jogo selecionar) {
        Integer numeroSelecionar = selecionar != null && selecionar.getNumero() > 0 ? selecionar.getNumero() : null;
        String faseSelecionar = selecionar == null ? null : selecionar.getFase();
        String equipaASelecionar = selecionar == null ? null : selecionar.getEquipaA();
        String equipaBSelecionar = selecionar == null ? null : selecionar.getEquipaB();

        jogos = RepositorioDados.carregarJogos();
        jogosModel.clear();

        List<Jogo> ordenados = new ArrayList<>(jogos);
        ordenados.sort(comparadorJogos());

        Jogo novoSelecionado = null;
        for (Jogo jogo : ordenados) {
            jogosModel.addElement(jogo);
            if (numeroSelecionar != null && jogo.getNumero() == numeroSelecionar) {
                novoSelecionado = jogo;
            } else if (numeroSelecionar == null
                    && faseSelecionar != null
                    && faseSelecionar.equals(jogo.getFase())
                    && equipaASelecionar != null && equipaASelecionar.equals(jogo.getEquipaA())
                    && equipaBSelecionar != null && equipaBSelecionar.equals(jogo.getEquipaB())) {
                novoSelecionado = jogo;
            }
        }

        if (novoSelecionado != null) {
            listaJogos.setSelectedValue(novoSelecionado, true);
            jogoEmEdicao = novoSelecionado;
        }
        listaJogos.repaint();
    }

    private Comparator<Jogo> comparadorJogos() {
        return (a, b) -> {
            boolean aElim = a.getNumero() > 0;
            boolean bElim = b.getNumero() > 0;
            if (aElim && bElim) return Integer.compare(a.getNumero(), b.getNumero());
            if (aElim) return 1;
            if (bElim) return -1;
            int fase = String.valueOf(a.getFase()).compareTo(String.valueOf(b.getFase()));
            if (fase != 0) return fase;
            int equipaA = String.valueOf(a.getEquipaA()).compareTo(String.valueOf(b.getEquipaA()));
            if (equipaA != 0) return equipaA;
            return String.valueOf(a.getEquipaB()).compareTo(String.valueOf(b.getEquipaB()));
        };
    }
}
