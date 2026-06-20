package org.modelo;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlterarAlojamento extends BaseFrame {
    private transient JPanel alterarAlojamento;
    private transient JPanel menuPrincipal;
    private transient JButton btnGestao;
    private transient JButton btnEquipas;
    private transient JButton btnMerch;
    private transient JButton btnCarrinho;
    private transient JLabel lblNomeCampeonato;

    private JComboBox cmbJogo;
    private JComboBox cmbEquipa;
    private JTextField txtNomeHotel;
    private JTextField txtMorada;
    private JTextField txtNumeroQuartos;
    private JButton btnGuardar;

    private ArrayList<Equipa> equipas = new ArrayList<>();
    private ArrayList<JogoCalendario> jogos = new ArrayList<>();

    /** As (2) equipas do jogo selecionado, na mesma ordem do cmbEquipa. */
    private final transient ArrayList<Equipa> equipasDoJogo = new ArrayList<>();

    /** Evita reagir a alterações do cmbEquipa enquanto este é repreenchido. */
    private transient boolean carregando;

    private static final String FICHEIRO_EQUIPAS =
            "dados" + File.separator + "equipas.dat";

    public AlterarAlojamento(String title) {
        super(title);

        setContentPane(alterarAlojamento);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnEquipas = btnEquipas;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();

        lerEquipasDoDisco();
        jogos = RepositorioDados.carregarJogosCalendario();

        preencherComboJogos();

        cmbJogo.addActionListener(e -> aoMudarJogo());
        cmbEquipa.addActionListener(e -> { if (!carregando) mostrarAlojamentoSelecionado(); });
        btnGuardar.addActionListener(e -> guardarAlojamento());

        aoMudarJogo();

        pack();
        setLocationRelativeTo(null);
    }

    private void preencherComboJogos() {
        cmbJogo.removeAllItems();
        for (JogoCalendario jogo : jogos) {
            cmbJogo.addItem(jogo.getDescricaoCompleta());
        }
    }

    /** Quando muda o jogo, recarrega as equipas desse jogo e pré-preenche. */
    private void aoMudarJogo() {
        carregando = true;
        preencherComboEquipasDoJogo();
        carregando = false;
        mostrarAlojamentoSelecionado();
    }

    /** Preenche o cmbEquipa apenas com as 2 equipas do jogo selecionado. */
    private void preencherComboEquipasDoJogo() {
        equipasDoJogo.clear();
        cmbEquipa.removeAllItems();

        int j = cmbJogo.getSelectedIndex();
        if (j < 0 || j >= jogos.size()) return;

        JogoCalendario jogo = jogos.get(j);
        adicionarEquipaDoJogo(jogo.getEquipaA());
        adicionarEquipaDoJogo(jogo.getEquipaB());
    }

    private void adicionarEquipaDoJogo(String nomeEquipa) {
        Equipa equipa = equipaPorNome(nomeEquipa);
        if (equipa != null) {
            equipasDoJogo.add(equipa);
            cmbEquipa.addItem(equipa.getNome() + " - " + equipa.getTipo());
        }
    }

    /** Procura a equipa pelo nome (preferindo a do tipo "Seleção"). */
    private Equipa equipaPorNome(String nomeEquipa) {
        Equipa fallback = null;
        for (Equipa equipa : equipas) {
            if (equipa.getNome().equals(nomeEquipa)) {
                if ("Seleção".equals(equipa.getTipo())) {
                    return equipa;
                }
                if (fallback == null) {
                    fallback = equipa;
                }
            }
        }
        return fallback;
    }

    /** Pré-preenche os campos com o alojamento atual da equipa selecionada. */
    private void mostrarAlojamentoSelecionado() {
        int i = cmbEquipa.getSelectedIndex();
        if (i < 0 || i >= equipasDoJogo.size()) {
            txtNomeHotel.setText("");
            txtMorada.setText("");
            txtNumeroQuartos.setText("");
            return;
        }

        Alojamento a = equipasDoJogo.get(i).getAlojamento();
        if (a != null) {
            txtNomeHotel.setText(a.getNomeHotel());
            txtMorada.setText(a.getMorada());
            txtNumeroQuartos.setText(String.valueOf(a.getNumeroQuartos()));
        } else {
            txtNomeHotel.setText("");
            txtMorada.setText("");
            txtNumeroQuartos.setText("");
        }
    }

    private void guardarAlojamento() {
        int i = cmbEquipa.getSelectedIndex();
        if (i < 0 || i >= equipasDoJogo.size()) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo e uma equipa.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nomeHotel = txtNomeHotel.getText().trim();
        String morada = txtMorada.getText().trim();
        String quartosTexto = txtNumeroQuartos.getText().trim();

        if (nomeHotel.isEmpty() || morada.isEmpty() || quartosTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int numeroQuartos;
        try {
            numeroQuartos = Integer.parseInt(quartosTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O número de quartos tem de ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (numeroQuartos <= 0) {
            JOptionPane.showMessageDialog(this, "O número de quartos tem de ser maior que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        equipasDoJogo.get(i).setAlojamento(new Alojamento(nomeHotel, morada, numeroQuartos));
        guardarEquipasDisco();

        JOptionPane.showMessageDialog(this, "Alojamento atualizado com sucesso!");
    }

    private void guardarEquipasDisco() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHEIRO_EQUIPAS))) {
            oos.writeObject(equipas);
        } catch (IOException ex) {
            Logger.getLogger(AlterarAlojamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    private void lerEquipasDoDisco() {
        File f = new File(FICHEIRO_EQUIPAS);
        if (f.canRead()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                equipas = (ArrayList<Equipa>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(AlterarAlojamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
