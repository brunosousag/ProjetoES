package org.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Um grupo do mundial (ex.: "GRUPO A") e a sua composição.
 *
 * Contém APENAS os nomes das equipas — nada de logística (alojamento,
 * deslocação, etc.). Esses dados vivem na classe Equipa, criada na
 * aplicação. A ligação entre as duas faz-se pelo nome da equipa
 * (mais tarde, se necessário, por um id).
 */
public class Grupo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;                 // "GRUPO A"
    private List<String> equipas;        // só os nomes das equipas

    public Grupo(String nome) {
        this.nome = nome;
        this.equipas = new ArrayList<>();
    }

    public Grupo(String nome, List<String> equipas) {
        this.nome = nome;
        this.equipas = new ArrayList<>(equipas);
    }

    public String getNome() {
        return nome;
    }

    public List<String> getEquipas() {
        return equipas;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEquipas(List<String> equipas) {
        this.equipas = equipas;
    }

    public void adicionarEquipa(String nomeEquipa) {
        equipas.add(nomeEquipa);
    }

    public boolean contemEquipa(String nomeEquipa) {
        return equipas.contains(nomeEquipa);
    }

    @Override
    public String toString() {
        return nome + ": " + String.join(", ", equipas);
    }
}