package com.example.jbstrand.probabilisticchess;

/**
 * Created by jbstrand on 18/04/2015.
 */
public class backend_piece {
    private backend_pieceType type;
    private backend_pieceColour colour;

    public backend_piece(backend_pieceType new_type, backend_pieceColour new_colour) {
        type = new_type;
        colour = new_colour;
    }

    public void set_pieceType(backend_pieceType new_type) {
        type = new_type;
    }

    public backend_pieceType get_pieceType() {
        return type;
    }

    public void set_pieceColour(backend_pieceColour new_colour) {
        colour = new_colour;
    }

    public backend_pieceColour get_pieceColour() {
        return colour;
    }
}