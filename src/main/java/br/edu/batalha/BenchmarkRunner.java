package br.edu.batalha;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public final class BenchmarkRunner {
    public record AlgorithmSpec(String name, Supplier<SortingAlgorithm> factory, boolean quadratic) { }
    public record RawResult(String algorithm, int size, String distribution, int repetition,
                            boolean warmup, long timeNs, long comparisons,
                            long movements, long heapDeltaBytes, boolean valid) { }
    public record SummaryResult(String algorithm, int size, String distribution,
                                int samples, double meanTimeNs, double sdTimeNs,
                                double meanComparisons, double sdComparisons,
                                double meanMovements, double sdMovements,
                                double meanHeapDeltaBytes, double sdHeapDeltaBytes) { }
    public record Results(List<RawResult> raw, List<SummaryResult> summary) { }

    private final List<AlgorithmSpec> specs;
    private final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

    public BenchmarkRunner(boolean includeBubble, QuickSort.PivotStrategy pivot) {
        specs = new ArrayList<>();
        if (includeBubble) specs.add(new AlgorithmSpec("BubbleSort", BubbleSort::new, true));
        specs.add(new AlgorithmSpec("InsertionSort", InsertionSort::new, true));
        specs.add(new AlgorithmSpec("SelectionSort", SelectionSort::new, true));
        specs.add(new AlgorithmSpec("MergeSort", MergeSort::new, false));
        specs.add(new AlgorithmSpec("QuickSort_" + pivot, () -> new QuickSort(pivot), false));
    }

    public Results run(int[] sizes, int quadraticLimit) {
        List<RawResult> raw = new ArrayList<>();
        List<SummaryResult> summary = new ArrayList<>();
        for (int size : sizes) {
            for (DataGenerator.Distribution distribution : DataGenerator.Distribution.values()) {
                int[] original = DataGenerator.generate(size, distribution);
                int[] expected = Arrays.copyOf(original, original.length);
                Arrays.sort(expected);
                for (AlgorithmSpec spec : specs) {
                    if (spec.quadratic() && size > quadraticLimit) continue;
                    List<RawResult> batch = new ArrayList<>();
                    for (int repetition = 1; repetition <= 5; repetition++) {
                        int[] copy = Arrays.copyOf(original, original.length);
                        SortingAlgorithm algorithm = spec.factory().get();
                        // Mede heap 'used' antes e depois; não é pico nem alocação total.
                        long before = memoryBean.getHeapMemoryUsage().getUsed();
                        algorithm.sort(copy);
                        long after = memoryBean.getHeapMemoryUsage().getUsed();
                        boolean valid = algorithm.isSorted(copy) && Arrays.equals(copy, expected);
                        RawResult point = new RawResult(spec.name(), size, distribution.name(),
                                repetition, repetition == 1, algorithm.getExecutionTime(),
                                algorithm.getComparisons(), algorithm.getSwaps(), after - before, valid);
                        if (!valid) throw new IllegalStateException("Ordenação inválida: " + point);
                        raw.add(point);
                        if (repetition > 1) batch.add(point);
                    }
                    summary.add(summarize(spec.name(), size, distribution.name(), batch));
                    System.out.printf(Locale.ROOT, "%-27s n=%-7d %-10s média=%.3f ms%n",
                            spec.name(), size, distribution, summary.get(summary.size() - 1).meanTimeNs() / 1e6);
                }
            }
        }
        return new Results(List.copyOf(raw), List.copyOf(summary));
    }

    private SummaryResult summarize(String algorithm, int size, String distribution, List<RawResult> sample) {
        return new SummaryResult(algorithm, size, distribution, sample.size(),
                mean(sample, 0), sd(sample, 0), mean(sample, 1), sd(sample, 1),
                mean(sample, 2), sd(sample, 2), mean(sample, 3), sd(sample, 3));
    }

    private double value(RawResult row, int field) {
        return switch (field) {
            case 0 -> row.timeNs();
            case 1 -> row.comparisons();
            case 2 -> row.movements();
            case 3 -> row.heapDeltaBytes();
            default -> throw new IllegalArgumentException("campo inválido");
        };
    }

    private double mean(List<RawResult> sample, int field) {
        return sample.stream().mapToDouble(r -> value(r, field)).average().orElseThrow();
    }

    /** Desvio padrão amostral: divisor k-1, com k=4. */
    private double sd(List<RawResult> sample, int field) {
        double mean = mean(sample, field);
        double sum = sample.stream().mapToDouble(r -> Math.pow(value(r, field) - mean, 2)).sum();
        return Math.sqrt(sum / (sample.size() - 1));
    }
}
