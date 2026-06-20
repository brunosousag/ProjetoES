package org.modelo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Guarda o perfil do utilizador atual (Vendedor / Gestor) de forma global,
 * para que todas as janelas abertas partilhem o mesmo estado.
 *
 * Por agora não há login nem password: serve apenas para alternar entre os
 * dois perfis e controlar o que cada um pode ver.
 */
public final class Sessao {

    public enum Perfil {
        VENDEDOR,
        GESTOR
    }

    /** A aplicação arranca sempre como Vendedor. */
    private static Perfil perfilAtual = Perfil.VENDEDOR;

    private static final List<PerfilListener> listeners = new CopyOnWriteArrayList<>();

    private Sessao() {
    }

    public static Perfil getPerfil() {
        return perfilAtual;
    }

    public static boolean isGestor() {
        return perfilAtual == Perfil.GESTOR;
    }

    public static void setPerfil(Perfil novoPerfil) {
        if (novoPerfil == null || novoPerfil == perfilAtual) {
            return;
        }
        perfilAtual = novoPerfil;
        for (PerfilListener listener : listeners) {
            listener.perfilAlterado(perfilAtual);
        }
    }

    public static void registarListener(PerfilListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public static void removerListener(PerfilListener listener) {
        listeners.remove(listener);
    }

    /** Notificado sempre que o perfil ativo muda. */
    public interface PerfilListener {
        void perfilAlterado(Perfil novoPerfil);
    }
}