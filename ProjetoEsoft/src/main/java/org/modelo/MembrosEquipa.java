package org.modelo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MembrosEquipa extends BaseFrame {
    private transient JPanel membrosEquipa;
    private transient JPanel menuPrincipal;
    private transient JButton btnGestao;
    private transient JButton btnEquipas;
    private transient JButton btnMerch;
    private transient JButton btnCarrinho;
    private transient JLabel lblNomeCampeonato;
    private transient JLabel lblTitulo;
    private JPanel visualizarMembros;

    private final transient Equipa equipa;

    /** Grelha onde os membros são desenhados dinamicamente. */
    private transient JPanel painelTabela;

    private static final Color FUNDO_TABELA = new Color(-2565410, true);
    private static final Color FUNDO_HEADER = new Color(-16035707, true);
    private static final Color TEXTO_HEADER = new Color(-525825, true);
    private static final Color COR_LINHA = new Color(-16448250, true);

    public MembrosEquipa(String title, Equipa equipa) {
        super(title);
        this.equipa = equipa;

        setContentPane(membrosEquipa);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnEquipas = btnEquipas;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();

        lblTitulo.setText("Membros de " + equipa.getNome() + " (" + equipa.getTipo() + ")");

        configurarTabela();
        preencherMembros();

        pack();
        setLocationRelativeTo(null);
    }

    private void configurarTabela() {
        painelTabela = new JPanel(new GridBagLayout());
        painelTabela.setBackground(FUNDO_TABELA);

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(FUNDO_TABELA);
        topo.add(painelTabela, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(topo);
        scroll.setBorder(BorderFactory.createLineBorder(COR_LINHA));
        scroll.getViewport().setBackground(FUNDO_TABELA);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        visualizarMembros.removeAll();
        visualizarMembros.setLayout(new BorderLayout());
        visualizarMembros.add(scroll, BorderLayout.CENTER);
    }

    private void preencherMembros() {
        painelTabela.removeAll();

        List<String> membros = obterMembros();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        gbc.gridy = 0;
        painelTabela.add(criarCelula("Nome", true), gbc);

        if (membros.isEmpty()) {
            gbc.gridy = 1;
            painelTabela.add(criarCelula("(sem membros)", false), gbc);
        } else {
            int linha = 1;
            for (String nome : membros) {
                gbc.gridy = linha++;
                painelTabela.add(criarCelula(nome, false), gbc);
            }
        }

        painelTabela.revalidate();
        painelTabela.repaint();
    }

    /** Membros conforme o tipo: Seleção → jogadores, Arbitragem → árbitros. */
    private List<String> obterMembros() {
        List<String> nomes = new ArrayList<>();
        String tipo = equipa.getTipo();

        if ("Seleção".equals(tipo)) {
            for (Jogador j : RepositorioDados.jogadoresDaEquipa(equipa.getId())) {
                nomes.add(j.getNome());
            }
        } else if ("Arbitragem".equals(tipo)) {
            for (Arbitro a : RepositorioDados.arbitrosDaEquipa(equipa.getId())) {
                nomes.add(a.getNome());
            }
        }
        return nomes;
    }

    private JLabel criarCelula(String texto, boolean cabecalho) {
        JLabel lbl = new JLabel(texto);
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_LINHA),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        if (cabecalho) {
            lbl.setBackground(FUNDO_HEADER);
            lbl.setForeground(TEXTO_HEADER);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            lbl.setBackground(FUNDO_TABELA);
        }
        return lbl;
    }
}
