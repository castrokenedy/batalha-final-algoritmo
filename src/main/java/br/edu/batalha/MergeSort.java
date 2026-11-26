package br.edu.batalha;

public final class MergeSort extends AbstractSortingAlgorithm {
    @Override protected void sortInternal(int[] a) {
        if (a.length < 2) return;
        int[] aux = new int[a.length];
        mergeSort(a, aux, 0, a.length);
    }

    private void mergeSort(int[] a, int[] aux, int lo, int hi) {
        if (hi - lo < 2) return;
        int mid = lo + (hi - lo) / 2;
        mergeSort(a, aux, lo, mid);
        mergeSort(a, aux, mid, hi);
        int i = lo, j = mid;
        for (int k = lo; k < hi; k++) {
            if (i == mid) write(aux, k, a[j++]);
            else if (j == hi) write(aux, k, a[i++]);
            else if (lessOrEqual(a[i], a[j])) write(aux, k, a[i++]);
            else write(aux, k, a[j++]);
        }
        for (int k = lo; k < hi; k++) write(a, k, aux[k]);
    }
}
