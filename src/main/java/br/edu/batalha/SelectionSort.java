package br.edu.batalha;

public final class SelectionSort extends AbstractSortingAlgorithm {
    @Override protected void sortInternal(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (less(a[j], a[min])) min = j;
            }
            swap(a, i, min);
        }
    }
}
