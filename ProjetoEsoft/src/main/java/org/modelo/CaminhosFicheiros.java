package org.modelo;

import java.io.File;

public class CaminhosFicheiros {

    public static final String PASTA_DADOS = escolherPastaDados();

    /**
     * O projecto aparece dentro da pasta ProjetoEsoft, mas às vezes o IntelliJ
     * executa a aplicação a partir da pasta exterior do projecto.
     *
     * Se existir ProjetoEsoft/dados, usamos essa pasta. Caso contrário, usamos
     * simplesmente dados. Isto evita carregar ficheiros .dat antigos da pasta
     * errada, o que fazia alguns jogadores aparecerem associados à equipa errada.
     */
    private static String escolherPastaDados() {
        File pastaDadosDentroDoModulo = new File("ProjetoEsoft/dados");

        if (pastaDadosDentroDoModulo.exists() && pastaDadosDentroDoModulo.isDirectory()) {
            return "ProjetoEsoft/dados";
        }

        return "dados";
    }

    public static final String FICHEIRO_EQUIPAS =
            PASTA_DADOS + "/equipas.dat";

    public static final String FICHEIRO_PRODUTOS =
            PASTA_DADOS + "/produtos.dat";

    public static final String FICHEIRO_JOGADORES =
            PASTA_DADOS + "/jogadores.dat";

    public static final String FICHEIRO_ARBITROS =
            PASTA_DADOS + "/arbitros.dat";

    public static final String FICHEIRO_JOGOS =
            PASTA_DADOS + "/jogos.dat";

    public static final String FICHEIRO_JOGOS_CALENDARIO =
            PASTA_DADOS + "/jogos_calendario.dat";

    public static final String FICHEIRO_GRUPOS =
            PASTA_DADOS + "/grupos.dat";

    public static final String FICHEIRO_ESTADIOS =
            PASTA_DADOS + "/estadios.dat";

    public static final String FICHEIRO_BANCADAS =
            PASTA_DADOS + "/bancadas.dat";

    public static final String FICHEIRO_VENDAS =
            PASTA_DADOS + "/vendas.dat";
}
