package org.modelo;

import javax.swing.*;
import java.util.ArrayList;

public class ComprarMerch extends BaseFrame {

    private Produto produtoSelecionado;
    private ArrayList<Produto> produtos;

    private JPanel painelPrincipal;

    // botões do menu/cabeçalho
    private JPanel menuPrincipal;
    private JLabel lblNomeCampeonato;
    private JButton btnGestao;
    private JButton btnClassificacaoGeral;
    private JButton btnMerch;
    private JButton btnCarrinho;

    // componentes da compra
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

    public ComprarMerch(String title) {
        super(title);

        setContentPane(painelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnClassificacaoGeral = btnClassificacaoGeral;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();
        configurarMerchCompra();

        pack();
        setLocationRelativeTo(null);
    }

    private void configurarMerchCompra() {

        JPopupMenu popupItens = new JPopupMenu();
        JPopupMenu popupTam = new JPopupMenu();

        JMenuItem xs = new JMenuItem("XS");
        JMenuItem s = new JMenuItem("S");
        JMenuItem m = new JMenuItem("M");
        JMenuItem g = new JMenuItem("L");

        spinnerQuantidade.setModel(new SpinnerNumberModel(0, 0, 99, 1));

        produtos = RepositorioDados.carregarProdutos();

        for (Produto produto : produtos) {
            JMenuItem itemProduto = new JMenuItem(produto.getNome());

            itemProduto.addActionListener(e -> selecionarProduto(produto));

            popupItens.add(itemProduto);
        }

        xs.addActionListener(e -> selecionarTamanho("XS"));
        s.addActionListener(e -> selecionarTamanho("S"));
        m.addActionListener(e -> selecionarTamanho("M"));
        g.addActionListener(e -> selecionarTamanho("L"));

        popupTam.add(xs);
        popupTam.add(s);
        popupTam.add(m);
        popupTam.add(g);

        btnProdutos.addActionListener(e ->
                popupItens.show(btnProdutos, 0, btnProdutos.getHeight())
        );

        btnTam.addActionListener(e ->
                popupTam.show(btnTam, 0, btnTam.getHeight())
        );

        btnComprar.addActionListener(e -> comprarProduto());
        btnLimpar.addActionListener(e -> limparSelecionados());
    }

    private void selecionarProduto(Produto produto) {
        produtoSelecionado = produto;
        produtoEsc.setText(produto.getNome());

        tamEsc.setText("");
        popupTam.removeAll();

        if (!produto.temAlgumStock()) {
            tamEsc.setText("Sem stock");
            btnTam.setEnabled(false);
            btnComprar.setEnabled(false);
            return;
        }

        for (String tamanho : produto.getStockPorTamanho().keySet()) {
            int stock = produto.getStockDoTamanho(tamanho);

            if (stock > 0) {
                JMenuItem itemTamanho = new JMenuItem(tamanho + " (" + stock + " disponíveis)");

                itemTamanho.addActionListener(e -> selecionarTamanho(tamanho));

                popupTam.add(itemTamanho);
            }
        }

        btnTam.setEnabled(true);
        btnComprar.setEnabled(true);
    }

    private void limparSelecionados() {
        produtoSelecionado = null;
        produtoEsc.setText("");
        tamEsc.setText("");

        spinnerQuantidade.setModel(new SpinnerNumberModel(0, 0, 99, 1));

        if (popupTam != null) {
            popupTam.removeAll();
        }

        btnTam.setEnabled(true);
        btnComprar.setEnabled(true);
    }

    private int obterQuantidade() {
        return (int) spinnerQuantidade.getValue();
    }

    private void comprarProduto() {

        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Deve selecionar um produto.",
                    "Produto obrigatório",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String tamanho = tamEsc.getText();
        int quantidade = obterQuantidade();

        if (tamanho.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Deve selecionar um tamanho.",
                    "Tamanho obrigatório",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (quantidade <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "A quantidade deve ser superior a 0.",
                    "Quantidade inválida",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        FinalizarCompra pagamento =
                new FinalizarCompra("Campeonato Mundial 2026 - Finalizar Compra");

        pagamento.setVisible(true);
    }

    private void selecionarTamanho(String tamanho) {
        tamEsc.setText(tamanho);

        int stock = produtoSelecionado.getStockDoTamanho(tamanho);

        SpinnerNumberModel modelo = new SpinnerNumberModel(
                1,
                1,
                stock,
                1
        );

        spinnerQuantidade.setModel(modelo);
    }
}