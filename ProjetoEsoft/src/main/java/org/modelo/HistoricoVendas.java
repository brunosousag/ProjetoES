package org.modelo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class HistoricoVendas extends BaseFrame {

    private JButton btnEquipas;
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

    private ArrayList<Venda> vendas;

    public HistoricoVendas(String title) {
        super(title);

        setContentPane(HistoricoVendas);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnEquipas = btnEquipas;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        imprimirButton.addActionListener(this::imprimirFatura);

        configurarMenuGestao();
        preencherTabela();

        pack();
        setLocationRelativeTo(null);
    }

    public void preencherTabela() {
        vendas = RepositorioDados.carregarVendas();

        String[] colunas = {
                "Fatura",
                "Nome",
                "NIF",
                "Total",
                "Data",
                "Pagamento",
                "Selecionar"
        };

        Object[][] dados = new Object[vendas.size()][colunas.length];
        for (int i = 0; i < vendas.size(); i++) {
            Venda v = vendas.get(i);
            dados[i][0] = v.getNumFatura();
            dados[i][1] = v.getNomeCliente();
            dados[i][2] = v.getNif();
            dados[i][3] = v.getTotalFormatado();
            dados[i][4] = v.getDataFormatada();
            dados[i][5] = v.getMetodoPagamento().name();
            dados[i][6] = Boolean.FALSE;
        }

        DefaultTableModel tabela = new DefaultTableModel(dados, colunas) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == colunas.length - 1) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == colunas.length - 1;
            }
        };

        table1.setModel(tabela);
        table1.setForeground(Color.BLACK);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table1.getColumnCount() - 1; i++) {
            table1.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }

    public void imprimirFatura(ActionEvent actionEvent) {
        StringBuilder selecionadas = new StringBuilder();
        int contador = 0;
        for (int i = 0; i < table1.getRowCount(); i++) {
            Boolean checked = (Boolean) table1.getValueAt(i, table1.getColumnCount() - 1);
            if (Boolean.TRUE.equals(checked) && i < vendas.size()) {
                if (selecionadas.length() > 0) selecionadas.append(", ");
                selecionadas.append(vendas.get(i).getNumFatura());
                contador++;
            }
        }

        if (contador == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione pelo menos uma fatura para imprimir.",
                    "Nenhuma fatura selecionada",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                contador + " fatura(s) (" + selecionadas + ") vão ser impressas dentro de segundos!"
        );
    }
}
