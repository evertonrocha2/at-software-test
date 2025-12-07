TABELA DE DECISÃO - API ViaCEP
Consulta por Endereço (UF + Cidade + Logradouro)

API: https://viacep.com.br/ws/{UF}/{cidade}/{logradouro}/json/

Esta tabela modela diferentes combinações de parâmetros para a consulta por endereço da API ViaCEP, identificando o comportamento esperado para cada combinação.

--

CONDIÇÕES

C1: UF válida? (V = válida, I = inválida)
C2: Cidade com acentuação? (S = sim, N = não)
C3: Logradouro existe? (S = sim, N = não, P = parcial)
C4: Formato do logradouro? (C = completo, P = parcial, I = inexistente)

--

AÇÕES

A1: Retornar lista de CEPs encontrados
A2: Retornar lista vazia []
A3: Retornar erro HTTP (400/404)
A4: Retornar array vazio mas com status 200

--

REGRAS DE DECISÃO

| #   | C1  | C2  | C3  | C4  | AÇÃO ESPERADA | EXEMPLO                            |
| --- | --- | --- | --- | --- | ------------- | ---------------------------------- |
| 1   | V   | N   | S   | C   | A1            | SP, Sao Paulo, Avenida Paulista    |
| 2   | V   | S   | S   | C   | A1            | SP, São Paulo, Avenida Paulista    |
| 3   | V   | N   | S   | P   | A1 ou A4      | SP, Sao Paulo, Avenida (parcial)   |
| 4   | V   | S   | S   | P   | A1 ou A4      | SP, São Paulo, Avenida (parcial)   |
| 5   | V   | N   | N   | I   | A2 ou A4      | SP, Sao Paulo, Rua Inexistente XYZ |
| 6   | V   | S   | N   | I   | A2 ou A4      | SP, São Paulo, Rua Inexistente XYZ |
| 7   | I   | N   | -   | -   | A3 ou A4      | XX, Teste, Teste                   |
| 8   | I   | S   | -   | -   | A3 ou A4      | XX, Teste, Teste                   |
| 9   | V   | N   | P   | P   | A1 ou A4      | SP, Sao Paulo, Rua (busca parcial) |
| 10  | V   | S   | P   | P   | A1 ou A4      | SP, São Paulo, Rua (busca parcial) |

--

OBSERVAÇÕES IMPORTANTES

API aceita cidade com ou sem acentuação: A API ViaCEP é tolerante com relação à acentuação. Pode-se usar "Sao Paulo" ou "São Paulo" e ambos funcionam.

UF inválida: UFs que não existem (como "XX", "ZZ", "99") geralmente retornam array vazio ou erro, mas isso pode variar.

Logradouro parcial: Quando se usa parte do nome do logradouro, a API pode retornar múltiplos resultados ou array vazio, dependendo da especificidade.

Caso especial - Cidade sem acentuação preferida: Em alguns casos, usar cidade sem acento pode retornar mais resultados porque muitos sistemas armazenam dados sem acentuação.

--

TESTES IMPLEMENTADOS

Os testes em ViaCepApiTest.java cobrem as seguintes regras:

- Regra 1: Teste com UF válida, cidade sem acento, logradouro completo
- Regra 2: Teste com UF válida, cidade com acento (se suportado)
- Regra 5: Teste com logradouro inexistente
- Regra 7: Teste com UF inválida
- Regra 3 e 9: Testes com buscas parciais

Todos os testes validam o status code HTTP e o formato da resposta (array ou objeto).

--

JUSTIFICATIVA DAS COMBINAÇÕES

A tabela de decisão foi criada para cobrir os principais cenários de uso da API:

Combinações válidas e inválidas de UF testam a validação de entrada da API.

Cidade com e sem acentuação testa a tolerância da API a diferentes formatos de entrada.

Logradouro existente, parcial e inexistente testa diferentes níveis de precisão na busca.

Essas combinações garantem que a integração com a API funcione corretamente em diferentes cenários, incluindo casos extremos e falhas.
