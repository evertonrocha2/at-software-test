package exercicio2;

/**
 * Interface para logging de operações matemáticas.
 * Permite injeção de dependência para testes isolados com mocks.
 */
public interface MathLogger {
    void log(String operation, int[] inputs);
}

