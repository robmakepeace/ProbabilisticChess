package com.example.jbstrand.probabilisticchess;

/**
 * Created by jbstrand on 18/04/2015.
 */
public class backend_square {
    public int X;
    public int Y;

    public backend_square(int new_X, int new_Y) {
        if (checkXY(new_X, new_Y)) {
            X = new_X;
            Y = new_Y;
        }
    }

    public backend_square(String str) {
        int temp_X = (int) str.charAt(0) - (int) 'a';
        int temp_Y = str.charAt(1) - 1;
        if (checkXY(temp_X, temp_Y)) {
            X = temp_X;
            Y = temp_Y;
        }
    }

    private boolean checkXY(int X, int Y) {
        if (X >= 0 && X <= 7 && Y >= 0 && Y <= 7) {
            return true;
        } else {
            return false;
        }
    }

}