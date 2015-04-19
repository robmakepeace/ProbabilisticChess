package com.example.jbstrand.probabilisticchess;

public class backend_random {
    public static final int A = 48271;
    public static final int M = 2147483647;
    public static final int Q = M / A;
    public static final int R = M % A;


    public backend_random() {
        this((int) (System.currentTimeMillis() % Integer.MAX_VALUE));
    }

    public backend_random(int initialValue) {
        if (initialValue < 0)
            initialValue += M;
        state = initialValue;
        if (state == 0)
            state = 1;
    }

    public int nextInt() {
        int tmpState = A * (state % Q) - R * (state / Q);
        if (tmpState >= 0)
            state = tmpState;
        else
            state = tmpState + M;
        return state;
    }

    private int state;
}
