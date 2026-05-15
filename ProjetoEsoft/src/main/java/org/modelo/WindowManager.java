package org.modelo;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class WindowManager {

    private static final Map<String, JFrame> janelasAbertas = new HashMap<>();

    public static void abrirJanela(
            JFrame janelaAtual,
            String chave,
            String mensagem,
            JFrame novaJanela
    ) {
        JFrame janelaExistente = janelasAbertas.get(chave);

        if (janelaExistente != null && janelaExistente.isVisible()) {
            JOptionPane.showMessageDialog(janelaAtual, mensagem);
            janelaExistente.toFront();
            janelaExistente.requestFocus();
            return;
        }

        janelasAbertas.put(chave, novaJanela);

        novaJanela.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                janelasAbertas.remove(chave);
            }
        });

        novaJanela.setVisible(true);
    }

    public static void fecharTodasAsJanelas() {

        for (JFrame frame : janelasAbertas.values()) {
            if (frame != null && frame.isVisible()) {
                frame.dispose();
            }
        }

        janelasAbertas.clear();
    }
}