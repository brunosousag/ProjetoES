package org.modelo;

import java.util.ArrayList;

public class RepositorioDados {

    public static ArrayList<Equipa> carregarEquipas() {
        return FicheiroBinario.carregarLista(
                CaminhosFicheiros.FICHEIRO_EQUIPAS
        );
    }

    public static void guardarEquipas(ArrayList<Equipa> equipas) {
        FicheiroBinario.guardarLista(
                CaminhosFicheiros.FICHEIRO_EQUIPAS,
                equipas
        );
    }

    public static ArrayList<Produto> carregarProdutos() {
        return FicheiroBinario.carregarLista(
                CaminhosFicheiros.FICHEIRO_PRODUTOS
        );
    }

    public static void guardarProdutos(ArrayList<Produto> produtos) {
        FicheiroBinario.guardarLista(
                CaminhosFicheiros.FICHEIRO_PRODUTOS,
                produtos
        );
    }

    public static ArrayList<Jogador> carregarJogadores() {
        return FicheiroBinario.carregarLista(
                CaminhosFicheiros.FICHEIRO_JOGADORES
        );
    }

    public static void guardarJogadores(ArrayList<Jogador> jogadores) {
        FicheiroBinario.guardarLista(
                CaminhosFicheiros.FICHEIRO_JOGADORES,
                jogadores
        );
    }

    public static ArrayList<Jogo> carregarJogos() {
        return FicheiroBinario.carregarLista(
                CaminhosFicheiros.FICHEIRO_JOGOS
        );
    }

    public static void guardarJogos(ArrayList<Jogo> jogos) {
        FicheiroBinario.guardarLista(
                CaminhosFicheiros.FICHEIRO_JOGOS,
                jogos
        );
    }

    /** Devolve os jogadores cuja equipa corresponde ao nome dado. */
    public static ArrayList<Jogador> jogadoresDaEquipa(String nomeEquipa) {
        ArrayList<Jogador> resultado = new ArrayList<>();
        for (Jogador jogador : carregarJogadores()) {
            if (jogador.getEquipa().equals(nomeEquipa)) {
                resultado.add(jogador);
            }
        }
        return resultado;
    }
}