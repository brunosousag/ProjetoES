package org.example;

import javax.swing.*;

public class janelaPrincipal extends JFrame {

    // ── Campos obrigatórios — nomes têm de coincidir exactamente com os binding do .form ──
    private JPanel    janelaPrincipal; // painel raiz (binding da grelha raiz do .form)
    private JPanel    menuPrincipal;
    private JButton   button2;  // CLASSIFICAÇÕES
    private JButton   button3;  // CARRINHO
    private JButton   button6;  // MERCH
    private JButton GESTAOButton;
    private JPanel jogoListado;
    private JButton button5;
    private JCheckBox checkBox1;
    private JButton button4;
    private JPanel listaGrupo;

    public janelaPrincipal() {
        setContentPane(janelaPrincipal);
        setTitle("Mundial 2026");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        configurarMenuGestao();
    }

    private void configurarMenuGestao() {
        JPopupMenu popup = new JPopupMenu();

        JMenuItem itemEquipas = new JMenuItem("Criar Equipas");
        JMenuItem itemGrupos  = new JMenuItem("Criar Grupos");
        JMenuItem itemFases   = new JMenuItem("Criar Fases do Torneio");

        itemEquipas.addActionListener(e -> System.out.println("Abrir gestão de Equipas"));
        itemGrupos.addActionListener(e  -> System.out.println("Abrir gestão de Grupos"));
        itemFases.addActionListener(e   -> System.out.println("Abrir gestão de Fases"));

        popup.add(itemEquipas);
        popup.add(itemGrupos);
        popup.add(itemFases);

        GESTAOButton.addActionListener(e ->
            popup.show(GESTAOButton, 0, GESTAOButton.getHeight())
        );
    }
}