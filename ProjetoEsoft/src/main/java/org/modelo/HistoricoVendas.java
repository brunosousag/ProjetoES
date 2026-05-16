package org.modelo;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class HistoricoVendas extends BaseFrame {

    private JButton btnClassificacaoGeral;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;

    private JPanel HistoricoVendas;
    private JPanel HistoricoTitulo;
    private JPanel TituloVendas;
    private JPanel ListaVendas;

    private JList list1;
    private JList list2;
    private JList list3;

    private JButton imprimirButton;
    private JPanel menuPrincipal;
    private JLabel lblNomeCampeonato;

    public HistoricoVendas(String title) {
        super(title);

        setContentPane(HistoricoVendas);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnClassificacaoGeral = btnClassificacaoGeral;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();
        preencherListas();
        paginaImprimir();

        pack();
        setLocationRelativeTo(null);
    }

    public void paginaImprimir() {
        imprimirButton.addActionListener(e -> {

            ImprimirFatura janela =
                    new ImprimirFatura("Campeonato Mundial 2026 - Imprimir Fatura");

            janela.setVisible(true);
        });
    }

    public void preencherListas(){
        //Centrar texto das listas
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        list1.setCellRenderer(renderer);
        list2.setCellRenderer(renderer);
        list3.setCellRenderer(renderer);
    }

    //Descomentar para testar página
    /*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HistoricoVendas("Campeonato Mundial 2026 - Histórico de Vendas").setVisible(true);
        });
    }
     */
}