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

        finalizarCompraButton.addActionListener(this::finalizarCompra);
        voltarButton.addActionListener(this::btnVoltarActionPerformed);

        setContentPane(layoutborder);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void finalizarCompra(ActionEvent actionEvent) {

        JOptionPane.showMessageDialog(this,
                "Pagamento efetuado com sucesso!"
        );

        FinalizarCompra pagamento = new FinalizarCompra("Campeonato Mundial 2026 - Finalizar Compra");
        dispose();
    }

    private void btnVoltarActionPerformed(ActionEvent actionEvent) {
        dispose();
    }

}
