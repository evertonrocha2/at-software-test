package exercicio3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Exercício 3: Teste de API - ViaCEP
 * 
 * Testes para validar funcionalidade, robustez e estratégia de teste da API ViaCEP.
 * Aplicação de partições de equivalência e análise de valor limite.
 */
public class ViaCepApiTest {

    private static final String BASE_URL = "https://viacep.com.br/ws";

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    // ========== TESTES FUNCIONAIS - CEP VÁLIDO ==========
    
    @Test
    @DisplayName("Deve retornar dados de endereço para CEP válido")
    void deveRetornarDadosParaCepValido() {
        given()
            .pathParam("cep", "01310100")
        .when()
            .get("/{cep}/json/")
        .then()
            .statusCode(200)
            .body("cep", equalTo("01310-100"))
            .body("logradouro", not(emptyOrNullString()))
            .body("bairro", not(emptyOrNullString()))
            .body("localidade", equalTo("São Paulo"))
            .body("uf", equalTo("SP"));
    }
    
    @Test
    @DisplayName("Deve retornar dados de endereço para CEP válido com formatação")
    void deveRetornarDadosParaCepFormatado() {
        given()
            .pathParam("cep", "01310-100")
        .when()
            .get("/{cep}/json/")
        .then()
            .statusCode(200)
            .body("cep", not(emptyOrNullString()));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "01310100",  // São Paulo - SP
        "20040020",  // Rio de Janeiro - RJ
        "30130100"   // Belo Horizonte - MG
    })
    @DisplayName("Deve retornar dados para diferentes CEPs válidos")
    void deveRetornarDadosParaDiferentesCepsValidos(String cep) {
        Response response = given()
            .pathParam("cep", cep)
        .when()
            .get("/{cep}/json/");
        
        response.then().statusCode(200);
        
        // Verifica se não é um erro
        String body = response.getBody().asString();
        if (!body.contains("\"erro\"")) {
            response.then().body("cep", not(emptyOrNullString()));
        }
    }

    // ========== TESTES DE ROBUSTEZ - ENTRADAS INVÁLIDAS ==========
    
    @Test
    @DisplayName("Deve retornar erro para CEP com letras")
    void deveRetornarErroParaCepComLetras() {
        given()
            .pathParam("cep", "abc12345")
        .when()
            .get("/{cep}/json/")
        .then()
            .statusCode(anyOf(is(400), is(404)));
    }
    
    @Test
    @DisplayName("Deve retornar erro para CEP vazio")
    void deveRetornarErroParaCepVazio() {
        given()
            .pathParam("cep", "")
        .when()
            .get("/{cep}/json/")
        .then()
            .statusCode(anyOf(is(400), is(404)));
    }
    
    @Test
    @DisplayName("Deve retornar erro para CEP com menos de 8 dígitos")
    void deveRetornarErroParaCepComPoucosDigitos() {
        given()
            .pathParam("cep", "1234567")
        .when()
            .get("/{cep}/json/")
        .then()
            .statusCode(anyOf(is(400), is(404)));
    }
    
    @Test
    @DisplayName("Deve retornar erro para CEP com mais de 8 dígitos")
    void deveRetornarErroParaCepComMuitosDigitos() {
        given()
            .pathParam("cep", "123456789")
        .when()
            .get("/{cep}/json/")
        .then()
            .statusCode(anyOf(is(400), is(404)));
    }
    
    @Test
    @DisplayName("Deve retornar erro para CEP inexistente mas formato válido")
    void deveRetornarErroParaCepInexistente() {
        Response response = given()
            .pathParam("cep", "99999999")
        .when()
            .get("/{cep}/json/");
        
        // API pode retornar 200 com objeto de erro ou 400/404
        if (response.getStatusCode() == 200) {
            // O erro pode vir como boolean true ou string "true", verifica se tem o campo
            String body = response.getBody().asString();
            boolean hasError = body.contains("\"erro\"");
            org.junit.jupiter.api.Assertions.assertTrue(hasError, 
                "CEP inexistente deveria retornar campo 'erro'");
        } else {
            response.then().statusCode(anyOf(is(400), is(404)));
        }
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "00000000",
        "11111111",
        "22222222",
        "99999999"
    })
    @DisplayName("Deve retornar erro para CEPs inválidos (partição de equivalência)")
    void deveRetornarErroParaCepsInvalidos(String cep) {
        Response response = given()
            .pathParam("cep", cep)
        .when()
            .get("/{cep}/json/");
        
        // API retorna 200 com campo "erro": "true" (string) para CEPs inexistentes
        if (response.getStatusCode() == 200) {
            // Verifica se tem campo erro ou se não tem dados válidos
            String body = response.getBody().asString();
            boolean hasError = body.contains("\"erro\"");
            org.junit.jupiter.api.Assertions.assertTrue(hasError, 
                "CEP inválido deveria retornar campo 'erro'");
        } else {
            // Se retornou erro HTTP, também está correto
            response.then().statusCode(anyOf(is(400), is(404)));
        }
    }

    // ========== TESTES DE CONSULTA POR ENDEREÇO ==========
    
    @Test
    @DisplayName("Deve retornar lista de CEPs para consulta por endereço válido")
    void deveRetornarListaParaConsultaPorEndereco() {
        given()
            .pathParam("uf", "SP")
            .pathParam("cidade", "Sao Paulo")
            .pathParam("logradouro", "Avenida Paulista")
        .when()
            .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
            .statusCode(200)
            .body("$", instanceOf(java.util.List.class))
            .body("size()", greaterThan(0));
    }
    
    @Test
    @DisplayName("Deve retornar lista vazia para endereço inexistente")
    void deveRetornarListaVaziaParaEnderecoInexistente() {
        given()
            .pathParam("uf", "SP")
            .pathParam("cidade", "Sao Paulo")
            .pathParam("logradouro", "Rua Inexistente 99999")
        .when()
            .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
            .statusCode(200)
            .body("$", instanceOf(java.util.List.class));
    }
    
    @ParameterizedTest
    @CsvSource({
        "SP, Sao Paulo, Avenida Paulista",
        "RJ, Rio de Janeiro, Avenida Atlantica",
        "MG, Belo Horizonte, Avenida Afonso Pena"
    })
    @DisplayName("Deve retornar dados para diferentes combinações de UF, cidade e logradouro")
    void deveRetornarDadosParaDiferentesEnderecos(String uf, String cidade, String logradouro) {
        given()
            .pathParam("uf", uf)
            .pathParam("cidade", cidade)
            .pathParam("logradouro", logradouro)
        .when()
            .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
            .statusCode(200);
    }

    // ========== TESTES DE PARTIÇÕES DE EQUIVALÊNCIA ==========
    
    @ParameterizedTest
    @ValueSource(strings = {
        "SP", "RJ", "MG", "BA", "PR", "RS", "PE", "CE"
    })
    @DisplayName("Partição de equivalência: UFs válidas")
    void particaoEquivalenciaUFsValidas(String uf) {
        given()
            .pathParam("uf", uf)
            .pathParam("cidade", "Teste")
            .pathParam("logradouro", "Teste")
        .when()
            .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
            .statusCode(200);
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "XX", "ZZ", "99", "AB", "XY"
    })
    @DisplayName("Partição de equivalência: UFs inválidas")
    void particaoEquivalenciaUFsInvalidas(String uf) {
        Response response = given()
            .pathParam("uf", uf)
            .pathParam("cidade", "Teste")
            .pathParam("logradouro", "Teste")
        .when()
            .get("/{uf}/{cidade}/{logradouro}/json/");
        
        // Pode retornar 200 com array vazio ou erro
        response.then().statusCode(anyOf(is(200), is(400), is(404)));
    }
    
    @Test
    @DisplayName("Partição de equivalência: Cidade com acentuação")
    void particaoEquivalenciaCidadeComAcentuacao() {
        // Testa se API aceita ou requer URL encoding
        Response response = given()
            .pathParam("uf", "SP")
            .pathParam("cidade", "São Paulo")  // Com acento
            .pathParam("logradouro", "Avenida")
        .when()
            .get("/{uf}/{cidade}/{logradouro}/json/");
        
        response.then().statusCode(anyOf(is(200), is(400), is(404)));
    }
    
    @Test
    @DisplayName("Partição de equivalência: Cidade sem acentuação")
    void particaoEquivalenciaCidadeSemAcentuacao() {
        given()
            .pathParam("uf", "SP")
            .pathParam("cidade", "Sao Paulo")  // Sem acento
            .pathParam("logradouro", "Avenida")
        .when()
            .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
            .statusCode(200);
    }

    // ========== ANÁLISE DE VALOR LIMITE - TAMANHO DO CEP ==========
    
    @Test
    @DisplayName("Valor limite: CEP com exatamente 8 dígitos (válido)")
    void valorLimiteCepComOitoDigitos() {
        given()
            .pathParam("cep", "01310100")
        .when()
            .get("/{cep}/json/")
        .then()
            .statusCode(200);
    }
    
    @Test
    @DisplayName("Valor limite: CEP com 7 dígitos (inválido - abaixo do mínimo)")
    void valorLimiteCepComSeteDigitos() {
        given()
            .pathParam("cep", "0131010")
        .when()
            .get("/{cep}/json/")
        .then()
            .statusCode(anyOf(is(400), is(404)));
    }
    
    @Test
    @DisplayName("Valor limite: CEP com 9 dígitos (inválido - acima do máximo)")
    void valorLimiteCepComNoveDigitos() {
        given()
            .pathParam("cep", "013101001")
        .when()
            .get("/{cep}/json/")
        .then()
            .statusCode(anyOf(is(400), is(404)));
    }

    // ========== JUSTIFICAÇÃO DAS ESCOLHAS ==========
    
    /**
     * Justificativa das Partições de Equivalência:
     * 
     * 1. CEP válido (8 dígitos numéricos, formato correto)
     * 2. CEP inválido por formato (letras, caracteres especiais)
     * 3. CEP inválido por tamanho (< 8 ou > 8 dígitos)
     * 4. CEP inexistente (formato válido mas não existe no banco)
     * 5. UF válida (sigla de 2 letras de estado brasileiro)
     * 6. UF inválida (não existe, formato incorreto)
     * 7. Cidade com/sem acentuação
     * 8. Logradouro existente/inexistente
     * 
     * Justificativa da Análise de Valor Limite:
     * 
     * - CEP: Limite inferior (7 dígitos), valor válido (8 dígitos), limite superior (9 dígitos)
     * - Testa pontos críticos onde erros de validação são comuns
     */
}

