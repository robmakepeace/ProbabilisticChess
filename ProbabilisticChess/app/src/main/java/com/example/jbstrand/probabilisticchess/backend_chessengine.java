package com.example.jbstrand.probabilisticchess;

public class backend_chessengine {
    public static backend_pieceColour computerColour = backend_pieceColour.White;
    private static backend_random randy = new backend_random();

    public static void setColour(backend_pieceColour colour) {
        computerColour = colour;
    }

    public static boolean computerMove(backend_board board) {
        boolean status = false;
        backend_square start = new backend_square(0, 0);
        backend_square stop = new backend_square(0, 0);
        backend_piece start_piece;
        int x, y, total;

        int randStart, randStop;
        int[][] takes = new int[1000][4];
        int[][] empty = new int[16][3];
        int i, k;
        i = 0;
        k = 0;
        int[][][] options = new int[16][8][8];
        for (start.X = 0; start.X < 8; start.X++) {
            for (start.Y = 0; start.Y < 8; start.Y++) {
                start_piece = board.get_Board(start);
                if (start_piece.get_pieceType() != backend_pieceType.None) {
                    if (start_piece.get_pieceColour() == computerColour) {
                        options[i] = backend_move.getOptions(board, start);
                        total = 0;
                        for (x = 0; x < 8; x++) {
                            for (y = 0; y < 8; y++) {
                                total += options[i][x][y];
                            }
                        }
                        //for each piece record number of options
                        empty[i][0] = start.X;
                        empty[i][1] = start.Y;
                        empty[i][2] = total;
                        if (total > 0) {
                            for (stop.X = 0; stop.X < 8; stop.X++) {
                                for (stop.Y = 0; stop.Y < 8; stop.Y++) {
                                    if (options[i][stop.X][stop.Y] == 1) {
                                        //record all possible movements
                                        takes[k][0] = start.X;
                                        takes[k][1] = start.Y;
                                        takes[k][2] = stop.X;
                                        takes[k][3] = stop.Y;
                                    }
                                    k++;
                                }
                            }
                            i++;
                        }
                    }
                }
            }
        }
        if (k > 0) {
            //randomlly move based on options
            randStart = randy.nextInt() % k;
            start.X = takes[randStart][0];
            start.X = takes[randStart][1];
            stop.X = takes[randStart][2];
            stop.X = takes[randStart][3];
        }
        status = backend_move.moveB(board, start, stop);
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
