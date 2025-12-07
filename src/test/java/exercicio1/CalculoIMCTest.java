package exercicio1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

/**
 * Exercício 1: Teste Exploratório e Análise de Comportamento Esperado
 * 
 * Testes para o código original do CalculoIMC do repositório do professor.
 * Baseado em análise de valor limite e partições de equivalência.
 */
public class CalculoIMCTest {

    // ========== TESTES DE CÁLCULO DO IMC ==========
    
    @Test
    @DisplayName("Deve calcular IMC corretamente para valores válidos")
    void deveCalcularIMCCorretamente() {
        // Teste manual exploratório: peso 70kg, altura 1.75m
        double imc = CalculoIMC.calcularPeso(70.0, 1.75);
        
        // IMC esperado: 70 / (1.75 * 1.75) = 70 / 3.0625 = 22.857...
        assertThat(imc).isCloseTo(22.86, within(0.01));
    }
    
    @ParameterizedTest
    @CsvSource({
        "50.0, 1.60, 19.53",
        "60.0, 1.70, 20.76",
        "70.0, 1.75, 22.86",
        "80.0, 1.80, 24.69",
        "90.0, 1.85, 26.30"
    })
    @DisplayName("Deve calcular IMC corretamente para diferentes combinações de peso e altura")
    void deveCalcularIMCParaDiferentesValores(double peso, double altura, double imcEsperado) {
        double imc = CalculoIMC.calcularPeso(peso, altura);
        assertThat(imc).isCloseTo(imcEsperado, within(0.1));
    }
    
    // ========== TESTES DE CLASSIFICAÇÃO - ANÁLISE DE VALOR LIMITE ==========
    
    @Test
    @DisplayName("Deve classificar 'Magreza grave' para IMC abaixo de 16.0")
    void deveClassificarMagrezaGrave() {
        assertThat(CalculoIMC.classificarIMC(15.0)).isEqualTo("Magreza grave");
        assertThat(CalculoIMC.classificarIMC(15.99)).isEqualTo("Magreza grave");
        assertThat(CalculoIMC.classificarIMC(0.1)).isEqualTo("Magreza grave");
    }
    
    @Test
    @DisplayName("Deve classificar 'Magreza moderada' para IMC entre 16.0 e 17.0 (valor limite)")
    void deveClassificarMagrezaModerada() {
        assertThat(CalculoIMC.classificarIMC(16.0)).isEqualTo("Magreza moderada");
        assertThat(CalculoIMC.classificarIMC(16.5)).isEqualTo("Magreza moderada");
        assertThat(CalculoIMC.classificarIMC(16.99)).isEqualTo("Magreza moderada");
    }
    
    @Test
    @DisplayName("Deve classificar 'Magreza leve' para IMC entre 17.0 e 18.5 (valor limite)")
    void deveClassificarMagrezaLeve() {
        assertThat(CalculoIMC.classificarIMC(17.0)).isEqualTo("Magreza leve");
        assertThat(CalculoIMC.classificarIMC(17.5)).isEqualTo("Magreza leve");
        assertThat(CalculoIMC.classificarIMC(18.49)).isEqualTo("Magreza leve");
    }
    
    @Test
    @DisplayName("Deve classificar 'Saudável' para IMC entre 18.5 e 25.0 (valor limite)")
    void deveClassificarSaudavel() {
        assertThat(CalculoIMC.classificarIMC(18.5)).isEqualTo("Saudável");
        assertThat(CalculoIMC.classificarIMC(20.0)).isEqualTo("Saudável");
        assertThat(CalculoIMC.classificarIMC(24.99)).isEqualTo("Saudável");
    }
    
    @Test
    @DisplayName("Deve classificar 'Sobrepeso' para IMC entre 25.0 e 30.0 (valor limite)")
    void deveClassificarSobrepeso() {
        assertThat(CalculoIMC.classificarIMC(25.0)).isEqualTo("Sobrepeso");
        assertThat(CalculoIMC.classificarIMC(27.5)).isEqualTo("Sobrepeso");
        assertThat(CalculoIMC.classificarIMC(29.99)).isEqualTo("Sobrepeso");
    }
    
    @Test
    @DisplayName("Deve classificar 'Obesidade Grau I' para IMC entre 30.0 e 35.0 (valor limite)")
    void deveClassificarObesidadeGrauI() {
        assertThat(CalculoIMC.classificarIMC(30.0)).isEqualTo("Obesidade Grau I");
        assertThat(CalculoIMC.classificarIMC(32.5)).isEqualTo("Obesidade Grau I");
        assertThat(CalculoIMC.classificarIMC(34.99)).isEqualTo("Obesidade Grau I");
    }
    
    @Test
    @DisplayName("Deve classificar 'Obesidade Grau II' para IMC entre 35.0 e 40.0 (valor limite)")
    void deveClassificarObesidadeGrauII() {
        assertThat(CalculoIMC.classificarIMC(35.0)).isEqualTo("Obesidade Grau II");
        assertThat(CalculoIMC.classificarIMC(37.5)).isEqualTo("Obesidade Grau II");
        assertThat(CalculoIMC.classificarIMC(39.99)).isEqualTo("Obesidade Grau II");
    }
    
    @Test
    @DisplayName("Deve classificar 'Obesidade Grau III' para IMC maior ou igual a 40.0 (valor limite)")
    void deveClassificarObesidadeGrauIII() {
        assertThat(CalculoIMC.classificarIMC(40.0)).isEqualTo("Obesidade Grau III");
        assertThat(CalculoIMC.classificarIMC(45.0)).isEqualTo("Obesidade Grau III");
        assertThat(CalculoIMC.classificarIMC(50.0)).isEqualTo("Obesidade Grau III");
    }
    
    // ========== TESTES DE COMPORTAMENTO INESPERADO (PROBLEMAS ENCONTRADOS) ==========
    
    @Test
    @DisplayName("PROBLEMA ENCONTRADO: Aceita peso zero (comportamento inesperado)")
    void problemaAceitaPesoZero() {
        // O código original NÃO valida entrada - aceita zero!
        double imc = CalculoIMC.calcularPeso(0.0, 1.75);
        assertThat(imc).isEqualTo(0.0);
        
        // Resultado inesperado: IMC zero é classificado como "Magreza grave"
        assertThat(CalculoIMC.classificarIMC(imc)).isEqualTo("Magreza grave");
    }
    
    @Test
    @DisplayName("PROBLEMA ENCONTRADO: Aceita peso negativo (comportamento inesperado)")
    void problemaAceitaPesoNegativo() {
        // O código original NÃO valida entrada - aceita negativo!
        double imc = CalculoIMC.calcularPeso(-70.0, 1.75);
        assertThat(imc).isNegative();
        
        // Resultado inesperado: IMC negativo é classificado como "Magreza grave"
        assertThat(CalculoIMC.classificarIMC(imc)).isEqualTo("Magreza grave");
    }
    
    @Test
    @DisplayName("PROBLEMA ENCONTRADO: Aceita altura zero (gera divisão por zero - Infinity)")
    void problemaAceitaAlturaZero() {
        // O código original NÃO valida entrada - aceita zero!
        double imc = CalculoIMC.calcularPeso(70.0, 0.0);
        assertThat(imc).isInfinite();
        
        // Resultado inesperado: Infinity é classificado como "Obesidade Grau III"
        assertThat(CalculoIMC.classificarIMC(imc)).isEqualTo("Obesidade Grau III");
    }
    
    @Test
    @DisplayName("PROBLEMA ENCONTRADO: Aceita altura negativa (comportamento inesperado)")
    void problemaAceitaAlturaNegativa() {
        // O código original NÃO valida entrada - aceita negativo!
        double imc = CalculoIMC.calcularPeso(70.0, -1.75);
        assertThat(imc).isPositive(); // Negativo dividido por positivo ao quadrado = positivo
        
        // Resultado inesperado: aceita e calcula
        assertThat(imc).isCloseTo(22.86, within(0.01));
    }
    
    @Test
    @DisplayName("PROBLEMA ENCONTRADO: Aceita valores extremamente altos (possível overflow)")
    void problemaAceitaValoresExtremos() {
        // O código original NÃO valida entrada - aceita valores extremos!
        double imc = CalculoIMC.calcularPeso(1000.0, 3.0);
        
        // Resultado inesperado: aceita valores não realistas
        assertThat(imc).isCloseTo(111.11, within(0.1));
        assertThat(CalculoIMC.classificarIMC(imc)).isEqualTo("Obesidade Grau III");
    }
    
    // ========== TESTES DE PARTIÇÕES DE EQUIVALÊNCIA ==========
    
    @ParameterizedTest
    @ValueSource(doubles = {15.0, 10.0, 5.0, 0.0, -5.0})
    @DisplayName("Partição de equivalência: IMC < 16.0 → Magreza grave")
    void particaoEquivalenciaMagrezaGrave(double imc) {
        assertThat(CalculoIMC.classificarIMC(imc)).isEqualTo("Magreza grave");
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {17.5, 18.0, 18.4})
    @DisplayName("Partição de equivalência: 17.0 <= IMC < 18.5 → Magreza leve")
    void particaoEquivalenciaMagrezaLeve(double imc) {
        assertThat(CalculoIMC.classificarIMC(imc)).isEqualTo("Magreza leve");
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {20.0, 22.0, 24.0})
    @DisplayName("Partição de equivalência: 18.5 <= IMC < 25.0 → Saudável")
    void particaoEquivalenciaSaudavel(double imc) {
        assertThat(CalculoIMC.classificarIMC(imc)).isEqualTo("Saudável");
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {26.0, 28.0, 29.0})
    @DisplayName("Partição de equivalência: 25.0 <= IMC < 30.0 → Sobrepeso")
    void particaoEquivalenciaSobrepeso(double imc) {
        assertThat(CalculoIMC.classificarIMC(imc)).isEqualTo("Sobrepeso");
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {50.0, 60.0, 100.0, 200.0})
    @DisplayName("Partição de equivalência: IMC >= 40.0 → Obesidade Grau III")
    void particaoEquivalenciaObesidadeGrauIII(double imc) {
        assertThat(CalculoIMC.classificarIMC(imc)).isEqualTo("Obesidade Grau III");
    }
}

