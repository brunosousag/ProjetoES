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

    public static ArrayList<Arbitro> carregarArbitros() {
        return FicheiroBinario.carregarLista(
                CaminhosFicheiros.FICHEIRO_ARBITROS
        );
    }

    public static void guardarArbitros(ArrayList<Arbitro> arbitros) {
        FicheiroBinario.guardarLista(
                CaminhosFicheiros.FICHEIRO_ARBITROS,
                arbitros
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

    /** Devolve os jogadores que pertencem à equipa com o id dado. */
    public static ArrayList<Jogador> jogadoresDaEquipa(int idEquipa) {
        ArrayList<Jogador> resultado = new ArrayList<>();
        for (Jogador jogador : carregarJogadores()) {
            if (jogador.getEquipaId() == idEquipa) {
                resultado.add(jogador);
            }
        }
        return resultado;
    }

    /** Conveniência: resolve o nome para id e devolve os jogadores dessa equipa. */
    public static ArrayList<Jogador> jogadoresDaEquipa(String nomeEquipa) {
        return jogadoresDaEquipa(idEquipaPorNome(nomeEquipa));
    }

    /** Devolve os árbitros que pertencem à equipa com o id dado. */
    public static ArrayList<Arbitro> arbitrosDaEquipa(int idEquipa) {
        ArrayList<Arbitro> resultado = new ArrayList<>();
        for (Arbitro arbitro : carregarArbitros()) {
            if (arbitro.getEquipaId() == idEquipa) {
                resultado.add(arbitro);
            }
        }
        return resultado;
    }

    /** Devolve o id da primeira equipa com o nome dado, ou -1 se não existir. */
    public static int idEquipaPorNome(String nomeEquipa) {
        for (Equipa equipa : carregarEquipas()) {
            if (equipa.getNome().equals(nomeEquipa)) {
                return equipa.getId();
            }
        }
        return -1;
    }

    /** Devolve o nome da equipa com o id dado, ou "" se não existir. */
    public static String nomeEquipaPorId(int idEquipa) {
        for (Equipa equipa : carregarEquipas()) {
            if (equipa.getId() == idEquipa) {
                return equipa.getNome();
            }
        }
        return "";
    }
}