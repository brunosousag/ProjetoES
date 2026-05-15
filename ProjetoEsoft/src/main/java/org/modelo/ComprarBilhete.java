package org.modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ComprarBilhete extends BaseFrame {
    private JPanel painelPrincipal;
    private JPanel Bancadas;
    private JPanel estadio;
    private JPanel NorteSuperior;
    private JPanel CentralSuperior;
    private JPanel SulSuperior;
    private JPanel Oeste;
    private JPanel Este;
    private JPanel SulInferior;
    private JPanel NorteInferior;
    private JPanel CentralInferior;
    private JButton comprarButton;
    private JButton adicionarMerchButton;
    private JSpinner quantBilhetes;
    private JRadioButton selecionarRadioButton;
    private JRadioButton selecionarRadioButton7;
    private JRadioButton selecionarRadioButton6;
    private JRadioButton selecionarRadioButton2;
    private JRadioButton selecionarRadioButton3;
    private JRadioButton selecionarRadioButton4;
    private JRadioButton selecionarRadioButton5;
    private JRadioButton selecionarRadioButton1;
    private JPanel listaGrupo;
    private JCheckBox adicionarAoCarrinhoCheckBox;
    private JPanel menuPrincipal;
    private JButton btnClassificacaoGeral;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;

    private FinalizarCompra finalizarCompraAberta; //pra tratar as varias abas abertas

    public ComprarBilhete(String title) {
        super(title);

        NorteSuperior.setBorder(BorderFactory.createLineBorder(Color.orange,3));
        SulSuperior.setBorder(BorderFactory.createLineBorder(Color.green,3));
        Oeste.setBorder(BorderFactory.createLineBorder(Color.red,3));
        Este.setBorder(BorderFactory.createLineBorder(Color.green,3));
        SulInferior.setBorder(BorderFactory.createLineBorder(Color.red,3));
        NorteInferior.setBorder(BorderFactory.createLineBorder(Color.green,3));
        CentralInferior.setBorder(BorderFactory.createLineBorder(Color.green,3));
        CentralSuperior.setBorder(BorderFactory.createLineBorder(Color.green,3));

        comprarButton.addActionListener(this::btnComprarActionPerformed);

        adicionarMerchButton.addActionListener(e ->
                WindowManager.abrirJanela(
                        this,
                        "comprarMerch",
                        "A janela Comprar Merch já está aberta!",
                        new ComprarMerch("Campeonato Mundial 2026 - Comprar Merch")
                )
        );

        quantBilhetes.setModel(new SpinnerNumberModel(1, 1, 99, 1));

        setContentPane(painelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnClassificacaoGeral = btnClassificacaoGeral;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();

        pack();
        setLocationRelativeTo(null);
    }

    private void btnComprarActionPerformed(ActionEvent actionEvent) {

        if (finalizarCompraAberta != null && finalizarCompraAberta.isVisible()) {
            JOptionPane.showMessageDialog(
                    this,
                    "A janela de finalização da compra já está aberta!"
            );

            finalizarCompraAberta.toFront();
            finalizarCompraAberta.requestFocus();
            return;
        }

        finalizarCompraAberta = new FinalizarCompra("Campeonato Mundial 2026 - Finalizar compra");

        finalizarCompraAberta.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                finalizarCompraAberta = null;
            }
        });

        finalizarCompraAberta.setVisible(true);
    }

}
