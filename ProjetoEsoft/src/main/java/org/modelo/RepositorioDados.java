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
}