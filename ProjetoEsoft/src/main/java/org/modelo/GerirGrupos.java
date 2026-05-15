package org.modelo;

import javax.swing.*;

public class GerirGrupos extends JFrame {
    private JPanel janelaGrupos;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JPanel Grupo;
    private JButton adicionarEquipaButton;
    private JButton modificarGrupoMudaEstadoButton;


    public GerirGrupos(String title) {
        super(title);

        setContentPane(janelaGrupos);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

}
