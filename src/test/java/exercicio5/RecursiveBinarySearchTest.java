package exercicio5;

import exercicio5.algorithms.RecursiveBinarySearch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Exercício 5: Análise Estrutural de Código
 * 
 * Testes white-box para RecursiveBinarySearch do repositório TheAlgorithms/Java.
 * Cobre decisões, ramificações e diferentes execuções da funcionalidade.
 * 
 * Código fonte: https://github.com/TheAlgorithms/Java/blob/master/src/main/java/com/thealgorithms/searches/RecursiveBinarySearch.java
 */
public class RecursiveBinarySearchTest {

    // ========== TESTES DE DECISÕES E RAMIFICAÇÕES ==========
    
    @Test
    @DisplayName("Deve encontrar elemento quando está no meio (comparison == 0)")
    void deveEncontrarElementoNoMeio() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {1, 2, 3, 4, 5};
        int index = searcher.find(array, 3);
        assertThat(index).isEqualTo(2);
    }
    
    @Test
    @DisplayName("Deve encontrar elemento no início do array (ramificação esquerda)")
    void deveEncontrarElementoNoInicio() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {1, 2, 3, 4, 5};
        int index = searcher.find(array, 1);
        assertThat(index).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Deve encontrar elemento no final do array (ramificação direita)")
    void deveEncontrarElementoNoFinal() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {1, 2, 3, 4, 5};
        int index = searcher.find(array, 5);
        assertThat(index).isEqualTo(4);
    }
    
    @Test
    @DisplayName("Deve retornar -1 quando elemento não existe (right < left)")
    void deveRetornarMenosUmQuandoNaoEncontrado() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {1, 2, 3, 4, 5};
        int index = searcher.find(array, 99);
        assertThat(index).isEqualTo(-1);
    }
    
    @Test
    @DisplayName("Deve buscar na subárvore esquerda quando comparison > 0")
    void deveBuscarNaSubarvoreEsquerda() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {1, 3, 5, 7, 9, 11, 13};
        // Busca por 3, que está à esquerda do meio (7)
        int index = searcher.find(array, 3);
        assertThat(index).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Deve buscar na subárvore direita quando comparison < 0")
    void deveBuscarNaSubarvoreDireita() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {1, 3, 5, 7, 9, 11, 13};
        // Busca por 11, que está à direita do meio (7)
        int index = searcher.find(array, 11);
        assertThat(index).isEqualTo(5);
    }

    // ========== TESTES DE VALORES LIMITE ==========
    
    @Test
    @DisplayName("Deve funcionar com array de um único elemento")
    void deveFuncionarComArrayDeUmElemento() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {42};
        int index = searcher.find(array, 42);
        assertThat(index).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Deve retornar -1 para array vazio")
    void deveRetornarMenosUmParaArrayVazio() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {};
        int index = searcher.find(array, 1);
        assertThat(index).isEqualTo(-1);
    }
    
    @Test
    @DisplayName("Deve retornar -1 para array null")
    void deveRetornarMenosUmParaArrayNull() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = null;
        int index = searcher.find(array, 1);
        assertThat(index).isEqualTo(-1);
    }
    
    @Test
    @DisplayName("Deve funcionar com array de dois elementos")
    void deveFuncionarComArrayDeDoisElementos() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {10, 20};
        
        assertThat(searcher.find(array, 10)).isEqualTo(0);
        assertThat(searcher.find(array, 20)).isEqualTo(1);
        assertThat(searcher.find(array, 15)).isEqualTo(-1);
    }

    // ========== TESTES DE DIFERENTES EXECUÇÕES ==========
    
    @Test
    @DisplayName("Deve funcionar com array grande (múltiplas recursões)")
    void deveFuncionarComArrayGrande() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = new Integer[1000];
        for (int i = 0; i < 1000; i++) {
            array[i] = i * 2; // Array: 0, 2, 4, 6, ..., 1998
        }
        
        // Busca no início
        assertThat(searcher.find(array, 0)).isEqualTo(0);
        // Busca no meio
        assertThat(searcher.find(array, 1000)).isEqualTo(500);
        // Busca no final
        assertThat(searcher.find(array, 1998)).isEqualTo(999);
        // Busca elemento inexistente
        assertThat(searcher.find(array, 1999)).isEqualTo(-1);
    }
    
    @Test
    @DisplayName("Deve cobrir caminho quando elemento está logo após o meio")
    void deveCobrirCaminhoElementoAposMeio() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {1, 2, 3, 4, 5, 6, 7};
        // Meio é 4 (índice 3), busca 5 que está imediatamente após
        int index = searcher.find(array, 5);
        assertThat(index).isEqualTo(4);
    }
    
    @Test
    @DisplayName("Deve cobrir caminho quando elemento está logo antes do meio")
    void deveCobrirCaminhoElementoAntesMeio() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {1, 2, 3, 4, 5, 6, 7};
        // Meio é 4 (índice 3), busca 3 que está imediatamente antes
        int index = searcher.find(array, 3);
        assertThat(index).isEqualTo(2);
    }

    // ========== TESTES DE COBERTURA DE RAMOS ==========
    
    @ParameterizedTest
    @CsvSource({
        "1, 0",    // Primeiro elemento
        "5, 4",    // Último elemento
        "3, 2",    // Elemento do meio
        "2, 1",    // Segundo elemento
        "4, 3"     // Quarto elemento
    })
    @DisplayName("Deve encontrar todos os elementos em array ordenado")
    void deveEncontrarTodosElementos(Integer target, int expectedIndex) {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {1, 2, 3, 4, 5};
        int index = searcher.find(array, target);
        assertThat(index).isEqualTo(expectedIndex);
    }
    
    @ParameterizedTest
    @CsvSource({
        "0, -1",   // Menor que todos
        "6, -1",   // Maior que todos
        "-1, -1"   // Negativo
    })
    @DisplayName("Deve retornar -1 para elementos fora do range")
    void deveRetornarMenosUmParaElementosForaRange(Integer target, int expectedIndex) {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {1, 2, 3, 4, 5};
        int index = searcher.find(array, target);
        assertThat(index).isEqualTo(expectedIndex);
    }

    // ========== TESTES COM DIFERENTES TIPOS ==========
    
    @Test
    @DisplayName("Deve funcionar com String")
    void deveFuncionarComString() {
        RecursiveBinarySearch<String> searcher = new RecursiveBinarySearch<>();
        String[] array = {"apple", "banana", "cherry", "date", "elderberry"};
        assertThat(searcher.find(array, "cherry")).isEqualTo(2);
        assertThat(searcher.find(array, "zebra")).isEqualTo(-1);
    }
    
    @Test
    @DisplayName("Deve funcionar com Double")
    void deveFuncionarComDouble() {
        RecursiveBinarySearch<Double> searcher = new RecursiveBinarySearch<>();
        Double[] array = {1.1, 2.2, 3.3, 4.4, 5.5};
        assertThat(searcher.find(array, 3.3)).isEqualTo(2);
        assertThat(searcher.find(array, 2.5)).isEqualTo(-1);
    }

    // ========== TESTES DE COBERTURA DE DECISÕES ==========
    
    @Test
    @DisplayName("Deve cobrir caso quando right == left (último elemento)")
    void deveCobrirCasoRightIgualLeft() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {1, 2, 3};
        // Busca que força right == left
        int index = searcher.find(array, 2);
        assertThat(index).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Deve cobrir todos os caminhos de comparação")
    void deveCobrirTodosCaminhosComparacao() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        Integer[] array = {10, 20, 30, 40, 50, 60, 70};
        
        // comparison == 0: elemento encontrado
        assertThat(searcher.find(array, 40)).isEqualTo(3);
        
        // comparison > 0: busca à esquerda
        assertThat(searcher.find(array, 20)).isEqualTo(1);
        
        // comparison < 0: busca à direita
        assertThat(searcher.find(array, 60)).isEqualTo(5);
    }
    
    @Test
    @DisplayName("Deve testar múltiplas chamadas recursivas")
    void deveTestarMultiplasChamadasRecursivas() {
        RecursiveBinarySearch<Integer> searcher = new RecursiveBinarySearch<>();
        // Array que força várias chamadas recursivas
        Integer[] array = {1, 5, 9, 13, 17, 21, 25, 29, 33, 37};
        
        // Busca elementos que requerem múltiplas recursões
        assertThat(searcher.find(array, 1)).isEqualTo(0);   // Múltiplas recursões à esquerda
        assertThat(searcher.find(array, 37)).isEqualTo(9);  // Múltiplas recursões à direita
        assertThat(searcher.find(array, 17)).isEqualTo(4);  // Múltiplas recursões ao centro
    }
}

