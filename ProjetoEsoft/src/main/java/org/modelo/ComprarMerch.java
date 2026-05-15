package org.modelo;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ComprarMerch extends BaseFrame {

    private JPanel painelPrincipal;
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
    private JPanel menuPrincipal;
    private JLabel lblNomeCampeonato;
    private JButton btnGestao;
    private JButton btnClassificacaoGeral;
    private JButton btnMerch;
    private JButton btnCarrinho;

    public ComprarMerch(String title) {
        super(title);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(painelPrincipal);

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

        JMenuItem itemTShirt = new JMenuItem("T-Shirt");
        JMenuItem itemCachecol = new JMenuItem("Cachecol");

        JMenuItem xs = new JMenuItem("XS");
        JMenuItem s = new JMenuItem("S");
        JMenuItem m = new JMenuItem("M");
        JMenuItem g = new JMenuItem("G");

        spinnerQuantidade.setModel(new SpinnerNumberModel(1, 1, 99, 1));

        btnComprar.addActionListener(e -> comprarProduto());

        itemTShirt.addActionListener(this::btnTShirtActionPerformed);
        itemCachecol.addActionListener(this::btnCachecolActionPerformed);

        xs.addActionListener(e -> selecionarTamanho("XS"));
        s.addActionListener(e -> selecionarTamanho("S"));
        m.addActionListener(e -> selecionarTamanho("M"));
        g.addActionListener(e -> selecionarTamanho("G"));

        popupItens.add(itemTShirt);
        popupItens.add(itemCachecol);

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
    }

    private int obterQuantidade() {
        return (int) spinnerQuantidade.getValue();
    }

    private void comprarProduto() {

        String produto = produtoEsc.getText();
        String tamanho = tamEsc.getText();
        int quantidade = obterQuantidade();

        FinalizarCompra pagamento =
                new FinalizarCompra("Campeonato Mundial 2026 - Finalizar Compra");

        pagamento.setVisible(true);
    }

    private void selecionarTamanho(String tamanho) {
        tamEsc.setText(tamanho);
    }

    private void btnCachecolActionPerformed(ActionEvent actionEvent) {

        produtoEsc.setText("Cachecol Oficial");
//        lblTxtCompra.setText("Preço: 14.99€");
    }

    private void btnTShirtActionPerformed(ActionEvent actionEvent) {

        produtoEsc.setText("T-Shirt Oficial");
//        lblTxtCompra.setText("Preço: 29.99€");
    }
}