# Batalha dos Algoritmos de Ordenação

**Projeto Final · Projeto e Análise de Algoritmos · 2026**  
**Professor:** Alexandre Barbosa de Souza · **Instituição:** UniSales

> Um estudo experimental sobre o desempenho, a correção e os custos computacionais de cinco algoritmos clássicos de ordenação, utilizando Java e análise de dados em Python.

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white) ![Python](https://img.shields.io/badge/Python-3.13-3776AB?logo=python&logoColor=white) ![Padrão](https://img.shields.io/badge/Design%20Pattern-Strategy-5A67D8) ![Licença](https://img.shields.io/badge/uso-acad%C3%AAmico-lightgrey)

## Sobre o projeto

Implementamos e comparamos os algoritmos **Bubble Sort, Insertion Sort, Selection Sort, Merge Sort e Quick Sort** sobre as mesmas entradas, avaliando **tempo**, **comparações entre valores**, **movimentações** e **variação observada do heap da JVM**. Três distribuições são estudadas: aleatória uniforme, crescente e decrescente.

A arquitetura usa a interface `SortingAlgorithm` (padrão **Strategy**) e uma classe-base para instrumentação. Assim, cada estratégia pode ser implementada e testada separadamente sem alterar o contrato comum.

## Situação da integração no repositório

O trabalho está organizado em entregas individuais via Pull Requests; esta tabela será atualizada à medida que as mudanças forem **efetivamente mescladas à `main`**.

| Etapa | Responsável | Escopo | Situação |
|---|---|---|---|
| 1 | Kenedy | Contrato `SortingAlgorithm`, classe-base, `InsertionSort` e gerador de dados | **Em preparação / PR** |
| 2 | Artur | `SelectionSort`, `MergeSort`, benchmark e exportação CSV | A integrar |
| 3 | Ketleen | `BubbleSort`, `QuickSort`, validação automatizada e gráficos Python | A integrar |
| 4 | Equipe | Relatório acadêmico, evidências, revisão, apresentação e entrega | A integrar |

> **Nota acadêmica:** o enunciado prevê grupos de quatro ou cinco integrantes, com um algoritmo por participante. A distribuição acima representa as três frentes conhecidas no momento; a composição final e a atribuição individual de algoritmos devem ser confirmadas com o professor. Commits e autoria devem refletir apenas trabalhos realizados por quem os assina.

## Algoritmos e características

| Algoritmo | Melhor caso | Caso médio | Pior caso | Espaço auxiliar | Estável? |
|---|---|---|---|---|---|
| Bubble Sort (com parada antecipada) | Ω(n) | Θ(n²) | O(n²) | O(1) | Sim |
| Insertion Sort | Ω(n) | Θ(n²) | O(n²) | O(1) | Sim |
| Selection Sort | Θ(n²) | Θ(n²) | Θ(n²) | O(1) | Não |
| Merge Sort | Θ(n log n) | Θ(n log n) | Θ(n log n) | O(n) | Sim |
| Quick Sort (mediana de três) | Ω(n log n) | Θ(n log n), típico | O(n²) | Pilha dependente da implementação | Não |

As classificações se referem às implementações e hipóteses analisadas no relatório, não constituem garantia de tempo de parede em qualquer máquina.

## Protocolo experimental

- **Tamanhos:** 100, 1.000, 5.000, 10.000, 50.000 e 100.000 elementos.
- **Algoritmos quadráticos:** até 50.000 elementos.
- **Distribuições:** aleatória, ordenada e inversamente ordenada.
- **Reprodutibilidade:** `Random(42L)` e cópias idênticas (`Arrays.copyOf`) para cada estratégia.
- **Repetições:** cinco; a primeira como *warm-up*, descartada dos cálculos finais; quatro para média e desvio padrão amostral.
- **Corretude:** `isSorted()` e comparação com `Arrays.sort()`.

O protocolo de cinco algoritmos produz **405 execuções brutas**, incluindo *warm-ups*, e **81 agrupamentos**, contendo quatro amostras válidas cada. Esses números são esperados **após a integração de todas as etapas**, não resultados da branch inicial.

### Definição das métricas

| Métrica | Significado |
|---|---|
| `tempo_ns` | Tempo apenas do método de ordenação medido com `System.nanoTime()` |
| `comparacoes` | Comparações entre valores do vetor |
| `movimentacoes` | Escritas em posições de vetores; não equivalem necessariamente a permutas |
| `delta_heap_bytes` | Diferença de heap usado antes e depois, sujeita a ruído de GC; **não é pico de memória** |

## Ambiente de referência do experimento

A execução completa previamente registrada pela equipe usou o seguinte equipamento. Os **CSVs e gráficos correspondentes serão publicados em uma entrega posterior**, após revisão dos arquivos originais.

| Componente | Configuração |
|---|---|
| CPU | AMD Ryzen 5 3400G — 4 núcleos, 8 threads |
| RAM | 13,92 GB |
| JVM | Java HotSpot 64-bit, Java 21.0.10 LTS |
| Python | 3.13.5 |
| Sistema operacional | Windows (versão a registrar) |

Foram observados 405 resultados marcados `valido=true` e 81 agrupamentos na execução de referência. A publicação dos arquivos permite auditoria independente das medidas e das conclusões.

## Resultados e visualizações

Os resultados definitivos serão expostos em quatro dimensões para cada cenário: **tempo**, **comparações**, **movimentações** e **memória observada** (12 gráficos em escala log-log). Para evitar imagens quebradas nesta primeira entrega, as figuras serão incorporadas quando a etapa de gráficos for mesclada.

**Discussão preliminar (qualitativa):** algoritmos quadráticos tendem a crescer mais rapidamente para entradas grandes. Insertion e Bubble (com otimização) podem beneficiar-se de vetores já ordenados; Merge e Quick, por sua vez, costumam apresentar melhor escalabilidade, com diferenças práticas influenciadas por cache, predição de desvios, JIT e GC. Os valores numéricos e o ranking definitivo dependerão dos CSVs publicados.

## Estrutura do projeto ao final da integração

```text
src/main/java/br/edu/batalha/
  SortingAlgorithm.java
  AbstractSortingAlgorithm.java
  BubbleSort.java
  InsertionSort.java
  SelectionSort.java
  MergeSort.java
  QuickSort.java
  DataGenerator.java
  BenchmarkRunner.java
  CsvExporter.java
  Main.java
src/test/java/br/edu/batalha/SortValidatorTest.java
scripts/gerar_graficos.py
resultados/
graficos/
docs/
README.md
requirements.txt
```

**Na etapa 1**, apenas `SortingAlgorithm.java`, `AbstractSortingAlgorithm.java`, `InsertionSort.java` e `DataGenerator.java` estão disponíveis. Os comandos de benchmark abaixo só funcionarão **após a integração dos módulos restantes**.

## Como executar

### Etapa 1 — validar compilação da base (Windows PowerShell)

Requer **JDK 21** (ou compatível) e execução no diretório raiz do projeto:

```powershell
New-Item -ItemType Directory -Force build/classes | Out-Null
javac -encoding UTF-8 -d build/classes `
  src/main/java/br/edu/batalha/SortingAlgorithm.java `
  src/main/java/br/edu/batalha/AbstractSortingAlgorithm.java `
  src/main/java/br/edu/batalha/InsertionSort.java `
  src/main/java/br/edu/batalha/DataGenerator.java
```

> A base **compila, mas não possui ainda um `Main` executável** nesta etapa. Isso é intencional: integração progressiva com PRs independentes.

### Após a integração — testes e benchmark completo

```powershell
New-Item -ItemType Directory -Force build/classes | Out-Null
javac -encoding UTF-8 -d build/classes (Get-ChildItem src/main/java/br/edu/batalha/*.java).FullName (Get-ChildItem src/test/java/br/edu/batalha/*.java).FullName
java -cp build/classes br.edu.batalha.SortValidatorTest
java -cp build/classes br.edu.batalha.Main --help
java -cp build/classes br.edu.batalha.Main
```

### Após a integração — gráficos

```powershell
python -m pip install -r requirements.txt
python scripts/gerar_graficos.py
```

**Atenção:** execuções de teste e benchmarks podem sobrescrever os CSVs. Faça cópias antes de repetir experimentos; não inclua `.venv/` ou `build/` no repositório.

## Fluxo de colaboração

Cada integrante deve criar uma branch a partir da `main` atualizada, fazer commits com autoria própria, abrir um PR para revisão e mesclá-lo após validação. Exemplos de nomes:

- `feat/kenedy-base`
- `feat/artur-benchmark`
- `feat/ketleen-integracao`

Convenção de commits: `feat(core): adicionar interface de ordenacao`, `test(sort): validar casos de borda`, `docs(readme): documentar resultados experimentais`.

## Entregáveis acadêmicos

Código-fonte no GitHub, relatórios, CSVs com resultados brutos e consolidados, gráficos, slides e demonstração técnica. A documentação final seguirá o enunciado do Projeto Final e os modelos fornecidos pelo professor, incluindo evidências reais de execução e contribuições individuais.

---

**Projeto acadêmico em desenvolvimento.** Os créditos finais, o(s) integrante(s) adicional(is) e os links para os documentos serão preenchidos e revisados pela equipe antes da entrega.
