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
	 * backend_pieces 15/8/5/3/3/1
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

    public static backend_pieceColour colour;

    public static activity_game game;

    public static int BLACK_CASTLE;
    public static int WHITE_CASTLE;

    private static backend_random randy = new backend_random();

    public static void setGameActivity(activity_game gameActivity) {
        game = gameActivity;
    }

    //initialises the board backend_pieces
    // black is up the top and white the bottom
    public static backend_board initialiseBoard() {
        backend_board board = new backend_board();
        board.set_Board(new backend_square(0,0), new backend_piece(backend_pieceType.Rook, backend_pieceColour.White));
        board.set_Board(new backend_square(1,0), new backend_piece(backend_pieceType.Knight, backend_pieceColour.White));
        board.set_Board(new backend_square(2,0), new backend_piece(backend_pieceType.Bishop, backend_pieceColour.White));
        board.set_Board(new backend_square(3,0), new backend_piece(backend_pieceType.Queen, backend_pieceColour.White));
        board.set_Board(new backend_square(4,0), new backend_piece(backend_pieceType.King, backend_pieceColour.White));
        board.set_Board(new backend_square(5,0), new backend_piece(backend_pieceType.Bishop, backend_pieceColour.White));
        board.set_Board(new backend_square(6,0), new backend_piece(backend_pieceType.Knight, backend_pieceColour.White));
        board.set_Board(new backend_square(7,0), new backend_piece(backend_pieceType.Rook, backend_pieceColour.White));

        board.set_Board(new backend_square(0,1), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.White));
        board.set_Board(new backend_square(1,1), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.White));
        board.set_Board(new backend_square(2,1), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.White));
        board.set_Board(new backend_square(3,1), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.White));
        board.set_Board(new backend_square(4,1), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.White));
        board.set_Board(new backend_square(5,1), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.White));
        board.set_Board(new backend_square(6,1), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.White));
        board.set_Board(new backend_square(7,1), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.White));

        board.set_Board(new backend_square(0,6), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.Black));
        board.set_Board(new backend_square(1,6), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.Black));
        board.set_Board(new backend_square(2,6), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.Black));
        board.set_Board(new backend_square(3,6), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.Black));
        board.set_Board(new backend_square(4,6), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.Black));
        board.set_Board(new backend_square(5,6), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.Black));
        board.set_Board(new backend_square(6,6), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.Black));
        board.set_Board(new backend_square(7,6), new backend_piece(backend_pieceType.Pawn, backend_pieceColour.Black));

        board.set_Board(new backend_square(0,7), new backend_piece(backend_pieceType.Rook, backend_pieceColour.Black));
        board.set_Board(new backend_square(1,7), new backend_piece(backend_pieceType.Knight, backend_pieceColour.Black));
        board.set_Board(new backend_square(2,7), new backend_piece(backend_pieceType.Bishop, backend_pieceColour.Black));
        board.set_Board(new backend_square(3,7), new backend_piece(backend_pieceType.Queen, backend_pieceColour.Black));
        board.set_Board(new backend_square(4,7), new backend_piece(backend_pieceType.King, backend_pieceColour.Black));
        board.set_Board(new backend_square(5,7), new backend_piece(backend_pieceType.Bishop, backend_pieceColour.Black));
        board.set_Board(new backend_square(6,7), new backend_piece(backend_pieceType.Knight, backend_pieceColour.Black));
        board.set_Board(new backend_square(7,7), new backend_piece(backend_pieceType.Rook, backend_pieceColour.Black));
        return board;
    }

    public static boolean calculate(backend_piece attacker, backend_piece defender) {
        boolean status = false;
        int denominator;
        int numerator;
        int rand;
        String message;

        numerator = costbackend_piece(defender);
        denominator = costbackend_piece(attacker) + costbackend_piece(defender);
        rand = randy.nextInt() % (denominator);
        game.printToConsole("Attacker: " + costbackend_piece(attacker) + ", Defender: " + costbackend_piece(defender) + ", Probability " + numerator + "/" + denominator);
        game.printToConsole("You scored " + rand + ", at least " + numerator + " required.");
        if (rand >= numerator) {
            status = true;
        }
        return status;
    }

    public static int costbackend_piece(backend_piece piece) {
        switch (piece.get_pieceType()) {
            case Pawn:
                return 1;
            case Rook:
                return 5;
            case Knight:
                return 3;
            case Bishop:
                return 3;
            case Queen:
                return 9;
            case King:
                return 15;
            default:
                return 1;
        }
    }
    public static String lookup_colour(backend_piece piece) {
        String str = "Empty";
        switch (piece.get_pieceColour()) {
            case Black:
                str = "Black ";
                break;
            case White:
                str = "White ";
                break;
        }
        return str;
    }
    public static String lookup_type(backend_piece piece) {
        String str = "Empty";
        switch (piece.get_pieceType()) {
            case Pawn:
                str = "Pawn";
                break;
            case Rook:
                str = "Rook";
                break;
            case Knight:
                str = "Knight";
                break;
            case Bishop:
                str = "Bishop";
                break;
            case Queen:
                str = "Queen";
                break;
            case King:
                str = "King";
                break;
        }
        return str;
    }
    public static String lookup_piece(backend_piece piece) {
        String str = "Empty";
        str = lookup_colour(piece);
        str += lookup_type(piece);
        return str;
    }
}
