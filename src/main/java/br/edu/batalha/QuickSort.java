package br.edu.batalha;

/** Particionamento Lomuto; mediana de três padrão, último elemento como alternativa clássica. */
public final class QuickSort extends AbstractSortingAlgorithm {
    public enum PivotStrategy { LAST_ELEMENT, MEDIAN_OF_THREE }
    private final PivotStrategy strategy;

    public QuickSort() { this(PivotStrategy.MEDIAN_OF_THREE); }
    public QuickSort(PivotStrategy strategy) {
        this.strategy = java.util.Objects.requireNonNull(strategy);
    }

    @Override protected void sortInternal(int[] a) {
        if (a.length > 1) quickSort(a, 0, a.length - 1);
    }

    private void quickSort(int[] a, int lo, int hi) {
        while (lo < hi) {
            int p = partition(a, lo, hi);
            // Recursão somente sobre a menor partição: profundidade O(log n),
            // mesmo quando o pivô produz partições extremamente desequilibradas.
            if (p - lo < hi - p) {
                quickSort(a, lo, p - 1);
                lo = p + 1;
            } else {
                quickSort(a, p + 1, hi);
                hi = p - 1;
            }
        }
    }

    private int partition(int[] a, int lo, int hi) {
        if (strategy == PivotStrategy.MEDIAN_OF_THREE) {
            int mid = lo + (hi - lo) / 2;
            int pivotIndex = medianIndex(a, lo, mid, hi);
            swap(a, pivotIndex, hi);
        }
        int pivot = a[hi];
        int boundary = lo;
        for (int j = lo; j < hi; j++) {
            if (lessOrEqual(a[j], pivot)) {
                swap(a, boundary, j);
                boundary++;
            }
        }
        swap(a, boundary, hi);
        return boundary;
    }

    private int medianIndex(int[] a, int x, int y, int z) {
        if (less(a[x], a[y])) {
            if (less(a[y], a[z])) return y;
            return less(a[x], a[z]) ? z : x;
        }
        if (less(a[x], a[z])) return x;
        return less(a[y], a[z]) ? z : y;
    }
}
