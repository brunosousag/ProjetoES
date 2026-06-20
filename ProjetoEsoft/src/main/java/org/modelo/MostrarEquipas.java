package org.modelo;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
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

    // Construídos em código dentro de visualizarEquipas.
    private transient JComboBox cmbEquipa;
    private transient JComboBox cmbInfo;
    private transient JPanel painelInfo;

    private ArrayList<Equipa> equipas = new ArrayList<>();

    // Cores para manter o mesmo aspeto (cabeçalho escuro, fundo claro).
    private static final Color FUNDO_TABELA = new Color(-2565410, true);
    private static final Color FUNDO_HEADER = new Color(-16035707, true);
    private static final Color TEXTO_HEADER = new Color(-525825, true);
    private static final Color COR_LINHA = new Color(-16448250, true);

    private static final String[] INFOS = {"Membros", "Alojamento", "Deslocamento"};

    private static final String FICHEIRO_EQUIPAS = CaminhosFicheiros.FICHEIRO_EQUIPAS;

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
        construirUI();
        atualizarInfo();

        pack();
        setLocationRelativeTo(null);
    }

    /** Monta a barra de seleção (equipa + informação) e a área de visualização. */
    private void construirUI() {
        cmbEquipa = new JComboBox();
        for (Equipa equipa : equipas) {
            cmbEquipa.addItem(equipa.getNome() + " - " + equipa.getTipo());
        }

        cmbInfo = new JComboBox();
        for (String info : INFOS) {
            cmbInfo.addItem(info);
        }

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        barra.setBackground(FUNDO_TABELA);
        barra.add(new JLabel("Equipa:"));
        barra.add(cmbEquipa);
        barra.add(Box.createHorizontalStrut(15));
        barra.add(new JLabel("Ver:"));
        barra.add(cmbInfo);

        painelInfo = new JPanel(new GridBagLayout());
        painelInfo.setBackground(FUNDO_TABELA);

        // Encosta o conteúdo ao topo.
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(FUNDO_TABELA);
        topo.add(painelInfo, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(topo);
        scroll.setBorder(BorderFactory.createLineBorder(COR_LINHA));
        scroll.getViewport().setBackground(FUNDO_TABELA);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        visualizarEquipas.removeAll();
        visualizarEquipas.setLayout(new BorderLayout());
        visualizarEquipas.add(barra, BorderLayout.NORTH);
        visualizarEquipas.add(scroll, BorderLayout.CENTER);

        cmbEquipa.addActionListener(e -> atualizarInfo());
        cmbInfo.addActionListener(e -> atualizarInfo());
    }

    /** Mostra a informação escolhida (membros/alojamento/deslocamento) da equipa. */
    private void atualizarInfo() {
        painelInfo.removeAll();

        Equipa equipa = equipaSelecionada();
        String info = (String) cmbInfo.getSelectedItem();
        if (equipa != null && info != null) {
            switch (info) {
                case "Membros":
                    mostrarMembros(equipa);
                    break;
                case "Alojamento":
                    mostrarAlojamento(equipa);
                    break;
                case "Deslocamento":
                    mostrarDeslocamento(equipa);
                    break;
                default:
                    break;
            }
        }

        painelInfo.revalidate();
        painelInfo.repaint();
    }

    private void mostrarMembros(Equipa equipa) {
        List<String> linhas = new ArrayList<>();
        linhas.add("Membros");
        List<String> membros = obterMembros(equipa);
        if (membros.isEmpty()) {
            linhas.add("(sem membros)");
        } else {
            linhas.addAll(membros);
        }
        desenharLista(linhas);
    }

    private void mostrarAlojamento(Equipa equipa) {
        Alojamento a = equipa.getAlojamento();
        if (a == null) {
            desenharLista(java.util.Arrays.asList("(sem alojamento definido)"));
        } else {
            desenharTabela(
                    new String[]{"Hotel", "Morada"},
                    new String[]{a.getNomeHotel(), a.getMorada()});
        }
    }

    private void mostrarDeslocamento(Equipa equipa) {
        Deslocacao d = equipa.getDeslocacao();
        if (d == null) {
            desenharLista(java.util.Arrays.asList("(sem deslocação definida)"));
        } else {
            desenharTabela(
                    new String[]{"Tipo de Transporte", "Origem", "Destino"},
                    new String[]{d.getTipoTransporte(), d.getOrigem(), d.getDestino()});
        }
    }

    /** Desenha as linhas em coluna; a primeira é o cabeçalho. */
    private void desenharLista(List<String> linhas) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        for (int i = 0; i < linhas.size(); i++) {
            gbc.gridy = i;
            painelInfo.add(criarCelula(linhas.get(i), i == 0), gbc);
        }
    }

    /** Cabeçalho (colunas) numa linha e os valores na linha seguinte. */
    private void desenharTabela(String[] colunas, String[] valores) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        for (int c = 0; c < colunas.length; c++) {
            gbc.gridx = c;
            painelInfo.add(criarCelula(colunas[c], true), gbc);
        }

        gbc.gridy = 1;
        for (int c = 0; c < valores.length; c++) {
            gbc.gridx = c;
            painelInfo.add(criarCelula(valores[c], false), gbc);
        }
    }

    /** Membros conforme o tipo: Seleção → jogadores, Arbitragem → árbitros. */
    private List<String> obterMembros(Equipa equipa) {
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

    private Equipa equipaSelecionada() {
        int i = cmbEquipa.getSelectedIndex();
        return (i >= 0 && i < equipas.size()) ? equipas.get(i) : null;
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
