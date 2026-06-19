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

    public static ArrayList<JogoCalendario> carregarJogosCalendario() {
        return FicheiroBinario.carregarLista(
                CaminhosFicheiros.FICHEIRO_JOGOS_CALENDARIO
        );
    }

    public static void guardarJogosCalendario(ArrayList<JogoCalendario> jogos) {
        FicheiroBinario.guardarLista(
                CaminhosFicheiros.FICHEIRO_JOGOS_CALENDARIO,
                jogos
        );
    }

    public static ArrayList<Grupo> carregarGrupos() {
        return FicheiroBinario.carregarLista(
                CaminhosFicheiros.FICHEIRO_GRUPOS
        );
    }

    public static void guardarGrupos(ArrayList<Grupo> grupos) {
        FicheiroBinario.guardarLista(
                CaminhosFicheiros.FICHEIRO_GRUPOS,
                grupos
        );
    }

    public static ArrayList<Estadio> carregarEstadios() {
        return FicheiroBinario.carregarLista(
                CaminhosFicheiros.FICHEIRO_ESTADIOS
        );
    }

    public static void guardarEstadios(ArrayList<Estadio> estadios) {
        FicheiroBinario.guardarLista(
                CaminhosFicheiros.FICHEIRO_ESTADIOS,
                estadios
        );
    }

    public static ArrayList<Bancada> carregarBancadas() {
        return FicheiroBinario.carregarLista(
                CaminhosFicheiros.FICHEIRO_BANCADAS
        );
    }

    public static void guardarBancadas(ArrayList<Bancada> bancadas) {
        FicheiroBinario.guardarLista(
                CaminhosFicheiros.FICHEIRO_BANCADAS,
                bancadas
        );
    }

    public static ArrayList<Venda> carregarVendas() {
        ArrayList<Venda> vendas = FicheiroBinario.carregarLista(
                CaminhosFicheiros.FICHEIRO_VENDAS
        );
        return vendas == null ? new ArrayList<>() : vendas;
    }

    public static void guardarVendas(ArrayList<Venda> vendas) {
        FicheiroBinario.guardarLista(
                CaminhosFicheiros.FICHEIRO_VENDAS,
                vendas
        );
    }

    public static void adicionarVenda(Venda venda) {
        ArrayList<Venda> vendas = carregarVendas();
        vendas.add(venda);
        guardarVendas(vendas);
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