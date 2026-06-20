package org.modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class JanelaPrincipal extends BaseFrame {

    private enum FiltroJogos {
        HOJE,
        TODOS,
        ANTERIORES,
        DISPONIVEL
    }

    private JPanel janelaPrincipal;
    private JPanel menuPrincipal;
    private JButton btnEquipas;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;
    private JButton btnUser;
    private JLabel lblNomeCampeonato;

    private JPanel painelFiltro;
    private JLabel lblMostrar;
    private JRadioButton rbHoje;
    private JRadioButton rbTodos;
    private JRadioButton rbAnteriores;
    private JRadioButton rbDisponivel;
    private JLabel lblEquipa;
    private JComboBox<String> cmbEquipa;

    private JScrollPane scrollLista;
    private JPanel painelLista;

    private JPanel totalMerch;
    private JLabel lblCarrinhoNum;
    private JButton btnComprar;

    private static final Color FUNDO_PAGINA = new Color(0xFFFFFF);
    private static final Color FUNDO_CARD = new Color(0x3A3F47);
    private static final Color TEXTO_CARD = new Color(0xF7F7F7);
    private static final Color TEXTO_GRUPO = new Color(0x1F2228);
    private static final Color STATUS_LIVRE_FUNDO = new Color(0x2D6A4F);
    private static final Color STATUS_ESGOTADO_FUNDO = new Color(0x8B1E1E);
    private static final Color STATUS_TEXTO = new Color(0xF7F7F7);
    private static final Color BOTAO_ADICIONAR = new Color(0xFFB627);
    private static final Color BOTAO_ADICIONAR_TEXTO = new Color(0x1F2228);
    private static final Color BOTAO_INDISPONIVEL = new Color(0x7A7A7A);

    private static final String TODAS_AS_EQUIPAS = "Todas as equipas";

    private ArrayList<JogoCalendario> jogos;

    public JanelaPrincipal(String title) {
        super(title);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(janelaPrincipal);

        super.btnGestao = btnGestao;
        super.btnEquipas = btnEquipas;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;
        super.btnUser = btnUser;

        configurarMenuGestao();
        configurarFiltro();
        configurarListaJogos();
        carregarDados();
        configurarFiltroEquipa();
        renderizarJogos();

        atualizarContadorCarrinho();
        CarrinhoStore.getInstance().registarListener(this::onCarrinhoAlterado);

        btnComprar.addActionListener(this::btnComprarActionPerformed);

        pack();
        setLocationRelativeTo(null);
    }

    private void configurarFiltro() {
        ButtonGroup grupoFiltro = new ButtonGroup();
        grupoFiltro.add(rbHoje);
        grupoFiltro.add(rbTodos);
        grupoFiltro.add(rbAnteriores);
        grupoFiltro.add(rbDisponivel);

        rbHoje.setSelected(true);   // filtro "Hoje" como predefinição

        rbHoje.addActionListener(e -> renderizarJogos());
        rbTodos.addActionListener(e -> renderizarJogos());
        rbAnteriores.addActionListener(e -> renderizarJogos());
        rbDisponivel.addActionListener(e -> renderizarJogos());
    }

    private void configurarFiltroEquipa() {
        cmbEquipa.addItem(TODAS_AS_EQUIPAS);

        Set<String> equipas = new TreeSet<>();
        for (JogoCalendario jogo : jogos) {
            equipas.add(jogo.getEquipaA());
            equipas.add(jogo.getEquipaB());
        }
        for (String equipa : equipas) {
            cmbEquipa.addItem(equipa);
        }

        cmbEquipa.addActionListener(e -> renderizarJogos());
    }

    private String getEquipaSelecionada() {
        Object selecionada = cmbEquipa.getSelectedItem();
        return selecionada == null ? TODAS_AS_EQUIPAS : selecionada.toString();
    }

    private boolean aplicaFiltroEquipa(JogoCalendario jogo, String equipa) {
        if (TODAS_AS_EQUIPAS.equals(equipa)) return true;
        return equipa.equals(jogo.getEquipaA()) || equipa.equals(jogo.getEquipaB());
    }

    private void configurarListaJogos() {
        painelLista.setLayout(new BoxLayout(painelLista, BoxLayout.Y_AXIS));
        painelLista.setBackground(FUNDO_PAGINA);
        scrollLista.getViewport().setBackground(FUNDO_PAGINA);
        scrollLista.setBorder(BorderFactory.createEmptyBorder());
        scrollLista.getVerticalScrollBar().setUnitIncrement(16);
    }

    private void carregarDados() {
        jogos = RepositorioDados.carregarJogosCalendario();

        if (jogos == null) {
            jogos = new ArrayList<>();
        }

        System.out.println("Jogos calendário carregados: " + jogos.size());
    }

    private FiltroJogos getFiltroSelecionado() {
        if (rbHoje.isSelected()) return FiltroJogos.HOJE;
        if (rbAnteriores.isSelected()) return FiltroJogos.ANTERIORES;
        if (rbDisponivel.isSelected()) return FiltroJogos.DISPONIVEL;
        return FiltroJogos.TODOS;
    }

    private boolean aplicaFiltro(JogoCalendario jogo, FiltroJogos filtro) {
        switch (filtro) {
            case HOJE: return jogo.isHoje();
            case ANTERIORES: return jogo.isAnterior();
            case DISPONIVEL: return !jogo.isEsgotado();
            case TODOS: return true;
            default: return true;
        }
    }

    private void renderizarJogos() {
        painelLista.removeAll();

        FiltroJogos filtro = getFiltroSelecionado();
        Map<String, List<JogoCalendario>> porData = agruparPorData(filtro);

        if (porData.isEmpty()) {
            painelLista.add(criarMensagemVazia());
        } else {
            for (Map.Entry<String, List<JogoCalendario>> entry : porData.entrySet()) {
                painelLista.add(criarHeaderData(entry.getKey()));

                for (JogoCalendario jogo : entry.getValue()) {
                    painelLista.add(criarCardJogo(jogo));
                    painelLista.add(Box.createVerticalStrut(10));
                }

                painelLista.add(Box.createVerticalStrut(15));
            }
        }

        painelLista.add(Box.createVerticalGlue());
        painelLista.revalidate();
        painelLista.repaint();
    }

    private Map<String, List<JogoCalendario>> agruparPorData(FiltroJogos filtro) {
        List<JogoCalendario> filtrados = new ArrayList<>();
        String equipa = getEquipaSelecionada();

        for (JogoCalendario jogo : jogos) {
            if (!aplicaFiltroEquipa(jogo, equipa)) continue;
            if (aplicaFiltro(jogo, filtro)) filtrados.add(jogo);
        }

        filtrados.sort(Comparator.comparing(JogoCalendario::getDataHora));

        Map<String, List<JogoCalendario>> porData = new LinkedHashMap<>();
        for (JogoCalendario jogo : filtrados) {
            porData
                    .computeIfAbsent(jogo.getDataFormatada(), k -> new ArrayList<>())
                    .add(jogo);
        }
        return porData;
    }

    private JComponent criarHeaderData(String data) {
        JLabel lbl = new JLabel("📅 " + data);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 16f));
        lbl.setForeground(TEXTO_GRUPO);
        lbl.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JComponent criarMensagemVazia() {
        JLabel lbl = new JLabel("Não há jogos para este filtro.");
        lbl.setFont(lbl.getFont().deriveFont(Font.ITALIC, 14f));
        lbl.setForeground(TEXTO_GRUPO);
        lbl.setBorder(BorderFactory.createEmptyBorder(20, 4, 20, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel criarCardJogo(JogoCalendario jogo) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(FUNDO_CARD);
        card.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 4);

        JLabel lblEquipas = new JLabel( jogo.getEquipaA() + "   X   " + jogo.getEquipaB());
        lblEquipas.setForeground(TEXTO_CARD);
        lblEquipas.setFont(lblEquipas.getFont().deriveFont(Font.BOLD, 15f));
        lblEquipas.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(lblEquipas, gbc);
        gbc.fill = GridBagConstraints.NONE;

        JLabel lblHora = new JLabel("🕐 " + jogo.getHoraFormatada());
        lblHora.setForeground(TEXTO_CARD);
        lblHora.setFont(lblHora.getFont().deriveFont(Font.PLAIN, 13f));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
        card.add(lblHora, gbc);

        JLabel lblStatus = criarLabelStatus(jogo);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        card.add(lblStatus, gbc);

        JLabel lblInfo = new JLabel(descricaoFaseGrupo(jogo));
        lblInfo.setForeground(TEXTO_CARD);
        lblInfo.setFont(lblInfo.getFont().deriveFont(Font.PLAIN, 13f));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        card.add(lblInfo, gbc);
        gbc.weightx = 0;

        JComponent controloCarrinho = criarControloCarrinho(jogo);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        card.add(controloCarrinho, gbc);

        return card;
    }

    private String descricaoFaseGrupo(JogoCalendario jogo) {
        String grupo = jogo.getGrupo();
        String grupoFormatado = tituloCaso(grupo);
        boolean faseDeGrupos = grupo != null && grupo.trim().toUpperCase().startsWith("GRUPO");
        String prefixo = faseDeGrupos ? "Primeira Fase - " : "";
        return prefixo + grupoFormatado + " - " + jogo.getEstadio();
    }

    private String tituloCaso(String texto) {
        if (texto == null || texto.isBlank()) return "";
        StringBuilder sb = new StringBuilder();
        for (String palavra : texto.trim().toLowerCase().split("\\s+")) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(palavra.charAt(0)));
            if (palavra.length() > 1) sb.append(palavra.substring(1));
        }
        return sb.toString();
    }

    private JLabel criarLabelStatus(JogoCalendario jogo) {
        JLabel lbl = new JLabel();
        lbl.setOpaque(true);
        lbl.setForeground(STATUS_TEXTO);
        lbl.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 12f));

        if (jogo.isEsgotado()) {
            lbl.setText("Status: Esgotado");
            lbl.setBackground(STATUS_ESGOTADO_FUNDO);
        } else {
            lbl.setText("Status: Livre");
            lbl.setBackground(STATUS_LIVRE_FUNDO);
        }
        return lbl;
    }

    private JComponent criarControloCarrinho(JogoCalendario jogo) {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        painel.setOpaque(false);

        if (jogo.isEsgotado()) {
            JButton btn = new JButton("Indisponível");
            btn.setFont(btn.getFont().deriveFont(Font.BOLD, 12f));
            btn.setEnabled(false);
            btn.setBackground(BOTAO_INDISPONIVEL);
            btn.setForeground(TEXTO_CARD);
            painel.add(btn);
            return painel;
        }

        int qtd = CarrinhoStore.getInstance().getQuantidadeBilhete(jogo);

        if (qtd > 0) {
            JLabel lblNoCarrinho = new JLabel(qtd + " no carrinho");
            lblNoCarrinho.setForeground(TEXTO_CARD);
            lblNoCarrinho.setFont(lblNoCarrinho.getFont().deriveFont(Font.PLAIN, 12f));
            lblNoCarrinho.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
            painel.add(lblNoCarrinho);
        }

        JButton btnAdicionar = new JButton("Escolher bancada");
        btnAdicionar.setFont(btnAdicionar.getFont().deriveFont(Font.BOLD, 12f));
        btnAdicionar.setBackground(BOTAO_ADICIONAR);
        btnAdicionar.setForeground(BOTAO_ADICIONAR_TEXTO);
        btnAdicionar.addActionListener(e -> abrirComprarBilhete(jogo));
        painel.add(btnAdicionar);
        return painel;
    }

    private void abrirComprarBilhete(JogoCalendario jogo) {
        WindowManager.abrirJanela(
                this,
                "comprarBilhete",
                "A janela Comprar Bilhete já está aberta!",
                new ComprarBilhete(jogo)
        );
    }

    private void atualizarContadorCarrinho() {
        int n = CarrinhoStore.getInstance().getNumeroItens();
        lblCarrinhoNum.setText("🛒 " + n);
    }

    private void onCarrinhoAlterado() {
        atualizarContadorCarrinho();
        // Após uma compra concluída o carrinho é esvaziado (dispara este
        // método) e os jogos já foram gravados em disco — recarregamos para o
        // calendário refletir os bilhetes vendidos / lugares esgotados.
        recarregarJogos();

        int scroll = scrollLista.getVerticalScrollBar().getValue();
        renderizarJogos();
        SwingUtilities.invokeLater(() ->
                scrollLista.getVerticalScrollBar().setValue(scroll));
    }

    private void recarregarJogos() {
        ArrayList<JogoCalendario> atualizados = RepositorioDados.carregarJogosCalendario();
        if (atualizados != null) {
            jogos = atualizados;
        }
    }

    private void btnComprarActionPerformed(ActionEvent e) {
        if (CarrinhoStore.getInstance().getItens().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "O carrinho está vazio. Adicione pelo menos um item para continuar a compra.",
                    "Carrinho vazio",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        WindowManager.abrirJanela(
                this,
                "carrinho",
                "A janela Carrinho já está aberta!",
                new Carrinho("Campeonato Mundial 2026 - Carrinho")
        );
    }

    //inicilizar ficheiros binarios e abrir aplicacao
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InicializadorDados.inicializar();
            new JanelaPrincipal("Campeonato Mundial 2026").setVisible(true);
        });
    }
}