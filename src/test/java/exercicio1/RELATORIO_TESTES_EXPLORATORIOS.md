RELATÓRIO DE TESTES EXPLORATÓRIOS MANUAIS - CALCULADORA IMC

Engenheiro de Testes: Equipe QA
Sistema Testado: CalculoIMC

--

OBJETIVO

Fiz testes exploratórios manuais para identificar falhas, comportamentos inesperados e dificuldades de uso antes do lançamento do aplicativo de cálculo de IMC. O objetivo era entender como o sistema se comporta na prática, testando vários cenários que um usuário real poderia encontrar.

--

METODOLOGIA

Testei diferentes tipos de cenários:

- Valores válidos e normais que um usuário comum usaria
- Valores limite onde a classificação muda
- Valores inválidos como negativos e zero
- Valores extremos para ver como o sistema reage
- Comportamentos inesperados que podem indicar problemas

Cada teste foi executado manualmente rodando o programa, inserindo os valores e observando os resultados. Anotei tudo para identificar padrões e problemas.

--

CENÁRIOS TESTADOS E RESULTADOS

Cenário 1: Teste com Entradas Válidas Normais

Peso: 70.0 kg
Altura: 1.75 m

Resultado: O sistema calculou o IMC como 22.86 kg/m² e classificou como "Saudável". Comportamento esperado e correto para valores normais.

Cenário 2: Teste com Peso Normal, Altura Normal

Peso: 60.0 kg
Altura: 1.70 m

Resultado: IMC calculado foi 20.76 kg/m², classificado como "Saudável". Tudo funcionou como esperado.

Cenário 3: Teste com Valores no Limite Inferior

Peso: 50.0 kg
Altura: 1.60 m

Resultado: IMC foi 19.53 kg/m², classificado como "Saudável". O sistema funcionou corretamente mesmo com valores mais baixos.

Cenário 4: Teste com Valores no Limite Superior

Peso: 80.0 kg
Altura: 1.80 m

Resultado: IMC calculado foi 24.69 kg/m², ainda classificado como "Saudável". Funcionou corretamente próximo ao limite.

Cenário 5: Teste com IMC Exatamente no Limite 18.5

Peso: 58.5 kg
Altura: 1.78 m

Resultado: IMC ficou em torno de 18.48 kg/m² (devido à precisão de ponto flutuante), classificado como "Saudável". Este é um caso interessante porque valores exatamente no limite podem variar ligeiramente por causa da precisão numérica.

Cenário 6: PROBLEMA ENCONTRADO - Peso Zero

Peso: 0.0 kg
Altura: 1.75 m

Resultado: O sistema aceitou peso zero e calculou IMC como 0.0, classificando como "Magreza grave". Este é um problema porque:

- O sistema não valida se o peso é maior que zero
- Aceita valor zero que é biologicamente inválido
- Classifica IMC zero como "Magreza grave", o que não faz sentido
- Pode fornecer informação médica incorreta ao usuário

Cenário 7: PROBLEMA ENCONTRADO - Peso Negativo

Peso: -70.0 kg
Altura: 1.75 m

Resultado: O sistema aceitou peso negativo e calculou IMC como -22.86 kg/m² (negativo!), classificando como "Magreza grave". Problemas identificados:

- Sistema não valida se peso é positivo
- Aceita valores negativos que são fisicamente impossíveis
- IMC negativo é classificado como "Magreza grave", comportamento inesperado
- Resultado matematicamente incorreto e biologicamente impossível

Cenário 8: ERRO CRÍTICO - Altura Zero (Divisão por Zero)

Peso: 70.0 kg
Altura: 0.0 m

Resultado: Quando altura é zero, ocorre divisão por zero e o IMC fica como Infinity. O sistema então classifica Infinity como "Obesidade Grau III". Este é um erro crítico porque:

- Sistema não valida se altura é maior que zero
- Divisão por zero resulta em Infinity
- Infinity é classificado como "Obesidade Grau III", comportamento sem sentido
- Aplicação pode quebrar ou fornecer resultado completamente errado

Cenário 9: PROBLEMA ENCONTRADO - Altura Negativa

Peso: 70.0 kg
Altura: -1.75 m

Resultado: O sistema aceitou altura negativa. Como a altura é elevada ao quadrado, o resultado ficou positivo (22.86 kg/m²), classificado como "Saudável". Problema identificado:

- Sistema não valida se altura é positiva
- Aceita valores negativos que são fisicamente impossíveis
- Altura negativa não existe na realidade, então o resultado está correto matematicamente mas é biologicamente impossível

Cenário 10: PROBLEMA ENCONTRADO - Valores Extremamente Altos

Peso: 1000.0 kg
Altura: 3.0 m

Resultado: O sistema aceitou esses valores extremos, calculou IMC como 111.11 kg/m² e classificou como "Obesidade Grau III". Problema:

- Sistema não valida limites razoáveis para peso e altura
- Aceita valores que são fisicamente impossíveis para humanos
- Pode aceitar entradas irrealistas sem alertar o usuário

Cenário 11: PROBLEMA ENCONTRADO - Valores Extremamente Baixos

Peso: 5.0 kg
Altura: 2.5 m

Resultado: O sistema aceitou essa combinação estranha, calculou IMC como 0.8 kg/m² e classificou como "Magreza grave". Problema:

- Sistema não valida limites mínimos razoáveis
- Aceita combinações de peso/altura biologicamente impossíveis

Cenário 12: Teste no Limite de Classificação - IMC 16.0

IMC: 16.0 (testado diretamente no método de classificação)

Resultado: O valor exato do limite (16.0) foi classificado corretamente como "Magreza moderada". O limite está funcionando corretamente.

Cenário 13: Teste no Limite de Classificação - IMC 25.0

IMC: 25.0 (testado diretamente no método de classificação)

Resultado: Classificado corretamente como "Sobrepeso". O limite está funcionando.

Cenário 14: Dificuldade de Uso - Mensagens de Erro

Peso: texto "abc"
Altura: 1.75

Resultado: Quando tentei inserir texto no campo de peso, o programa lançou uma exceção técnica (NumberFormatException). Problemas de usabilidade:

- Não há tratamento de exceções amigável
- Usuário recebe mensagem técnica ao invés de algo claro
- Falta validação de entrada com mensagens que o usuário entenda
- Experiência do usuário fica ruim quando há erro

--

FALHAS E COMPORTAMENTOS INESPERADOS IDENTIFICADOS

1. FALHA CRÍTICA: Aceita altura zero causando divisão por zero
   Severidade: Alta
   Impacto: Aplicação pode quebrar ou gerar resultado sem sentido
   Recomendação: Adicionar validação para altura maior que zero

2. FALHA: Aceita peso zero sem validação
   Severidade: Média
   Impacto: Fornece classificação incorreta (IMC zero como "Magreza grave")
   Recomendação: Adicionar validação para peso maior que zero

3. FALHA: Aceita valores negativos (peso e altura)
   Severidade: Média
   Impacto: Resultados matematicamente incorretos ou biologicamente impossíveis
   Recomendação: Adicionar validação para valores positivos

4. FALHA: Aceita valores extremos sem limites razoáveis
   Severidade: Baixa
   Impacto: Pode aceitar entradas irrealistas
   Recomendação: Considerar limites mínimos e máximos razoáveis

5. FALHA DE USABILIDADE: Não há tratamento de erros amigável
   Severidade: Média
   Impacto: Experiência do usuário ruim quando há erro de entrada
   Recomendação: Adicionar tratamento de exceções com mensagens claras

6. COMPORTAMENTO INESPERADO: Infinity é classificado como "Obesidade Grau III"
   Severidade: Alta
   Impacto: Classificação sem sentido para valor inválido
   Recomendação: Validar entrada antes de classificar

7. COMPORTAMENTO INESPERADO: IMC negativo é classificado como "Magreza grave"
   Severidade: Média
   Impacto: Classificação incorreta para valor inválido
   Recomendação: Validar entrada antes de classificar

--

DIFICULDADES DE USO IDENTIFICADAS

Encontrei algumas dificuldades que os usuários podem enfrentar:

1. Quando há erro de entrada, não há feedback claro. O programa simplesmente lança uma exceção técnica que o usuário comum não vai entender.

2. Não há validação prévia dos dados. O usuário só descobre que algo está errado depois de inserir os valores e executar.

3. As mensagens de erro são técnicas (como "NumberFormatException") ao invés de algo amigável como "Por favor, insira apenas números".

4. O usuário não sabe quais valores são aceitos. Não há indicação de limites ou formato esperado.

--

CONCLUSÕES E RECOMENDAÇÕES

O sistema funciona bem para entradas válidas dentro de faixas razoáveis. Ele calcula o IMC corretamente e classifica adequadamente quando recebe dados normais. Porém, encontrei algumas falhas críticas quando o sistema recebe valores problemáticos, especialmente zero e valores negativos.

RECOMENDAÇÕES PRIORITÁRIAS:

1. URGENTE: Adicionar validação de entrada para altura maior que zero. Isso evita a divisão por zero que pode quebrar a aplicação.

2. IMPORTANTE: Adicionar validação de entrada para peso maior que zero.

3. IMPORTANTE: Adicionar validação para valores negativos, já que não fazem sentido fisicamente.

4. DESEJÁVEL: Implementar tratamento de exceções com mensagens amigáveis ao usuário.

5. DESEJÁVEL: Considerar limites razoáveis (por exemplo, altura entre 0.5m e 2.5m) para evitar valores extremos.

Essas correções são importantes antes do lançamento, especialmente a validação de altura zero, que pode causar problemas sérios.

Assinatura: Equipe QA
Data: 06/12/2024
