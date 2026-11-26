package br.edu.batalha;

public final class BubbleSort extends AbstractSortingAlgorithm {
    @Override protected void sortInternal(int[] a) {
        for (int end = a.length - 1; end > 0; end--) {
            boolean changed = false;
            for (int i = 0; i < end; i++) {
                if (greater(a[i], a[i + 1])) {
                    swap(a, i, i + 1);
                    changed = true;
                }
            }
            if (!changed) break;
        }
    }
}
