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

    public ComprarBilhete(String title) {
        super(title);

        UITheme.applyTheme(painelPrincipal);
        UITheme.styleHeader(menuPrincipal);
        UITheme.styleTitleLabel(lblNomeCampeonato);
        UITheme.setBackground(jogoListado, UITheme.BG_CARD);
        jogoListado.setBorder(UITheme.cardBorder());

        // Stadium sections — availability colors
        styleSection(NorteSuperior,   UITheme.SECTION_AVAILABLE, new Color(39, 174, 96));
        styleSection(CentralSuperior, UITheme.SECTION_AVAILABLE, new Color(39, 174, 96));
        styleSection(SulSuperior,     UITheme.SECTION_AVAILABLE, new Color(39, 174, 96));
        styleSection(Este,            UITheme.SECTION_AVAILABLE, new Color(39, 174, 96));
        styleSection(NorteInferior,   UITheme.SECTION_AVAILABLE, new Color(39, 174, 96));
        styleSection(CentralInferior, UITheme.SECTION_AVAILABLE, new Color(39, 174, 96));
        styleSection(Oeste,           UITheme.SECTION_FEW,       new Color(230, 126, 34));
        styleSection(SulInferior,     UITheme.SECTION_FULL,      new Color(192, 57, 43));

        comprarButton.addActionListener(this::btnComprarActionPerformed);
        quantBilhetes.setModel(new SpinnerNumberModel(1, 1, 99, 1));

        UITheme.stylePrimaryButton(comprarButton);
        UITheme.styleSecondaryButton(adicionarMerchButton);
        fixCampoPanel();

        setContentPane(painelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void styleSection(JPanel section, Color bg, Color borderColor) {
        UITheme.setBackground(section, bg);
        section.setBorder(UITheme.sectionBorder(borderColor));
        for (Component c : section.getComponents()) {
            if (c instanceof JLabel lbl) lbl.setForeground(Color.WHITE);
        }
    }

    /** Finds the unbound CAMPO panel (center of the 3×3 estadio grid) and styles it. */
    private void fixCampoPanel() {
        for (Component c : estadio.getComponents()) {
            if (c instanceof JPanel pnl && pnl != NorteSuperior && pnl != CentralSuperior
                    && pnl != SulSuperior && pnl != Este && pnl != Oeste
                    && pnl != NorteInferior && pnl != CentralInferior && pnl != SulInferior) {
                UITheme.setBackground(pnl, UITheme.CAMPO_BG);
                for (Component child : pnl.getComponents()) {
                    if (child instanceof JLabel lbl) {
                        lbl.setForeground(Color.WHITE);
                        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18f));
                    }
                }
            }
        }
    }

    private void btnComprarActionPerformed(ActionEvent actionEvent) {
        FinalizarCompra janela = new FinalizarCompra("Campeonato Mundial 2026 - Finalizar compra");
        janela.setVisible(true);
        dispose();
    }

}
