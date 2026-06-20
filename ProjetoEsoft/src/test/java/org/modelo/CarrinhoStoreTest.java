package org.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Área: CARRINHO.
 *
 * Requisito: adicionar duas vezes o mesmo bilhete (mesmo jogo + mesma bancada)
 * NÃO deve criar dois items distintos no carrinho — em vez disso, a quantidade
 * do item existente é incrementada. Esta regra garante que o utilizador pode
 * voltar atrás e juntar mais bilhetes sem o carrinho inchar com linhas
 * repetidas, e é a base do cálculo "já no carrinho" usado pelo limite.
 */
public class CarrinhoStoreTest {

    private CarrinhoStore carrinho;
    private JogoCalendario jogo;
    private Bancada bancada;

    @BeforeEach
    public void setUp() {
        // O CarrinhoStore é um singleton — o seu estado sobrevive entre testes.
        // Por isso esvaziamos o carrinho antes de cada teste para começar limpo.
        carrinho = CarrinhoStore.getInstance();
        carrinho.limpar();

        jogo = new JogoCalendario(
                "Oitavos de final",
                "Portugal",
                "Brasil",
                LocalDate.of(2026, 6, 20),
                LocalTime.of(20, 0),
                "Estádio de Teste",
                50000,    // capacidade total do jogo
                0,        // bilhetes vendidos
                40.0      // preço base do bilhete
        );

        bancada = new Bancada(
                "Norte",
                Bancada.Setor.NORTE,
                Bancada.Piso.INFERIOR,
                1000,
                1.0
        );
    }

    @Test
    public void testAdicionarMesmoBilheteSomaQuantidadesEmUnicoItem() {
        // 1ª adição: 3 bilhetes para Portugal vs Brasil na bancada Norte.
        carrinho.adicionarBilhete(jogo, bancada, 3);

        // 2ª adição: utilizador volta atrás e mete mais 2 do MESMO jogo+bancada.
        carrinho.adicionarBilhete(jogo, bancada, 2);

        // O carrinho deve ter UM ÚNICO item (a 2ª adição soma à 1ª, não duplica).
        assertEquals(1, carrinho.getItens().size(),
                "Adicionar o mesmo jogo+bancada duas vezes não pode criar items duplicados");

        // E a quantidade total para esse par jogo+bancada deve ser a soma: 3+2 = 5.
        assertEquals(5, carrinho.getQuantidadeBilhete(jogo, bancada),
                "A quantidade total deve ser a soma das duas adições (3 + 2 = 5)");
    }
}
