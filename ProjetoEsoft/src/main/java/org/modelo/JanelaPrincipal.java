package org.modelo;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class JanelaPrincipal extends BaseFrame {

    private JPanel janelaPrincipal;
    private JPanel menuPrincipal;
    private JButton btnClassificacaoGeral;
    private JButton btnCarrinho;
    private JButton btnMerch;
    private JButton btnGestao;
    private JPanel jogoListado;
    private JButton btnComprar;
    private JCheckBox adicionarAoCarrinhoCheckBox;
    private JPanel listaGrupo;
    private JPanel totalMerch;


    public JanelaPrincipal(String title) {
        super(title);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(janelaPrincipal);

        super.btnGestao = btnGestao;
        super.btnClassificacaoGeral = btnClassificacaoGeral;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();

        btnComprar.addActionListener(this::btnComprarActionPerformed);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnComprarActionPerformed(ActionEvent actionEvent) {

        WindowManager.abrirJanela(
                this,
                "comprarBilhete",
                "A janela Comprar Bilhete já está aberta!",
                new ComprarBilhete("Campeonato Mundial 2026 - Comprar bilhete")
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JanelaPrincipal("Campeonato Mundial 2026").setVisible(true);
        });
    }
}
