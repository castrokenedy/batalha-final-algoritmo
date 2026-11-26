package br.edu.batalha;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/** Suíte independente de JUnit: execute com -ea. Falhas lançam AssertionError. */
public final class SortValidatorTest {
    private record Candidate(String name, Supplier<SortingAlgorithm> factory) { }
    private static final List<Candidate> ALGORITHMS = List.of(
            new Candidate("BubbleSort", BubbleSort::new),
            new Candidate("InsertionSort", InsertionSort::new),
            new Candidate("SelectionSort", SelectionSort::new),
            new Candidate("MergeSort", MergeSort::new),
            new Candidate("QuickSortMedian", QuickSort::new),
            new Candidate("QuickSortClassico", () -> new QuickSort(QuickSort.PivotStrategy.LAST_ELEMENT)));

    private SortValidatorTest() { }

    public static void main(String[] args) {
        int[][] cases = {
                {}, {42}, {7, 7, 7, 7}, {-2, 0, -10, 5, -2},
                {1, 2, 3, 4, 5}, {5, 4, 3, 2, 1},
                {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, -1, 1},
                {3, 1, 2, 3, 0, 0, -9, 12}
        };
        int checks = 0;
        for (Candidate candidate : ALGORITHMS) {
            for (int[] input : cases) {
                verify(candidate.factory().get(), input, candidate.name());
                checks++;
            }
            Random rnd = new Random(42L);
            for (int i = 0; i < 25; i++) {
                int[] input = new int[rnd.nextInt(128)];
                for (int j = 0; j < input.length; j++) input[j] = rnd.nextInt(21) - 10;
                verify(candidate.factory().get(), input, candidate.name());
                checks++;
            }
            // Garante que as métricas são reiniciadas mesmo ao reutilizar instância.
            SortingAlgorithm reused = candidate.factory().get();
            reused.sort(new int[]{2, 1});
            reused.sort(new int[]{});
            require(reused.getComparisons() == 0 && reused.getSwaps() == 0,
                    "contadores não resetados: " + candidate.name());
            checks++;
        }
        boolean threw = false;
        try { new BubbleSort().sort(null); }
        catch (NullPointerException ex) { threw = true; }
        require(threw, "null deveria ser rejeitado");
        System.out.println("PASSOU: " + checks + " casos + null; 6 variantes.");
    }

    private static void verify(SortingAlgorithm algorithm, int[] input, String name) {
        int[] actual = Arrays.copyOf(input, input.length);
        int[] expected = Arrays.copyOf(input, input.length);
        Arrays.sort(expected);
        algorithm.sort(actual);
        require(algorithm.isSorted(actual), name + " não ordenou: " + Arrays.toString(input));
        require(Arrays.equals(expected, actual), name + " divergiu de Arrays.sort");
        require(algorithm.getComparisons() >= 0 && algorithm.getSwaps() >= 0 &&
                algorithm.getExecutionTime() >= 0, name + " métricas inválidas");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
