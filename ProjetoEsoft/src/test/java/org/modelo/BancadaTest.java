package org.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Área: VENDA DE BILHETES.
 *
 * Requisito: o número de lugares disponíveis de uma bancada tem de reflectir
 * sempre as vendas já feitas e nunca pode ficar negativo, mesmo que se peça
 * uma venda maior do que o que ainda está livre — a bancada limita-se a
 * esgotar.
 */
public class BancadaTest {

    @Test
    public void testDisponibilidadeReflecteVendasENuncaVaiNegativo() {
        Bancada bancada = new Bancada(
                "Bancada de Teste",
                Bancada.Setor.NORTE,
                Bancada.Piso.INFERIOR,
                1000,    // capacidade total
                1.0      // multiplicador de preço
        );

        // Estado inicial: tudo livre, não esgotada.
        assertEquals(1000, bancada.getLugaresDisponiveis(),
                "Bancada acabada de criar deve ter todos os lugares livres");
        assertFalse(bancada.isEsgotada(),
                "Bancada vazia nunca pode estar esgotada");

        // Venda parcial: sobram 700.
        bancada.venderLugares(300);
        assertEquals(700, bancada.getLugaresDisponiveis(),
                "Após vender 300 lugares devem sobrar 700");
        assertFalse(bancada.isEsgotada());

        // Tentativa de vender muito mais que o disponível:
        // a bancada esgota mas a disponibilidade não pode ir a negativo.
        bancada.venderLugares(5000);
        assertEquals(0, bancada.getLugaresDisponiveis(),
                "Lugares disponíveis nunca podem ser um número negativo");
        assertTrue(bancada.isEsgotada(),
                "Quando a capacidade é atingida a bancada deve marcar-se como esgotada");
    }
}
