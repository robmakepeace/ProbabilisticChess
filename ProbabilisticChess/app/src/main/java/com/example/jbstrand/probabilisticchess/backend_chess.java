package com.example.jbstrand.probabilisticchess;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class backend_chess {

	/**
	 * @param args
	 */
	//Computer Chess
	//Only deal with check
	/*
	 * Pieces 15/8/5/3/3/1
	 * Mobility (available moves)
	 * King Protection
	 * Isolated Pawns (Double, backward,isolated)
	 * evaluative function = 200(K-k)+9(Q-q)+5(R-r)+3(B-b+N-n)+(P-p)-0.5(D-d+S-s+I-i) + 0.1(M-m)
	 * rows 1-8(bottom) cols a-h(left)
	 * 
	 */

	public static boolean GAME_TYPE;

	public static boolean GAME_OPTIONS;
    public static boolean GAME_THREATS;

	public static int ONE_PLAYER = 0;
	public static int TWO_PLAYER = 1;
	public static int CHESS_WITH_FRIENDS = 2;
	public static int GAME_MODE = TWO_PLAYER;

	public static int PLAYING = 0;
	public static int WON = 1;
	public static int GAME_STATUS = PLAYING;

	public static activity_game game;
	static void setGameActivity(activity_game gameActivity) {
		game = gameActivity;
	}
	public static int BLACK = 1;
	public static int WHITE = 2;
	public static int BLACK_PAWN = 1;
	public static int BLACK_ROOK = 2;
	public static int BLACK_KNIGHT = 3;
	public static int BLACK_BISHOP = 4;
	public static int BLACK_QUEEN = 5;
	public static int BLACK_KING = 6;
	public static int WHITE_PAWN = 7;
	public static int WHITE_ROOK = 8;
	public static int WHITE_KNIGHT = 9;
	public static int WHITE_BISHOP = 10;
	public static int WHITE_QUEEN = 11;
	public static int WHITE_KING = 12;

	private static int BLACK_CASTLE;
	private static int WHITE_CASTLE;
	public static int [][] board = new int[8][8];
	private static int [][] checkBoard = new int[8][8];
	private static int [][] list = new int[32][3];

	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	private static backend_random randy = new backend_random();

	public static boolean moveA(int colour, int start) {
		int curx = 0;
		int cury = 0;
		boolean returnValue = false;
		copyBoard(false);

		curx = getRow(start);
		cury = getCol(start);
		if ((checkInit(colour, curx, cury))) {
			returnValue = true;
		}
		return returnValue;
	}
	public static boolean moveB(int colour, int start, int stop) {
		int curx = 0;
		int cury = 0;
		int newx = 0;
		int newy = 0;
		boolean returnValue = false;
		copyBoard(false);

		curx = getRow(start);
		cury = getCol(start);
		newx = getRow(stop);
		newy = getCol(stop);
		if ((checkInit(colour, curx, cury) && checkDest(colour, curx, cury, newx, newy))) {
			returnValue = true;
		}
		return returnValue;
	}
	public static boolean checkInit(int colour, int x, int y) {
		boolean status = false;
		if (x >= 0 && x <8 && y >= 0 && y <8) {
			if (board[x][y]> 0) {
				if (colour == colour(x,y)) {
					status = true;
				}
			}
		}
		return status;
	}
	public static int colour(int x, int y) {
		if (x >= 0 && x <8 && y >= 0 && y <8) {
			if (board[x][y]> 0 && board[x][y] <= 6) {
				return BLACK;
			} else if (board[x][y] >= 7) {
				return WHITE;
			}
		}
		return 0;
	}
	public static boolean notcolour(int colour, int x, int y) {
		if (colour(x,y) != colour) {
			return true;
		}
		return false;

	}
	public static boolean checkDest(int colour, int curx, int cury, int newx, int newy) {
		boolean status = false;

		//if (check(3-colour)) {
		//	game.printToConsole("Warning! You are in check!");
		//}
		if (checkInit(colour, curx, cury) == true) {
			int piece = board[curx][cury];
			if (piece == BLACK_PAWN || piece == WHITE_PAWN) {
				status = movePawn(colour, curx, cury, newx,  newy);
			} else if (piece == BLACK_ROOK|| piece == WHITE_ROOK) {
				status = moveRook(colour, curx, cury, newx, newy);
			} else if (piece == BLACK_KNIGHT|| piece == WHITE_KNIGHT) {
				status = moveKnight(colour, curx, cury, newx, newy);
			} else if (piece == BLACK_BISHOP|| piece == WHITE_BISHOP) {
				status = moveBishop(colour, curx, cury, newx, newy);
			} else if (piece == BLACK_QUEEN|| piece == WHITE_QUEEN) {
				status = moveQueen(colour, curx, cury, newx, newy);
			} else if (piece == BLACK_KING|| piece == WHITE_KING) {
				status = moveKing(colour, curx, cury, newx, newy);
			}
		}/*
		if (game_TYPE == NORMAL && check(3-colour)) {
			game.printToConsole("Can't do that move as you are in check");
			status = false;
			
		} else {*/
		if (status && GAME_STATUS == PLAYING) {
			if(colour == WHITE) {
				game.printToConsole("Black's turn");
			} else {
				game.printToConsole("White's turn");
			}
		}
			copyBoard(true);
		//}
		return status;
	}
	public static boolean movePawn(int colour, int curx, int cury, int newx, int newy) {
		boolean status = false;
		if (checkPawn(colour, curx,cury,newx,newy)) {
			status = true;

			if (colour == WHITE) {
				if (newx == 7)
					commitMove(WHITE_QUEEN,colour, curx,cury,newx,newy);
				else
					commitMove(WHITE_PAWN,colour, curx,cury,newx,newy);
			} else if (colour == BLACK) {
				if (newx == 0)
					commitMove(BLACK_QUEEN,colour, curx,cury,newx,newy);
				else
					commitMove(BLACK_PAWN,colour, curx,cury,newx,newy);
			}
		}
		return status;
	}
	public static boolean checkPawn(int colour, int curx, int cury, int newx, int newy) {
		boolean status = false;
		if (cury == newy) {
			if (colour == WHITE && colour(newx,newy) != WHITE && colour(newx,newy) != BLACK && (newx -1 == curx || (newx -2 == curx && curx == 1))) {
				status = true;
			} else if (colour == BLACK && colour(newx,newy) != WHITE && colour(newx,newy) != BLACK && (newx +1 == curx || (newx +2 == curx && curx == 6))) {
				status = true;
			}
		} else {
			if (colour == WHITE && colour(newx,newy) == BLACK && newx -1 == curx && Math.abs(cury - newy) == 1) {
				status = true;
			} else if (colour == BLACK && colour(newx,newy) == WHITE && newx + 1 == curx && Math.abs(cury - newy) == 1) {
				status = true;
			}
		}
		return status;
	}
	public static boolean moveRook(int colour, int curx, int cury, int newx, int newy) {
		boolean status = false;

		if (checkRook(colour,curx,cury,newx,newy)) {
			status = true;
			if (colour == WHITE) {
				commitMove(WHITE_ROOK,colour, curx,cury,newx,newy);
				if (cury  == 7)
					WHITE_CASTLE = 0;
			} else if (colour == BLACK) {
				commitMove(BLACK_ROOK,colour, curx,cury,newx,newy);
				if (cury  == 7)
					BLACK_CASTLE = 0;
			}
		}

		return status;
	}
	public static boolean checkRook(int colour, int curx, int cury, int newx, int newy) {
		int tempx = curx;
		int tempy = cury;

		boolean status = false;
		if (tempx == newx ) {
			while (tempy > newy && !(colour(tempx, tempy) == colour %2+1) && notcolour(colour,tempx,tempy -1)) {
				tempy--;
			}
			while (tempy < newy && !(colour(tempx, tempy) == colour %2+1) && notcolour(colour,tempx,tempy +1)) {
				tempy++;
			}
		} else if (tempy == newy) {
			while (tempx > newx && !(colour(tempx, tempy) == colour %2+1) && notcolour(colour,tempx-1,tempy)) {
				tempx--;
			}
			while (tempx < newx && !(colour(tempx, tempy) == colour %2+1) && notcolour(colour,tempx+1,tempy)) {
				tempx++;
			}
		}
		if (tempx == newx && tempy == newy) {
			status = true;
		}

		return status;
	}
	public static boolean moveKnight(int colour,int curx, int cury, int newx, int newy) {
		boolean status = false;
		if (checkKnight(colour, curx,cury,newx,newy)) {
			status = true;
			if (colour == WHITE) {
				commitMove(WHITE_KNIGHT,colour, curx,cury,newx,newy);
				if (cury  == 6)
					WHITE_CASTLE = 0;
			} else if (colour == BLACK) {
				commitMove(BLACK_KNIGHT,colour, curx,cury,newx,newy);
				if (cury  == 6)
					BLACK_CASTLE = 0;
			}
		}

		return status;
	}
	public static boolean checkKnight(int colour, int curx, int cury, int newx, int newy) {
		boolean status = false;
		int deltaX = Math.abs(curx - newx);
		int deltaY = Math.abs(cury - newy);
		if (notcolour(colour,newx,newy) && (deltaX == 1 && deltaY == 2 || deltaX == 2 && deltaY == 1))
			status = true;

		return status;
	}
	public static boolean moveBishop(int colour, int curx, int cury, int newx, int newy) {
		boolean status = false;

		if (checkBishop(colour,curx,cury,newx,newy)) {
			status = true;
			if (colour == WHITE) {
				commitMove(WHITE_BISHOP,colour, curx,cury,newx,newy);
				if (cury  == 6)
					WHITE_CASTLE = 0;
			} else if (colour == BLACK) {
				commitMove(BLACK_BISHOP,colour, curx,cury,newx,newy);
				if (cury  == 6)
					BLACK_CASTLE = 0;
			}
		}

		return status;
	}
	public static boolean checkBishop(int colour, int curx, int cury, int newx, int newy) {
		int deltax = Math.abs(curx-newx);
		int deltay = Math.abs(cury-newy);
		int tempx = curx;
		int tempy = cury;
		boolean status = false;

		if (deltax != 0 && deltax == deltay) {
			while (tempy > newy && tempx > newx &&!(colour(tempx, tempy) == colour %2+1) &&notcolour(colour,tempx-1,tempy -1)) {
				tempx--;
				tempy--;
			}
			while (tempy > newy && tempx < newx &&!(colour(tempx, tempy) == colour %2+1) &&notcolour(colour,tempx+1,tempy -1)) {
				tempx++;
				tempy--;
			}
			while (tempy < newy && tempx > newx &&!(colour(tempx, tempy) == colour %2+1) &&notcolour(colour,tempx-1,tempy +1)) {
				tempx--;
				tempy++;
			}
			while (tempy < newy && tempx < newx &&!(colour(tempx, tempy) == colour %2+1) &&notcolour(colour,tempx+1,tempy +1)) {
				tempx++;
				tempy++;
			}
		}
		if (tempx == newx && tempy == newy) {
			status = true;
		}

		return status;
	}
	public static boolean moveQueen(int colour, int curx, int cury, int newx, int newy) {
		boolean status = false;

		if (checkQueen(colour,curx,cury,newx,newy)) {
			status = true;
			if (colour == WHITE) {
				commitMove(WHITE_QUEEN,colour, curx,cury,newx,newy);
			} else if (colour == BLACK) {
				commitMove(BLACK_QUEEN,colour, curx,cury,newx,newy);
			}
		}
		return status;
	}


	public static boolean checkQueen(int colour, int curx, int cury, int newx, int newy) {
		boolean status = false;

		if (checkBishop(colour,curx,cury,newx,newy) || checkRook(colour,curx,cury,newx,newy)) {
			status = true;
		}
		return status;
	}
	public static boolean moveKing(int colour, int curx, int cury, int newx, int newy) {
		boolean status = false;

		if (checkKing(colour,curx,cury,newx,newy)) {
			status = true;
			WHITE_CASTLE = 0;
			BLACK_CASTLE = 0;
			if (colour == WHITE) {
				commitMove(WHITE_KING,colour, curx,cury,newx,newy);
			} else if (colour == BLACK) {
				commitMove(BLACK_KING,colour, curx,cury,newx,newy);
			}
		}
		return status;
	}
	public static boolean checkKing(int colour, int curx, int cury, int newx, int newy) {
		boolean status = false;
		int deltaX = Math.abs(curx - newx);
		int deltaY = Math.abs(cury - newy);

		if (deltaX <= 1 && deltaY <= 1 && notcolour(colour, newx,newy)) {
			status = true;
		}
		return status;
	}
	public static void commitMove(int piece, int colour, int curx, int cury, int newx, int newy) {
		boolean status = true;
		String message;
		if (GAME_MODE == ONE_PLAYER && colour == backend_chessengine.computerColour) {
			//Keep screen
		} else {
			game.flushConsole();
		}
		if (colour(newx,newy) != 0 && colour(newx,newy) != colour && GAME_TYPE) {
			status = false;
			status = calculate(checkBoard[curx][cury],checkBoard[newx][newy]);
		}
		if (colour(newx,newy) == colour) {
			status = false;
		}


		if (status) {
			if (checkBoard[newx][newy] == 0) {
				message = lookupPiece(piece);
				message = message.concat(" moves");
				game.printToConsole(message);
			} else {
				message = lookupPiece(checkBoard[curx][cury]);
				message = message.concat(" takes ");
				message = message.concat(lookupPiece(checkBoard[newx][newy]));
				game.printToConsole(message);
			}
			if (checkBoard[newx][newy] == BLACK_KING) {
				GAME_STATUS = WON;
				game.printToConsole("WHITE WINS!!!");
			} else if (checkBoard[newx][newy] == WHITE_KING) {
				GAME_STATUS = WON;
				game.printToConsole("BLACK WINS!!!");
			}
			checkBoard[newx][newy] = piece;
			checkBoard[curx][cury] = 0;

		} else {
			message = lookupPiece(piece);
			message = message.concat(" fails to take ");
			message = message.concat(lookupPiece(checkBoard[newx][newy]));
			game.printToConsole(message);


		}
	}
	public static boolean calculate(int attacker, int defender) {
		boolean status = false;
		int denominator;
		int numerator;
		int rand;
		String message;

		numerator = costPiece(defender);
		denominator = costPiece(attacker) + costPiece(defender);
		rand = randy.nextInt() % (denominator);
		game.printToConsole("Attacker: " + costPiece(attacker) + ", Defender: " + costPiece(defender) +", Probability " + numerator+"/"+denominator);
		game.printToConsole("You scored " + rand + ", at least " + numerator + " required.");
		if (rand >= numerator) {
			status = true;
		}
		return status;
	}

	public static boolean check(int colour) {
		boolean status = false;
		int kingx = list[16*(2-colour)+12][1];
		int kingy = list[16*(2-colour)+12][2];
		game.printToConsole("King is at " + kingx + ", " + kingy);
		int i = 0;
		while(status == false && i < 8) {
			status = checkPawn(colour,list[16*(2-colour)+i][1],list[16*(2-colour)+i][2],kingx,kingy);
			i++;
		}
		if(status == false) {
			status = checkRook(colour,list[16*(2-colour)+8][1],list[16*(2-colour)+8][2],kingx,kingy);
		}
		if(status == false) {
			status = checkKnight(colour,list[16*(2-colour)+9][1],list[16*(2-colour)+9][2],kingx,kingy);
		}
		if(status == false) {
			status = checkBishop(colour,list[16*(2-colour)+10][1],list[16*(2-colour)+10][2],kingx,kingy);
		}
		if(status == false) {
			status = checkQueen(colour,list[16*(2-colour)+11][1],list[16*(2-colour)+11][2],kingx,kingy);
		}
		if(status == false) {
			status = checkBishop(colour,list[16*(2-colour)+13][1],list[16*(2-colour)+13][2],kingx,kingy);
		}
		if(status == false) {
			status = checkKnight(colour,list[16*(2-colour)+14][1],list[16*(2-colour)+14][2],kingx,kingy);
		}
		if(status == false) {
			status = checkRook(colour,list[16*(2-colour)+15][1],list[16*(2-colour)+15][2],kingx,kingy);
		}
		return status;
	}

	public static int costPiece(int piece) {
		switch (piece) {
			case 1: return 1;
			case 2: return 5;
			case 3: return 3;
			case 4: return 3;
			case 5: return 9;
			case 6: return 15;
			case 7: return 1;
			case 8: return 5;
			case 9: return 3;
			case 10: return 3;
			case 11: return 9;
			case 12: return 15;
			default: return 1;
		}
	}
	public static String lookupPiece(int piece) {
		String piece_name = "Empty";
		switch (piece) {
			case 0: piece_name =  "Empty"; break;
			case 1: piece_name =  "Black Pawn"; break;
			case 2: piece_name =  "Black Rook"; break;
			case 3: piece_name =  "Black Knight"; break;
			case 4: piece_name =  "Black Bishop"; break;
			case 5: piece_name =  "Black Queen"; break;
			case 6: piece_name =  "Black King"; break;
			case 7: piece_name =  "White Pawn";	 break;
			case 8: piece_name =  "White Rook"; break;
			case 9: piece_name =  "White Knight"; break;
			case 10: piece_name =  "White Bishop"; break;
			case 11: piece_name =  "White Queen"; break;
			case 12: piece_name =  "White King"; break;
		}
		return piece_name;
	}
	public static void copyBoard(boolean save) {
		int i,j;
		for (i = 0;i < 8; i++) {
			for (j = 0; j < 8; j++) {
				if (save)
					board[i][j] = checkBoard[i][j];
				else
					checkBoard[i][j] = board[i][j];

			}
		}
	}

		//initialises the board pieces
		// black is up the top and white the bottom
		public static void initialiseBoard() {
			int i,j;
			BLACK_CASTLE = 1;
			WHITE_CASTLE = 1;
			for (i = 0; i < 8; i++) {
				for (j = 0; j < 8; j++) {
					board[i][j] = 0;
				}
			}
			for (j = 0; j < 8; j++) {
				updateList(j,BLACK_PAWN,6,j);
				updateList(j+16,WHITE_PAWN,1,j);
			}
			updateList(8,BLACK_ROOK,7,0);
			updateList(24,WHITE_ROOK,0,0);
			updateList(9,BLACK_KNIGHT,7,1);
			updateList(25,WHITE_KNIGHT,0,1);
			updateList(10,BLACK_BISHOP,7,2);
			updateList(26,WHITE_BISHOP,0,2);
			updateList(12,BLACK_QUEEN,7,4);
			updateList(28,WHITE_QUEEN,0,4);
			updateList(11,BLACK_KING,7,3);
			updateList(27,WHITE_KING,0,3);
			updateList(13,BLACK_BISHOP,7,5);
			updateList(29,WHITE_BISHOP,0,5);
			updateList(14,BLACK_KNIGHT,7,6);
			updateList(30,WHITE_KNIGHT,0,6);
			updateList(15,BLACK_ROOK,7,7);
			updateList(31,WHITE_ROOK,0,7);
		}
		public static void updateList(int index, int piece, int x, int y) {
			list[index][0] = piece;
			list[index][1] = x;
			list[index][2] = y;
			board[x][y] = piece;
		}
		public static int getRow(int index) {
			return index/8;
		}
		public static int getCol(int index) {
			return index%8;
		}
		public static int getIndex(int row,int col) {
			return 8*row + col;
		}
		public static int[][] getOptions(int colour, int index) {
			int newRow, newCol;
			boolean status;
			int row = getRow(index);
			int col = getCol(index);
			int piece = board[row][col];
			int [][]buffer = new int[65][2];
			int i = 0;
			if(piece != 0) {
				for(newRow = 0; newRow < 8; newRow++) {
					for(newCol = 0; newCol < 8; newCol++) {
						status = false;
						if (piece == BLACK_PAWN || piece == WHITE_PAWN) {
							status = checkPawn(colour, row, col, newRow,  newCol);
						} else if (piece == BLACK_ROOK|| piece == WHITE_ROOK) {
							status = checkRook(colour, row, col, newRow, newCol);
						} else if (piece == BLACK_KNIGHT|| piece == WHITE_KNIGHT) {
							status = checkKnight(colour, row, col, newRow, newCol);
						} else if (piece == BLACK_BISHOP|| piece == WHITE_BISHOP) {
							status = checkBishop(colour, row, col, newRow, newCol);
						} else if (piece == BLACK_QUEEN|| piece == WHITE_QUEEN) {
							status = checkQueen(colour, row, col, newRow, newCol);
						} else if (piece == BLACK_KING|| piece == WHITE_KING) {
							status = checkKing(colour, row, col, newRow, newCol);
						}
						if (status && (newRow != row || newCol != col)) {
							buffer[i+1][0] = getIndex(newRow, newCol);
							if (colour(newRow,newCol) != 0 && colour(newRow,newCol) != colour) {
								buffer[i+1][1] = 1;
							} else {
								buffer[i+1][1] = 0;
							}
							i++;
						}
					}
				}
				buffer[0][0] = i;
			} else {
				buffer[0][0] = 0;
			}
			return buffer;
		}
		public static int[][] getThreats(int colour) {
			int target, piece, attacker;
			boolean scared;
			int [][]buffer = new int[65][2];
			int i;
			i=0;
			for(target = 0; target < 64; target++) {
				scared = false;
				for(piece = 0; colour(getRow(target),getCol(target)) == colour && piece < 64; piece++) {
					attacker = board[getRow(piece)][getCol(piece)];
					if (colour(getRow(piece),getCol(piece)) == (colour%2+1) && piece != target) {
						if (attacker == BLACK_PAWN || attacker == WHITE_PAWN) {
							if (checkPawn((colour%2)+1, getRow(piece), getCol(piece), getRow(target),  getCol(target)))
								scared = true;
						} else if (attacker == BLACK_ROOK|| attacker == WHITE_ROOK) {
							if (checkRook((colour%2)+1, getRow(piece), getCol(piece), getRow(target), getCol(target)))
								scared = true;
						} else if (attacker == BLACK_KNIGHT||  attacker == WHITE_KNIGHT) {
							if (checkKnight((colour%2)+1, getRow(piece), getCol(piece), getRow(target), getCol(target)))
								scared = true;
						} else if (attacker == BLACK_BISHOP||  attacker == WHITE_BISHOP) {
							if (checkBishop((colour%2)+1, getRow(piece), getCol(piece), getRow(target), getCol(target)))
								scared = true;
						} else if (attacker == BLACK_QUEEN||  attacker == WHITE_QUEEN) {
							if (checkQueen((colour%2)+1, getRow(piece), getCol(piece), getRow(target), getCol(target)))
								scared = true;
						} else if (attacker == BLACK_KING||  attacker == WHITE_KING) {
							if (checkKing((colour%2)+1, getRow(piece), getCol(piece), getRow(target), getCol(target)))
								scared = true;
						}
					}
				}
				if (scared) {
					buffer[i+1][0] = target;
					if (board[getRow(target)][getCol(target)] == BLACK_KING || board[getRow(target)][getCol(target)] == WHITE_KING) {
						buffer[i+1][1] = 1;
					} else {
						buffer[i+1][1] = 0;
					}
					i++;
				}
			}
			buffer[0][0] = i;
			return buffer;
		}
	}
