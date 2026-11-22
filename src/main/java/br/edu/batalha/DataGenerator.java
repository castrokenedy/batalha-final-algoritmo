package br.edu.batalha;

import java.util.Random;

public final class DataGenerator {
    public enum Distribution { ALEATORIO, ORDENADO, INVERSO }
    private DataGenerator() { }

    public static int[] generate(int size, Distribution distribution) {
        if (size < 0) throw new IllegalArgumentException("size negativo");
        int[] a = new int[size];
        switch (distribution) {
            case ALEATORIO -> {
                Random random = new Random(42L);
                for (int i = 0; i < size; i++) a[i] = random.nextInt();
            }
            case ORDENADO -> {
                for (int i = 0; i < size; i++) a[i] = i;
            }
            case INVERSO -> {
                for (int i = 0; i < size; i++) a[i] = size - i;
            }
        }
        return a;
    }
}
