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

    private JComboBox cmbJogo;
    private JComboBox cmbEquipa;
    private JTextField txtTipoTransporte;
    private JComboBox cmbOrigem;
    private JComboBox cmbDestino;
    private JButton btnGuardar;

    private ArrayList<Equipa> equipas = new ArrayList<>();
    private ArrayList<JogoCalendario> jogos = new ArrayList<>();

    /** As (2) equipas do jogo selecionado, na mesma ordem do cmbEquipa. */
    private final transient ArrayList<Equipa> equipasDoJogo = new ArrayList<>();

    /** Evita reagir a alterações dos combos enquanto estão a ser repreenchidos. */
    private transient boolean carregando;

    private static final String FICHEIRO_EQUIPAS = CaminhosFicheiros.FICHEIRO_EQUIPAS;

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
        jogos = RepositorioDados.carregarJogosCalendario();

        preencherComboJogos();

        cmbJogo.addActionListener(e -> aoMudarJogo());
        cmbEquipa.addActionListener(e -> { if (!carregando) aoMudarEquipa(); });
        btnGuardar.addActionListener(e -> guardarDeslocacao());

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

    /** Quando muda o jogo, recarrega as equipas desse jogo e atualiza o resto. */
    private void aoMudarJogo() {
        carregando = true;
        preencherComboEquipasDoJogo();
        carregando = false;
        aoMudarEquipa();
    }

    /** Quando muda a equipa, atualiza os locais (hotel/estádio) e pré-preenche. */
    private void aoMudarEquipa() {
        atualizarLocais();
        mostrarDeslocacaoSelecionada();
    }

    /** Preenche o cmbEquipa apenas com as 2 equipas do jogo selecionado. */
    private void preencherComboEquipasDoJogo() {
        equipasDoJogo.clear();
        cmbEquipa.removeAllItems();

        JogoCalendario jogo = jogoSelecionado();
        if (jogo == null) return;

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

    /**
     * Atualiza as opções de Origem/Destino: hotel (do alojamento da equipa) e
     * estádio (do jogo). Sugere por defeito hotel → estádio.
     */
    private void atualizarLocais() {
        cmbOrigem.removeAllItems();
        cmbDestino.removeAllItems();

        JogoCalendario jogo = jogoSelecionado();
        Equipa equipa = equipaSelecionada();
        if (jogo == null || equipa == null) return;

        String estadio = jogo.getEstadio();
        String hotel = equipa.getAlojamento() != null
                ? equipa.getAlojamento().getNomeHotel()
                : null;

        if (hotel != null) {
            cmbOrigem.addItem(hotel);
            cmbDestino.addItem(hotel);
        }
        cmbOrigem.addItem(estadio);
        cmbDestino.addItem(estadio);

        // Sugestão por defeito: sair do hotel para o estádio.
        if (hotel != null) {
            cmbOrigem.setSelectedItem(hotel);
            cmbDestino.setSelectedItem(estadio);
        }
    }

    /** Pré-seleciona a deslocação atual da equipa (se existir). */
    private void mostrarDeslocacaoSelecionada() {
        Equipa equipa = equipaSelecionada();
        if (equipa == null) {
            txtTipoTransporte.setText("");
            return;
        }

        Deslocacao d = equipa.getDeslocacao();
        if (d != null) {
            txtTipoTransporte.setText(d.getTipoTransporte());
            selecionarSeExistir(cmbOrigem, d.getOrigem());
            selecionarSeExistir(cmbDestino, d.getDestino());
        } else {
            txtTipoTransporte.setText("");
        }
    }

    private void selecionarSeExistir(JComboBox combo, String valor) {
        if (valor == null) return;
        for (int k = 0; k < combo.getItemCount(); k++) {
            if (valor.equals(combo.getItemAt(k))) {
                combo.setSelectedIndex(k);
                return;
            }
        }
    }

    private void guardarDeslocacao() {
        Equipa equipa = equipaSelecionada();
        if (equipa == null) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo e uma equipa.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (equipa.getAlojamento() == null) {
            JOptionPane.showMessageDialog(this,
                    "Defina primeiro o alojamento desta equipa — a deslocação parte/chega ao hotel.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tipo = txtTipoTransporte.getText().trim();
        Object origem = cmbOrigem.getSelectedItem();
        Object destino = cmbDestino.getSelectedItem();

        if (tipo.isEmpty() || origem == null || destino == null) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (origem.equals(destino)) {
            JOptionPane.showMessageDialog(this,
                    "A origem e o destino têm de ser diferentes (rota hotel ↔ estádio).",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        equipa.setDeslocacao(new Deslocacao(tipo, origem.toString(), destino.toString()));
        guardarEquipasDisco();

        JOptionPane.showMessageDialog(this, "Deslocação atualizada com sucesso!");
    }

    private JogoCalendario jogoSelecionado() {
        int j = cmbJogo.getSelectedIndex();
        return (j >= 0 && j < jogos.size()) ? jogos.get(j) : null;
    }

    private Equipa equipaSelecionada() {
        int i = cmbEquipa.getSelectedIndex();
        return (i >= 0 && i < equipasDoJogo.size()) ? equipasDoJogo.get(i) : null;
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
