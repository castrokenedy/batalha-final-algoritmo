package br.edu.batalha;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public final class CsvExporter {
    private CsvExporter() { }

    public static void export(Path directory, BenchmarkRunner.Results results) throws IOException {
        Files.createDirectories(directory);
        writeRaw(directory.resolve("resultados_brutos.csv"), results.raw());
        writeSummary(directory.resolve("resultados_consolidados.csv"), results.summary());
    }

    private static void writeRaw(Path file, List<BenchmarkRunner.RawResult> rows) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            w.write("algoritmo,tamanho,distribuicao,repeticao,warmup,tempo_ns,comparacoes,movimentacoes,delta_heap_bytes,valido\n");
            for (BenchmarkRunner.RawResult r : rows) {
                w.write(String.format(Locale.ROOT, "%s,%d,%s,%d,%s,%d,%d,%d,%d,%s%n",
                        r.algorithm(), r.size(), r.distribution(), r.repetition(), r.warmup(),
                        r.timeNs(), r.comparisons(), r.movements(), r.heapDeltaBytes(), r.valid()));
            }
        }
    }

    private static void writeSummary(Path file, List<BenchmarkRunner.SummaryResult> rows) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            w.write("algoritmo,tamanho,distribuicao,amostras,media_tempo_ns,dp_tempo_ns,media_comparacoes,dp_comparacoes,media_movimentacoes,dp_movimentacoes,media_delta_heap_bytes,dp_delta_heap_bytes\n");
            for (BenchmarkRunner.SummaryResult r : rows) {
                w.write(String.format(Locale.ROOT,
                        "%s,%d,%s,%d,%.6f,%.6f,%.6f,%.6f,%.6f,%.6f,%.6f,%.6f%n",
                        r.algorithm(), r.size(), r.distribution(), r.samples(),
                        r.meanTimeNs(), r.sdTimeNs(), r.meanComparisons(), r.sdComparisons(),
                        r.meanMovements(), r.sdMovements(), r.meanHeapDeltaBytes(), r.sdHeapDeltaBytes()));
            }
        }
    }
}
