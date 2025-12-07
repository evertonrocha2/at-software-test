ANÁLISE DE COBERTURA DE CÓDIGO
Exercício 5 - RecursiveBinarySearch

Classe testada: exercicio5.algorithms.RecursiveBinarySearch

Este documento apresenta a análise da cobertura de código dos testes unitários para a classe RecursiveBinarySearch, identificando trechos cobertos e não cobertos, além de propor melhorias.

--

CONFIGURAÇÃO E EXECUÇÃO

Para gerar o relatório de cobertura:

1. Compilar o projeto: mvn clean compile
2. Executar os testes: mvn test
3. Gerar relatório JaCoCo: mvn jacoco:report
4. Abrir relatório: target/site/jacoco/index.html

Configuração no pom.xml:

O plugin JaCoCo está configurado para:

- Gerar relatório após execução dos testes
- Verificar cobertura mínima de 50% por pacote
- Incluir todas as classes do pacote exercicio5.algorithms

--

COBERTURA ESPERADA

A classe RecursiveBinarySearch tem os seguintes elementos a serem cobertos:

Método find(T[] arr, T target):

- Validação de array null ou vazio
- Chamada recursiva para binsear

Método binsear(T[] arr, int left, int right, T target):

- Condição right >= left (caso base recursão)
- Cálculo do índice médio
- Comparação arr[mid].compareTo(target)
- Três ramificações: comparison == 0, comparison > 0, comparison < 0
- Retorno recursivo à esquerda (left, mid - 1)
- Retorno recursivo à direita (mid + 1, right)
- Retorno -1 quando não encontrado (right < left)

--

TESTES IMPLEMENTADOS E COBERTURA

Decisões e Ramificações Cobertas:

1. comparison == 0 (elemento encontrado no meio)

   - Teste: deveEncontrarElementoNoMeio()
   - Cobertura: ✅ Cobre o caso onde o elemento está exatamente no índice médio

2. comparison > 0 (buscar à esquerda)

   - Teste: deveBuscarNaSubarvoreEsquerda()
   - Cobertura: ✅ Cobre chamada recursiva com left, mid - 1

3. comparison < 0 (buscar à direita)

   - Teste: deveBuscarNaSubarvoreDireita()
   - Cobertura: ✅ Cobre chamada recursiva com mid + 1, right

4. right < left (elemento não encontrado)

   - Teste: deveRetornarMenosUmQuandoNaoEncontrado()
   - Cobertura: ✅ Cobre caso base da recursão

5. Array null ou vazio
   - Testes: deveRetornarMenosUmParaArrayNull(), deveRetornarMenosUmParaArrayVazio()
   - Cobertura: ✅ Cobre validação inicial no método find()

Valores Limite Cobertos:

1. Array com um único elemento

   - Teste: deveFuncionarComArrayDeUmElemento()
   - Cobertura: ✅ Cobre caso onde left == right

2. Array com dois elementos

   - Teste: deveFuncionarComArrayDeDoisElementos()
   - Cobertura: ✅ Cobre múltiplas execuções em array pequeno

3. Array grande (múltiplas recursões)
   - Teste: deveFuncionarComArrayGrande()
   - Cobertura: ✅ Cobre profundidade de recursão e performance

Diferentes Execuções Cobertas:

1. Elemento no início do array

   - Teste: deveEncontrarElementoNoInicio()
   - Cobertura: ✅ Cobre caminho completo até encontrar no início

2. Elemento no final do array

   - Teste: deveEncontrarElementoNoFinal()
   - Cobertura: ✅ Cobre caminho completo até encontrar no final

3. Múltiplas chamadas recursivas

   - Teste: deveTestarMultiplasChamadasRecursivas()
   - Cobertura: ✅ Cobre diferentes profundidades de recursão

4. Elemento logo antes/depois do meio
   - Testes: deveCobrirCaminhoElementoAntesMeio(), deveCobrirCaminhoElementoAposMeio()
   - Cobertura: ✅ Cobre casos próximos ao índice médio

Cobertura de Tipos Genéricos:

1. Integer

   - Todos os testes principais usam Integer
   - Cobertura: ✅ Completa

2. String

   - Teste: deveFuncionarComString()
   - Cobertura: ✅ Valida funcionamento com tipos diferentes

3. Double
   - Teste: deveFuncionarComDouble()
   - Cobertura: ✅ Valida funcionamento com tipos diferentes

Testes Parametrizados:

- deveEncontrarTodosElementos(): Testa todos os elementos de um array
- deveRetornarMenosUmParaElementosForaRange(): Testa elementos fora do range
- Cobertura: ✅ Aumenta cobertura através de múltiplas execuções

--

TRECHOS NÃO COBERTOS (SE HOUVER)

Após análise detalhada dos testes, os seguintes trechos podem não estar totalmente cobertos:

1. Overflow em cálculos de índice médio

   - Status: ⚠️ Potencialmente não coberto
   - Risco: Baixo (cálculo mid = left + (right - left) / 2 evita overflow)
   - Recomendação: Adicionar teste com array extremamente grande (se possível)

2. Comparação de tipos incompatíveis

   - Status: ⚠️ Não testado explicitamente
   - Risco: Baixo (garantido pelo tipo genérico T extends Comparable<T>)
   - Recomendação: Considerar teste com tipo personalizado que implementa Comparable

3. Casos extremos de recursão profunda
   - Status: ⚠️ Parcialmente coberto
   - Risco: Médio (pode causar StackOverflowError)
   - Recomendação: Adicionar teste de limite de recursão ou documentar limitação conhecida

--

ANÁLISE DE COBERTURA ESPERADA

Com base nos testes implementados, a cobertura esperada é:

Linhas de código: ~95%+
Ramificações (branches): ~100%
Métodos: 100%
Classes: 100%

Pontos fortes da cobertura atual:

- Todas as decisões (if/else) estão cobertas
- Todas as ramificações recursivas estão cobertas
- Casos extremos (null, vazio, um elemento, dois elementos) estão cobertos
- Diferentes tipos genéricos estão cobertos
- Múltiplas profundidades de recursão estão cobertas

Pontos de melhoria:

- Adicionar teste de stress com array muito grande para validar limite de recursão
- Documentar comportamento esperado em caso de StackOverflowError
- Considerar teste de performance para validar complexidade O(log n)

--

PROPOSTAS DE MELHORIA

Para alcançar cobertura ainda maior e robustez:

1. Teste de limite de recursão

   - Adicionar teste que valida comportamento com array extremamente grande
   - Verificar se há limite prático de recursão e documentar

2. Teste de tipos personalizados

   - Criar classe de teste que implementa Comparable para validar genéricos

3. Teste de caso edge: right == left

   - Já coberto por deveCobrirCasoRightIgualLeft(), mas pode ser expandido

4. Análise de complexidade

   - Adicionar testes de performance que validam complexidade O(log n)
   - Comparar tempo de execução entre diferentes tamanhos de array

5. Validação de precondições
   - Documentar se array precisa estar ordenado (sim, mas poderia ter teste que valida isso)

--

CONCLUSÃO

A suíte de testes para RecursiveBinarySearch possui cobertura abrangente dos caminhos de execução, decisões e ramificações do código. Os testes cobrem:

- Todas as condições lógicas (comparison == 0, > 0, < 0)
- Todas as ramificações recursivas
- Casos extremos e valores limite
- Diferentes tipos genéricos
- Múltiplas profundidades de recursão

A cobertura de código está em nível adequado para garantir robustez e confiabilidade. Os trechos não cobertos identificados são casos extremos de baixa probabilidade ou limitações conhecidas da linguagem (como limite de recursão).

Os testes seguem princípios de white-box testing, analisando a estrutura interna do código para garantir que todos os caminhos lógicos sejam exercitados. Isso aumenta a confiança de que o algoritmo funciona corretamente em diversos cenários.

Para verificar a cobertura exata, execute os testes com JaCoCo e analise o relatório HTML gerado em target/site/jacoco/index.html.
