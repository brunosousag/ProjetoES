package org.modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ComprarBilhete extends JFrame {
    private JPanel painelPrincipal;
    private JPanel menuPrincipal;
    private JLabel lblNomeCampeonato;
    private JButton btnGestao;
    private JButton btnClassificacaoGeral;
    private JButton btnMerch;
    private JButton btnCarrinho;
    private JPanel JogoEscolhido;
    private JPanel jogoListado;
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

        quantBilhetes.setModel(new SpinnerNumberModel(1, 1, 99, 1));

        setContentPane(painelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void btnComprarActionPerformed(ActionEvent actionEvent) {
        FinalizarCompra janela = new FinalizarCompra("Campeonato Mundial 2026 - Finalizar compra");
        janela.setVisible(true);
        dispose();
    }

}
