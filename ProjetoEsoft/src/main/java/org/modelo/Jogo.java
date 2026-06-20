package org.modelo;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Jogo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Número oficial do jogo na árvore eliminatória.
     * Na fase de grupos fica a 0, porque esses jogos são registados manualmente.
     */
    private int numero;

    private String fase;                      // "GRUPO A", "Oitavos de final", "Quartos de final", ...
    private String equipaA;
    private String equipaB;
    private int golosA;
    private int golosB;
    private Map<String, Integer> marcadores;  // nome do jogador -> golos marcados neste jogo
    private String vencedorDesempate;         // equipa que avança em caso de empate, por exemplo por penáltis
    private boolean terminado;

    public Jogo(String fase, String equipaA, String equipaB) {
        this.fase = fase;
        this.equipaA = equipaA;
        this.equipaB = equipaB;
        this.marcadores = new LinkedHashMap<>();
    }

    public int getNumero() { return numero; }
    public String getFase() { return fase; }
    public String getEquipaA() { return equipaA; }
    public String getEquipaB() { return equipaB; }
    public int getGolosA() { return golosA; }
    public int getGolosB() { return golosB; }

    public Map<String, Integer> getMarcadores() {
        if (marcadores == null) {
            marcadores = new LinkedHashMap<>();
        }
        return marcadores;
    }

    public boolean isTerminado() { return terminado; }
    public String getVencedorDesempate() { return vencedorDesempate; }

    public void setNumero(int numero) { this.numero = numero; }
    public void setFase(String fase) { this.fase = fase; }
    public void setEquipaA(String equipaA) { this.equipaA = equipaA; }
    public void setEquipaB(String equipaB) { this.equipaB = equipaB; }
    public void setGolosA(int golosA) { this.golosA = golosA; }
    public void setGolosB(int golosB) { this.golosB = golosB; }
    public void setMarcadores(Map<String, Integer> marcadores) {
        this.marcadores = marcadores == null ? new LinkedHashMap<>() : marcadores;
    }
    public void setVencedorDesempate(String vencedorDesempate) { this.vencedorDesempate = vencedorDesempate; }
    public void setTerminado(boolean terminado) { this.terminado = terminado; }

    /** Equipa vencedora do jogo, ou null se ainda não houver vencedor definido. */
    public String getVencedora() {
        if (!terminado) return null;
        if (golosA > golosB) return equipaA;
        if (golosB > golosA) return equipaB;
        return vencedorDesempate;
    }

    /** Equipa derrotada do jogo, ou null se ainda não houver derrotado definido. */
    public String getPerdedora() {
        String vencedora = getVencedora();
        if (vencedora == null) return null;
        if (vencedora.equals(equipaA)) return equipaB;
        if (vencedora.equals(equipaB)) return equipaA;
        return null;
    }

    public boolean isEmpate() {
        return golosA == golosB;
    }

    @Override
    public String toString() {
        String prefixo = numero > 0 ? "Jogo " + numero + "  ·  " : "";
        String txt = prefixo + fase + "  ·  " + equipaA + " " + golosA + "–" + golosB + " " + equipaB;

        String venc = getVencedora();
        if (terminado && isEmpate() && venc != null) {
            txt += "  ·  🏆 " + venc;
        } else if (terminado && venc != null) {
            txt += "  ·  🏆 " + venc;
        } else if (numero > 0) {
            txt += "  ·  vencedor: a definir";
        }
        return txt;
    }
}
