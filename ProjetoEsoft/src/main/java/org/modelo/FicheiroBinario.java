package org.modelo;

import java.io.*;
import java.util.ArrayList;

public class FicheiroBinario {

    public static <T> void guardarLista(String caminhoFicheiro, ArrayList<T> lista) {
        criarPastaSeNaoExistir(caminhoFicheiro);

        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(caminhoFicheiro))) {

            output.writeObject(lista);

        } catch (IOException e) {
            System.out.println("Erro ao guardar ficheiro: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> carregarLista(String caminhoFicheiro) {
        File ficheiro = new File(caminhoFicheiro);

        if (!ficheiro.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(ficheiro))) {

            return (ArrayList<T>) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar ficheiro: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static void criarPastaSeNaoExistir(String caminhoFicheiro) {
        File ficheiro = new File(caminhoFicheiro);
        File pasta = ficheiro.getParentFile();

        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs();
        }
    }
}