package br.edu.batalha;

import java.nio.file.Path;
import java.util.Arrays;

public final class Main {
    private Main() { }

    public static void main(String[] args) throws Exception {
        boolean smoke = Arrays.asList(args).contains("--smoke");
        boolean four = Arrays.asList(args).contains("--four");
        boolean classic = Arrays.asList(args).contains("--classic-pivot");
        if (Arrays.asList(args).contains("--help")) {
            System.out.println("Uso: java br.edu.batalha.Main [--smoke] [--four] [--classic-pivot]");
            return;
        }
        int[] sizes = smoke ? new int[]{100, 250} :
                new int[]{100, 1_000, 5_000, 10_000, 50_000, 100_000};
        int quadraticLimit = 50_000;
        QuickSort.PivotStrategy pivot = classic
                ? QuickSort.PivotStrategy.LAST_ELEMENT
                : QuickSort.PivotStrategy.MEDIAN_OF_THREE;
        System.out.println("Configuração: " + (four ? "4" : "5") +
                " algoritmos; pivot=" + pivot + "; testes=" + (smoke ? "smoke" : "completos"));
        BenchmarkRunner runner = new BenchmarkRunner(!four, pivot);
        BenchmarkRunner.Results results = runner.run(sizes, quadraticLimit);
        Path output = Path.of("resultados");
        CsvExporter.export(output, results);
        System.out.println("CSV gravado em: " + output.toAbsolutePath());
        System.out.println("Execuções: " + results.raw().size() +
                "; agrupamentos: " + results.summary().size());
    }
}
