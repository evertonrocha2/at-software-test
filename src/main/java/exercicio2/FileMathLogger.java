package exercicio2;

import classesdeajuda.Logg;
import java.util.Arrays;

/**
 * Implementação de MathLogger usando a classe Logg do professor.
 * Utiliza a classe de ajuda disponibilizada (Classes de Ajuda/Logg/Logg.java)
 * para fazer logging real em arquivo.
 * 
 * Esta implementação demonstra o uso das Classes de Ajuda do professor
 * de forma integrada aos testes.
 */
public class FileMathLogger implements MathLogger {
    
    private final Logg logger;
    
    public FileMathLogger(String logFileName) {
        this.logger = new Logg(logFileName);
    }
    
    public FileMathLogger() {
        this.logger = new Logg("math_functions.log");
    }
    
    @Override
    public void log(String operation, int[] inputs) {
        String message = String.format("Operação: %s | Entradas: %s", 
            operation, Arrays.toString(inputs));
        logger.writeDatedLogg(message);
    }
}

