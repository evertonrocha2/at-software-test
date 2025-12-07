DOCUMENTO TÉCNICO - REFATORAÇÃO DA CLASSE MathFunctions

Classe: exercicio2.MathFunctions

Este documento descreve a análise crítica do código legado, as alterações realizadas na refatoração da classe MathFunctions e as justificativas técnicas baseadas em princípios de desenvolvimento sustentável.

--

PARTE 1: ANÁLISE CRÍTICA DO CÓDIGO LEGADO

O código original tinha métodos estáticos. Vou analisar os principais problemas que encontrei.

Problemas Estruturais Identificados

Acoplamento com dependências diretas

O código original tinha métodos estáticos que não permitiam injeção de dependências. Se precisássemos adicionar logging ou rastreamento, teríamos que colocar tudo dentro da própria classe, criando um acoplamento forte. Qualquer mudança futura exigiria modificar a classe diretamente, o que viola o princípio aberto/fechado.

Falta de testabilidade

Com métodos estáticos, não dá pra substituir dependências nos testes. Se os métodos precisassem de logging, seria impossível testar isoladamente sem executar o logging de verdade. Isso atrapalha testes unitários puros e pode deixar os testes mais lentos ou dependentes de recursos externos.

Impossibilidade de injeção de dependências

O código original não suporta injeção de dependências. Sem isso, qualquer mudança na forma de registrar ou rastrear operações exigiria mexer direto no código da classe.

Violação do princípio de responsabilidade única

Se precisássemos adicionar logging, teríamos que colocar essa responsabilidade direto na classe MathFunctions. Isso viola o princípio de responsabilidade única, onde cada classe deve ter apenas uma razão para mudar.

Problemas de Qualidade de Código

Baixa flexibilidade para evolução

O código estático é rígido. Qualquer mudança nas dependências ou na forma de rastrear operações exigiria modificar a classe principal, aumentando o risco de bugs.

Dificuldade para testes isolados

Testes unitários devem ser rápidos, isolados e não depender de recursos externos. Com métodos estáticos, isso fica difícil ou impossível. Se quiséssemos testar só a lógica matemática sem executar logging real, não teríamos como.

Falta de extensibilidade

O design original não permite adicionar novos tipos de rastreamento ou logging sem modificar o código existente. Isso vai contra o princípio de estender ao invés de modificar.

Impacto no desenvolvimento sustentável

Código difícil de testar é difícil de manter. Sem poder testar isoladamente, aumenta o risco de bugs passarem despercebidos. Código acoplado também é mais difícil de modificar com segurança quando precisamos evoluir o sistema.

--

PARTE 2: DESCRIÇÃO DETALHADA DAS ALTERAÇÕES REALIZADAS

Esta seção descreve cada mudança feita durante a refatoração, explicando o que foi alterado e as justificativas técnicas para cada decisão tomada.

Etapa 1: Criação da Interface MathLogger

O que foi feito: Criamos uma interface MathLogger com um método log que recebe o nome da operação e os parâmetros de entrada.

Justificativa técnica: Criar uma interface desacopla a classe MathFunctions de implementações concretas de logging. Segue o princípio de inversão de dependência, onde dependemos de abstrações ao invés de implementações concretas. Isso deixa o código mais flexível e facilita criar mocks para testes.

Código criado:

- Interface MathLogger com método log(String operation, int[] inputs)

Benefícios: Permite criar diferentes implementações de logger sem modificar a classe principal, facilita testes com mocks, e segue princípios SOLID.

Etapa 2: Refatoração dos Métodos de Estáticos para Instância

O que foi feito: Transformamos os métodos estáticos em métodos de instância. Adicionamos um construtor que recebe uma implementação de MathLogger. O logger fica armazenado como um campo privado final da classe.

Justificativa técnica: Converter para métodos de instância permite injeção de dependências via construtor. O campo final garante que o logger não pode ser alterado depois que o objeto é criado, mantendo a dependência imutável. Isso segue o padrão de injeção de dependência, deixando o código testável e flexível.

Mudanças específicas:

- Removido modificador static de todos os métodos
- Adicionado campo private final MathLogger logger
- Criado construtor public MathFunctions(MathLogger logger)
- Cada método agora chama logger.log() antes de retornar

Benefícios: Permite injeção de diferentes implementações de logger, deixa os testes mais simples e rápidos, e mantém a dependência imutável.

Etapa 3: Integração do Logger nos Métodos

O que foi feito: Adicionamos chamadas ao logger.log() em cada método matemático, registrando o nome da operação e os parâmetros de entrada.

Justificativa técnica: Registrar as operações permite rastreabilidade. A implementação é flexível porque depende da interface, não de uma implementação específica. Isso permite escolher entre logging em arquivo, em console, ou até mesmo logging nulo para testes.

Mudanças em cada método:

- multiplyByTwo: registra operação e número de entrada
- generateMultiplicationTable: registra operação, número base e limite
- isPrime: registra operação e número verificado (em todos os pontos de retorno)
- calculateAverage: registra operação e array de números

Benefícios: Rastreabilidade completa das operações, flexibilidade para diferentes tipos de logging, e mantém o comportamento matemático original.

Etapa 4: Criação da Implementação MockMathLogger

O que foi feito: Criamos MockMathLogger que implementa MathLogger mas não faz nada. É uma implementação vazia só para testes.

Justificativa técnica: Mocks são essenciais para testes unitários eficientes. Permitem testar a lógica matemática sem executar código de logging real, deixando os testes mais rápidos e isolados. MockMathLogger não tem dependências externas, então os testes não precisam criar arquivos ou fazer operações de I/O.

Código criado:

- Classe MockMathLogger que implementa MathLogger
- Método log() vazio que não faz nada

Benefícios: Testes mais rápidos, testes isolados sem dependências, e facilita executar grandes volumes de testes (como testes baseados em propriedades).

Etapa 5: Criação da Implementação FileMathLogger

O que foi feito: Criamos FileMathLogger que usa a classe Logg do professor para fazer logging real em arquivo. Ela formata as mensagens e registra com data e hora usando a classe de ajuda disponibilizada.

Justificativa técnica: FileMathLogger mostra como usar a refatoração em produção, integrando com as classes de ajuda do professor. Mostra que a refatoração permite flexibilidade - podemos ter logging real em produção e logging mock em testes. Isso segue o princípio de separação de responsabilidades.

Código criado:

- Classe FileMathLogger que implementa MathLogger
- Usa classesdeajuda.Logg para logging em arquivo
- Formata mensagens com operação e entradas

Benefícios: Integração com classes existentes, demonstra uso real, e mostra a flexibilidade do design refatorado.

Comparação Antes e Depois

Antes da refatoração:

- Métodos estáticos públicos
- Sem capacidade de injeção de dependências
- Impossível testar isoladamente
- Acoplamento direto com implementações

Depois da refatoração:

- Métodos de instância com injeção via construtor
- Interface MathLogger para abstração
- Fácil criação de mocks para testes
- Desacoplamento entre lógica matemática e logging
- Flexibilidade para diferentes implementações

--

PARTE 3: RESPOSTAS ARGUMENTATIVAS - PRINCÍPIOS APLICADOS E BOAS PRÁTICAS

Esta seção explica os princípios de engenharia de software aplicados durante a refatoração e sua relação com boas práticas de desenvolvimento sustentável.

Princípio de Inversão de Dependência (DIP)

Aplicação: A classe MathFunctions agora depende da abstração MathLogger (interface) ao invés de uma implementação concreta. Isso segue o princípio de inversão de dependência, onde módulos de alto nível não devem depender de módulos de baixo nível, ambos devem depender de abstrações.

Justificativa: Ao depender de uma interface, podemos trocar facilmente a implementação do logger sem modificar MathFunctions. Isso deixa o código mais flexível e testável. Em testes, usamos MockMathLogger. Em produção, podemos usar FileMathLogger ou qualquer outra implementação.

Relação com desenvolvimento sustentável: Código que depende de abstrações é mais fácil de evoluir e manter. Quando precisamos mudar como o logging funciona, não precisamos mexer na classe principal, reduzindo o risco de bugs.

Princípio de Responsabilidade Única (SRP)

Aplicação: MathFunctions agora tem apenas uma responsabilidade principal - executar operações matemáticas. A responsabilidade de logging foi separada para classes específicas (FileMathLogger, MockMathLogger).

Justificativa: A classe MathFunctions não precisa saber como fazer logging, só precisa registrar que uma operação foi executada. O "como" fazer o logging fica com as implementações específicas. Isso segue o princípio de responsabilidade única, onde cada classe tem apenas uma razão para mudar.

Relação com desenvolvimento sustentável: Código com responsabilidades bem definidas é mais fácil de entender, testar e modificar. Se precisarmos mudar como o logging funciona, não afetamos a lógica matemática, e vice-versa.

Injeção de Dependência (DI)

Aplicação: O logger é injetado via construtor, tornando as dependências explícitas e obrigatórias. O campo é final, garantindo que não pode ser alterado depois que o objeto é criado.

Justificativa: Injeção via construtor deixa as dependências claras e obrigatórias. O programador não pode esquecer de fornecer um logger, e o código fica mais testável porque podemos passar um mock facilmente. O campo final garante imutabilidade, evitando problemas de estado mutável.

Relação com desenvolvimento sustentável: Dependências explícitas deixam o código mais claro e previsível. É fácil ver o que a classe precisa para funcionar, e é fácil substituir essas dependências em testes. Isso reduz bugs e facilita manutenção.

Princípio Aberto/Fechado (OCP)

Aplicação: A classe MathFunctions está fechada para modificação mas aberta para extensão. Podemos criar novas implementações de MathLogger sem modificar MathFunctions.

Justificativa: Se precisarmos adicionar um novo tipo de logger (por exemplo, logger que envia para um servidor remoto), podemos simplesmente criar uma nova implementação de MathLogger. Não precisamos mexer em MathFunctions. Isso segue o princípio aberto/fechado.

Relação com desenvolvimento sustentável: Poder estender funcionalidades sem modificar código existente reduz o risco de bugs. O código existente continua funcionando enquanto adicionamos novas funcionalidades.

Testabilidade e Isolamento

Aplicação: Com a refatoração, podemos testar a lógica matemática isoladamente usando MockMathLogger, que não executa código real de logging.

Justificativa: Testes unitários devem ser rápidos, isolados e não depender de recursos externos. Com MockMathLogger, os testes executam só a lógica matemática, sem criar arquivos ou fazer operações de I/O. Isso é essencial para testes baseados em propriedades que executam milhares de vezes.

Relação com desenvolvimento sustentável: Testes rápidos e isolados incentivam escrever mais testes e executá-los frequentemente. Isso detecta bugs mais cedo e permite refatorar com confiança. Testes que dependem de recursos externos são mais lentos e podem falhar por razões que não têm nada a ver com o código testado.

Flexibilidade e Manutenibilidade

Aplicação: O design refatorado permite trocar facilmente a estratégia de logging sem modificar a lógica matemática.

Justificativa: Em desenvolvimento, podemos usar MockMathLogger para testes rápidos. Em produção, podemos usar FileMathLogger para logging real. Em testes de integração, podemos usar uma implementação que registra em banco de dados. Tudo isso sem mexer em MathFunctions.

Relação com desenvolvimento sustentável: Código flexível é mais fácil de adaptar a diferentes necessidades e ambientes. Isso reduz o custo de manutenção e facilita a evolução do sistema ao longo do tempo.

Imutabilidade e Thread Safety

Aplicação: O campo logger é final, garantindo que não pode ser alterado depois que o objeto é criado. Isso torna a classe thread-safe para leitura.

Justificativa: Campos finais em Java têm garantias de visibilidade entre threads. Como o logger não muda depois da criação, múltiplas threads podem usar a mesma instância de MathFunctions sem problemas de concorrência (assumindo que o logger seja thread-safe).

Relação com desenvolvimento sustentável: Código thread-safe é mais robusto e menos propenso a bugs difíceis de reproduzir. Imutabilidade torna o comportamento previsível e facilita raciocinar sobre o código.

Testes Baseados em Propriedades e Contraprovas

Aplicação: Os testes baseados em propriedades usando jqwik validam comportamentos matemáticos invariantes através de geração aleatória de dados. Durante a execução, o jqwik pode encontrar contraprovas (counterexamples) que violam as propriedades esperadas.

Exemplos de contraprovas que os testes podem identificar:

MultiplyByTwo: Se o método retornasse um número ímpar para alguma entrada, isso violaria a propriedade de que "resultado sempre é par". O jqwik geraria um contraexemplo mostrando qual entrada causou a violação.

GenerateMultiplicationTable: Se algum elemento da tabela não fosse múltiplo do número original, isso violaria a propriedade. O jqwik identificaria qual número e qual posição na tabela causaram o problema.

IsPrime: Se um número fosse identificado como primo mas tivesse divisores entre 2 e sqrt(n), isso violaria a propriedade. O jqwik mostraria qual número primo e qual divisor causaram a violação.

CalculateAverage: Se a média calculada fosse menor que o menor valor ou maior que o maior valor do array, isso violaria a propriedade. O jqwik geraria um contraexemplo com o array específico que causou a violação.

Discussão sobre causas de contraprovas:

Se um teste de propriedade encontrar uma contraprova, isso indica um bug real no código. Por exemplo, se MultiplyByTwo retornar um número ímpar, pode ser erro de implementação, problema de overflow, ou bug na lógica matemática. As contraprovas são valiosas porque mostram exatamente qual entrada causa o problema, facilitando o debug e a correção.

Os geradores personalizados (como primeNumbers(), extremeValues()) ajudam a garantir que os testes cubram casos relevantes e extremos. Se uma contraprova aparecer usando valores extremos, isso pode indicar problemas com limites ou overflow que não aparecem em testes normais.

Relação com desenvolvimento sustentável: Testes baseados em propriedades com contraprovas detectam bugs que testes unitários tradicionais podem não encontrar. O jqwik gera milhares de casos de teste automaticamente, aumentando a confiança no código sem precisar escrever manualmente cada caso possível. Quando uma contraprova é encontrada, ela fornece informações precisas sobre o bug, acelerando a correção.

Benefícios para Desenvolvimento Sustentável

A refatoração torna o código mais sustentável de várias formas:

Facilita evolução futura: Quando precisarmos adicionar novos tipos de rastreamento ou logging, não precisamos mexer em MathFunctions. Podemos criar novas implementações de MathLogger.

Reduz acoplamento: A classe MathFunctions não está mais acoplada a implementações específicas de logging. Isso deixa o código mais modular e reutilizável.

Melhora testabilidade: Com mocks, podemos testar rapidamente e isoladamente, o que incentiva escrever mais testes e aumenta a confiança no código.

Torna dependências explícitas: Fica claro que MathFunctions precisa de um logger para funcionar. Isso evita bugs causados por dependências implícitas ou esquecidas.

Facilita manutenção: Código com responsabilidades bem definidas e baixo acoplamento é mais fácil de entender e modificar com segurança.

--

CONCLUSÃO

A refatoração transformou um código com métodos estáticos em um design mais flexível e sustentável, seguindo princípios SOLID e práticas de desenvolvimento modernas. O código refatorado é mais testável, mais flexível e mais fácil de manter, enquanto preserva toda a funcionalidade matemática original.

As mudanças foram feitas de forma incremental, permitindo validar cada etapa através de testes. O uso de interfaces e injeção de dependência deixa o código preparado para evoluções futuras sem precisar modificar a classe principal.

Esta refatoração mostra como pequenas mudanças estruturais podem ter grande impacto na qualidade, testabilidade e manutenibilidade do código, seguindo princípios fundamentais de engenharia de software para desenvolvimento sustentável.
