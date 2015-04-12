package com.example.jbstrand.probabilisticchess;

public class ChessEngine {
	public static int computerColour = Chess.WHITE;
	private static Random randy = new Random();
	
	public static void setColour(int colour) {
		computerColour = colour;
	}
	public static boolean computerMove() {
		boolean status = false;
		int start, stop;
		int randStart, randStop;
		int [][]takes = new int[16][10];
		int [][]empty = new int[16][3];
		int i,j,k,l;
		int [][][]options = new int[16][65][2] ;
		k=0;
		takes[k][1] = 0;
		i=0;
		for (start = 0; start < 64; start++) {
			if (Chess.colour(Chess.getRow(start), Chess.getCol(start))== computerColour) {	
				options[i] = Chess.getOptions(computerColour, start);
				if (options[i][0][0] > 0) {
					empty[i][0] = start;
					empty[i][1] = options[i][0][0];	
					empty[i][2] = i;	
					l = 2;
					for (j = 1; j <= options[i][0][0]&& j<=64; j++) {
						if (options[i][j][1] == 1) {
							takes[k][0] = start;
							takes[k][1]++;	
							takes[k][l] = options[i][j][0];	
							l++;
						}
					}
					if (takes[k][1] > 0) {
						k++;
						takes[k][1] = 0;	
					}
					i++;
				}
			}
		}
		if (k>0) {
			randStart = randy.nextInt()%k;		
			start = takes[randStart][0];	
			randStop = (randy.nextInt()%takes[randStart][1]) + 2;
			stop = takes[randStart][randStop];				
		} else {
			randStart = randy.nextInt()%i;		
			start = empty[randStart][0];	
			randStop = randy.nextInt()%empty[randStart][1]+1;		
			stop = options[empty[randStart][2]][randStop][0];
		}
		status = Chess.moveB(computerColour,start, stop);
		return status;
	}	
	
	/*
	//Random Move Selection
	public static boolean computerMove() {
		boolean status = false;
		int start, stop;
		int randStart, randStop;
		int [][]takes = new int[16][10];
		int [][]empty = new int[16][3];
		int i,j,k,l;
		int [][][]options = new int[16][65][2] ;
		k=0;
		takes[k][1] = 0;
		i=0;
		for (start = 0; start < 64; start++) {
			if (Chess.colour(Chess.getRow(start), Chess.getCol(start))== computerColour) {	
				options[i] = Chess.getOptions(computerColour, start);
				if (options[i][0][0] > 0) {
					empty[i][0] = start;
					empty[i][1] = options[i][0][0];	
					empty[i][2] = i;	
					l = 2;
					for (j = 1; j <= options[i][0][0]&& j<=64; j++) {
						if (options[i][j][1] == 1) {
							takes[k][0] = start;
							takes[k][1]++;	
							takes[k][l] =options[i][j][0];	
							l++;
						}
					}
					if (takes[k][1] > 0) {
						k++;
						takes[k][1] = 0;	
					}
					i++;
				}
			}
		}
		if (k>0) {
			randStart = randy.nextInt()%k;		
			start = takes[randStart][0];	
			randStop = (randy.nextInt()%takes[randStart][1]) + 2;
			stop = takes[randStart][randStop];				
		} else {
			randStart = randy.nextInt()%i;		
			start = empty[randStart][0];	
			randStop = randy.nextInt()%empty[randStart][1]+1;		
			stop = options[empty[randStart][2]][randStop][0];
		}
		status = Chess.moveB(computerColour,start, stop);
		return status;
	} */	
}
