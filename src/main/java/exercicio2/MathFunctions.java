package exercicio2;

import java.util.Arrays;

/**
 * Classe que implementa diversas funções matemáticas.
 * Refatorada para suportar injeção de dependência via construtor,
 * permitindo uso de mocks em testes futuros.
 */
public class MathFunctions {
    
    private final MathLogger logger;
    
    /**
     * Construtor que recebe o logger como dependência
     * 
     * @param logger Implementação do MathLogger para registro de operações
     */
    public MathFunctions(MathLogger logger) {
        this.logger = logger;
    }
    
    /**
     * Multiplica um número por dois
     * 
     * @param number Número a ser multiplicado
     * @return Resultado da multiplicação (sempre par)
     */
    public int multiplyByTwo(int number) {
        int result = number * 2;
        logger.log("MultiplyByTwo", new int[]{number});
        return result;
    }
    
    /**
     * Gera uma tabela de multiplicação para um número
     * 
     * @param number Número base para a tabela
     * @param limit Quantidade de elementos na tabela
     * @return Array com os múltiplos do número
     */
    public int[] generateMultiplicationTable(int number, int limit) {
        int[] result = new int[limit];
        for (int i = 0; i < limit; i++) {
            result[i] = number * (i + 1);
        }
        logger.log("GenerateMultiplicationTable", new int[]{number, limit});
        return result;
    }
    
    /**
     * Verifica se um número é primo
     * 
     * @param number Número a ser verificado
     * @return true se o número for primo, false caso contrário
     */
    public boolean isPrime(int number) {
        if (number <= 1) {
            logger.log("IsPrime", new int[]{number});
            return false;
        }
        
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                logger.log("IsPrime", new int[]{number});
                return false;
            }
        }
        
        logger.log("IsPrime", new int[]{number});
        return true;
    }
    
    /**
     * Calcula a média de um array de números
     * 
     * @param numbers Array de números inteiros
     * @return Média dos números (double)
     * @throws IllegalArgumentException se o array for null ou vazio
     */
    public double calculateAverage(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty.");
        }
        
        double average = Arrays.stream(numbers).average().orElseThrow();
        logger.log("CalculateAverage", numbers);
        return average;
    }
}

