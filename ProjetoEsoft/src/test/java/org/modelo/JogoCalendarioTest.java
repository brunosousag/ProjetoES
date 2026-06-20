package org.modelo;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Área: JANELA PRINCIPAL.
 *
 * Requisito: o método isEsgotado() de um jogo do calendário deve devolver
 * true exactamente quando os bilhetes vendidos atingem a capacidade total
 * (e os bilhetes disponíveis ficam a 0). É este método que sustenta o
 * filtro "Disponível" da janela principal — se a verificação falhar, o
 * filtro mostra jogos esgotados ou esconde jogos que ainda têm lugares.
 */
public class JogoCalendarioTest {

    @Test
    public void testIsEsgotadoQuandoBilhetesAtingemCapacidade() {
        JogoCalendario jogo = new JogoCalendario(
                "Oitavos de final",
                "Portugal",
                "Brasil",
                LocalDate.of(2026, 7, 1),
                LocalTime.of(20, 0),
                "Estádio de Teste",
                1000,    // capacidade total
                0,       // bilhetes vendidos
                40.0     // preço
        );

        // Estado inicial: nenhum vendido -> não esgotado e 1000 disponíveis.
        assertFalse(jogo.isEsgotado(),
                "Um jogo sem bilhetes vendidos não pode estar esgotado");
        assertEquals(1000, jogo.getBilhetesDisponiveis(),
                "Sem vendas, os bilhetes disponíveis devem ser iguais à capacidade");

        // Quase cheio (999/1000): ainda há 1 lugar — NÃO esgotado.
        jogo.setBilhetesVendidos(999);
        assertFalse(jogo.isEsgotado(),
                "Com 1 lugar ainda livre o jogo NÃO está esgotado");
        assertEquals(1, jogo.getBilhetesDisponiveis());

        // Capacidade atingida (1000/1000): esgotado e 0 disponíveis.
        jogo.setBilhetesVendidos(1000);
        assertTrue(jogo.isEsgotado(),
                "Quando os vendidos igualam a capacidade o jogo deve estar esgotado");
        assertEquals(0, jogo.getBilhetesDisponiveis(),
                "Bilhetes disponíveis devem ser 0 quando o jogo está esgotado");
    }
}
