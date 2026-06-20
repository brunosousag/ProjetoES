package org.modelo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InicializadorDados {

    public static void inicializar() {
        criarEquipasSeNaoExistirem();
        criarProdutosSeNaoExistirem();
        criarJogadoresSeNaoExistirem();
        criarArbitrosSeNaoExistirem();
        criarGruposSeNaoExistirem();
        criarEstadiosSeNaoExistirem();
        criarBancadasSeNaoExistirem();
        criarJogosCalendarioSeNaoExistirem();
    }

    /**
     * Cria o layout padrão de bancadas — partilhado por todos os estádios.
     * Distribuído por setores (Norte/Sul/Este/Oeste/Camarotes/Mob. reduzida)
     * e pisos. O multiplicador de preço espelha a localização: central e
     * inferior é mais caro, terceiro piso é mais barato.
     */
    private static void criarBancadasSeNaoExistirem() {
        File ficheiro = new File(CaminhosFicheiros.FICHEIRO_BANCADAS);

        if (ficheiro.exists()) {
            return;
        }

        ArrayList<Bancada> bancadas = new ArrayList<>();

        // 1º piso inferior (mais perto do relvado, mais caro)
        bancadas.add(new Bancada("1º piso inferior Norte Esq", Bancada.Setor.NORTE, Bancada.Piso.INFERIOR, 800, 1.5));
        bancadas.add(new Bancada("1º piso inferior Norte", Bancada.Setor.NORTE, Bancada.Piso.INFERIOR, 1000, 1.6));
        bancadas.add(new Bancada("1º piso inferior Norte Drt", Bancada.Setor.NORTE, Bancada.Piso.INFERIOR, 800, 1.5));
        bancadas.add(new Bancada("1º piso inferior Oeste", Bancada.Setor.OESTE, Bancada.Piso.INFERIOR, 1200, 1.4));
        bancadas.add(new Bancada("1º piso inferior Este", Bancada.Setor.ESTE, Bancada.Piso.INFERIOR, 1200, 1.4));
        bancadas.add(new Bancada("Sul inferior Esq", Bancada.Setor.SUL, Bancada.Piso.INFERIOR, 800, 1.5));
        bancadas.add(new Bancada("1º piso inferior Sul", Bancada.Setor.SUL, Bancada.Piso.INFERIOR, 1000, 1.6));
        bancadas.add(new Bancada("Sul inferior Drt", Bancada.Setor.SUL, Bancada.Piso.INFERIOR, 800, 1.5));

        // 2º piso superior (preço médio)
        bancadas.add(new Bancada("2º piso Sup Oeste Esq", Bancada.Setor.OESTE, Bancada.Piso.SUPERIOR_2, 600, 1.1));
        bancadas.add(new Bancada("2º piso Sup Oeste Cent", Bancada.Setor.OESTE, Bancada.Piso.SUPERIOR_2, 700, 1.2));
        bancadas.add(new Bancada("2º piso Sup Oeste Drt", Bancada.Setor.OESTE, Bancada.Piso.SUPERIOR_2, 600, 1.1));
        bancadas.add(new Bancada("2º piso Sup Est Esq", Bancada.Setor.ESTE, Bancada.Piso.SUPERIOR_2, 600, 1.1));
        bancadas.add(new Bancada("2º piso Sup Est Cent", Bancada.Setor.ESTE, Bancada.Piso.SUPERIOR_2, 700, 1.2));
        bancadas.add(new Bancada("2º piso Sup Est Drt", Bancada.Setor.ESTE, Bancada.Piso.SUPERIOR_2, 600, 1.1));

        // 3º piso superior (mais barato)
        bancadas.add(new Bancada("3º superior Norte Esq", Bancada.Setor.NORTE, Bancada.Piso.SUPERIOR_3, 1000, 0.8));
        bancadas.add(new Bancada("3º superior Norte Cent", Bancada.Setor.NORTE, Bancada.Piso.SUPERIOR_3, 1200, 0.9));
        bancadas.add(new Bancada("3º superior Norte Drt", Bancada.Setor.NORTE, Bancada.Piso.SUPERIOR_3, 1000, 0.8));
        bancadas.add(new Bancada("3º superior Sul Esq", Bancada.Setor.SUL, Bancada.Piso.SUPERIOR_3, 1000, 0.8));
        bancadas.add(new Bancada("3º superior Sul Cent", Bancada.Setor.SUL, Bancada.Piso.SUPERIOR_3, 1200, 0.9));
        bancadas.add(new Bancada("3º superior Sul Drt", Bancada.Setor.SUL, Bancada.Piso.SUPERIOR_3, 1000, 0.8));

        // Camarotes (premium)
        bancadas.add(new Bancada("Camarotes Norte", Bancada.Setor.CAMAROTE, Bancada.Piso.CAMAROTE, 200, 3.0));
        bancadas.add(new Bancada("Camarotes Sul", Bancada.Setor.CAMAROTE, Bancada.Piso.CAMAROTE, 200, 3.0));

        // Mobilidade reduzida (preço base)
        bancadas.add(new Bancada("Mobilidade reduzida Norte Esq", Bancada.Setor.MOBILIDADE_REDUZIDA, Bancada.Piso.MOBILIDADE_REDUZIDA, 50, 1.0));
        bancadas.add(new Bancada("Mobilidade reduzida Norte Drt", Bancada.Setor.MOBILIDADE_REDUZIDA, Bancada.Piso.MOBILIDADE_REDUZIDA, 50, 1.0));
        bancadas.add(new Bancada("Mobilidade reduzida Sul Esq", Bancada.Setor.MOBILIDADE_REDUZIDA, Bancada.Piso.MOBILIDADE_REDUZIDA, 50, 1.0));
        bancadas.add(new Bancada("Mobilidade reduzida Sul Drt", Bancada.Setor.MOBILIDADE_REDUZIDA, Bancada.Piso.MOBILIDADE_REDUZIDA, 50, 1.0));

        RepositorioDados.guardarBancadas(bancadas);
    }

    private static void criarEquipasSeNaoExistirem() {
        File ficheiro = new File(CaminhosFicheiros.FICHEIRO_EQUIPAS);

        if (ficheiro.exists()) {
            return;
        }

        ArrayList<Equipa> equipas = new ArrayList<>();

        equipas.add(new Equipa(1, "Portugal", "Seleção"));
        equipas.add(new Equipa(2, "Brasil", "Seleção"));
        equipas.add(new Equipa(3, "Argentina", "Seleção"));
        equipas.add(new Equipa(4, "França", "Seleção"));
        equipas.add(new Equipa(5, "Espanha", "Seleção"));
        equipas.add(new Equipa(6, "Japão", "Seleção"));
        equipas.add(new Equipa(7, "Portugal", "Arbitragem"));
        equipas.add(new Equipa(8, "Portugal", "Médica"));

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

        jogadores.add(new Jogador("Kylian Mbappé", 4, 11));
        jogadores.add(new Jogador("Lionel Messi", 3, 10));
        jogadores.add(new Jogador("Cristiano Ronaldo", 1, 9));
        jogadores.add(new Jogador("Neymar", 2, 8));
        jogadores.add(new Jogador("Antoine Griezmann", 4, 7));
        jogadores.add(new Jogador("Rodrygo", 2, 7));
        jogadores.add(new Jogador("Vinícius Júnior", 2, 6));
        jogadores.add(new Jogador("Álvaro Morata", 5, 6));
        jogadores.add(new Jogador("Bruno Fernandes", 1, 5));
        jogadores.add(new Jogador("Lamine Yamal", 5, 5));
        jogadores.add(new Jogador("Julián Álvarez", 3, 4));
        jogadores.add(new Jogador("Takefusa Kubo", 6, 4));
        jogadores.add(new Jogador("Olivier Giroud", 4, 3));
        jogadores.add(new Jogador("Kaoru Mitoma", 6, 3));
        jogadores.add(new Jogador("Pedri", 5, 2));

        RepositorioDados.guardarJogadores(jogadores);
    }

    private static void criarArbitrosSeNaoExistirem() {
        File ficheiro = new File(CaminhosFicheiros.FICHEIRO_ARBITROS);

        if (ficheiro.exists()) {
            return;
        }

        ArrayList<Arbitro> arbitros = new ArrayList<>();

        arbitros.add(new Arbitro("João Pinheiro", 7));
        arbitros.add(new Arbitro("Ismail Elfath", 7));
        arbitros.add(new Arbitro("Raphael Claus", 7));
        arbitros.add(new Arbitro("Mustapha Ghorbal", 7));
        arbitros.add(new Arbitro("Yusuke Araki", 7));
        arbitros.add(new Arbitro("Campbell-Kirk Kawana-Waugh", 7));

        RepositorioDados.guardarArbitros(arbitros);
    }

    /**
     * Cria o calendário inicial da fase de grupos.
     *
     * Já não é hardcoded: gera automaticamente o primeiro jogo de cada equipa
     * (1ª jornada de todos os grupos) a partir dos grupos e estádios já criados.
     * Por isso este método tem de correr DEPOIS de criarGruposSeNaoExistirem()
     * e criarEstadiosSeNaoExistirem().
     */
    private static void criarJogosCalendarioSeNaoExistirem() {
        File ficheiro = new File(CaminhosFicheiros.FICHEIRO_JOGOS_CALENDARIO);

        if (ficheiro.exists()) {
            return;
        }

        new LogicaTorneio().gerarPrimeiraJornada();
    }

    private static void criarEstadiosSeNaoExistirem() {
        File ficheiro = new File(CaminhosFicheiros.FICHEIRO_ESTADIOS);

        if (ficheiro.exists()) {
            return;
        }

        ArrayList<Estadio> estadios = new ArrayList<>();


        // Estados Unidos (11)
        estadios.add(new Estadio("AT&T Stadium", 94000, "Arlington", "Estados Unidos"));
        estadios.add(new Estadio("MetLife Stadium", 82500, "East Rutherford", "Estados Unidos"));
        estadios.add(new Estadio("Mercedes-Benz Stadium", 75000, "Atlanta", "Estados Unidos"));
        estadios.add(new Estadio("Arrowhead Stadium", 73000, "Kansas City", "Estados Unidos"));
        estadios.add(new Estadio("NRG Stadium", 72000, "Houston", "Estados Unidos"));
        estadios.add(new Estadio("Levi's Stadium", 71000, "Santa Clara", "Estados Unidos"));
        estadios.add(new Estadio("SoFi Stadium", 70000, "Inglewood", "Estados Unidos"));
        estadios.add(new Estadio("Lincoln Financial Field", 69000, "Filadélfia", "Estados Unidos"));
        estadios.add(new Estadio("Lumen Field", 69000, "Seattle", "Estados Unidos"));
        estadios.add(new Estadio("Gillette Stadium", 65000, "Foxborough", "Estados Unidos"));
        estadios.add(new Estadio("Hard Rock Stadium", 65000, "Miami Gardens", "Estados Unidos"));

        // México (3)
        estadios.add(new Estadio("Estadio Azteca", 83000, "Cidade do México", "México"));
        estadios.add(new Estadio("Estadio BBVA", 53500, "Monterrey", "México"));
        estadios.add(new Estadio("Estadio Akron", 48000, "Zapopan (Guadalajara)", "México"));

        // Canadá (2)
        estadios.add(new Estadio("BC Place", 54000, "Vancouver", "Canadá"));
        estadios.add(new Estadio("BMO Field", 45000, "Toronto", "Canadá"));



        RepositorioDados.guardarEstadios(estadios);
    }

    /**
     * Cria o ficheiro grupos.dat com os 12 grupos do Mundial 2026 (48 equipas).
     * O ficheiro guarda APENAS os nomes das equipas — a logística (alojamento,
     * deslocação) fica na classe Equipa, ligada depois pelo nome.
     *
     * NOTA: a composição abaixo é ilustrativa. Substituir pelos grupos do
     * sorteio oficial quando estiverem definidos.
     */
    private static void criarGruposSeNaoExistirem() {
        File ficheiro = new File(CaminhosFicheiros.FICHEIRO_GRUPOS);

        if (ficheiro.exists()) {
            return;
        }

        ArrayList<Grupo> grupos = new ArrayList<>();

        grupos.add(new Grupo("GRUPO A", Arrays.asList("México", "Coreia do Sul", "República Checa", "África do Sul")));
        grupos.add(new Grupo("GRUPO B", Arrays.asList("Suiça", "Canadá", "Catar", "Bósnia e Herzegovina")));
        grupos.add(new Grupo("GRUPO C", Arrays.asList("Escócia", "Marrocos", "Brasil", "Haiti")));
        grupos.add(new Grupo("GRUPO D", Arrays.asList("EUA", "Austrália", "Turquia", "Paraguai")));
        grupos.add(new Grupo("GRUPO E", Arrays.asList("Alemanha", "Costa do Marfim", "Equador", "Curaçau")));
        grupos.add(new Grupo("GRUPO F", Arrays.asList("Suécia", "Japão", "Países Baixos", "Tunísia")));
        grupos.add(new Grupo("GRUPO G", Arrays.asList("Nova Zelândia", "Irão", "Bélgica", "Egipto")));
        grupos.add(new Grupo("GRUPO H", Arrays.asList("Uruguai", "Arábia Saudita", "Espanha", "Cabo Verde")));
        grupos.add(new Grupo("GRUPO I", Arrays.asList("Noruega", "França", "Senegal", "Iraque")));
        grupos.add(new Grupo("GRUPO J", Arrays.asList("Argentina", "Áustria", "Jordânia", "Argélia")));
        grupos.add(new Grupo("GRUPO K", Arrays.asList("Colombia", "DR Congo", "Portugal", "Uzbequistão")));
        grupos.add(new Grupo("GRUPO L", Arrays.asList("Inglaterra", "Gana", "Panamá", "Croácia")));

        RepositorioDados.guardarGrupos(grupos);
    }
}