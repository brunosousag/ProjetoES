package org.modelo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Carrinho extends BaseFrame {

    private JPanel painelPrincipal;
    private JPanel menuPrincipal;
    private JLabel lblNomeCampeonato;
    private JButton btnGestao;
    private JButton btnClassificacaoGeral;
    private JButton btnMerch;
    private JButton btnCarrinho;

    private JPanel cabecalhoCarrinho;
    private JLabel lblTitulo;

    private JScrollPane scrollTabela;
    private JTable tabelaCarrinho;

    private JPanel painelTotal;
    private JLabel lblTotalTexto;
    private JLabel lblTotalValor;

    private JPanel painelBotoes;
    private JButton btnContinuar;
    private JButton btnRemover;
    private JButton btnLimpar;
    private JButton btnFinalizar;

    private static final String[] COLUNAS = {
            "Tipo",
            "Descrição",
            "Detalhe",
            "Quantidade",
            "Preço Unitário",
            "Subtotal"
    };

    private CarrinhoStore.CarrinhoListener storeListener;

    public Carrinho(String title) {
        super(title);

        setContentPane(painelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnClassificacaoGeral = btnClassificacaoGeral;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();
        configurarTabela();
        atualizarVista();

        storeListener = this::atualizarVista;
        CarrinhoStore.getInstance().registarListener(storeListener);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                CarrinhoStore.getInstance().removerListener(storeListener);
            }
        });

        btnContinuar.addActionListener(this::btnContinuarActionPerformed);
        btnRemover.addActionListener(this::btnRemoverActionPerformed);
        btnLimpar.addActionListener(this::btnLimparActionPerformed);
        btnFinalizar.addActionListener(this::btnFinalizarActionPerformed);

        pack();
        setLocationRelativeTo(null);
    }

    private void configurarTabela() {
        DefaultTableModel modelo = new DefaultTableModel(new Object[0][0], COLUNAS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaCarrinho.setModel(modelo);
        tabelaCarrinho.setRowHeight(28);
        tabelaCarrinho.setForeground(Color.BLACK);
        tabelaCarrinho.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centrar = new DefaultTableCellRenderer();
        centrar.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelaCarrinho.getColumnCount(); i++) {
            tabelaCarrinho.getColumnModel().getColumn(i).setCellRenderer(centrar);
        }
    }

    private void atualizarVista() {
        DefaultTableModel modelo = (DefaultTableModel) tabelaCarrinho.getModel();
        modelo.setRowCount(0);

        List<ItemCarrinho> itens = CarrinhoStore.getInstance().getItens();
        for (ItemCarrinho item : itens) {
            modelo.addRow(new Object[]{
                    item.getTipo() == ItemCarrinho.Tipo.BILHETE ? "Bilhete" : "Produto",
                    item.getDescricao(),
                    item.getDetalhe(),
                    item.getQuantidade(),
                    formatarPreco(item.getPrecoUnitario()),
                    formatarPreco(item.getSubtotal())
            });
        }

        lblTotalValor.setText(formatarPreco(CarrinhoStore.getInstance().getTotal()));
    }

    private String formatarPreco(double valor) {
        return String.format("%.2f€", valor).replace('.', ',');
    }

    private void btnContinuarActionPerformed(ActionEvent e) {
        dispose();
    }

    private void btnRemoverActionPerformed(ActionEvent e) {
        int linha = tabelaCarrinho.getSelectedRow();

        if (linha < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um item para remover.",
                    "Nenhum item selecionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        CarrinhoStore.getInstance().removerItem(linha);
    }

    private void btnLimparActionPerformed(ActionEvent e) {
        if (CarrinhoStore.getInstance().getItens().isEmpty()) {
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja limpar todo o carrinho?",
                "Limpar carrinho",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        CarrinhoStore.getInstance().limpar();
    }

    private void btnFinalizarActionPerformed(ActionEvent e) {
        if (!CarrinhoStore.getInstance().temBilhetes()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Adicione pelo menos um jogo ao carrinho para continuar a compra.",
                    "Carrinho sem jogos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        WindowManager.abrirJanela(
                this,
                "comprarBilhete",
                "A janela Comprar Bilhete já está aberta!",
                new ComprarBilhete("Campeonato Mundial 2026 - Comprar bilhete")
        );
    }
}