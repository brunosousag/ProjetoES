package org.modelo;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.ActionEvent;


public class ImprimirFatura extends BaseFrame {
    private JPanel menuPrincipal;
    private JButton btnClassificacaoGeral;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;
    private JLabel lblNomeCampeonato;
    private JButton btnImprimir;
    private JTable table1;
    private JPanel ImprimirFatura;

    public ImprimirFatura(String title) {
        super(title);

        setContentPane(ImprimirFatura);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnClassificacaoGeral = btnClassificacaoGeral;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        btnImprimir.addActionListener(this::finalizarImprimir);

        configurarMenuGestao();
        preencherTabela();

        pack();
        setLocationRelativeTo(null);
    }

    public void preencherTabela() {
        String[] colunas = {
                "Nº Fatura",
                "Nome",
                "Total",
                "Data"
        };

        Object[][] dados = {

                {"1234", "João Silva", "120€", "12/05/2026"},
                {"1235", "Maria Costa", "85€", "13/05/2026"},
                {"1236", "Empresa X", "900€", "14/05/2026"}

        };

        DefaultTableModel tabela =
                new DefaultTableModel(dados, colunas);

        table1.setModel(tabela);

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

    public void finalizarImprimir(ActionEvent actionEvent) {
        JOptionPane.showMessageDialog(
                this,
                "A fatura vai ser imprimida dentro de segundos!"
        );
    }
}
