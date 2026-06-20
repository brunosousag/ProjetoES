package org.modelo;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MostrarEquipas extends BaseFrame {
    private transient JPanel mostrarEquipas;
    private transient JPanel menuPrincipal;
    private transient JButton btnEquipas;
    private transient JButton btnCarrinho;
    private transient JButton btnMerch;
    private transient JButton btnGestao;
    private transient JLabel lblNomeCampeonato;
    private JPanel visualizarEquipas;

    private ArrayList<Equipa> equipas = new ArrayList<>();

    /** Grelha onde as equipas são desenhadas dinamicamente (dentro de visualizarEquipas). */
    private transient JPanel painelTabela;

    // Cores para manter o mesmo aspeto (cabeçalho escuro, células com bordas, fundo claro).
    private static final Color FUNDO_TABELA = new Color(-2565410, true);
    private static final Color FUNDO_HEADER = new Color(-16035707, true);
    private static final Color TEXTO_HEADER = new Color(-525825, true);
    private static final Color COR_LINHA = new Color(-16448250, true);

    private static final String[] COLUNAS = {"Nome", "Tipo"};

    private static final String FICHEIRO_EQUIPAS =
            "dados" + File.separator + "equipas.dat";

    public MostrarEquipas(String title) {
        super(title);

        setContentPane(mostrarEquipas);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnEquipas = btnEquipas;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();

        lerEquipasDoDisco();

        configurarTabelaEquipas();
        preencherTabela();

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Constrói uma grelha dinâmica dentro de visualizarEquipas, mantendo o aspeto
     * de tabela (cabeçalho escuro, células com bordas, fundo claro).
     */
    private void configurarTabelaEquipas() {
        painelTabela = new JPanel(new GridBagLayout());
        painelTabela.setBackground(FUNDO_TABELA);

        // Encosta a grelha ao topo para as linhas manterem a altura natural.
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(FUNDO_TABELA);
        topo.add(painelTabela, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(topo);
        scroll.setBorder(BorderFactory.createLineBorder(COR_LINHA));
        scroll.getViewport().setBackground(FUNDO_TABELA);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        visualizarEquipas.removeAll();
        visualizarEquipas.setLayout(new BorderLayout());
        visualizarEquipas.add(scroll, BorderLayout.CENTER);
    }

    /** Redesenha a grelha com todas as equipas atualmente em memória. */
    private void preencherTabela() {
        painelTabela.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        for (int c = 0; c < COLUNAS.length; c++) {
            gbc.gridx = c;
            painelTabela.add(criarCelula(COLUNAS[c], true), gbc);
        }

        int linha = 1;
        for (Equipa equipa : equipas) {
            gbc.gridy = linha++;
            String[] valores = {
                    equipa.getNome(),
                    equipa.getTipo()
            };
            for (int c = 0; c < valores.length; c++) {
                gbc.gridx = c;
                painelTabela.add(criarCelula(valores[c], false), gbc);
            }
        }

        painelTabela.revalidate();
        painelTabela.repaint();
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

    @SuppressWarnings("unchecked")
    private void lerEquipasDoDisco() {
        File f = new File(FICHEIRO_EQUIPAS);
        if (f.canRead()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                equipas = (ArrayList<Equipa>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(MostrarEquipas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
