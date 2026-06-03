package org.modelo;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FinalizarCompra extends JFrame {
    private JPanel layoutborder;
    private JPanel resumo;
    private JPanel DadosCliente;
    private JTextField textField1;
    private JTextField textField2;
    private JPanel titulo;
    private JPanel MetodoPagamento;
    private JRadioButton multibancoRadioButton;
    private JRadioButton dinheiroRadioButton;
    private JPanel finalizarPagamento;
    private JButton voltarButton;
    private JButton finalizarCompraButton;

    public FinalizarCompra(String title) {
        super(title);

        setContentPane(layoutborder);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        configurarMetodoPagamento();

        finalizarCompraButton.addActionListener(this::finalizarCompra);
        voltarButton.addActionListener(this::btnVoltarActionPerformed);

        pack();
        setLocationRelativeTo(null);
    }

    private void configurarMetodoPagamento() {
        ButtonGroup grupoPagamento = new ButtonGroup();

        grupoPagamento.add(multibancoRadioButton);
        grupoPagamento.add(dinheiroRadioButton);
    }

    private void finalizarCompra(ActionEvent actionEvent) {

        if (!multibancoRadioButton.isSelected() && !dinheiroRadioButton.isSelected()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Deve selecionar um método de pagamento.",
                    "Método de pagamento obrigatório",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Pagamento efetuado com sucesso!"
        );

        dispose();
        WindowManager.fecharTodasAsJanelas();
    }

    private void btnVoltarActionPerformed(ActionEvent actionEvent) {
        dispose();
    }
}