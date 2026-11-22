package br.edu.batalha;

import java.util.Objects;

/** Regra de contagem: comparação entre valores, escrita em posição de vetor (inclusive auxiliar). */
public abstract class AbstractSortingAlgorithm implements SortingAlgorithm {
    protected long comparisons;
    protected long movements;
    private long executionTimeNs;

    @Override
    public final void sort(int[] array) {
        Objects.requireNonNull(array, "array não pode ser null");
        comparisons = 0L;
        movements = 0L;
        executionTimeNs = 0L;
        long start = System.nanoTime();
        try {
            sortInternal(array);
        } finally {
            executionTimeNs = System.nanoTime() - start;
        }
    }

    protected abstract void sortInternal(int[] array);

    protected final boolean greater(int a, int b) {
        comparisons++;
        return a > b;
    }

    protected final boolean less(int a, int b) {
        comparisons++;
        return a < b;
    }

    protected final boolean lessOrEqual(int a, int b) {
        comparisons++;
        return a <= b;
    }

    protected final void write(int[] array, int index, int value) {
        array[index] = value;
        movements++;
    }

    protected final void swap(int[] array, int i, int j) {
        if (i == j) return;
        int t = array[i];
        write(array, i, array[j]);
        write(array, j, t);
    }

    @Override public final long getComparisons() { return comparisons; }
    /** Contrato histórico 'getSwaps': retorna escritas/movimentações, não permutas. */
    @Override public final long getSwaps() { return movements; }
    @Override public final long getExecutionTime() { return executionTimeNs; }

    @Override
    public final boolean isSorted(int[] array) {
        Objects.requireNonNull(array, "array não pode ser null");
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) return false;
        }
        return true;
    }
}
