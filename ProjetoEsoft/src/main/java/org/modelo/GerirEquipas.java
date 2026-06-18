package org.modelo;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GerirEquipas extends BaseFrame {
    private transient JPanel gerirEquipas;
    private JLabel lblDadosEquipa;
    private JTextField txtFldNomeEquipa;
    private JTextField txtFldDescricao;
    private JTextField txtFldNacionalidade;
    private transient JLabel lblNacionalidade;
    private transient JLabel lblDescricao;
    private transient JLabel lblNomeEquipa;
    private transient JPanel menuPrincipal;
    private transient JButton btnClassificacaoGeral;
    private transient JButton btnCarrinho;
    private transient JButton btnMerch;
    private transient JButton btnGestao;
    private transient JLabel lblNomeCampeonato;
    private transient JButton btnAdicionarEquipa;
    private transient JCheckBox checkBox1;
    private transient JButton gerirDeslocaçãoButton;
    private transient JButton gerirAlojamentoButton;
    private transient JComboBox comboBox1;
    private JPanel visualizarEquipas;

    private ArrayList<Equipa> equipas = new ArrayList<>();

    /** Grelha onde as equipas são desenhadas dinamicamente (dentro de visualizarEquipas). */
    private transient JPanel painelTabela;

    // Cores retiradas do mockup do formulário (GerirEquipas.form), para manter o mesmo aspeto.
    private static final Color FUNDO_TABELA = new Color(-2565410, true);
    private static final Color FUNDO_HEADER = new Color(-16035707, true);
    private static final Color TEXTO_HEADER = new Color(-525825, true);
    private static final Color COR_LINHA = new Color(-16448250, true);

    private static final String[] COLUNAS = {"Nome", "Descrição", "Nacionalidade"};

    private static final String FICHEIRO_EQUIPAS =
            "dados" + File.separator + "equipas.dat";

    public GerirEquipas(String title) {
        super(title);

        setContentPane(gerirEquipas);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnClassificacaoGeral = btnClassificacaoGeral;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();

        lerEquipasDoDisco();

        configurarTabelaEquipas();
        mostrarEquipas();

        btnAdicionarEquipa.addActionListener(e -> adicionarEquipa());

        pack();
        setLocationRelativeTo(null);
    }

    private void adicionarEquipa() {
        String nome = txtFldNomeEquipa.getText().trim();
        String descricao = txtFldDescricao.getText().trim();
        String nacionalidade = txtFldNacionalidade.getText().trim();

        if (nome.isEmpty() || descricao.isEmpty() || nacionalidade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        equipas.add(new Equipa(nome, descricao, nacionalidade));
        guardarEquipasDisco();
        mostrarEquipas();

        txtFldNomeEquipa.setText("");
        txtFldDescricao.setText("");
        txtFldNacionalidade.setText("");

        JOptionPane.showMessageDialog(this, "Equipa adicionada com sucesso!");
    }

    /**
     * Substitui a tabela estática do formulário por uma grelha dinâmica, mantendo
     * o mesmo aspeto (cabeçalho escuro, células com bordas, fundo claro).
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
    private void mostrarEquipas() {
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
                    equipa.getDescricao(),
                    equipa.getNacionalidade()
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

    private void guardarEquipasDisco() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHEIRO_EQUIPAS))) {
            oos.writeObject(equipas);
        } catch (IOException ex) {
            Logger.getLogger(GerirEquipas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    private void lerEquipasDoDisco() {
        File f = new File(FICHEIRO_EQUIPAS);
        if (f.canRead()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                equipas = (ArrayList<Equipa>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(GerirEquipas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

