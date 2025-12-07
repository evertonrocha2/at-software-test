package exercicio2;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import net.jqwik.api.statistics.Statistics;
import org.assertj.core.api.Assertions;
import exercicio2.MathFunctions;
import exercicio2.MockMathLogger;

import java.util.Arrays;

/**
 * Exercício 2: Testes Baseados em Propriedades e Simulação de Dependências
 * 
 * Testes usando jqwik para validar propriedades matemáticas dos métodos.
 */
public class MathFunctionsPropertyTest {

    private final MockMathLogger logger = new MockMathLogger();

    // ========== PROPRIEDADE 1: MultiplyByTwo sempre retorna número par ==========
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("MultiplyByTwo: O resultado deve ser sempre par")
    void multiplyByTwoShouldAlwaysReturnEvenNumber(@ForAll @IntRange(min = Integer.MIN_VALUE / 2, max = Integer.MAX_VALUE / 2) int number) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // When
        int result = mathFunctions.multiplyByTwo(number);
        
        // Then - Resultado de multiplicar por 2 é sempre par
        Assertions.assertThat(result % 2).as("Resultado de multiplicar por 2 deve ser sempre par")
                .isEqualTo(0);
    }
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("MultiplyByTwo: Resultado deve ser igual a 2 * número")
    void multiplyByTwoShouldEqualTwoTimesNumber(@ForAll @IntRange(min = Integer.MIN_VALUE / 2, max = Integer.MAX_VALUE / 2) int number) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // When
        int result = mathFunctions.multiplyByTwo(number);
        
        // Then
        Assertions.assertThat(result).as("Resultado deve ser igual a 2 * %d", number)
                .isEqualTo(2 * number);
    }
    
    // ========== PROPRIEDADE 2: GenerateMultiplicationTable - todos são múltiplos ==========
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("GenerateMultiplicationTable: Todos os elementos devem ser múltiplos do número original")
    void generateMultiplicationTableShouldReturnMultiplesOfNumber(
            @ForAll @IntRange(min = -1000, max = 1000) int number,
            @ForAll @IntRange(min = 1, max = 50) int limit) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // Previne overflow
        Assume.that(Math.abs(number) <= Integer.MAX_VALUE / limit);
        
        // When
        int[] table = mathFunctions.generateMultiplicationTable(number, limit);
        
        // Then - Todos os elementos são múltiplos do número (exceto quando number == 0)
        if (number == 0) {
            Assertions.assertThat(table).containsOnly(0);
        } else {
            for (int i = 0; i < table.length; i++) {
                Assertions.assertThat(table[i] % number)
                        .as("Elemento %d da tabela (%d) deve ser múltiplo de %d", i, table[i], number)
                        .isEqualTo(0);
            }
        }
    }
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("GenerateMultiplicationTable: Tabela deve ter tamanho igual ao limit")
    void generateMultiplicationTableShouldHaveCorrectSize(
            @ForAll @IntRange(min = -100, max = 100) int number,
            @ForAll @IntRange(min = 1, max = 20) int limit) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // When
        int[] table = mathFunctions.generateMultiplicationTable(number, limit);
        
        // Then
        Assertions.assertThat(table).as("Tabela deve ter tamanho %d", limit)
                .hasSize(limit);
    }
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("GenerateMultiplicationTable: Elementos devem estar em ordem crescente para número positivo")
    void generateMultiplicationTableShouldBeAscendingForPositiveNumber(
            @ForAll @IntRange(min = 1, max = 100) int number,
            @ForAll @IntRange(min = 2, max = 20) int limit) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // Previne overflow
        Assume.that(number <= Integer.MAX_VALUE / limit);
        
        // When
        int[] table = mathFunctions.generateMultiplicationTable(number, limit);
        
        // Then - Deve estar em ordem crescente
        for (int i = 0; i < table.length - 1; i++) {
            Assertions.assertThat(table[i])
                    .as("Elemento %d (%d) deve ser menor que elemento %d (%d)", i, table[i], i + 1, table[i + 1])
                    .isLessThan(table[i + 1]);
        }
    }
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("GenerateMultiplicationTable: Primeiro elemento deve ser igual ao número")
    void generateMultiplicationTableFirstElementShouldEqualNumber(
            @ForAll @IntRange(min = -1000, max = 1000) int number,
            @ForAll @IntRange(min = 1, max = 50) int limit) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // When
        int[] table = mathFunctions.generateMultiplicationTable(number, limit);
        
        // Then
        Assertions.assertThat(table[0])
                .as("Primeiro elemento da tabela deve ser igual a %d", number)
                .isEqualTo(number);
    }
    
    // ========== PROPRIEDADE 3: IsPrime - números primos não têm divisores além de 1 e ele mesmo ==========
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("IsPrime: Números primos não devem ter divisores entre 2 e sqrt(n)")
    void isPrimeShouldHaveNoDivisorsBetween2AndSqrt(
            @ForAll("primeNumbers") int prime) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        Assume.that(mathFunctions.isPrime(prime));
        
        // When & Then - Verifica que não há divisores
        for (int i = 2; i <= Math.sqrt(prime); i++) {
            Assertions.assertThat(prime % i)
                    .as("Número primo %d não deve ser divisível por %d", prime, i)
                    .isNotEqualTo(0);
        }
    }
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("IsPrime: Números <= 1 não são primos")
    void isPrimeShouldReturnFalseForNumbersLessOrEqualToOne(
            @ForAll @IntRange(min = -100, max = 1) int number) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // When
        boolean result = mathFunctions.isPrime(number);
        
        // Then
        Assertions.assertThat(result)
                .as("Número %d não deve ser primo (<= 1)", number)
                .isFalse();
    }
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("IsPrime: Números pares maiores que 2 não são primos")
    void isPrimeShouldReturnFalseForEvenNumbersGreaterThanTwo(
            @ForAll("evenNumbersGreaterThanTwo") int number) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // When
        boolean result = mathFunctions.isPrime(number);
        
        // Then
        Assertions.assertThat(result)
                .as("Número par %d maior que 2 não deve ser primo", number)
                .isFalse();
    }
    
    @Provide
    Arbitrary<Integer> primeNumbers() {
        // Números primos conhecidos para testes
        return Arbitraries.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 
                               53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113);
    }
    
    @Provide
    Arbitrary<Integer> evenNumbersGreaterThanTwo() {
        return Arbitraries.integers()
                .between(4, 1000)
                .filter(n -> n % 2 == 0);
    }
    
    // ========== PROPRIEDADE 4: CalculateAverage - média está entre min e max ==========
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("CalculateAverage: O resultado deve estar sempre entre o menor e o maior valor do array")
    void calculateAverageShouldBeBetweenMinAndMax(@ForAll @Size(min = 1, max = 100) int[] numbers) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // When
        double average = mathFunctions.calculateAverage(numbers);
        
        // Then - A média deve estar entre o mínimo e o máximo
        int min = Arrays.stream(numbers).min().orElseThrow();
        int max = Arrays.stream(numbers).max().orElseThrow();
        
        Assertions.assertThat(average)
                .as("Média deve estar entre %d e %d", min, max)
                .isGreaterThanOrEqualTo((double) min)
                .isLessThanOrEqualTo((double) max);
    }
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("CalculateAverage: Média de array com todos elementos iguais deve ser igual ao elemento")
    void calculateAverageShouldEqualElementWhenAllEqual(@ForAll @IntRange(min = -1000, max = 1000) int value,
                                                         @ForAll @IntRange(min = 1, max = 50) int size) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        int[] numbers = new int[size];
        Arrays.fill(numbers, value);
        
        // When
        double average = mathFunctions.calculateAverage(numbers);
        
        // Then
        Assertions.assertThat(average)
                .as("Média de array com todos elementos %d deve ser %d", value, value)
                .isEqualTo((double) value);
    }
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("CalculateAverage: Média deve ser igual à soma dividida pelo tamanho")
    void calculateAverageShouldEqualSumDividedByLength(@ForAll @Size(min = 1, max = 50) int[] numbers) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // Previne overflow na soma
        long sum = Arrays.stream(numbers).mapToLong(i -> (long) i).sum();
        Assume.that(Math.abs(sum) <= Long.MAX_VALUE / 2);
        
        // When
        double average = mathFunctions.calculateAverage(numbers);
        
        // Then
        double expectedAverage = (double) sum / numbers.length;
        Assertions.assertThat(average)
                .as("Média deve ser igual à soma (%d) dividida pelo tamanho (%d)", sum, numbers.length)
                .isCloseTo(expectedAverage, Assertions.offset(0.0001));
    }
    
    // ========== TESTES DE EXCEÇÕES (não são propriedades, mas são necessários) ==========
    
    @org.junit.jupiter.api.Test
    @Label("CalculateAverage: Deve lançar exceção para array null")
    void calculateAverageShouldThrowExceptionForNullArray() {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // When & Then
        Assertions.assertThatThrownBy(() -> mathFunctions.calculateAverage(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null or empty");
    }
    
    @org.junit.jupiter.api.Test
    @Label("CalculateAverage: Deve lançar exceção para array vazio")
    void calculateAverageShouldThrowExceptionForEmptyArray() {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // When & Then
        Assertions.assertThatThrownBy(() -> mathFunctions.calculateAverage(new int[0]))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null or empty");
    }
    
    // ========== GERADORES PERSONALIZADOS PARA CASOS EXTREMOS ==========
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("MultiplyByTwo: Deve funcionar com valores extremos")
    void multiplyByTwoShouldWorkWithExtremeValues(@ForAll("extremeValues") int number) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // Previne overflow
        Assume.that(Math.abs(number) <= Integer.MAX_VALUE / 2);
        
        // When
        int result = mathFunctions.multiplyByTwo(number);
        
        // Then
        Assertions.assertThat(result % 2).isEqualTo(0);
    }
    
    @Provide
    Arbitrary<Integer> extremeValues() {
        return Arbitraries.oneOf(
                Arbitraries.of(Integer.MAX_VALUE / 2, Integer.MIN_VALUE / 2, 0, 1, -1),
                Arbitraries.integers().between(-1000000, 1000000)
        );
    }
    
    @Property
    @Report(Reporting.GENERATED)
    @Label("GenerateMultiplicationTable: Deve funcionar com casos extremos")
    void generateMultiplicationTableShouldWorkWithExtremeCases(
            @ForAll("smallPositiveNumbers") int number,
            @ForAll @IntRange(min = 1, max = 10) int limit) {
        // Given
        MathFunctions mathFunctions = new MathFunctions(logger);
        
        // When
        int[] table = mathFunctions.generateMultiplicationTable(number, limit);
        
        // Then
        Assertions.assertThat(table).hasSize(limit);
        Assertions.assertThat(table[0]).isEqualTo(number);
    }
    
    @Provide
    Arbitrary<Integer> smallPositiveNumbers() {
        return Arbitraries.integers().between(1, 100);
    }
}

