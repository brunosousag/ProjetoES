package org.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Centraliza a lógica do torneio:
 *   - geração dos jogos da fase de grupos (calendário);
 *   - apuramento da fase de grupos (quem passa);
 *   - progressão pelas fases eliminatórias
 *     (16avos -> oitavos -> quartos -> meias-finais -> final).
 */
public class LogicaTorneio {

    // ------------------------------------------------------------------ fases

    /** Nomes das fases eliminatórias (têm de coincidir com os usados em GerirJogos). */
    public static final String FASE_16AVOS  = "Dezasseis-avos de final";
    public static final String TERCEIROS_QUALIFICADOS = "Terceiros qualificados";
    public static final String FASE_OITAVOS = "Oitavos de final";
    public static final String FASE_QUARTOS = "Quartos de final";
    public static final String FASE_MEIAS   = "Meias-finais";
    public static final String FASE_FINAL   = "Final";

    /** Fases eliminatórias por ordem de progressão (da primeira à última). */
    public static final String[] FASES_ELIMINATORIAS = {
            FASE_16AVOS, FASE_OITAVOS, FASE_QUARTOS, FASE_MEIAS, FASE_FINAL
    };

    // ----------------------------------------------- parâmetros da geração do calendário

    /** Hora mais cedo a que um jogo pode começar. */
    private static final int HORA_MIN = 14;            // 14:00
    /** Hora mais tarde a que um jogo pode começar (ainda antes da meia-noite). */
    private static final int HORA_MAX = 23;            // 23:xx
    /** Preços possíveis para o bilhete (€). */
    private static final double[] PRECOS = {35.0, 40.0, 45.0, 50.0, 55.0};
    /** Quantos grupos jogam no mesmo dia (espalha a jornada pelo calendário). */
    private static final int GRUPOS_POR_DIA = 2;

    // ------------------------------------------------------------------ dados

    private final List<Equipa> equipas;
    private final List<Grupo> grupos;
    private final List<Estadio> estadios;
    private final List<JogoCalendario> jogosGrupos;     // fase de grupos (calendário)
    private final List<Jogo> jogosEliminatorios;        // fases a eliminar
    private final Random random = new Random();

    public LogicaTorneio() {
        this.equipas = RepositorioDados.carregarEquipas();
        this.grupos = RepositorioDados.carregarGrupos();
        this.estadios = RepositorioDados.carregarEstadios();
        this.jogosGrupos = RepositorioDados.carregarJogosCalendario();
        this.jogosEliminatorios = RepositorioDados.carregarJogos();
    }

    // ------------------------------------------------- fase de grupos: consulta

    /** Nomes dos grupos existentes (ex.: "GRUPO A"), pela ordem do ficheiro. */
    public List<String> grupos() {
        List<String> nomes = new ArrayList<>();
        for (Grupo grupo : grupos) {
            nomes.add(grupo.getNome());
        }
        return nomes;
    }

    /** Equipas que disputam um determinado grupo. */
    public List<String> equipasDoGrupo(String grupo) {
        for (Grupo g : grupos) {
            if (g.getNome().equals(grupo)) {
                return new ArrayList<>(g.getEquipas());
            }
        }
        return new ArrayList<>();
    }

    // -------------------------------------- fase de grupos: geração do calendário

    /**
     * Gera o primeiro jogo de cada equipa (1ª jornada de todos os grupos).
     * É apenas um atalho para gerarJornada() quando ainda não há jogos.
     */
    public List<JogoCalendario> gerarPrimeiraJornada() {
        return gerarJornada();
    }

    /**
     * Gera uma jornada para cada grupo: empareia aleatoriamente as equipas que
     * ainda não se defrontaram (nenhuma joga contra a mesma duas vezes), atribui
     * data e hora (entre as 14:00 e as 00:00) e um estádio com a sua capacidade.
     * Os jogos são acrescentados ao calendário e guardados no ficheiro.
     *
     * Voltar a chamar gera a jornada seguinte — a usar depois de registados os
     * resultados da jornada anterior.
     */
    public List<JogoCalendario> gerarJornada() {
        Set<String> paresJogados = paresJogados();
        LocalDate dataBase = dataBaseProximaJornada();

        List<JogoCalendario> novos = new ArrayList<>();
        int indiceGrupo = 0;
        for (Grupo grupo : grupos) {
            List<String[]> pares = emparelhar(grupo.getEquipas(), paresJogados);
            LocalDate dia = dataBase.plusDays(indiceGrupo / GRUPOS_POR_DIA);
            for (String[] par : pares) {
                novos.add(criarJogo(grupo.getNome(), par[0], par[1], dia));
                paresJogados.add(chavePar(par[0], par[1]));
            }
            indiceGrupo++;
        }

        jogosGrupos.addAll(novos);
        RepositorioDados.guardarJogosCalendario(new ArrayList<>(jogosGrupos));
        return novos;
    }

    /** Cria um JogoCalendario com hora, estádio e preço atribuídos aleatoriamente. */
    private JogoCalendario criarJogo(String grupo, String equipaA, String equipaB, LocalDate data) {
        LocalTime hora = horaAleatoria();
        Estadio estadio = estadioAleatorio();
        double preco = PRECOS[random.nextInt(PRECOS.length)];
        return new JogoCalendario(
                grupo, equipaA, equipaB,
                data, hora,
                estadio.getNome(), estadio.getCapacidade(),
                0,              // bilhetes vendidos: começa a zero
                preco
        );
    }

    /** Hora aleatória entre as 14:00 e as 23:45. */
    private LocalTime horaAleatoria() {
        int hora = HORA_MIN + random.nextInt(HORA_MAX - HORA_MIN + 1);  // 14..23
        int minuto = random.nextInt(4) * 15;                            // 0,15,30,45
        return LocalTime.of(hora, minuto);
    }

    private Estadio estadioAleatorio() {
        return estadios.get(random.nextInt(estadios.size()));
    }

    /** Próxima data livre: hoje (se não há jogos) ou o dia a seguir ao último jogo. */
    private LocalDate dataBaseProximaJornada() {
        if (jogosGrupos.isEmpty()) {
            return LocalDate.now();
        }
        LocalDate ultima = jogosGrupos.get(0).getData();
        for (JogoCalendario jogo : jogosGrupos) {
            if (jogo.getData().isAfter(ultima)) {
                ultima = jogo.getData();
            }
        }
        return ultima.plusDays(1);
    }

    /** Confrontos já marcados (chave normalizada "A|B"), para não repetir jogos. */
    private Set<String> paresJogados() {
        Set<String> pares = new HashSet<>();
        for (JogoCalendario jogo : jogosGrupos) {
            pares.add(chavePar(jogo.getEquipaA(), jogo.getEquipaB()));
        }
        return pares;
    }

    /**
     * Empareia as equipas de um grupo em jogos, garantindo que cada equipa joga
     * uma vez e que nenhum par já se defrontou. Usa backtracking porque, nas
     * últimas jornadas, pode só restar um emparelhamento válido.
     */
    private List<String[]> emparelhar(List<String> equipas, Set<String> paresJogados) {
        List<String> restantes = new ArrayList<>(equipas);
        Collections.shuffle(restantes, random);
        List<String[]> pares = new ArrayList<>();
        emparelharRec(restantes, paresJogados, pares);
        return pares;
    }

    private boolean emparelharRec(List<String> restantes, Set<String> paresJogados, List<String[]> pares) {
        if (restantes.size() < 2) {
            return true;   // 0 (ou 1, se o grupo for ímpar) equipas por emparelhar
        }
        String a = restantes.remove(0);
        for (int i = 0; i < restantes.size(); i++) {
            if (!paresJogados.contains(chavePar(a, restantes.get(i)))) {
                String b = restantes.remove(i);
                pares.add(new String[]{a, b});
                if (emparelharRec(restantes, paresJogados, pares)) {
                    return true;
                }
                pares.remove(pares.size() - 1);
                restantes.add(i, b);
            }
        }
        restantes.add(0, a);
        return false;
    }

    private String chavePar(String a, String b) {
        return (a.compareTo(b) <= 0) ? a + "|" + b : b + "|" + a;
    }

    // ------------------------------------------------- fase de grupos: apuramento

    /**
     * Devolve, por ordem de classificação, as equipas apuradas da fase de grupos.
     *
     * TODO (a fazer em conjunto): definir o critério de apuramento
     * (pontos -> diferença de golos -> golos marcados) e quantas equipas
     * passam por grupo. Depende de termos resultado/golos nos jogos de grupo,
     * que o JogoCalendario ainda não guarda.
     */
    public List<String> apuradosFaseGrupos() {
        // TODO
        return new ArrayList<>();
    }

    // ------------------------------------------------------------------ fases eliminatórias

    /** Fase seguinte à fase dada, ou null se já for a final (ou fase desconhecida). */
    public String proximaFase(String fase) {
        for (int i = 0; i < FASES_ELIMINATORIAS.length - 1; i++) {
            if (FASES_ELIMINATORIAS[i].equals(fase)) {
                return FASES_ELIMINATORIAS[i + 1];
            }
        }
        return null;
    }

    /** Equipas vencedoras dos jogos já terminados de uma fase. */
    public List<String> vencedores(String fase) {
        List<String> vencedores = new ArrayList<>();
        for (Jogo jogo : jogosEliminatorios) {
            if (fase.equals(jogo.getFase()) && jogo.isTerminado()) {
                String venc = jogo.getVencedora();
                if (venc != null) {
                    vencedores.add(venc);
                }
            }
        }
        return vencedores;
    }

    /**
     * Cria os jogos da fase eliminatória seguinte a partir das equipas apuradas.
     *
     * TODO (a fazer em conjunto): emparelhar as equipas (sorteio/seeding),
     * criar os objetos Jogo com a fase correta e guardá-los.
     */
    public List<Jogo> gerarJogos(String fase, List<String> apurados) {
        // TODO
        return new ArrayList<>();
    }

    // ------------------------------------------------------------------ getters

    public List<Equipa> getEquipas() { return equipas; }
    public List<Grupo> getGrupos() { return grupos; }
    public List<Estadio> getEstadios() { return estadios; }
    public List<JogoCalendario> getJogosGrupos() { return jogosGrupos; }
    public List<Jogo> getJogosEliminatorios() { return jogosEliminatorios; }
}