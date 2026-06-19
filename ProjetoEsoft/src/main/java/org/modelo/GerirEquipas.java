package org.modelo;

import javax.swing.*;
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
    private transient JButton btnEquipas;
    private transient JButton btnCarrinho;
    private transient JButton btnMerch;
    private transient JButton btnGestao;
    private transient JLabel lblNomeCampeonato;
    private transient JButton btnAdicionarEquipa;
    private transient JButton gerirDeslocaçãoButton;
    private transient JButton gerirAlojamentoButton;
    private transient JComboBox comboBox1;

    private ArrayList<Equipa> equipas = new ArrayList<>();

    private static final String FICHEIRO_EQUIPAS =
            "dados" + File.separator + "equipas.dat";

    public GerirEquipas(String title) {
        super(title);

        setContentPane(gerirEquipas);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        super.btnGestao = btnGestao;
        super.btnEquipas = btnEquipas;
        super.btnMerch = btnMerch;
        super.btnCarrinho = btnCarrinho;

        configurarMenuGestao();

        lerEquipasDoDisco();

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

        txtFldNomeEquipa.setText("");
        txtFldDescricao.setText("");
        txtFldNacionalidade.setText("");

        JOptionPane.showMessageDialog(this, "Equipa adicionada com sucesso!");
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
