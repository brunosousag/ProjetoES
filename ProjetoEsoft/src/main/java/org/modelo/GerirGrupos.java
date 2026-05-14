package org.modelo;

import javax.swing.*;

public class GerirGrupos extends JFrame {
    private JPanel janelaGrupos;
    private JPanel nomeGrupos;
    private JPanel areaSelecionado;
    private JPanel classificacao;
    private JPanel detalhes;
    private JPanel equipasGrupo;
    private JPanel jogosGrupo;
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
    private JPanel menuPrincipal;
    private JLabel lblNomeCampeonato;
    private JButton btnGestao;
    private JButton btnClassificacaoGeral;
    private JButton btnMerch;
    private JButton btnCarrinho;
    private JPanel grupoSelecionado;
    private JLabel txtGrupoSelecionado;


    public GerirGrupos(String title) {
        super(title);

        setContentPane(janelaGrupos);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

}
