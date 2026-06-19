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

    private JComboBox cmbEquipa;
    private JTextField txtNomeHotel;
    private JTextField txtMorada;
    private JTextField txtNumeroQuartos;
    private JButton btnGuardar;

    private ArrayList<Equipa> equipas = new ArrayList<>();

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
        preencherComboEquipas();

        cmbEquipa.addActionListener(e -> mostrarAlojamentoSelecionado());
        btnGuardar.addActionListener(e -> guardarAlojamento());

        mostrarAlojamentoSelecionado();

        pack();
        setLocationRelativeTo(null);
    }

    private void preencherComboEquipas() {
        cmbEquipa.removeAllItems();
        for (Equipa equipa : equipas) {
            cmbEquipa.addItem(equipa.getNome());
        }
    }

    /** Pré-preenche os campos com o alojamento atual da equipa selecionada. */
    private void mostrarAlojamentoSelecionado() {
        int i = cmbEquipa.getSelectedIndex();
        if (i < 0 || i >= equipas.size()) return;

        Alojamento a = equipas.get(i).getAlojamento();
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
        if (i < 0 || i >= equipas.size()) {
            JOptionPane.showMessageDialog(this, "Selecione uma equipa.", "Erro", JOptionPane.ERROR_MESSAGE);
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

        equipas.get(i).setAlojamento(new Alojamento(nomeHotel, morada, numeroQuartos));
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
