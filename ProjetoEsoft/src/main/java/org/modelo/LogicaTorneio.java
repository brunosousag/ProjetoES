package org.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Centraliza a lógica do torneio:
 *   - geração dos jogos de calendário;
 *   - classificação da fase de grupos;
 *   - criação e atualização automática da árvore eliminatória.
 */
public class LogicaTorneio {

    // ------------------------------------------------------------------ fases

    public static final String FASE_OITAVOS = "Oitavos de final";
    public static final String FASE_QUARTOS = "Quartos de final";
    public static final String FASE_MEIAS   = "Meias-finais";
    public static final String FASE_TERCEIRO = "3.º lugar";
    public static final String FASE_FINAL   = "Final";

    /** Ordem visual usada no bracket. */
    public static final String[] FASES_BRACKET = {
            FASE_OITAVOS, FASE_QUARTOS, FASE_MEIAS, FASE_TERCEIRO, FASE_FINAL
    };

    /** Ordem competitiva principal. O jogo do 3.º lugar é derivado das meias-finais. */
    public static final String[] FASES_ELIMINATORIAS = {
            FASE_OITAVOS, FASE_QUARTOS, FASE_MEIAS, FASE_FINAL
    };

    // ----------------------------------------------- parâmetros da geração do calendário

    private static final int HORA_MIN = 14;
    private static final int HORA_MAX = 23;
    private static final double[] PRECOS = {35.0, 40.0, 45.0, 50.0, 55.0};
    private static final int GRUPOS_POR_DIA = 2;

    // ------------------------------------------------------------------ dados

    private final List<Equipa> equipas;
    private final List<Grupo> grupos;
    private final List<Estadio> estadios;
    private final List<JogoCalendario> jogosGrupos;
    private final List<Jogo> jogosEliminatorios;
    private final Random random = new Random();

    public LogicaTorneio() {
        this.equipas = RepositorioDados.carregarEquipas();
        this.grupos = RepositorioDados.carregarGrupos();
        this.estadios = RepositorioDados.carregarEstadios();
        this.jogosGrupos = RepositorioDados.carregarJogosCalendario();
        this.jogosEliminatorios = RepositorioDados.carregarJogos();
    }

    // ------------------------------------------------- fase de grupos: consulta

    public List<String> grupos() {
        List<String> nomes = new ArrayList<>();
        for (Grupo grupo : grupos) {
            nomes.add(grupo.getNome());
        }
        return nomes;
    }

    public List<String> equipasDoGrupo(String grupo) {
        for (Grupo g : grupos) {
            if (g.getNome().equals(grupo)) {
                return new ArrayList<>(g.getEquipas());
            }
        }
        return new ArrayList<>();
    }

    // -------------------------------------- fase de grupos: geração do calendário

    public List<JogoCalendario> gerarPrimeiraJornada() {
        return gerarJornada();
    }

    public List<JogoCalendario> gerarJornada() {
        Set<String> paresJogados = paresJogados();
        LocalDate dataBase = dataBaseProximaJornada(jogosGrupos);

        List<JogoCalendario> novos = new ArrayList<>();
        int indiceGrupo = 0;
        for (Grupo grupo : grupos) {
            List<String[]> pares = emparelhar(grupo.getEquipas(), paresJogados);
            LocalDate dia = dataBase.plusDays(indiceGrupo / GRUPOS_POR_DIA);
            for (String[] par : pares) {
                novos.add(criarJogoCalendario(grupo.getNome(), par[0], par[1], dia));
                paresJogados.add(chavePar(par[0], par[1]));
            }
            indiceGrupo++;
        }

        jogosGrupos.addAll(novos);
        RepositorioDados.guardarJogosCalendario(new ArrayList<>(jogosGrupos));
        return novos;
    }

    private JogoCalendario criarJogoCalendario(String grupo, String equipaA, String equipaB, LocalDate data) {
        LocalTime hora = horaAleatoria();
        Estadio estadio = estadioAleatorio();
        double preco = PRECOS[random.nextInt(PRECOS.length)];
        return new JogoCalendario(
                grupo, equipaA, equipaB,
                data, hora,
                estadio.getNome(), estadio.getCapacidade(),
                0,
                preco
        );
    }

    private LocalTime horaAleatoria() {
        int hora = HORA_MIN + random.nextInt(HORA_MAX - HORA_MIN + 1);
        int minuto = random.nextInt(4) * 15;
        return LocalTime.of(hora, minuto);
    }

    private Estadio estadioAleatorio() {
        if (estadios == null || estadios.isEmpty()) {
            return new Estadio("Estádio por definir", 50000, "Cidade por definir", "País por definir");
        }
        return estadios.get(random.nextInt(estadios.size()));
    }

    private LocalDate dataBaseProximaJornada(List<JogoCalendario> jogos) {
        if (jogos == null || jogos.isEmpty()) {
            return LocalDate.now();
        }
        LocalDate ultima = jogos.get(0).getData();
        for (JogoCalendario jogo : jogos) {
            if (jogo.getData().isAfter(ultima)) {
                ultima = jogo.getData();
            }
        }
        return ultima.plusDays(1);
    }

    private Set<String> paresJogados() {
        Set<String> pares = new HashSet<>();
        for (JogoCalendario jogo : jogosGrupos) {
            pares.add(chavePar(jogo.getEquipaA(), jogo.getEquipaB()));
        }
        return pares;
    }

    private List<String[]> emparelhar(List<String> equipas, Set<String> paresJogados) {
        List<String> restantes = new ArrayList<>(equipas);
        Collections.shuffle(restantes, random);
        List<String[]> pares = new ArrayList<>();
        emparelharRec(restantes, paresJogados, pares);
        return pares;
    }

    private boolean emparelharRec(List<String> restantes, Set<String> paresJogados, List<String[]> pares) {
        if (restantes.size() < 2) {
            return true;
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

    private static class EstatisticaEquipa {
        String equipa;
        int ordemOriginal;
        int pontos;
        int golosMarcados;
        int golosSofridos;

        int diferencaGolos() {
            return golosMarcados - golosSofridos;
        }
    }

    /**
     * Devolve os apurados de todos os grupos, pela ordem 1.º, 2.º, 1.º, 2.º, ...
     * Critérios: pontos -> diferença de golos -> golos marcados -> ordem do grupo.
     */
    public List<String> apuradosFaseGrupos() {
        ArrayList<Jogo> jogos = RepositorioDados.carregarJogos();
        List<String> apurados = new ArrayList<>();
        for (Grupo grupo : grupos) {
            List<String> classificados = classificadosDoGrupo(grupo, jogos);
            if (classificados.size() >= 1) apurados.add(classificados.get(0));
            if (classificados.size() >= 2) apurados.add(classificados.get(1));
        }
        return apurados;
    }

    private Map<String, List<String>> classificadosPorGrupo(ArrayList<Jogo> jogos) {
        Map<String, List<String>> resultado = new LinkedHashMap<>();
        for (Grupo grupo : grupos) {
            resultado.put(grupo.getNome(), classificadosDoGrupo(grupo, jogos));
        }
        return resultado;
    }

    private List<String> classificadosDoGrupo(Grupo grupo, ArrayList<Jogo> jogos) {
        List<EstatisticaEquipa> tabela = new ArrayList<>();
        Map<String, EstatisticaEquipa> porNome = new HashMap<>();

        for (int i = 0; i < grupo.getEquipas().size(); i++) {
            EstatisticaEquipa e = new EstatisticaEquipa();
            e.equipa = grupo.getEquipas().get(i);
            e.ordemOriginal = i;
            tabela.add(e);
            porNome.put(e.equipa, e);
        }

        for (Jogo jogo : jogos) {
            if (!grupo.getNome().equals(jogo.getFase()) || !jogo.isTerminado()) continue;

            EstatisticaEquipa a = porNome.get(jogo.getEquipaA());
            EstatisticaEquipa b = porNome.get(jogo.getEquipaB());
            if (a == null || b == null) continue;

            a.golosMarcados += jogo.getGolosA();
            a.golosSofridos += jogo.getGolosB();
            b.golosMarcados += jogo.getGolosB();
            b.golosSofridos += jogo.getGolosA();

            if (jogo.getGolosA() > jogo.getGolosB()) {
                a.pontos += 3;
            } else if (jogo.getGolosB() > jogo.getGolosA()) {
                b.pontos += 3;
            } else {
                a.pontos += 1;
                b.pontos += 1;
            }
        }

        tabela.sort(
                Comparator.comparingInt((EstatisticaEquipa e) -> e.pontos).reversed()
                        .thenComparing(Comparator.comparingInt(EstatisticaEquipa::diferencaGolos).reversed())
                        .thenComparing(Comparator.comparingInt((EstatisticaEquipa e) -> e.golosMarcados).reversed())
                        .thenComparingInt(e -> e.ordemOriginal)
        );

        List<String> classificados = new ArrayList<>();
        for (EstatisticaEquipa e : tabela) {
            classificados.add(e.equipa);
        }
        return classificados;
    }

    // ------------------------------------------------------------------ fases eliminatórias

    public static boolean isFaseEliminatoria(String fase) {
        if (fase == null) return false;
        for (String f : FASES_BRACKET) {
            if (f.equals(fase)) return true;
        }
        return false;
    }

    public static boolean isPlaceholder(String equipa) {
        if (equipa == null) return true;
        return equipa.startsWith("Vencedor Jogo ")
                || equipa.startsWith("Perdedor Jogo ")
                || equipa.startsWith("1.º ")
                || equipa.startsWith("2.º ")
                || equipa.trim().isEmpty();
    }

    public String proximaFase(String fase) {
        if (FASE_OITAVOS.equals(fase)) return FASE_QUARTOS;
        if (FASE_QUARTOS.equals(fase)) return FASE_MEIAS;
        if (FASE_MEIAS.equals(fase)) return FASE_FINAL;
        return null;
    }

    public List<String> vencedores(String fase) {
        ArrayList<Jogo> jogos = RepositorioDados.carregarJogos();
        List<String> vencedores = new ArrayList<>();
        for (Jogo jogo : jogos) {
            if (fase.equals(jogo.getFase()) && jogo.isTerminado()) {
                String venc = jogo.getVencedora();
                if (venc != null) {
                    vencedores.add(venc);
                }
            }
        }
        return vencedores;
    }

    public List<Jogo> gerarJogos(String fase, List<String> apurados) {
        List<Jogo> jogos = new ArrayList<>();
        for (int i = 0; i + 1 < apurados.size(); i += 2) {
            jogos.add(new Jogo(fase, apurados.get(i), apurados.get(i + 1)));
        }
        return jogos;
    }

    /**
     * Garante que existem os jogos 49 a 64 e atualiza automaticamente as equipas
     * dos jogos seguintes conforme os vencedores já registados.
     */
    public List<Jogo> garantirArvoreEliminatoria() {
        ArrayList<Jogo> todos = RepositorioDados.carregarJogos();
        if (todos == null) todos = new ArrayList<>();

        boolean alterado = removerEliminatoriasAntigasSemNumero(todos);
        Map<Integer, Jogo> mapa = mapearPorNumero(todos);
        Map<String, List<String>> classificados = classificadosPorGrupo(todos);

        // Oitavos: ordem compatível com o print da árvore automática.
        String[][] oitavos = paresOitavos(classificados);
        for (int i = 0; i < oitavos.length; i++) {
            alterado |= garantirJogo(todos, mapa, 49 + i, FASE_OITAVOS, oitavos[i][0], oitavos[i][1]);
        }

        // Quartos.
        alterado |= garantirJogo(todos, mapa, 57, FASE_QUARTOS, vencedorOuPlaceholder(mapa, 49), vencedorOuPlaceholder(mapa, 50));
        alterado |= garantirJogo(todos, mapa, 58, FASE_QUARTOS, vencedorOuPlaceholder(mapa, 53), vencedorOuPlaceholder(mapa, 54));
        alterado |= garantirJogo(todos, mapa, 59, FASE_QUARTOS, vencedorOuPlaceholder(mapa, 51), vencedorOuPlaceholder(mapa, 52));
        alterado |= garantirJogo(todos, mapa, 60, FASE_QUARTOS, vencedorOuPlaceholder(mapa, 55), vencedorOuPlaceholder(mapa, 56));

        // Meias-finais.
        alterado |= garantirJogo(todos, mapa, 61, FASE_MEIAS, vencedorOuPlaceholder(mapa, 57), vencedorOuPlaceholder(mapa, 58));
        alterado |= garantirJogo(todos, mapa, 62, FASE_MEIAS, vencedorOuPlaceholder(mapa, 59), vencedorOuPlaceholder(mapa, 60));

        // 3.º lugar: perdedores das meias.
        alterado |= garantirJogo(todos, mapa, 63, FASE_TERCEIRO, perdedorOuPlaceholder(mapa, 61), perdedorOuPlaceholder(mapa, 62));

        // Final: vencedores das meias.
        alterado |= garantirJogo(todos, mapa, 64, FASE_FINAL, vencedorOuPlaceholder(mapa, 61), vencedorOuPlaceholder(mapa, 62));

        ordenarJogosParaGravacao(todos);
        if (alterado) {
            RepositorioDados.guardarJogos(todos);
        }

        sincronizarCalendarioEliminatorias(todos);
        return filtrarEliminatorias(todos);
    }


    private boolean removerEliminatoriasAntigasSemNumero(ArrayList<Jogo> jogos) {
        boolean removeu = false;
        for (int i = jogos.size() - 1; i >= 0; i--) {
            Jogo jogo = jogos.get(i);
            if (jogo.getNumero() <= 0 && isFaseEliminatoria(jogo.getFase())) {
                jogos.remove(i);
                removeu = true;
            }
        }
        return removeu;
    }

    private Map<Integer, Jogo> mapearPorNumero(List<Jogo> jogos) {
        Map<Integer, Jogo> mapa = new HashMap<>();
        for (Jogo jogo : jogos) {
            if (jogo.getNumero() > 0) {
                mapa.put(jogo.getNumero(), jogo);
            }
        }
        return mapa;
    }

    private String[][] paresOitavos(Map<String, List<String>> classificados) {
        return new String[][]{
                {posicao(classificados, "GRUPO A", 0), posicao(classificados, "GRUPO B", 1)},
                {posicao(classificados, "GRUPO C", 0), posicao(classificados, "GRUPO D", 1)},
                {posicao(classificados, "GRUPO B", 0), posicao(classificados, "GRUPO A", 1)},
                {posicao(classificados, "GRUPO D", 0), posicao(classificados, "GRUPO C", 1)},
                {posicao(classificados, "GRUPO E", 0), posicao(classificados, "GRUPO F", 1)},
                {posicao(classificados, "GRUPO G", 0), posicao(classificados, "GRUPO H", 1)},
                {posicao(classificados, "GRUPO F", 0), posicao(classificados, "GRUPO E", 1)},
                {posicao(classificados, "GRUPO H", 0), posicao(classificados, "GRUPO G", 1)}
        };
    }

    private String posicao(Map<String, List<String>> classificados, String grupo, int posicao) {
        List<String> lista = classificados.get(grupo);
        if (lista != null && lista.size() > posicao) {
            return lista.get(posicao);
        }
        return (posicao + 1) + ".º " + grupo;
    }

    private boolean garantirJogo(
            ArrayList<Jogo> todos,
            Map<Integer, Jogo> mapa,
            int numero,
            String fase,
            String equipaA,
            String equipaB
    ) {
        Jogo jogo = mapa.get(numero);
        if (jogo == null) {
            jogo = new Jogo(fase, equipaA, equipaB);
            jogo.setNumero(numero);
            jogo.setTerminado(false);
            todos.add(jogo);
            mapa.put(numero, jogo);
            return true;
        }

        boolean alterado = false;
        if (jogo.getNumero() != numero) {
            jogo.setNumero(numero);
            alterado = true;
        }
        if (!fase.equals(jogo.getFase())) {
            jogo.setFase(fase);
            alterado = true;
        }

        // Só alteramos automaticamente equipas de jogos ainda não terminados.
        // Assim, um resultado já validado não é apagado por engano.
        if (!jogo.isTerminado()) {
            if (!textoIgual(jogo.getEquipaA(), equipaA) || !textoIgual(jogo.getEquipaB(), equipaB)) {
                jogo.setEquipaA(equipaA);
                jogo.setEquipaB(equipaB);
                jogo.setGolosA(0);
                jogo.setGolosB(0);
                jogo.setVencedorDesempate(null);
                jogo.getMarcadores().clear();
                alterado = true;
            }
        }

        return alterado;
    }

    private boolean textoIgual(String a, String b) {
        if (a == null) return b == null;
        return a.equals(b);
    }

    private String vencedorOuPlaceholder(Map<Integer, Jogo> mapa, int numero) {
        Jogo jogo = mapa.get(numero);
        String vencedor = jogo == null ? null : jogo.getVencedora();
        return vencedor == null ? "Vencedor Jogo " + numero : vencedor;
    }

    private String perdedorOuPlaceholder(Map<Integer, Jogo> mapa, int numero) {
        Jogo jogo = mapa.get(numero);
        String perdedor = jogo == null ? null : jogo.getPerdedora();
        return perdedor == null ? "Perdedor Jogo " + numero : perdedor;
    }

    private List<Jogo> filtrarEliminatorias(List<Jogo> todos) {
        List<Jogo> resultado = new ArrayList<>();
        for (Jogo jogo : todos) {
            if (isFaseEliminatoria(jogo.getFase())) {
                resultado.add(jogo);
            }
        }
        resultado.sort(Comparator.comparingInt(Jogo::getNumero));
        return resultado;
    }

    private void ordenarJogosParaGravacao(ArrayList<Jogo> jogos) {
        jogos.sort((a, b) -> {
            boolean aElim = a.getNumero() > 0;
            boolean bElim = b.getNumero() > 0;
            if (aElim && bElim) return Integer.compare(a.getNumero(), b.getNumero());
            if (aElim) return 1;
            if (bElim) return -1;
            int fase = String.valueOf(a.getFase()).compareTo(String.valueOf(b.getFase()));
            if (fase != 0) return fase;
            int equipaA = String.valueOf(a.getEquipaA()).compareTo(String.valueOf(b.getEquipaA()));
            if (equipaA != 0) return equipaA;
            return String.valueOf(a.getEquipaB()).compareTo(String.valueOf(b.getEquipaB()));
        });
    }

    /**
     * Mantém o calendário público coerente com a árvore eliminatória.
     * Remove entradas antigas das eliminatórias e recria apenas jogos com equipas reais.
     */
    private void sincronizarCalendarioEliminatorias(List<Jogo> todos) {
        ArrayList<JogoCalendario> calendario = RepositorioDados.carregarJogosCalendario();
        if (calendario == null) calendario = new ArrayList<>();

        Map<String, JogoCalendario> antigos = new HashMap<>();
        ArrayList<JogoCalendario> apenasGrupos = new ArrayList<>();

        for (JogoCalendario jogo : calendario) {
            if (isFaseEliminatoria(jogo.getGrupo())) {
                antigos.put(chaveCalendario(jogo.getGrupo(), jogo.getEquipaA(), jogo.getEquipaB()), jogo);
            } else {
                apenasGrupos.add(jogo);
            }
        }

        LocalDate dataBase = dataBaseProximaJornada(apenasGrupos);
        int indice = 0;

        List<Jogo> eliminatorias = filtrarEliminatorias(todos);
        for (Jogo jogo : eliminatorias) {
            if (isPlaceholder(jogo.getEquipaA()) || isPlaceholder(jogo.getEquipaB())) {
                continue;
            }

            String chave = chaveCalendario(jogo.getFase(), jogo.getEquipaA(), jogo.getEquipaB());
            JogoCalendario existente = antigos.get(chave);
            if (existente != null) {
                apenasGrupos.add(existente);
            } else {
                apenasGrupos.add(criarJogoCalendario(
                        jogo.getFase(),
                        jogo.getEquipaA(),
                        jogo.getEquipaB(),
                        dataBase.plusDays(indice / 2)
                ));
            }
            indice++;
        }

        RepositorioDados.guardarJogosCalendario(apenasGrupos);
    }

    private String chaveCalendario(String fase, String equipaA, String equipaB) {
        return fase + "|" + equipaA + "|" + equipaB;
    }

    /** Compatibilidade com o código antigo: agora também cria/atualiza jogos.dat. */
    public List<JogoCalendario> gerarOitavos() {
        garantirArvoreEliminatoria();
        ArrayList<JogoCalendario> calendario = RepositorioDados.carregarJogosCalendario();
        List<JogoCalendario> oitavos = new ArrayList<>();
        for (JogoCalendario jogo : calendario) {
            if (FASE_OITAVOS.equals(jogo.getGrupo())) {
                oitavos.add(jogo);
            }
        }
        return oitavos;
    }

    // ------------------------------------------------------------------ getters

    public List<Equipa> getEquipas() { return equipas; }
    public List<Grupo> getGrupos() { return grupos; }
    public List<Estadio> getEstadios() { return estadios; }
    public List<JogoCalendario> getJogosGrupos() { return jogosGrupos; }
    public List<Jogo> getJogosEliminatorios() { return jogosEliminatorios; }
}
