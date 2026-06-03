package org.modelo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class InicializadorDados {

    public static void inicializar() {
        criarEquipasSeNaoExistirem();
        criarProdutosSeNaoExistirem();
        criarJogadoresSeNaoExistirem();
    }

    private static void criarEquipasSeNaoExistirem() {
        File ficheiro = new File(CaminhosFicheiros.FICHEIRO_EQUIPAS);

        if (ficheiro.exists()) {
            return;
        }

        ArrayList<Equipa> equipas = new ArrayList<>();

        equipas.add(new Equipa("Portugal", "Seleção portuguesa", "Portuguesa"));
        equipas.add(new Equipa("Brasil", "Seleção brasileira", "Brasileira"));
        equipas.add(new Equipa("Argentina", "Seleção argentina", "Argentina"));
        equipas.add(new Equipa("França", "Seleção francesa", "Francesa"));
        equipas.add(new Equipa("Espanha", "Seleção espanhola", "Espanhola"));
        equipas.add(new Equipa("Japão", "Seleção japonesa", "Japonesa"));

        RepositorioDados.guardarEquipas(equipas);
    }

    private static void criarProdutosSeNaoExistirem() {
        File ficheiro = new File(CaminhosFicheiros.FICHEIRO_PRODUTOS);

        if (ficheiro.exists()) {
            return;
        }

        ArrayList<Produto> produtos = new ArrayList<>();

        HashMap<String, Integer> stockTShirt = new HashMap<>();
        stockTShirt.put("XS", 0);
        stockTShirt.put("S", 5);
        stockTShirt.put("M", 3);
        stockTShirt.put("L", 0);

        HashMap<String, Integer> stockCachecol = new HashMap<>();
        stockCachecol.put("Único", 10);

        HashMap<String, Integer> stockSweat = new HashMap<>();
        stockSweat.put("XS", 2);
        stockSweat.put("S", 4);
        stockSweat.put("M", 6);
        stockSweat.put("L", 3);

        HashMap<String, Integer> stockDecoracao = new HashMap<>();
        stockDecoracao.put("Único", 8);

        produtos.add(new Produto("T-Shirt Oficial", 24.99, stockTShirt));
        produtos.add(new Produto("Cachecol Oficial", 14.99, stockCachecol));
        produtos.add(new Produto("Sweat Oficial", 39.99, stockSweat));
        produtos.add(new Produto("Decoração Oficial", 19.99, stockDecoracao));

        RepositorioDados.guardarProdutos(produtos);
    }

    private static void criarJogadoresSeNaoExistirem() {
        File ficheiro = new File(CaminhosFicheiros.FICHEIRO_JOGADORES);

        if (ficheiro.exists()) {
            return;
        }

        ArrayList<Jogador> jogadores = new ArrayList<>();

        jogadores.add(new Jogador("Kylian Mbappé", "França", 11));
        jogadores.add(new Jogador("Lionel Messi", "Argentina", 10));
        jogadores.add(new Jogador("Cristiano Ronaldo", "Portugal", 9));
        jogadores.add(new Jogador("Neymar", "Brasil", 8));
        jogadores.add(new Jogador("Antoine Griezmann", "França", 7));
        jogadores.add(new Jogador("Rodrygo", "Brasil", 7));
        jogadores.add(new Jogador("Vinícius Júnior", "Brasil", 6));
        jogadores.add(new Jogador("Álvaro Morata", "Espanha", 6));
        jogadores.add(new Jogador("Bruno Fernandes", "Portugal", 5));
        jogadores.add(new Jogador("Lamine Yamal", "Espanha", 5));
        jogadores.add(new Jogador("Julián Álvarez", "Argentina", 4));
        jogadores.add(new Jogador("Takefusa Kubo", "Japão", 4));
        jogadores.add(new Jogador("Olivier Giroud", "França", 3));
        jogadores.add(new Jogador("Kaoru Mitoma", "Japão", 3));
        jogadores.add(new Jogador("Pedri", "Espanha", 2));

        RepositorioDados.guardarJogadores(jogadores);
    }
}