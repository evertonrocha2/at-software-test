ESPECIFICAÇÃO FUNCIONAL - CALCULADORA DE IMC

--

VISÃO GERAL

O sistema CalculoIMC é uma aplicação Java para cálculo do Índice de Massa Corporal (IMC). O sistema permite que o usuário insira seu peso e altura para calcular o IMC e receber uma classificação que indica se está dentro de uma faixa considerada saudável.

O IMC é uma medida padrão usada na área de saúde para avaliar se uma pessoa está dentro de um peso considerado adequado para sua altura. É uma fórmula simples mas útil como indicador inicial de saúde.

--

FUNCIONALIDADES PRINCIPAIS

Cálculo do IMC

O sistema calcula o IMC usando a fórmula matemática padrão: IMC = Peso (kg) / (Altura (m))²

Entrada:

- Peso em quilogramas (número real)
- Altura em metros (número real)

Saída:

- Valor numérico do IMC em kg/m² (tipo double)

Para valores válidos (peso e altura maiores que zero), o sistema deveria calcular corretamente e retornar um número real positivo, mantendo a precisão de ponto flutuante.

Porém, ao analisar o código, vi que ele não faz validação de entrada. Isso significa que aceita valores negativos, zero e valores extremos. Quando a altura é zero, ocorre divisão por zero e o resultado é Infinity. Também não há tratamento de exceções para quando o usuário insere dados inválidos.

Classificação do IMC

O sistema classifica o IMC em categorias baseadas em faixas de valores estabelecidas pela Organização Mundial da Saúde (OMS). Cada faixa corresponde a uma classificação específica.

As faixas de classificação são:

- IMC menor que 16.0: Magreza grave
- IMC entre 16.0 e 17.0: Magreza moderada
- IMC entre 17.0 e 18.5: Magreza leve
- IMC entre 18.5 e 25.0: Saudável
- IMC entre 25.0 e 30.0: Sobrepeso
- IMC entre 30.0 e 35.0: Obesidade Grau I
- IMC entre 35.0 e 40.0: Obesidade Grau II
- IMC maior ou igual a 40.0: Obesidade Grau III

Entrada: Valor do IMC (número real)

Saída: String com a classificação correspondente

Para cada faixa de IMC, o sistema deveria retornar a classificação correspondente. Valores exatamente nos limites devem ser classificados na categoria superior, e todas as faixas possíveis devem estar cobertas.

Ao testar, vi que o sistema classifica corretamente valores válidos dentro das faixas. Porém, para valores inválidos, o comportamento é inesperado:

- Valores negativos são classificados como "Magreza grave"
- Zero é classificado como "Magreza grave"
- Infinity é classificado como "Obesidade Grau III"

Isso acontece porque o código não valida se o IMC é um valor válido antes de classificar.

Interface com o Usuário

O sistema oferece uma interface de linha de comando simples. O fluxo de interação é o seguinte:

1. Sistema exibe um cabeçalho com a versão do programa
2. Sistema solicita o peso em quilogramas
3. Usuário insere o peso
4. Sistema solicita a altura em metros
5. Usuário insere a altura
6. Sistema calcula o IMC
7. Sistema exibe o IMC calculado no formato XX.XX kg/m²
8. Sistema exibe a classificação

A interface deveria ser clara e intuitiva, com mensagens de erro amigáveis caso a entrada seja inválida, e validação de entrada antes de processar.

Porém, ao testar, vi que a interface básica funciona, mas não há tratamento de erros amigável - quando há erro, o sistema simplesmente lança exceções técnicas. Também não há validação de entrada.

--

REGRAS DE NEGÓCIO

Validação de Entrada

A regra esperada seria que o peso deve ser um número positivo maior que zero, a altura deve ser um número positivo maior que zero, e ambos devem ser valores razoáveis para humanos.

Mas no código não há nenhuma regra implementada. O sistema aceita qualquer valor double, incluindo zero, negativos e valores extremos sem nenhuma verificação.

Cálculo do IMC

A regra é usar a fórmula matemática padrão: IMC = Peso / (Altura)². O sistema deve preservar a precisão numérica e, muito importante, evitar divisão por zero.

Classificação

As regras são seguir as faixas estabelecidas pela OMS, onde valores exatamente nos limites pertencem à categoria superior, e todas as faixas devem ser cobertas sem gaps.

--

ENTRADAS E SAÍDAS

Entradas:

Peso:

- Tipo: double
- Unidade: quilogramas (kg)
- Formato esperado: número decimal, como 70.5
- Valores válidos esperados: maior que 0 e menor que 500 (razoável para humanos)

Altura:

- Tipo: double
- Unidade: metros (m)
- Formato esperado: número decimal, como 1.75
- Valores válidos esperados: maior que 0 e menor que 3.0 (razoável para humanos)

Saídas:

IMC Calculado:

- Tipo: double
- Unidade: kg/m²
- Formato de exibição: XX.XX kg/m² (com 2 casas decimais)

Classificação:

- Tipo: String
- Valores possíveis: "Magreza grave", "Magreza moderada", "Magreza leve", "Saudável", "Sobrepeso", "Obesidade Grau I", "Obesidade Grau II", "Obesidade Grau III"

--

COMPORTAMENTO ESPERADO vs OBSERVADO

Casos Válidos

Com peso de 70.0 kg e altura de 1.75 m, o sistema calculou corretamente o IMC como 22.86 kg/m² e classificou como "Saudável". Está correto.

Com peso de 90.0 kg e altura de 1.80 m, o sistema calculou 27.78 kg/m² e classificou como "Sobrepeso". Também correto.

Casos Inválidos

Com peso zero e altura de 1.75 m, esperaríamos uma mensagem de erro ou validação. Porém, o sistema aceitou, calculou IMC como 0.0 e classificou como "Magreza grave". Está incorreto.

Com peso de 70.0 kg e altura zero, esperaríamos uma mensagem de erro ou validação devido à divisão por zero. Porém, o sistema calculou IMC como Infinity e classificou como "Obesidade Grau III". Este é um erro crítico.

Com peso negativo de -70.0 kg e altura de 1.75 m, esperaríamos validação. Porém, o sistema aceitou, calculou IMC negativo e classificou como "Magreza grave". Incorreto.

Valores Limite

Testamos os valores exatamente nos limites de classificação:

- IMC de 16.0 foi classificado corretamente como "Magreza moderada"
- IMC de 18.5 foi classificado corretamente como "Saudável"
- IMC de 25.0 foi classificado corretamente como "Sobrepeso"

Os limites estão funcionando corretamente.

--

REQUISITOS NÃO FUNCIONAIS

Confiabilidade

O sistema deveria lidar bem com entradas inválidas e não quebrar com divisão por zero. Porém, isso não está atendido. O sistema aceita valores inválidos e pode quebrar.

Usabilidade

As mensagens de erro deveriam ser claras e amigáveis, e a interface deveria ser intuitiva. A interface básica está ok, mas falta tratamento de erros amigável. Então está parcialmente atendido.

Robustez

O sistema deveria validar entradas antes de processar e evitar resultados sem sentido. Isso não está atendido - não há validação.

--

LIMITAÇÕES CONHECIDAS

Identificamos as seguintes limitações:

1. Não há validação de entrada, então aceita qualquer valor
2. Não há tratamento de exceções amigável
3. Aceita valores biologicamente impossíveis
4. Pode ocorrer divisão por zero quando altura é zero
5. Não há limites razoáveis para peso e altura

--

CONCLUSÃO

O sistema implementa corretamente a fórmula do IMC e a classificação para valores válidos. Funciona bem quando recebe dados normais e corretos. Porém, apresenta deficiências críticas na validação de entrada e no tratamento de erros. Essas deficiências podem levar a resultados incorretos ou até mesmo à quebra da aplicação em casos extremos.

Recomendo adicionar validação de entrada e tratamento de erros antes do lançamento em produção, especialmente para evitar a divisão por zero e para melhorar a experiência do usuário.
