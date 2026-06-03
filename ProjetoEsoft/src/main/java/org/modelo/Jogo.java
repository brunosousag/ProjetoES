package org.modelo;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Jogo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fase;                      // "Oitavos de final", "Quartos de final", ...
    private String equipaA;
    private String equipaB;
    private int golosA;
    private int golosB;
    private Map<String, Integer> marcadores;  // nome do jogador -> golos marcados neste jogo
    private String vencedorDesempate;         // equipa que avança em caso de empate (penáltis)
    private boolean terminado;

    public Jogo(String fase, String equipaA, String equipaB) {
        this.fase = fase;
        this.equipaA = equipaA;
        this.equipaB = equipaB;
        this.marcadores = new LinkedHashMap<>();
    }

    public String getFase() { return fase; }
    public String getEquipaA() { return equipaA; }
    public String getEquipaB() { return equipaB; }
    public int getGolosA() { return golosA; }
    public int getGolosB() { return golosB; }
    public Map<String, Integer> getMarcadores() { return marcadores; }
    public boolean isTerminado() { return terminado; }
    public String getVencedorDesempate() { return vencedorDesempate; }

    public void setFase(String fase) { this.fase = fase; }
    public void setEquipaA(String equipaA) { this.equipaA = equipaA; }
    public void setEquipaB(String equipaB) { this.equipaB = equipaB; }
    public void setGolosA(int golosA) { this.golosA = golosA; }
    public void setGolosB(int golosB) { this.golosB = golosB; }
    public void setMarcadores(Map<String, Integer> marcadores) { this.marcadores = marcadores; }
    public void setVencedorDesempate(String vencedorDesempate) { this.vencedorDesempate = vencedorDesempate; }
    public void setTerminado(boolean terminado) { this.terminado = terminado; }

    /** Equipa vencedora do jogo, ou null se ainda empatado sem desempate definido. */
    public String getVencedora() {
        if (golosA > golosB) return equipaA;
        if (golosB > golosA) return equipaB;
        return vencedorDesempate;   // empate -> decidido nos penáltis (pode ser null)
    }

    public boolean isEmpate() {
        return golosA == golosB;
    }

    @Override
    public String toString() {
        String venc = getVencedora();
        String txt = fase + "  ·  " + equipaA + " " + golosA + "–" + golosB + " " + equipaB;
        if (isEmpate() && venc != null) {
            txt += "  (" + venc + " nos penáltis)";
        } else if (venc != null) {
            txt += "  ·  🏆 " + venc;
        }
        return txt;
    }
}
