package org.modelo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComprarMerch extends BaseFrame {

    private static final Color AZUL_ESCURO = new Color(18, 45, 91);
    private static final Color AZUL = new Color(30, 91, 137);
    private static final Color VERDE = new Color(31, 174, 47);
    private static final Color ROXO = new Color(142, 75, 226);
    private static final Color LARANJA = new Color(255, 183, 106);
    private static final Color CINZA_FUNDO = new Color(245, 247, 250);
    private static final Color CINZA_TEXTO = new Color(70, 77, 87);
    private static final Color BORDA = new Color(205, 216, 230);

    private Produto produtoSelecionado;
    private String tamanhoSelecionado;
    private ArrayList<Produto> produtos;

    private JPanel painelPrincipal;

    // botões do menu/cabeçalho
    private JPanel menuPrincipal;
    private JLabel lblNomeCampeonato;
    private JButton btnGestao;
    private JButton btnEquipas;
    private JButton btnMerch;
    private JButton btnCarrinho;

    // componentes da compra mantidos por compatibilidade com o .form antigo
    private JButton btnProdutos;
    private JButton btnTam;
    private JLabel produtoEsc;
    private JLabel tamEsc;
    private JButton btnComprar;
    private JSpinner spinnerQuantidade;
    private JPanel cabecalhoMerch;
    private JLabel lblNomeMerch;
    private JLabel lblTxtCompra;
    private JPanel estruturaMerch;
    private JLabel lblNomeProduto;
    private JLabel lblNomeTamanho;
    private JPanel categMerch1;
    private JPanel categMerch2;
    private JButton btnLimpar;

    private JPopupMenu popupTam;

    // novos componentes para uma interface mais intuitiva
    private JPanel painelProdutos;
    private JPanel painelTamanhos;
    private JLabel lblPrecoProduto;
    private JLabel lblStockProduto;
    private JLabel lblSubtotal;
    private JLabel lblEstado;
    private JLabel lblTotalCarrinho;
    private JLabel lblNumeroItensCarrinho;
    private JButton btnFinalizarCompra;
    private final Map<Produto, JPanel> cardsProduto = new HashMap<>();
    private CarrinhoStore.CarrinhoListener listenerCarrinhoMerch;

    public ComprarMerch(String title) {
        super(title);

        produtos = RepositorioDados.carregarProdutos();
        construirInterface();

        setContentPane(painelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnEquipas = btnEquipas;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();
        configurarMerchCompra();

        setMinimumSize(new Dimension(1000, 700));
        pack();
        setLocationRelativeTo(null);
    }

    private void construirInterface() {
        painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(CINZA_FUNDO);
        painelPrincipal.setPreferredSize(new Dimension(1120, 740));

        painelPrincipal.add(criarCabecalho(), BorderLayout.NORTH);
        painelPrincipal.add(criarConteudo(), BorderLayout.CENTER);
        painelPrincipal.add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarCabecalho() {
        menuPrincipal = new JPanel(new BorderLayout(20, 0));
        menuPrincipal.setBackground(AZUL_ESCURO);
        menuPrincipal.setBorder(new EmptyBorder(18, 28, 18, 28));

        lblNomeCampeonato = new JLabel("🏆 CAMPEONATO MUNDIAL 2026");
        lblNomeCampeonato.setForeground(Color.WHITE);
        lblNomeCampeonato.setFont(lblNomeCampeonato.getFont().deriveFont(Font.BOLD, 18f));

        JPanel botoesMenu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        botoesMenu.setOpaque(false);

        btnGestao = criarBotaoMenu("TORNEIO", VERDE);
        btnEquipas = criarBotaoMenu("EQUIPAS", new Color(51, 116, 217));
        btnMerch = criarBotaoMenu("MERCH", ROXO);
        btnCarrinho = criarBotaoMenu("🛒 0", new Color(240, 199, 91));

        botoesMenu.add(btnGestao);
        botoesMenu.add(btnEquipas);
        botoesMenu.add(btnMerch);
        botoesMenu.add(btnCarrinho);

        menuPrincipal.add(lblNomeCampeonato, BorderLayout.WEST);
        menuPrincipal.add(botoesMenu, BorderLayout.EAST);
        return menuPrincipal;
    }

    private JButton criarBotaoMenu(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setPreferredSize(new Dimension(150, 38));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(botao.getFont().deriveFont(Font.BOLD, 14f));
        botao.setBorder(new LineBorder(new Color(230, 235, 245), 1));
        return botao;
    }

    private JPanel criarConteudo() {
        JPanel conteudo = new JPanel(new BorderLayout(18, 18));
        conteudo.setBackground(CINZA_FUNDO);
        conteudo.setBorder(new EmptyBorder(28, 28, 20, 28));

        cabecalhoMerch = new JPanel(new GridLayout(2, 1, 0, 6));
        cabecalhoMerch.setOpaque(false);

        lblNomeMerch = new JLabel("MERCH OFICIAL");
        lblNomeMerch.setFont(lblNomeMerch.getFont().deriveFont(Font.BOLD, 28f));
        lblNomeMerch.setForeground(new Color(35, 39, 47));

        lblTxtCompra = new JLabel("Escolhe o produto, seleciona o tamanho disponível e adiciona ao carrinho.");
        lblTxtCompra.setFont(lblTxtCompra.getFont().deriveFont(Font.PLAIN, 15f));
        lblTxtCompra.setForeground(CINZA_TEXTO);

        cabecalhoMerch.add(lblNomeMerch);
        cabecalhoMerch.add(lblTxtCompra);

        conteudo.add(cabecalhoMerch, BorderLayout.NORTH);

        JPanel corpo = new JPanel(new BorderLayout(20, 0));
        corpo.setOpaque(false);
        corpo.add(criarPainelProdutos(), BorderLayout.CENTER);
        corpo.add(criarPainelSelecao(), BorderLayout.EAST);

        conteudo.add(corpo, BorderLayout.CENTER);
        return conteudo;
    }

    private JPanel criarPainelProdutos() {
        JPanel wrapper = criarCartaoBase(new BorderLayout(0, 14));

        JLabel titulo = criarTituloSecao("1. Escolhe o produto");
        wrapper.add(titulo, BorderLayout.NORTH);

        painelProdutos = new JPanel(new GridLayout(0, 2, 14, 14));
        painelProdutos.setBackground(Color.WHITE);

        if (produtos == null || produtos.isEmpty()) {
            JLabel vazio = new JLabel("Ainda não existem produtos disponíveis.");
            vazio.setForeground(CINZA_TEXTO);
            painelProdutos.add(vazio);
        } else {
            for (Produto produto : produtos) {
                JPanel card = criarCardProduto(produto);
                cardsProduto.put(produto, card);
                painelProdutos.add(card);
            }
        }

        JScrollPane scroll = new JScrollPane(painelProdutos);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        wrapper.add(scroll, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel criarPainelSelecao() {
        JPanel painel = criarCartaoBase(new BorderLayout(0, 14));
        painel.setPreferredSize(new Dimension(360, 0));

        JLabel titulo = criarTituloSecao("2. Personaliza a compra");
        painel.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        JLabel lblProdutoTitulo = criarLabelPequeno("Produto selecionado");
        produtoEsc = criarLabelDestaque("Nenhum produto escolhido");
        lblPrecoProduto = criarLabelInfo("Preço: —");
        lblStockProduto = criarLabelInfo("Stock: —");

        centro.add(lblProdutoTitulo);
        centro.add(Box.createVerticalStrut(4));
        centro.add(produtoEsc);
        centro.add(Box.createVerticalStrut(6));
        centro.add(lblPrecoProduto);
        centro.add(Box.createVerticalStrut(4));
        centro.add(lblStockProduto);
        centro.add(Box.createVerticalStrut(18));

        JLabel lblTamanho = criarLabelPequeno("Tamanho disponível");
        tamEsc = criarLabelDestaque("Escolhe primeiro um produto");
        painelTamanhos = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        painelTamanhos.setOpaque(false);

        centro.add(lblTamanho);
        centro.add(Box.createVerticalStrut(4));
        centro.add(tamEsc);
        centro.add(Box.createVerticalStrut(8));
        centro.add(painelTamanhos);
        centro.add(Box.createVerticalStrut(18));

        JLabel lblQuantidade = criarLabelPequeno("Quantidade");
        spinnerQuantidade = new JSpinner(new SpinnerNumberModel(0, 0, 0, 1));
        spinnerQuantidade.setMaximumSize(new Dimension(120, 34));
        spinnerQuantidade.setEnabled(false);

        centro.add(lblQuantidade);
        centro.add(Box.createVerticalStrut(6));
        centro.add(spinnerQuantidade);
        centro.add(Box.createVerticalStrut(18));

        lblSubtotal = criarLabelDestaque("Subtotal: 0,00€");
        centro.add(lblSubtotal);
        centro.add(Box.createVerticalStrut(12));

        lblEstado = criarLabelInfo("Seleciona um produto para começar.");
        centro.add(lblEstado);

        painel.add(centro, BorderLayout.CENTER);

        JPanel acoes = new JPanel(new GridLayout(3, 1, 0, 8));
        acoes.setOpaque(false);

        btnComprar = criarBotaoAcao("🛒 Adicionar ao carrinho", LARANJA, new Color(30, 30, 30));
        btnLimpar = criarBotaoAcao("Limpar seleção", new Color(255, 91, 96), Color.WHITE);
        btnFinalizarCompra = criarBotaoAcao("Finalizar compra", VERDE, Color.WHITE);

        btnComprar.setEnabled(false);
        acoes.add(btnComprar);
        acoes.add(btnLimpar);
        acoes.add(btnFinalizarCompra);

        painel.add(acoes, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout(18, 0));
        rodape.setBackground(AZUL_ESCURO);
        rodape.setBorder(new EmptyBorder(18, 28, 18, 28));

        JPanel resumo = new JPanel(new GridLayout(2, 1, 0, 4));
        resumo.setOpaque(false);

        lblNumeroItensCarrinho = new JLabel("Carrinho: 0 itens");
        lblNumeroItensCarrinho.setForeground(Color.WHITE);
        lblNumeroItensCarrinho.setFont(lblNumeroItensCarrinho.getFont().deriveFont(Font.BOLD, 15f));

        lblTotalCarrinho = new JLabel("Total: 0,00€");
        lblTotalCarrinho.setForeground(new Color(255, 236, 190));
        lblTotalCarrinho.setFont(lblTotalCarrinho.getFont().deriveFont(Font.BOLD, 20f));

        resumo.add(lblNumeroItensCarrinho);
        resumo.add(lblTotalCarrinho);

        JLabel dica = new JLabel("Dica: podes adicionar vários produtos antes de finalizar a compra.");
        dica.setForeground(new Color(226, 232, 244));
        dica.setHorizontalAlignment(SwingConstants.RIGHT);

        rodape.add(resumo, BorderLayout.WEST);
        rodape.add(dica, BorderLayout.EAST);
        return rodape;
    }

    private JPanel criarCartaoBase(LayoutManager layout) {
        JPanel painel = new JPanel(layout);
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDA, 1, true),
                new EmptyBorder(18, 18, 18, 18)
        ));
        return painel;
    }

    private JLabel criarTituloSecao(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 18f));
        label.setForeground(new Color(35, 39, 47));
        return label;
    }

    private JLabel criarLabelPequeno(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));
        label.setForeground(CINZA_TEXTO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel criarLabelDestaque(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        label.setForeground(new Color(31, 41, 55));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel criarLabelInfo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 13f));
        label.setForeground(CINZA_TEXTO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JButton criarBotaoAcao(String texto, Color fundo, Color textoCor) {
        JButton botao = new JButton(texto);
        botao.setPreferredSize(new Dimension(220, 42));
        botao.setBackground(fundo);
        botao.setForeground(textoCor);
        botao.setFocusPainted(false);
        botao.setFont(botao.getFont().deriveFont(Font.BOLD, 14f));
        botao.setBorder(new LineBorder(fundo.darker(), 1));
        return botao;
    }

    private JPanel criarCardProduto(Produto produto) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDA, 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel icone = new JLabel(obterIconeProduto(produto), SwingConstants.CENTER);
        icone.setFont(icone.getFont().deriveFont(34f));

        JLabel nome = new JLabel(produto.getNome());
        nome.setFont(nome.getFont().deriveFont(Font.BOLD, 16f));
        nome.setForeground(new Color(35, 39, 47));

        JLabel preco = new JLabel(formatarPreco(produto.getPreco()));
        preco.setFont(preco.getFont().deriveFont(Font.BOLD, 15f));
        preco.setForeground(AZUL_ESCURO);

        JLabel stock = new JLabel(textoStock(produto));
        stock.setForeground(produto.temAlgumStock() ? new Color(30, 130, 76) : new Color(180, 50, 50));
        stock.setFont(stock.getFont().deriveFont(Font.PLAIN, 12f));

        JButton selecionar = criarBotaoAcao("Selecionar", AZUL, Color.WHITE);
        selecionar.setPreferredSize(new Dimension(140, 34));
        selecionar.setEnabled(produto.temAlgumStock());
        selecionar.addActionListener(e -> selecionarProduto(produto));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.add(nome);
        info.add(Box.createVerticalStrut(6));
        info.add(preco);
        info.add(Box.createVerticalStrut(6));
        info.add(stock);

        JPanel baixo = new JPanel(new BorderLayout());
        baixo.setOpaque(false);
        baixo.add(info, BorderLayout.CENTER);
        baixo.add(selecionar, BorderLayout.SOUTH);

        card.add(icone, BorderLayout.NORTH);
        card.add(baixo, BorderLayout.CENTER);

        return card;
    }

    private void configurarMerchCompra() {
        spinnerQuantidade.addChangeListener(e -> atualizarSubtotal());
        btnComprar.addActionListener(e -> comprarProduto());
        btnLimpar.addActionListener(e -> limparSelecionados());
        btnFinalizarCompra.addActionListener(this::abrirFinalizarCompra);

        listenerCarrinhoMerch = this::atualizarResumoCarrinho;
        CarrinhoStore.getInstance().registarListener(listenerCarrinhoMerch);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                CarrinhoStore.getInstance().removerListener(listenerCarrinhoMerch);
            }
        });

        atualizarResumoCarrinho();
        limparSelecionados();
    }

    private void selecionarProduto(Produto produto) {
        produtoSelecionado = produto;
        tamanhoSelecionado = null;

        atualizarCardsSelecionados();
        painelTamanhos.removeAll();

        if (produto == null) {
            produtoEsc.setText("Nenhum produto escolhido");
            tamEsc.setText("Escolhe primeiro um produto");
            lblPrecoProduto.setText("Preço: —");
            lblStockProduto.setText("Stock: —");
            lblEstado.setText("Seleciona um produto para começar.");
            spinnerQuantidade.setModel(new SpinnerNumberModel(0, 0, 0, 1));
            spinnerQuantidade.setEnabled(false);
            btnComprar.setEnabled(false);
            atualizarSubtotal();
            painelTamanhos.revalidate();
            painelTamanhos.repaint();
            return;
        }

        produtoEsc.setText(produto.getNome());
        lblPrecoProduto.setText("Preço: " + formatarPreco(produto.getPreco()));
        lblStockProduto.setText(textoStock(produto));
        tamEsc.setText("Seleciona um tamanho");
        spinnerQuantidade.setModel(new SpinnerNumberModel(0, 0, 0, 1));
        spinnerQuantidade.setEnabled(false);
        btnComprar.setEnabled(false);

        List<String> tamanhosDisponiveis = new ArrayList<>();
        for (String tamanho : tamanhosOrdenados(produto)) {
            int disponivel = obterStockDisponivel(produto, tamanho);
            JButton botaoTamanho = new JButton(tamanho + " (" + disponivel + ")");
            botaoTamanho.setFocusPainted(false);
            botaoTamanho.setFont(botaoTamanho.getFont().deriveFont(Font.BOLD, 12f));
            botaoTamanho.setBackground(disponivel > 0 ? new Color(230, 240, 252) : new Color(238, 238, 238));
            botaoTamanho.setForeground(disponivel > 0 ? AZUL_ESCURO : Color.GRAY);
            botaoTamanho.setBorder(new LineBorder(BORDA, 1, true));
            botaoTamanho.setEnabled(disponivel > 0);

            if (disponivel > 0) {
                tamanhosDisponiveis.add(tamanho);
                botaoTamanho.addActionListener(e -> selecionarTamanho(tamanho));
            }

            painelTamanhos.add(botaoTamanho);
        }

        if (tamanhosDisponiveis.isEmpty()) {
            tamEsc.setText("Sem stock disponível");
            lblEstado.setText("Este produto está esgotado.");
        } else {
            lblEstado.setText("Agora escolhe o tamanho.");
            if (tamanhosDisponiveis.size() == 1) {
                selecionarTamanho(tamanhosDisponiveis.get(0));
            }
        }

        painelTamanhos.revalidate();
        painelTamanhos.repaint();
        atualizarSubtotal();
    }

    private void selecionarTamanho(String tamanho) {
        if (produtoSelecionado == null) return;

        int disponivel = obterStockDisponivel(produtoSelecionado, tamanho);
        if (disponivel <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Este tamanho já não tem stock disponível.",
                    "Sem stock",
                    JOptionPane.WARNING_MESSAGE
            );
            selecionarProduto(produtoSelecionado);
            return;
        }

        tamanhoSelecionado = tamanho;
        tamEsc.setText(tamanho);
        spinnerQuantidade.setModel(new SpinnerNumberModel(1, 1, disponivel, 1));
        spinnerQuantidade.setEnabled(true);
        btnComprar.setEnabled(true);
        lblEstado.setText("Pronto para adicionar ao carrinho.");
        atualizarSubtotal();
    }

    private void limparSelecionados() {
        produtoSelecionado = null;
        tamanhoSelecionado = null;
        selecionarProduto(null);
    }

    private int obterQuantidade() {
        return (int) spinnerQuantidade.getValue();
    }

    private void comprarProduto() {
        if (produtoSelecionado == null) {
            mostrarAviso("Escolhe primeiro um produto.", "Produto obrigatório");
            return;
        }

        if (tamanhoSelecionado == null || tamanhoSelecionado.isBlank()) {
            mostrarAviso("Seleciona o tamanho do produto.", "Tamanho obrigatório");
            return;
        }

        int quantidade = obterQuantidade();
        if (quantidade <= 0) {
            mostrarAviso("A quantidade deve ser superior a 0.", "Quantidade inválida");
            return;
        }

        int disponivel = obterStockDisponivel(produtoSelecionado, tamanhoSelecionado);
        if (quantidade > disponivel) {
            mostrarAviso(
                    "Só existem " + disponivel + " unidades disponíveis para este tamanho.",
                    "Stock insuficiente"
            );
            selecionarProduto(produtoSelecionado);
            return;
        }

        CarrinhoStore.getInstance().adicionarProduto(produtoSelecionado, tamanhoSelecionado, quantidade);
        lblEstado.setText("Adicionado ao carrinho: " + quantidade + " x " + produtoSelecionado.getNome() + ".");

        Produto produtoAtual = produtoSelecionado;
        selecionarProduto(produtoAtual);
    }

    private void abrirFinalizarCompra(ActionEvent e) {
        if (CarrinhoStore.getInstance().getItens().isEmpty()) {
            mostrarAviso("O carrinho está vazio. Adiciona pelo menos um produto ou bilhete.", "Carrinho vazio");
            return;
        }

        WindowManager.abrirJanela(
                this,
                "finalizarCompra",
                "A janela de Finalizar Compra já está aberta!",
                new FinalizarCompra("Campeonato Mundial 2026 - Finalizar compra")
        );
    }

    private void atualizarSubtotal() {
        if (produtoSelecionado == null || tamanhoSelecionado == null || !spinnerQuantidade.isEnabled()) {
            lblSubtotal.setText("Subtotal: 0,00€");
            return;
        }

        int quantidade = obterQuantidade();
        double subtotal = produtoSelecionado.getPreco() * quantidade;
        lblSubtotal.setText("Subtotal: " + formatarPreco(subtotal));
    }

    private void atualizarResumoCarrinho() {
        int numeroItens = CarrinhoStore.getInstance().getNumeroItens();
        double total = CarrinhoStore.getInstance().getTotal();

        lblNumeroItensCarrinho.setText("Carrinho: " + numeroItens + (numeroItens == 1 ? " item" : " itens"));
        lblTotalCarrinho.setText("Total: " + formatarPreco(total));

        if (produtoSelecionado != null) {
            selecionarProduto(produtoSelecionado);
        }
    }

    private void atualizarCardsSelecionados() {
        for (Map.Entry<Produto, JPanel> entrada : cardsProduto.entrySet()) {
            JPanel card = entrada.getValue();
            boolean selecionado = entrada.getKey() == produtoSelecionado;
            card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(selecionado ? AZUL : BORDA, selecionado ? 3 : 1, true),
                    new EmptyBorder(selecionado ? 14 : 16, selecionado ? 14 : 16, selecionado ? 14 : 16, selecionado ? 14 : 16)
            ));
        }
    }

    private int obterStockDisponivel(Produto produto, String tamanho) {
        int stockTotal = produto.getStockDoTamanho(tamanho);
        int jaNoCarrinho = 0;
        String detalhe = "Tamanho " + tamanho;

        for (ItemCarrinho item : CarrinhoStore.getInstance().getItens()) {
            if (item.getTipo() == ItemCarrinho.Tipo.PRODUTO
                    && item.getDescricao().equals(produto.getNome())
                    && item.getDetalhe().equals(detalhe)) {
                jaNoCarrinho += item.getQuantidade();
            }
        }

        return Math.max(0, stockTotal - jaNoCarrinho);
    }

    private List<String> tamanhosOrdenados(Produto produto) {
        List<String> tamanhos = new ArrayList<>();
        String[] ordem = {"XS", "S", "M", "L", "XL", "Único"};

        for (String esperado : ordem) {
            if (produto.getStockPorTamanho().containsKey(esperado)) {
                tamanhos.add(esperado);
            }
        }

        for (String tamanho : produto.getStockPorTamanho().keySet()) {
            if (!tamanhos.contains(tamanho)) {
                tamanhos.add(tamanho);
            }
        }

        return tamanhos;
    }

    private String textoStock(Produto produto) {
        int total = 0;
        for (String tamanho : produto.getStockPorTamanho().keySet()) {
            total += obterStockDisponivel(produto, tamanho);
        }

        if (total <= 0) {
            return "Stock: esgotado";
        }

        return "Stock disponível: " + total + " un.";
    }

    private String obterIconeProduto(Produto produto) {
        String nome = produto.getNome().toLowerCase();
        if (nome.contains("t-shirt") || nome.contains("shirt")) return "👕";
        if (nome.contains("cachecol")) return "🧣";
        if (nome.contains("sweat")) return "🧥";
        if (nome.contains("decoração") || nome.contains("decoracao")) return "🏆";
        return "🛍";
    }

    private String formatarPreco(double valor) {
        return String.format("%.2f€", valor).replace('.', ',');
    }

    private void mostrarAviso(String mensagem, String titulo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, JOptionPane.WARNING_MESSAGE);
    }
}
