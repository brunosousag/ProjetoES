package org.modelo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HistoricoVendas extends BaseFrame {

    private JButton btnClassificacaoGeral;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;

    private JPanel HistoricoVendas;
    private JPanel HistoricoTitulo;

    private JList list2;
    private JList list3;

    private JButton imprimirButton;
    private JPanel menuPrincipal;
    private JLabel lblNomeCampeonato;
    private JTable table1;

    public HistoricoVendas(String title) {
        super(title);

        setContentPane(HistoricoVendas);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnClassificacaoGeral = btnClassificacaoGeral;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        imprimirButton.addActionListener(this::imprimirFatura);

        configurarMenuGestao();
        preencherTabela();


        pack();
        setLocationRelativeTo(null);
    }

    public void preencherTabela() {
        String[] colunas = {
                "Fatura",
                "Nome",
                "Total",
                "Data"
        };

        Object[][] dados = {
                {"1234", "João Silva", "120€", "12/05/2026"},
                {"1235", "Maria Costa", "85€", "13/05/2026"},
                {"1236", "Eduardo Almeida", "900€", "14/05/2026"}
        };

        DefaultTableModel tabela =
                new DefaultTableModel(dados, colunas);

        table1.setModel(tabela);

        //cor letras
        table1.setForeground(Color.BLACK);

        //Centrar texto tabela
        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();

        center.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table1.getColumnCount(); i++) {

            table1.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(center);
        }
    }

    public void imprimirFatura(ActionEvent actionEvent) {
        JOptionPane.showMessageDialog(
                this,
                "A fatura vai ser imprimida dentro de segundos!"
        );
    }
}