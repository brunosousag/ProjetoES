package org.modelo;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InicializadorDados {

    public static void inicializar() {
        // Os grupos têm de existir antes das equipas: a criação das equipas
        // usa-os para semear automaticamente todas as seleções que jogam.
        criarGruposSeNaoExistirem();
        criarEquipasSeNaoExistirem();
        criarProdutosSeNaoExistirem();
        criarJogadoresSeNaoExistirem();
        criarArbitrosSeNaoExistirem();
        criarEstadiosSeNaoExistirem();
        criarBancadasSeNaoExistirem();
        criarJogosCalendarioSeNaoExistirem();
        // Atribui um árbitro a cada jogo (sem repetir árbitro no mesmo horário).
        atribuirArbitrosAosJogos();
        // Depois: hotel por defeito para cada seleção e cada equipa de arbitragem,
        // perto do estádio do respetivo jogo.
        criarAlojamentosSeNaoExistirem();
    }

    /**
     * Atribui um árbitro a cada jogo do calendário, de forma rotativa, garantindo
     * que o mesmo árbitro não fica em dois jogos com a mesma data+hora.
     * Só preenche jogos que ainda não têm árbitro.
     */
    private static void atribuirArbitrosAosJogos() {
        ArrayList<JogoCalendario> jogos = RepositorioDados.carregarJogosCalendario();
        ArrayList<Arbitro> arbitros = RepositorioDados.carregarArbitros();
        if (arbitros.isEmpty()) {
            return;
        }

        // Árbitros já ocupados em cada horário (data+hora -> nomes de árbitros).
        Map<LocalDateTime, Set<String>> ocupadosPorHorario = new HashMap<>();
        for (JogoCalendario jogo : jogos) {
            if (jogo.getArbitro() != null) {
                ocupadosPorHorario
                        .computeIfAbsent(jogo.getDataHora(), k -> new HashSet<>())
                        .add(jogo.getArbitro());
            }
        }

        boolean alterou = false;
        int proximo = 0;   // índice rotativo, para distribuir os árbitros
        for (JogoCalendario jogo : jogos) {
            if (jogo.getArbitro() != null) {
                continue;
            }
            Set<String> ocupados = ocupadosPorHorario
                    .computeIfAbsent(jogo.getDataHora(), k -> new HashSet<>());

            // Procura o próximo árbitro livre neste horário.
            for (int n = 0; n < arbitros.size(); n++) {
                Arbitro candidato = arbitros.get((proximo + n) % arbitros.size());
                if (!ocupados.contains(candidato.getNome())) {
                    jogo.setArbitro(candidato.getNome());
                    ocupados.add(candidato.getNome());
                    proximo = (proximo + n + 1) % arbitros.size();
                    alterou = true;
                    break;
                }
            }
        }

        if (alterou) {
            RepositorioDados.guardarJogosCalendario(jogos);
        }
    }

    /**
     * Atribui a cada seleção um alojamento num hotel perto do estádio do seu
     * jogo. Corre depois de existirem equipas, estádios e calendário.
     * Só preenche equipas SEM alojamento, para não sobrescrever escolhas
     * feitas na aplicação.
     */
    private static void criarAlojamentosSeNaoExistirem() {
        ArrayList<Equipa> equipas = RepositorioDados.carregarEquipas();
        ArrayList<JogoCalendario> jogos = RepositorioDados.carregarJogosCalendario();
        ArrayList<Estadio> estadios = RepositorioDados.carregarEstadios();
        ArrayList<Arbitro> arbitros = RepositorioDados.carregarArbitros();

        boolean alterou = false;
        for (Equipa equipa : equipas) {
            if (equipa.getAlojamento() != null) {
                continue;   // já tem alojamento -> não mexe
            }

            // Seleção: estádio onde a equipa joga. Arbitragem: estádio do jogo
            // que o árbitro dela apita.
            String nomeEstadio;
            if ("Seleção".equals(equipa.getTipo())) {
                nomeEstadio = estadioDaEquipa(jogos, equipa.getNome());
            } else if ("Arbitragem".equals(equipa.getTipo())) {
                nomeEstadio = estadioDoArbitro(equipa, arbitros, jogos);
            } else {
                continue;   // outros tipos (ex.: Médica) não têm jogo associado
            }

            if (nomeEstadio == null) {
                continue;   // sem jogo associado -> sem hotel
            }
            String cidade = cidadeDoEstadio(estadios, nomeEstadio);
            equipa.setAlojamento(new Alojamento(
                    "Hotel " + cidade,
                    "Perto do " + nomeEstadio + ", " + cidade));
            alterou = true;
        }

        if (alterou) {
            RepositorioDados.guardarEquipas(equipas);
        }
    }

    /** Estádio do jogo que o árbitro desta equipa de arbitragem apita, ou null. */
    private static String estadioDoArbitro(Equipa equipaArbitragem,
                                           ArrayList<Arbitro> arbitros,
                                           ArrayList<JogoCalendario> jogos) {
        for (Arbitro arbitro : arbitros) {
            if (arbitro.getEquipaId() == equipaArbitragem.getId()) {
                for (JogoCalendario jogo : jogos) {
                    if (arbitro.getNome().equals(jogo.getArbitro())) {
                        return jogo.getEstadio();
                    }
                }
            }
        }
        return null;
    }

    /** Estádio do (primeiro) jogo em que a equipa participa, ou null. */
    private static String estadioDaEquipa(ArrayList<JogoCalendario> jogos, String nomeEquipa) {
        for (JogoCalendario jogo : jogos) {
            if (nomeEquipa.equals(jogo.getEquipaA()) || nomeEquipa.equals(jogo.getEquipaB())) {
                return jogo.getEstadio();
            }
        }
        return null;
    }

    /** Cidade do estádio com o nome dado, ou "" se não existir. */
    private static String cidadeDoEstadio(ArrayList<Estadio> estadios, String nomeEstadio) {
        for (Estadio estadio : estadios) {
            if (estadio.getNome().equals(nomeEstadio)) {
                return estadio.getCidade();
            }
        }
        return "";
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

        // Portugal e Japão como Seleção ANTES da arbitragem: existem equipas de
        // arbitragem com o mesmo nome, e o idEquipaPorNome devolve a 1ª — assim
        // os jogadores destas seleções resolvem para a Seleção, não a Arbitragem.
        // As restantes seleções dos jogos são criadas no fim (a partir dos grupos).
        equipas.add(new Equipa(1, "Portugal", "Seleção"));
        equipas.add(new Equipa(6, "Japão", "Seleção"));
        equipas.add(new Equipa(7, "Portugal", "Arbitragem"));
        equipas.add(new Equipa(8, "Estados-Unidos", "Arbitragem"));
        equipas.add(new Equipa(9, "Brasil", "Arbitragem"));
        equipas.add(new Equipa(10, "Argélia", "Arbitragem"));
        equipas.add(new Equipa(11, "Japão", "Arbitragem"));
        equipas.add(new Equipa(12, "Nova-Zelândia", "Arbitragem"));
        equipas.add(new Equipa(13, "Portugal", "Médica"));

        // Cria como "Seleção" todas as equipas presentes nos jogos (as dos
        // grupos), excepto as que já existam. O id continua a partir do maior.
        int proximoId = 1;
        for (Equipa equipa : equipas) {
            if (equipa.getId() >= proximoId) {
                proximoId = equipa.getId() + 1;
            }
        }
        for (Grupo grupo : RepositorioDados.carregarGrupos()) {
            for (String nomeEquipa : grupo.getEquipas()) {
                if (!existeSelecao(equipas, nomeEquipa)) {
                    equipas.add(new Equipa(proximoId++, nomeEquipa, "Seleção"));
                }
            }
        }

        RepositorioDados.guardarEquipas(equipas);
    }

    /** Verifica se já existe uma equipa do tipo "Seleção" com o nome dado. */
    private static boolean existeSelecao(ArrayList<Equipa> equipas, String nomeEquipa) {
        for (Equipa equipa : equipas) {
            if ("Seleção".equals(equipa.getTipo()) && equipa.getNome().equals(nomeEquipa)) {
                return true;
            }
        }
        return false;
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

        adicionarJogadores(jogadores, "México",
                "Guillermo Ochoa", "Jorge Sánchez", "César Montes", "Johan Vásquez",
                "Jesús Gallardo", "Edson Álvarez", "Luis Chávez", "Erick Sánchez",
                "Hirving Lozano", "Santiago Giménez", "Alexis Vega");
        adicionarJogadores(jogadores, "Coreia do Sul",
                "Jo Hyeon-woo", "Seol Young-woo", "Kim Min-jae", "Kim Young-gwon",
                "Kim Jin-su", "Hwang In-beom", "Park Yong-woo", "Lee Kang-in",
                "Jae-sung Lee", "Hwang Hee-chan", "Son Heung-min");
        adicionarJogadores(jogadores, "Suiça",
                "Yann Sommer", "Fabian Schär", "Manuel Akanji", "Ricardo Rodríguez",
                "Silvan Widmer", "Remo Freuler", "Granit Xhaka", "Dan Ndoye",
                "Xherdan Shaqiri", "Ruben Vargas", "Breel Embolo");
        adicionarJogadores(jogadores, "Canadá",
                "Maxime Crépeau", "Alistair Johnston", "Moïse Bombito", "Derek Cornelius",
                "Richie Laryea", "Ismaël Koné", "Stephen Eustáquio", "Alphonso Davies",
                "Jonathan David", "Jacob Shaffelburg", "Cyle Larin");
        adicionarJogadores(jogadores, "Escócia",
                "Angus Gunn", "Ryan Porteous", "Jack Hendry", "Kieran Tierney",
                "Anthony Ralston", "Billy Gilmour", "Callum McGregor", "Andy Robertson",
                "Scott McTominay", "John McGinn", "Ché Adams");
        adicionarJogadores(jogadores, "Marrocos",
                "Yassine Bounou", "Achraf Hakimi", "Nayef Aguerd", "Romain Saïss",
                "Noussair Mazraoui", "Sofyan Amrabat", "Azzedine Ounahi", "Bilal El Khannouss",
                "Hakim Ziyech", "Youssef En-Nesyri", "Brahim Díaz");
        adicionarJogadores(jogadores, "Portugal",
                "Diogo Costa", "João Cancelo", "Rúben Dias", "Gonçalo Inácio",
                "Nuno Mendes", "João Palhinha", "Vitinha", "Bruno Fernandes",
                "Bernardo Silva", "Cristiano Ronaldo", "Rafael Leão");
        adicionarJogadores(jogadores, "Austrália",
                "Mathew Ryan", "Gethin Jones", "Harry Souttar", "Kye Rowles",
                "Aziz Behich", "Connor Metcalfe", "Keanu Baccus", "Jackson Irvine",
                "Craig Goodwin", "Mitchell Duke", "Kusini Yengi");
        adicionarJogadores(jogadores, "Alemanha",
                "Marc-André ter Stegen", "Joshua Kimmich", "Antonio Rüdiger", "Jonathan Tah",
                "Maximilian Mittelstädt", "Robert Andrich", "Toni Kroos", "Jamal Musiala",
                "Ilkay Gündoğan", "Florian Wirtz", "Kai Havertz");
        adicionarJogadores(jogadores, "Costa do Marfim",
                "Yahia Fofana", "Wilfried Singo", "Odilon Kossounou", "Evan Ndicka",
                "Ghislain Konan", "Franck Kessié", "Jean Michaël Seri", "Seko Fofana",
                "Simon Adingra", "Sébastien Haller", "Nicolas Pépé");
        adicionarJogadores(jogadores, "Suécia",
                "Robin Olsen", "Emil Holm", "Victor Lindelöf", "Isak Hien",
                "Ludwig Augustinsson", "Dejan Kulusevski", "Jens Cajuste", "Anton Salétros",
                "Emil Forsberg", "Alexander Isak", "Viktor Gyökeres");
        adicionarJogadores(jogadores, "Japão",
                "Zion Suzuki", "Yukinari Sugawara", "Ko Itakura", "Takehiro Tomiyasu",
                "Hiroki Ito", "Wataru Endo", "Hidemasa Morita", "Takefusa Kubo",
                "Takumi Minamino", "Kaoru Mitoma", "Ayase Ueda");
        adicionarJogadores(jogadores, "Nova Zelândia",
                "Oliver Sail", "Tim Payne", "Michael Boxall", "Nando Pijnaker",
                "Liberato Cacace", "Joe Bell", "Marko Stamenic", "Matthew Garbett",
                "Callum McCowatt", "Chris Wood", "Ben Waine");
        adicionarJogadores(jogadores, "Irão",
                "Alireza Beiranvand", "Ramin Rezaeian", "Hossein Kanaani", "Shojae Khalilzadeh",
                "Milad Mohammadi", "Saeid Ezatolahi", "Saman Ghoddos", "Alireza Jahanbakhsh",
                "Mehdi Taremi", "Mohammad Mohebi", "Sardar Azmoun");
        adicionarJogadores(jogadores, "Uruguai",
                "Sergio Rochet", "Nahitan Nández", "Ronald Araújo", "José María Giménez",
                "Mathias Olivera", "Federico Valverde", "Manuel Ugarte", "Nicolás de la Cruz",
                "Facundo Pellistri", "Darwin Núñez", "Maximiliano Araújo");
        adicionarJogadores(jogadores, "Arábia Saudita",
                "Mohammed Al-Owais", "Ali Lajami", "Ali Al-Bulaihi", "Saud Abdulhamid",
                "Sultan Al-Ghannam", "Mohamed Kanno", "Abdullah Al-Khaibari", "Faisal Al-Ghamdi",
                "Nasser Al-Dawsari", "Saleh Al-Shehri", "Salem Al-Dawsari");

        RepositorioDados.guardarJogadores(jogadores);
    }

    /** Adiciona vários jogadores à mesma equipa (resolvida pelo nome), todos com 0 golos. */
    private static void adicionarJogadores(ArrayList<Jogador> jogadores, String nomeEquipa, String... nomes) {
        int idEquipa = RepositorioDados.idEquipaPorNome(nomeEquipa);
        for (String nome : nomes) {
            jogadores.add(new Jogador(nome, idEquipa, 0));
        }
    }

    private static void criarArbitrosSeNaoExistirem() {
        File ficheiro = new File(CaminhosFicheiros.FICHEIRO_ARBITROS);

        if (ficheiro.exists()) {
            return;
        }

        ArrayList<Arbitro> arbitros = new ArrayList<>();

        arbitros.add(new Arbitro("João Pinheiro", 7));
        arbitros.add(new Arbitro("Ismail Elfath", 8));
        arbitros.add(new Arbitro("Raphael Claus", 9));
        arbitros.add(new Arbitro("Mustapha Ghorbal", 10));
        arbitros.add(new Arbitro("Yusuke Araki", 11));
        arbitros.add(new Arbitro("Campbell-Kirk Kawana-Waugh", 12));

        RepositorioDados.guardarArbitros(arbitros);
    }

    /**
     * Cria o calendário inicial do torneio.
     *
     * Como a fase de grupos é externa, o torneio arranca já nos oitavos:
     * gera os 8 jogos a partir dos 1º e 2º classificados dos 8 grupos
     * (ver LogicaTorneio.gerarOitavos()). Cada jogo recebe data, hora,
     * estádio e preço aleatórios.
     *
     * Tem de correr DEPOIS de criarGruposSeNaoExistirem() e
     * criarEstadiosSeNaoExistirem().
     */
    private static void criarJogosCalendarioSeNaoExistirem() {
        File ficheiro = new File(CaminhosFicheiros.FICHEIRO_JOGOS_CALENDARIO);

        if (ficheiro.exists()) {
            return;
        }

        new LogicaTorneio().gerarOitavos();
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
     * Cria o ficheiro grupos.dat com os 8 grupos apurados para os oitavos.
     *
     * A fase de grupos é assumida como já concluída — o código só lida com
     * as 16 equipas qualificadas (1º e 2º de cada grupo). Por isso cada
     * Grupo guarda exactamente DUAS equipas, por esta ordem:
     *   índice 0 → 1º classificado;
     *   índice 1 → 2º classificado.
     * Esta ordem é usada por LogicaTorneio.gerarOitavos() para emparelhar
     * 1º de um grupo contra o 2º do grupo vizinho.
     *
     * NOTA: os nomes abaixo são ilustrativos. Substituir pelos apurados
     * reais quando definidos.
     */

    private static void criarGruposSeNaoExistirem() {
        File ficheiro = new File(CaminhosFicheiros.FICHEIRO_GRUPOS);

        if (ficheiro.exists()) {
            return;
        }

        ArrayList<Grupo> grupos = new ArrayList<>();

        grupos.add(new Grupo("GRUPO A", Arrays.asList("México", "Coreia do Sul")));
        grupos.add(new Grupo("GRUPO B", Arrays.asList("Suiça", "Canadá")));
        grupos.add(new Grupo("GRUPO C", Arrays.asList("Escócia", "Marrocos")));
        grupos.add(new Grupo("GRUPO D", Arrays.asList("Portugal", "Austrália")));
        grupos.add(new Grupo("GRUPO E", Arrays.asList("Alemanha", "Costa do Marfim")));
        grupos.add(new Grupo("GRUPO F", Arrays.asList("Suécia", "Japão")));
        grupos.add(new Grupo("GRUPO G", Arrays.asList("Nova Zelândia", "Irão")));
        grupos.add(new Grupo("GRUPO H", Arrays.asList("Uruguai", "Arábia Saudita")));

        RepositorioDados.guardarGrupos(grupos);
    }
}