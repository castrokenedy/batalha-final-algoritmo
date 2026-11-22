package br.edu.batalha;

public final class InsertionSort extends AbstractSortingAlgorithm {
    @Override protected void sortInternal(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= 0 && greater(a[j], key)) {
                write(a, j + 1, a[j]);
                j--;
            }
            if (j + 1 != i) write(a, j + 1, key);
        }
    }
}
