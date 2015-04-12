package com.example.jbstrand.probabilisticchess;


public class Random {
	public static final int A = 48271;
	public static final int M = 2147483647;
	public static final int Q = M / A;
	public static final int R = M % A;

		
	public Random() {
		this((int)(System.currentTimeMillis()%Integer.MAX_VALUE));
	}	
	public Random(int initialValue) {
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
