package org.modelo;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlterarDeslocacao extends BaseFrame {
    private transient JPanel alterarDeslocacao;
    private transient JPanel menuPrincipal;
    private transient JButton btnGestao;
    private transient JButton btnEquipas;
    private transient JButton btnMerch;
    private transient JButton btnCarrinho;
    private transient JLabel lblNomeCampeonato;

    private JComboBox cmbEquipa;
    private JTextField txtTipoTransporte;
    private JTextField txtOrigem;
    private JTextField txtDestino;
    private JButton btnGuardar;

    private ArrayList<Equipa> equipas = new ArrayList<>();

    private static final String FICHEIRO_EQUIPAS =
            "dados" + File.separator + "equipas.dat";

    public AlterarDeslocacao(String title) {
        super(title);

        setContentPane(alterarDeslocacao);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnEquipas = btnEquipas;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();

        lerEquipasDoDisco();
        preencherComboEquipas();

        cmbEquipa.addActionListener(e -> mostrarDeslocacaoSelecionada());
        btnGuardar.addActionListener(e -> guardarDeslocacao());

        mostrarDeslocacaoSelecionada();

        pack();
        setLocationRelativeTo(null);
    }

    private void preencherComboEquipas() {
        cmbEquipa.removeAllItems();
        for (Equipa equipa : equipas) {
            cmbEquipa.addItem(equipa.getNome());
        }
    }

    /** Pré-preenche os campos com a deslocação atual da equipa selecionada. */
    private void mostrarDeslocacaoSelecionada() {
        int i = cmbEquipa.getSelectedIndex();
        if (i < 0 || i >= equipas.size()) return;

        Deslocacao d = equipas.get(i).getDeslocacao();
        if (d != null) {
            txtTipoTransporte.setText(d.getTipoTransporte());
            txtOrigem.setText(d.getOrigem());
            txtDestino.setText(d.getDestino());
        } else {
            txtTipoTransporte.setText("");
            txtOrigem.setText("");
            txtDestino.setText("");
        }
    }

    private void guardarDeslocacao() {
        int i = cmbEquipa.getSelectedIndex();
        if (i < 0 || i >= equipas.size()) {
            JOptionPane.showMessageDialog(this, "Selecione uma equipa.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tipo = txtTipoTransporte.getText().trim();
        String origem = txtOrigem.getText().trim();
        String destino = txtDestino.getText().trim();

        if (tipo.isEmpty() || origem.isEmpty() || destino.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        equipas.get(i).setDeslocacao(new Deslocacao(tipo, origem, destino));
        guardarEquipasDisco();

        JOptionPane.showMessageDialog(this, "Deslocação atualizada com sucesso!");
    }

    private void guardarEquipasDisco() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHEIRO_EQUIPAS))) {
            oos.writeObject(equipas);
        } catch (IOException ex) {
            Logger.getLogger(AlterarDeslocacao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    private void lerEquipasDoDisco() {
        File f = new File(FICHEIRO_EQUIPAS);
        if (f.canRead()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                equipas = (ArrayList<Equipa>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(AlterarDeslocacao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
