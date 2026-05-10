package org.example;

import javax.swing.*;

public class GerirGrupos extends JFrame {
    private JPanel janelaGrupos;
    private JPanel nomeGrupos;
    private JPanel grupoSelecionado;
    private JPanel classificacao;
    private JPanel detalhes;
    private JPanel equipasGrupo;
    private JPanel jogosGrupo;
    private JLabel lblNomeCampeonato;
    private JLabel lblGrupoSelecionado;
    private JLabel lblPrimSorteio;
    private JLabel lblSegSorteio;
    private JLabel lblTercSorteio;
    private JLabel lblQuarSorteio;
    private JLabel lblNomeEquipas;
    private JLabel lblPrimGrupo;
    private JLabel lblSegGrupo;
    private JLabel lblTercGrupo;
    private JLabel lblQuarGrupo;
    private JLabel lblPrimJogo;
    private JLabel lblSegJogo;
    private JLabel lblNomeProx;
    private JLabel lblProxPrimJogo;
    private JLabel lblProxSegJogo;
    private JLabel lblNomeJogos;
    private JLabel lblNomeClassificacao;
    private JLabel lblPrimLugar;
    private JLabel lblSegLugar;
    private JLabel lblTercLugar;
    private JLabel lblQuarLugar;


    public GerirGrupos(String title) {
        super(title);

        setContentPane(janelaGrupos);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
    }

}
