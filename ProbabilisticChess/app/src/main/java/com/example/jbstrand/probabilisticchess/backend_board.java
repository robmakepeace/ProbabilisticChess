package com.example.jbstrand.probabilisticchess;

/**
 * Created by jbstrand on 18/04/2015.
 */
public class backend_board {
    private backend_piece[][] my_board;

    public backend_board() {
        my_board = new backend_piece[8][8];
        int x, y;
        for (x=0;x<8;x++) {
            for (y=0;y<8;y++) {
                my_board[x][y] = new backend_piece(backend_pieceType.None,backend_pieceColour.None);
            }
        }
    }

    public void set_Board(backend_square square, backend_piece piece) {
        my_board[square.X][square.Y] = piece;
    }

    public backend_piece get_Board(backend_square square) {
        return my_board[square.X][square.Y];
    }

    public backend_board copy_board() {
        backend_board copy = new backend_board();
        backend_square square;
        int x,y;

        for (x = 0; x < 8; x++) {
            for (y = 0; y < 8; y++) {
                square = new backend_square(x,y);
                copy.set_Board(square, this.get_Board(square));
            }
        }
        return copy;
    }
}