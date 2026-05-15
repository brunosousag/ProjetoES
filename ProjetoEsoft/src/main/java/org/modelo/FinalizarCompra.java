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
    private JRadioButton oRadioButton;
    private JRadioButton metodoPag;
    private JPanel finalizarPagamento;
    private JButton voltarButton;
    private JButton finalizarCompraButton;

    public FinalizarCompra(String title) {
        super(title);

        setContentPane(layoutborder);

        UITheme.applyTheme(layoutborder);

        // Order summary card
        UITheme.setBackground(resumo, UITheme.BG_CARD);
        resumo.setBorder(UITheme.cardBorder());

        // Customer data section
        UITheme.setBackground(DadosCliente, UITheme.BG_SECONDARY);

        // Payment method section
        UITheme.setBackground(MetodoPagamento, UITheme.BG_SECONDARY);

        UITheme.stylePrimaryButton(finalizarCompraButton);
        UITheme.styleSecondaryButton(voltarButton);

        finalizarCompraButton.addActionListener(this::finalizarCompra);

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
}
