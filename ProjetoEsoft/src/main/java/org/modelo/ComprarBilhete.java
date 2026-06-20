package org.modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Janela de escolha de bancada para um jogo específico.
 *
 * Construída 100% em código (sem .form) porque a lista de bancadas é
 * dinâmica — carregada de bancadas.dat — e replicar 25 painéis no form
 * designer tornava o ficheiro incomportável.
 *
 * Fluxo: o utilizador clica numa bancada → escolhe quantidade → confirma
 * com "Adicionar ao carrinho". O item é guardado no CarrinhoStore com a
 * bancada associada.
 */
public class ComprarBilhete extends BaseFrame {

    // Paleta — mesma do resto da aplicação
    private static final Color COR_FUNDO = new Color(0xFFFFFF);
    private static final Color COR_HEADER_JOGO = new Color(0x3A3F47);
    private static final Color COR_TEXTO_HEADER = new Color(0xF7F7F7);
    private static final Color COR_RELVADO = new Color(0x6acd59);
    private static final Color COR_DISPONIVEL = new Color(0x2D6A4F);
    private static final Color COR_POUCOS = new Color(0xE76F00);
    private static final Color COR_ESGOTADO = new Color(0x8B1E1E);
    private static final Color COR_SELECIONADA = new Color(0x1565C0);
    private static final Color COR_BOTAO_TEXTO = new Color(0x1F2228);

    // Cores do menu — alinhadas com o .form da JanelaPrincipal
    private static final Color COR_MENU = new Color(0x0F295B);
    private static final Color COR_TEXTO_MENU = new Color(0xF7FFFF);
    private static final Color COR_BTN_GESTAO = new Color(0x20AC27);
    private static final Color COR_BTN_CLASSIFICACOES = new Color(0x2E6EE9);
    private static final Color COR_BTN_MERCH = new Color(0x9050E9);
    private static final Color COR_BTN_CARRINHO = new Color(0xE9C46B);
    private static final Color COR_BOTAO_PRIMARIO = COR_BTN_CARRINHO;

    private final JogoCalendario jogo;
    private final List<Bancada> bancadas;

    private Bancada bancadaSelecionada;
    private JButton botaoBancadaSelecionada;
    private final Map<Bancada, JButton> botoesPorBancada = new LinkedHashMap<>();

    private JLabel lblBancadaSelecionada;
    private JLabel lblPrecoUnitario;
    private JSpinner spnQuantidade;
    private JButton btnAdicionarCarrinho;

    public ComprarBilhete(JogoCalendario jogo) {
        super("Campeonato Mundial 2026 - Comprar bilhete");
        this.jogo = jogo;
        this.bancadas = carregarBancadas();

        // Criar os botões do menu que o BaseFrame espera receber.
        // O BaseFrame.configurarMenuGestao() vai ligar listeners e
        // sincronizar o contador 🛒 com o CarrinhoStore.
        super.btnGestao = criarBotaoMenuColorido("TORNEIO", COR_BTN_GESTAO);
        super.btnEquipas = criarBotaoMenuColorido("EQUIPAS", COR_BTN_CLASSIFICACOES);
        super.btnMerch = criarBotaoMenuColorido("MERCH", COR_BTN_MERCH);
        super.btnCarrinho = criarBotaoMenuColorido(
                "🛒 " + CarrinhoStore.getInstance().getNumeroItens(),
                COR_BTN_CARRINHO
        );

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(criarConteudo());

        configurarMenuGestao();

        pack();
        setMinimumSize(new Dimension(1000, 800));
        setLocationRelativeTo(null);
    }

    private List<Bancada> carregarBancadas() {
        ArrayList<Bancada> lista = RepositorioDados.carregarBancadas();
        if (lista == null || lista.isEmpty()) {
            return new ArrayList<>();
        }
        // bancadas.dat é só o template; a ocupação (lugares livres/esgotados)
        // é a deste jogo. Lemos a versão persistida porque a janela principal
        // pode ter uma cópia em memória desatualizada após uma compra.
        JogoCalendario jogoAtual = jogoComOcupacaoAtual();
        for (Bancada b : lista) {
            b.setLugaresVendidos(jogoAtual.getLugaresVendidosBancada(b.getNome()));
        }
        return lista;
    }

    /** Versão persistida deste jogo (com a ocupação por bancada mais recente). */
    private JogoCalendario jogoComOcupacaoAtual() {
        String chave = jogo.getEquipaA() + " vs " + jogo.getEquipaB();
        for (JogoCalendario j : RepositorioDados.carregarJogosCalendario()) {
            if (chave.equals(j.getEquipaA() + " vs " + j.getEquipaB())) {
                return j;
            }
        }
        return jogo; // ainda não persistido — usa o recebido
    }

    private JPanel criarConteudo() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COR_FUNDO);
        root.add(criarMenuTopo(), BorderLayout.NORTH);
        root.add(criarPainelCentral(), BorderLayout.CENTER);
        root.add(criarPainelInferior(), BorderLayout.SOUTH);
        return root;
    }

    // --- Menu superior ----------------------------------------------------

    /**
     * Constrói a barra de navegação alinhada visualmente com a JanelaPrincipal:
     * fundo azul escuro, título à esquerda, GESTAO/EQUIPAS/MERCH esticam,
     * carrinho com largura fixa à direita.
     */
    private JPanel criarMenuTopo() {
        JPanel menu = new JPanel(new GridBagLayout());
        menu.setBackground(COR_MENU);
        menu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 4, 0, 4);
        gbc.gridy = 0;

        JLabel titulo = new JLabel("🏆 CAMPEONATO MUNDIAL 2026");
        titulo.setForeground(COR_TEXTO_MENU);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 14f));
        gbc.gridx = 0; gbc.weightx = 0;
        menu.add(titulo, gbc);

        gbc.weightx = 1;
        gbc.gridx = 1; menu.add(super.btnGestao, gbc);
        gbc.gridx = 2; menu.add(super.btnEquipas, gbc);
        gbc.gridx = 3; menu.add(super.btnMerch, gbc);

        gbc.weightx = 0;
        gbc.gridx = 4; menu.add(super.btnCarrinho, gbc);

        return menu;
    }

    private JButton criarBotaoMenuColorido(String texto, Color fundo) {
        JButton btn = new JButton(texto);
        btn.setBackground(fundo);
        btn.setForeground(COR_TEXTO_MENU);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 12f));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }

    // --- Centro: cabeçalho do jogo + grelha de bancadas -------------------

    private JPanel criarPainelCentral() {
        JPanel centro = new JPanel(new BorderLayout(0, 10));
        centro.setBackground(COR_FUNDO);
        centro.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        centro.add(criarHeaderJogo(), BorderLayout.NORTH);

        JPanel meio = new JPanel(new BorderLayout(0, 8));
        meio.setBackground(COR_FUNDO);

        JPanel tituloWrapper = new JPanel(new BorderLayout());
        tituloWrapper.setBackground(COR_FUNDO);
        tituloWrapper.add(criarTituloBancadas(), BorderLayout.CENTER);

        meio.add(tituloWrapper, BorderLayout.NORTH);
        meio.add(criarGrelhaBancadas(), BorderLayout.CENTER);
        meio.add(criarLegenda(), BorderLayout.SOUTH);

        centro.add(meio, BorderLayout.CENTER);
        return centro;
    }

    private JPanel criarHeaderJogo() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(COR_HEADER_JOGO);
        header.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 4, 3, 4);

        JLabel equipas = new JLabel(jogo.getEquipaA() + "   X   " + jogo.getEquipaB(),
                SwingConstants.CENTER);
        equipas.setForeground(COR_TEXTO_HEADER);
        equipas.setFont(equipas.getFont().deriveFont(Font.BOLD, 18f));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1;
        header.add(equipas, gbc);

        JLabel data = new JLabel("📅 Data: " + jogo.getDataFormatada());
        data.setForeground(COR_TEXTO_HEADER);
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        header.add(data, gbc);

        JLabel hora = new JLabel("🕐 Hora: " + jogo.getHoraFormatada(), SwingConstants.RIGHT);
        hora.setForeground(COR_TEXTO_HEADER);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        header.add(hora, gbc);

        JLabel estadio = new JLabel("🏟 Estádio: " + jogo.getEstadio());
        estadio.setForeground(COR_TEXTO_HEADER);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        header.add(estadio, gbc);

        return header;
    }

    private JLabel criarTituloBancadas() {
        JLabel lbl = new JLabel("Selecione a bancada", SwingConstants.CENTER);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    /**
     * Constrói o esquema visual do estádio com as bancadas:
     *
     *   [ 3º superior Norte ............................. ]
     *   [ Mob Reduzida Norte | Camarotes Norte           ]
     *   [ 1º piso inferior Norte ........................ ]
     *   [ 2º Oeste | 1º Oeste | RELVADO | 1º Este | 2º Este ]
     *   [ 1º piso inferior Sul .......................... ]
     *   [ Mob Reduzida Sul | Camarotes Sul              ]
     *   [ 3º superior Sul ............................... ]
     */
    private JPanel criarGrelhaBancadas() {
        JPanel grelha = new JPanel();
        grelha.setLayout(new BoxLayout(grelha, BoxLayout.Y_AXIS));
        grelha.setBackground(COR_FUNDO);
        grelha.setAlignmentX(Component.LEFT_ALIGNMENT);

        grelha.add(criarLinhaBancadas(filtrarPiso(Bancada.Setor.NORTE, Bancada.Piso.SUPERIOR_3)));
        grelha.add(Box.createVerticalStrut(4));
        grelha.add(criarLinhaBancadas(filtrarPiso(Bancada.Setor.MOBILIDADE_REDUZIDA, null,
                                                  Bancada.Setor.CAMAROTE, Bancada.Setor.NORTE)));
        grelha.add(Box.createVerticalStrut(4));
        grelha.add(criarLinhaBancadas(filtrarPiso(Bancada.Setor.NORTE, Bancada.Piso.INFERIOR)));
        grelha.add(Box.createVerticalStrut(4));
        grelha.add(criarLinhaCentral());
        grelha.add(Box.createVerticalStrut(4));
        grelha.add(criarLinhaBancadas(filtrarPiso(Bancada.Setor.SUL, Bancada.Piso.INFERIOR)));
        grelha.add(Box.createVerticalStrut(4));
        grelha.add(criarLinhaBancadas(filtrarPiso(Bancada.Setor.MOBILIDADE_REDUZIDA, null,
                                                  Bancada.Setor.CAMAROTE, Bancada.Setor.SUL)));
        grelha.add(Box.createVerticalStrut(4));
        grelha.add(criarLinhaBancadas(filtrarPiso(Bancada.Setor.SUL, Bancada.Piso.SUPERIOR_3)));

        return grelha;
    }

    /**
     * Constrói a linha central com Oeste | Relvado | Este.
     * O 2º piso (Oeste/Este) é mostrado nas extremidades.
     */
    private JPanel criarLinhaCentral() {
        JPanel linha = new JPanel(new GridLayout(1, 5, 4, 0));
        linha.setBackground(COR_FUNDO);
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        linha.add(criarColuna(filtrarPiso(Bancada.Setor.OESTE, Bancada.Piso.SUPERIOR_2)));
        linha.add(criarColuna(filtrarPiso(Bancada.Setor.OESTE, Bancada.Piso.INFERIOR)));
        linha.add(criarPainelRelvado());
        linha.add(criarColuna(filtrarPiso(Bancada.Setor.ESTE, Bancada.Piso.INFERIOR)));
        linha.add(criarColuna(filtrarPiso(Bancada.Setor.ESTE, Bancada.Piso.SUPERIOR_2)));

        return linha;
    }

    private JPanel criarColuna(List<Bancada> bancadasColuna) {
        JPanel col = new JPanel();
        col.setLayout(new GridLayout(bancadasColuna.size(), 1, 0, 4));
        col.setBackground(COR_FUNDO);
        for (Bancada b : bancadasColuna) {
            col.add(criarBotaoBancada(b));
        }
        return col;
    }

    private JPanel criarPainelRelvado() {
        JPanel relvado = new JPanel(new GridBagLayout());
        relvado.setBackground(COR_RELVADO);
        relvado.setBorder(BorderFactory.createLineBorder(new Color(0x1B4332), 2));
        JLabel lbl = new JLabel("RELVADO");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18f));
        relvado.add(lbl);
        return relvado;
    }

    private JPanel criarLinhaBancadas(List<Bancada> bancadasLinha) {
        JPanel linha = new JPanel(new GridLayout(1, Math.max(1, bancadasLinha.size()), 4, 0));
        linha.setBackground(COR_FUNDO);
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        for (Bancada b : bancadasLinha) {
            linha.add(criarBotaoBancada(b));
        }
        return linha;
    }

    private JButton criarBotaoBancada(Bancada bancada) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createLineBorder(new Color(0x222222), 1));

        JLabel nome = new JLabel(bancada.getNome(), SwingConstants.CENTER);
        nome.setForeground(Color.WHITE);
        nome.setFont(nome.getFont().deriveFont(Font.BOLD, 11f));

        JLabel disp = new JLabel(textoDisponibilidade(bancada), SwingConstants.CENTER);
        disp.setForeground(Color.WHITE);
        disp.setFont(disp.getFont().deriveFont(Font.PLAIN, 10f));

        btn.add(nome, BorderLayout.CENTER);
        btn.add(disp, BorderLayout.SOUTH);
        btn.setBackground(corPorEstado(bancada.getEstado()));

        if (!bancada.isEsgotada()) {
            btn.addActionListener(e -> selecionarBancada(bancada, btn));
        } else {
            btn.setEnabled(false);
        }

        botoesPorBancada.put(bancada, btn);
        return btn;
    }

    private String textoDisponibilidade(Bancada b) {
        switch (b.getEstado()) {
            case ESGOTADO: return "Esgotado";
            case POUCOS_LUGARES: return "Poucos lugares (" + b.getLugaresDisponiveis() + ")";
            default: return b.getLugaresDisponiveis() + " livres";
        }
    }

    private Color corPorEstado(Bancada.Estado estado) {
        switch (estado) {
            case ESGOTADO: return COR_ESGOTADO;
            case POUCOS_LUGARES: return COR_POUCOS;
            default: return COR_DISPONIVEL;
        }
    }

    private void selecionarBancada(Bancada bancada, JButton btn) {
        // Repor cor anterior
        if (botaoBancadaSelecionada != null && bancadaSelecionada != null) {
            botaoBancadaSelecionada.setBackground(corPorEstado(bancadaSelecionada.getEstado()));
        }

        bancadaSelecionada = bancada;
        botaoBancadaSelecionada = btn;
        btn.setBackground(COR_SELECIONADA);

        atualizarResumo();
        atualizarLimiteQuantidade();
    }

    private JPanel criarLegenda() {
        JPanel leg = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 4));
        leg.setBackground(COR_FUNDO);
        leg.setAlignmentX(Component.CENTER_ALIGNMENT);
        leg.add(criarBlocoLegenda("Disponível", COR_DISPONIVEL));
        leg.add(criarBlocoLegenda("Poucos lugares", COR_POUCOS));
        leg.add(criarBlocoLegenda("Esgotado", COR_ESGOTADO));
        leg.add(criarBlocoLegenda("Selecionada", COR_SELECIONADA));
        return leg;
    }

    private JPanel criarBlocoLegenda(String texto, Color cor) {
        JPanel bloco = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        bloco.setOpaque(false);
        JPanel quadrado = new JPanel();
        quadrado.setBackground(cor);
        quadrado.setPreferredSize(new Dimension(14, 14));
        bloco.add(quadrado);
        bloco.add(new JLabel(texto));
        return bloco;
    }

    // --- Painel inferior: resumo + botões ---------------------------------

    private JPanel criarPainelInferior() {
        JPanel inferior = new JPanel(new BorderLayout(0, 8));
        inferior.setBackground(COR_FUNDO);
        inferior.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        inferior.add(criarResumo(), BorderLayout.CENTER);
        inferior.add(criarBotoesFinais(), BorderLayout.SOUTH);

        return inferior;
    }

    private JPanel criarResumo() {
        JPanel resumo = new JPanel(new GridLayout(3, 2, 8, 12));
        resumo.setBackground(COR_FUNDO);
        resumo.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        resumo.add(criarLabelCampo("Bancada selecionada:"));
        lblBancadaSelecionada = new JLabel("—", SwingConstants.RIGHT);
        lblBancadaSelecionada.setFont(lblBancadaSelecionada.getFont().deriveFont(Font.BOLD, 15f));
        resumo.add(lblBancadaSelecionada);

        resumo.add(criarLabelCampo("Preço por bilhete:"));
        lblPrecoUnitario = new JLabel("—", SwingConstants.RIGHT);
        lblPrecoUnitario.setFont(lblPrecoUnitario.getFont().deriveFont(15f));
        resumo.add(lblPrecoUnitario);

        resumo.add(criarLabelCampo("Quantidade:"));
        spnQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));
        spnQuantidade.setEnabled(false);
        spnQuantidade.setFont(spnQuantidade.getFont().deriveFont(15f));
        spnQuantidade.setPreferredSize(new Dimension(0, 32));
        resumo.add(spnQuantidade);

        return resumo;
    }

    private JLabel criarLabelCampo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(lbl.getFont().deriveFont(14f));
        return lbl;
    }

    private JPanel criarBotoesFinais() {
        JPanel botoes = new JPanel(new GridLayout(1, 1, 12, 0));
        botoes.setBackground(COR_FUNDO);

        btnAdicionarCarrinho = new JButton("Adicionar ao carrinho");
        btnAdicionarCarrinho.setBackground(COR_BOTAO_PRIMARIO);
        btnAdicionarCarrinho.setForeground(COR_BOTAO_TEXTO);
        btnAdicionarCarrinho.setFont(btnAdicionarCarrinho.getFont().deriveFont(Font.BOLD, 13f));
        btnAdicionarCarrinho.setEnabled(false);
        btnAdicionarCarrinho.addActionListener(this::adicionarAoCarrinho);

        botoes.add(btnAdicionarCarrinho);
        return botoes;
    }

    private void atualizarResumo() {
        if (bancadaSelecionada == null) {
            lblBancadaSelecionada.setText("—");
            lblPrecoUnitario.setText("—");
            btnAdicionarCarrinho.setEnabled(false);
            return;
        }

        double preco = bancadaSelecionada.getPreco(jogo.getPrecoBilhete());
        lblBancadaSelecionada.setText(bancadaSelecionada.getNome());
        lblPrecoUnitario.setText(formatarPreco(preco));
        btnAdicionarCarrinho.setEnabled(true);
    }

    private void atualizarLimiteQuantidade() {
        int max = Math.max(1, bancadaSelecionada.getLugaresDisponiveis());
        spnQuantidade.setModel(new SpinnerNumberModel(1, 1, max, 1));
        spnQuantidade.setEnabled(true);
    }

    private String formatarPreco(double valor) {
        return String.format("%.2f€", valor).replace('.', ',');
    }

    private void adicionarAoCarrinho(ActionEvent e) {
        if (bancadaSelecionada == null) return;

        int qtd = (Integer) spnQuantidade.getValue();
        CarrinhoStore.getInstance().adicionarBilhete(jogo, bancadaSelecionada, qtd);

        JOptionPane.showMessageDialog(
                this,
                qtd + " bilhete(s) para " + bancadaSelecionada.getNome() + " adicionados ao carrinho.",
                "Adicionado ao carrinho",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
    }

    // Lista de bancadas para um setor + piso. Se piso == null, considera todos.
    private List<Bancada> filtrarPiso(Bancada.Setor setor, Bancada.Piso piso) {
        List<Bancada> resultado = new ArrayList<>();
        for (Bancada b : bancadas) {
            if (b.getSetor() == setor && (piso == null || b.getPiso() == piso)) {
                resultado.add(b);
            }
        }
        return resultado;
    }

    // Versão para a linha "Mob Reduzida + Camarote Norte/Sul":
    // devolve mobilidade reduzida do lado norte/sul (heurística pelo nome)
    // + camarote do mesmo lado, intercalados como no mockup.
    private List<Bancada> filtrarPiso(Bancada.Setor mobSetor, Bancada.Piso mobPiso,
                                      Bancada.Setor camSetor, Bancada.Setor lado) {
        String marcador = (lado == Bancada.Setor.NORTE) ? "Norte" : "Sul";
        List<Bancada> resultado = new ArrayList<>();
        Bancada mobEsq = null, mobDrt = null, cam = null;
        for (Bancada b : bancadas) {
            if (!b.getNome().contains(marcador)) continue;
            if (b.getSetor() == Bancada.Setor.MOBILIDADE_REDUZIDA) {
                if (b.getNome().endsWith("Esq")) mobEsq = b;
                else if (b.getNome().endsWith("Drt")) mobDrt = b;
            } else if (b.getSetor() == Bancada.Setor.CAMAROTE) {
                cam = b;
            }
        }
        if (mobEsq != null) resultado.add(mobEsq);
        if (cam != null) resultado.add(cam);
        if (mobDrt != null) resultado.add(mobDrt);
        return resultado;
    }
}