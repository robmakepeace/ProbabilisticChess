package com.example.jbstrand.probabilisticchess;

/**
 * Created by jbstrand on 18/04/2015.
 */
public class backend_move {

    public static boolean moveA(backend_board board, backend_square start) {
        backend_piece piece = board.get_Board(start);
        boolean returnValue = false;
        if ((checkInit(piece))) {
            returnValue = true;
        }
        return returnValue;
    }

    public static boolean moveB(backend_board board, backend_square start, backend_square stop) {
        backend_piece piece = board.get_Board(start);
        boolean returnValue = false;
        if ((checkInit(piece) && checkDest(board,piece,start,stop))) {
            returnValue = true;
        }
        return returnValue;
    }

    public static boolean checkInit(backend_piece piece) {
        boolean status = false;
        if (piece.get_pieceType() != backend_pieceType.None) {
            if (piece.get_pieceColour() == backend_chess.colour) {
                status = true;
            }
        }
        return status;
    }
    public static boolean checkDest(backend_board board, backend_piece piece,backend_square start, backend_square stop) {
        boolean status = false;

        if (checkInit(piece) == true) {
            if (piece.get_pieceType() == backend_pieceType.Pawn) {
                status = movePawn(board,piece,start,stop);
            } else if (piece.get_pieceType() == backend_pieceType.Rook) {
                status = moveRook(board,piece,start,stop);
            } else if (piece.get_pieceType() == backend_pieceType.Knight) {
                status = moveKnight(board,piece,start,stop);
            } else if (piece.get_pieceType() == backend_pieceType.Bishop) {
                status = moveBishop(board,piece,start,stop);
            } else if (piece.get_pieceType() == backend_pieceType.Queen) {
                status = moveQueen(board,piece,start,stop);
            } else if (piece.get_pieceType() == backend_pieceType.King) {
                status = moveKing(board,piece,start,stop);
            }
        }
        if (status && backend_chess.GAME_STATUS == backend_chess.PLAYING) {
            if (backend_chess.colour == backend_pieceColour.White) {
                backend_chess.game.printToConsole("Black's turn");
            } else {
                backend_chess.game.printToConsole("White's turn");
            }
        }
        return status;
    }

    public static boolean movePawn(backend_board board, backend_piece piece,backend_square start, backend_square stop) {
        boolean status = false;
        if (checkPawn(board,piece,start,stop)) {
            status = true;
            //Promote pawn to queen
            if (backend_chess.colour == backend_pieceColour.White) {
                if (stop.Y == 7)
                    piece.set_pieceType(backend_pieceType.Queen);
            //Promote pawn to queen
            } else if (backend_chess.colour == backend_pieceColour.Black) {
                if (stop.Y == 0)
                    piece.set_pieceType(backend_pieceType.Queen);
            }
            commitMove(board,piece,start,stop);
        }

        return status;
    }

    public static boolean checkPawn(backend_board board, backend_piece start_piece,backend_square start, backend_square stop) {
        boolean status = false;
        backend_piece stop_piece = board.get_Board(stop);
        boolean c_w, c_b, n_w, n_b;

        c_w = (start_piece.get_pieceColour() == backend_pieceColour.White);
        c_b = (start_piece.get_pieceColour() == backend_pieceColour.Black);

        if (stop_piece.get_pieceType() != backend_pieceType.None && start.X == stop.X) {
            //move one forward
            //move two forward if in first row
            if (c_w && (stop.Y - 1 == start.Y || (stop.Y - 2 == start.Y && start.Y == 1))) {
                status = true;
            } else if (c_b && (stop.Y + 1 == start.Y || (stop.Y + 2 == start.Y && start.Y == 6))) {
                status = true;
            }
        } else {
            n_w = (stop_piece.get_pieceColour() == backend_pieceColour.White);
            n_b = (stop_piece.get_pieceColour() == backend_pieceColour.Black);
            //Take piece diagonally
            //ADD ENPASSONE
            if (c_w && n_b && stop.X - 1 == start.X && Math.abs(start.Y - stop.Y) == 1) {
                status = true;
            } else if (c_b && n_w && stop.X + 1 == start.X && Math.abs(start.Y - stop.Y) == 1) {
                status = true;
            }
        }
        return status;
    }

    public static boolean moveRook(backend_board board, backend_piece piece,backend_square start, backend_square stop) {
        boolean status = false;

        if (checkRook(board,piece,start,stop)) {
            status = true;
            if (backend_chess.colour == backend_pieceColour.White) {
                commitMove(board,piece,start,stop);
                if (start.X == 7)
                    backend_chess.WHITE_CASTLE = 0;
            } else if (backend_chess.colour == backend_pieceColour.Black) {
                commitMove(board,piece,start,stop);
                if (start.X == 7)
                    backend_chess.BLACK_CASTLE = 0;
            }
        }
        return status;
    }

    public static boolean checkRook(backend_board board, backend_piece start_piece,backend_square start, backend_square stop) {
        boolean status = false;
        backend_piece next_piece;
        backend_piece curr_piece;
        backend_pieceColour start_colour;
        backend_pieceColour next_colour;
        int delta_x = 0;
        int delta_y = 0;

        //move vertically
        if (start.X == stop.X) {
            delta_x = 0;
            if (start.Y > stop.Y) {
                delta_y = -1;
            } else if (start.Y < stop.Y) {
                delta_y = +1;
            }
        // move horizontally
        } else if (start.Y == stop.Y) {
            delta_y = 0;
            if (start.X > stop.X) {
                delta_x = -1;
            } else if (start.X < stop.X) {
                delta_x = +1;
            }
        }

        //initialise
        start_colour = start_piece.get_pieceColour();
        curr_piece = new backend_piece(backend_pieceType.None,backend_pieceColour.None);//to get through the first time
        next_piece = board.get_Board(new backend_square(start.X + delta_x, start.Y + delta_y));
        if (next_piece.get_pieceType() != backend_pieceType.None) {
            next_colour = next_piece.get_pieceColour();
        } else {
            next_colour = start_colour;
        }
        //while we have space to go to destination
        //current square is empty (except first round)
        //next square is empty or has an enemy
        while ((start.X > stop.X || start.Y > stop.Y)  && curr_piece.get_pieceType() == backend_pieceType.None && (next_piece.get_pieceType() == backend_pieceType.None || next_colour != start_colour)) {
            //move along one step
            start.X = start.X + delta_x;
            start.Y = start.Y + delta_y;
            //reinitialise
            curr_piece = board.get_Board(new backend_square(start.X, start.Y));
            next_piece = board.get_Board(new backend_square(start.X + delta_x, start.Y + delta_y));
            if (next_piece.get_pieceType() != backend_pieceType.None) {
                next_colour = next_piece.get_pieceColour();
            } else {
                next_colour = start_colour;
            }
        }
        //get to destination
        if (start.X == stop.X && start.Y == stop.Y) {
            status = true;
        }
        return status;
    }

    public static boolean moveKnight(backend_board board, backend_piece piece,backend_square start, backend_square stop) {
        boolean status = false;
        if (checkKnight(board,piece,start,stop)) {
            status = true;
            if (backend_chess.colour == backend_pieceColour.White) {
                commitMove(board,piece,start,stop);
                if (start.X == 6)
                    backend_chess.WHITE_CASTLE = 0;
            } else if (backend_chess.colour == backend_pieceColour.Black) {
                commitMove(board,piece,start,stop);
                if (start.X == 6)
                    backend_chess.BLACK_CASTLE = 0;
            }
        }

        return status;
    }

    public static boolean checkKnight(backend_board board, backend_piece start_piece,backend_square start, backend_square stop) {
        boolean status = false;
        int deltaX = Math.abs(start.X - stop.X);
        int deltaY = Math.abs(start.Y - stop.Y);
        backend_piece stop_piece = board.get_Board(stop);
        backend_pieceColour start_colour = start_piece.get_pieceColour();
        backend_pieceColour stop_colour;
        if (stop_piece.get_pieceType() != backend_pieceType.None) {
            stop_colour = stop_piece.get_pieceColour();
        } else {
            stop_colour = start_colour;
        }
        if ((stop_piece.get_pieceType() != backend_pieceType.None || stop_colour != start_colour)&& (deltaX == 1 && deltaY == 2 || deltaX == 2 && deltaY == 1))
            status = true;
        return status;
    }

    public static boolean moveBishop(backend_board board, backend_piece piece,backend_square start, backend_square stop) {
        boolean status = false;

        if (checkBishop(board,piece,start,stop)) {
            status = true;
            if (backend_chess.colour == backend_pieceColour.White) {
                commitMove(board,piece,start,stop);
                if (start.X == 6)
                    backend_chess.WHITE_CASTLE = 0;
            } else if (backend_chess.colour == backend_pieceColour.Black) {
                commitMove(board,piece,start,stop);
                if (start.X == 6)
                    backend_chess.BLACK_CASTLE = 0;
            }
        }

        return status;
    }

    public static boolean checkBishop(backend_board board, backend_piece start_piece,backend_square start, backend_square stop) {
        boolean status = false;
        backend_piece next_piece;
        backend_piece curr_piece;
        backend_pieceColour start_colour;
        backend_pieceColour next_colour;
        int delta_x = 0;
        int delta_y = 0;

        if (start.Y > stop.Y) {
            delta_y = -1;
        } else if (start.Y < stop.Y) {
            delta_y = +1;
        }
        if (start.X > stop.X) {
            delta_x = -1;
        } else if (start.X < stop.X) {
            delta_x = +1;
        }

        //initialise
        start_colour = start_piece.get_pieceColour();
        curr_piece = new backend_piece(backend_pieceType.None,backend_pieceColour.None);//to get through the first time
        next_piece = board.get_Board(new backend_square(start.X + delta_x, start.Y + delta_y));
        if (next_piece.get_pieceType() != backend_pieceType.None) {
            next_colour = next_piece.get_pieceColour();
        } else {
            next_colour = start_colour;
        }
        //while we have space to go to destination
        //current square is empty (except first round)
        //next square is empty or has an enemy
        while ((start.X > stop.X || start.Y > stop.Y)  && curr_piece.get_pieceType() == backend_pieceType.None && (next_piece.get_pieceType() == backend_pieceType.None || next_colour != start_colour)) {
            //move along one step
            start.X = start.X + delta_x;
            start.Y = start.Y + delta_y;
            //reinitialise
            curr_piece = board.get_Board(new backend_square(start.X, start.Y));
            next_piece = board.get_Board(new backend_square(start.X + delta_x, start.Y + delta_y));
            if (next_piece.get_pieceType() != backend_pieceType.None) {
                next_colour = next_piece.get_pieceColour();
            } else {
                next_colour = start_colour;
            }
        }
        //get to destination
        if (start.X == stop.X && start.Y == stop.Y) {
            status = true;
        }
        return status;
    }

    public static boolean moveQueen(backend_board board, backend_piece piece,backend_square start, backend_square stop) {
        boolean status = false;

        if (checkQueen(board,piece,start,stop)) {
            status = true;
            commitMove(board,piece,start,stop);
        }
        return status;
    }


    public static boolean checkQueen(backend_board board, backend_piece piece,backend_square start, backend_square stop) {
        boolean status = false;

        if (checkBishop(board,piece,start,stop) || checkRook(board,piece,start,stop)) {
            status = true;
        }
        return status;
    }

    public static boolean moveKing(backend_board board, backend_piece piece,backend_square start, backend_square stop) {
        boolean status = false;

        if (checkKing(board,piece,start,stop)) {
            status = true;
            backend_chess.WHITE_CASTLE = 0;
            backend_chess.BLACK_CASTLE = 0;
            commitMove(board,piece,start,stop);
        }
        return status;
    }

    public static boolean checkKing(backend_board board, backend_piece start_piece,backend_square start, backend_square stop) {
        boolean status = false;
        int deltaX = Math.abs(start.X - stop.X);
        int deltaY = Math.abs(start.Y - stop.Y);
        backend_piece stop_piece = board.get_Board(stop);
        backend_pieceColour start_colour = start_piece.get_pieceColour();
        backend_pieceColour stop_colour;
        if (stop_piece.get_pieceType() != backend_pieceType.None) {
            stop_colour = stop_piece.get_pieceColour();
        } else {
            stop_colour = start_colour;
        }
        if (deltaX + deltaY >= 1 && deltaX <= 1 && deltaY <= 1 && ( stop_piece.get_pieceType() == backend_pieceType.None || start_colour != stop_colour)) {
            status = true;
        }
        return status;
    }

    public static void commitMove(backend_board board, backend_piece start_piece, backend_square start, backend_square stop) {
        boolean status = true;
        String message;
        backend_piece stop_piece = board.get_Board(stop);
        backend_pieceColour start_colour = start_piece.get_pieceColour();
        backend_pieceColour stop_colour;
        if (stop_piece.get_pieceType() != backend_pieceType.None) {
            stop_colour = stop_piece.get_pieceColour();
        } else {
            stop_colour = start_colour;
        }
        if (backend_chess.GAME_MODE == backend_chess.ONE_PLAYER && backend_chess.colour == backend_chessengine.computerColour) {
            //Keep screen
        } else {
            backend_chess.game.flushConsole();
        }
        if (stop_piece.get_pieceType() != backend_pieceType.None && stop_colour != backend_chess.colour && backend_chess.GAME_TYPE) {
            status = false;
            status = backend_chess.calculate(board.get_Board(start), board.get_Board(stop));
        }
        if (stop_colour == backend_chess.colour) {
            status = false;
        }


        if (status) {
            if (stop_piece.get_pieceType() == backend_pieceType.None) {
                message = backend_chess.lookup_piece(start_piece);
                message = message.concat(" moves");
                backend_chess.game.printToConsole(message);
            } else {
                message = backend_chess.lookup_piece(start_piece);
                message = message.concat(" takes ");
                message = message.concat(backend_chess.lookup_piece(stop_piece));
                backend_chess.game.printToConsole(message);
            }
            if (stop_piece.get_pieceType() == backend_pieceType.King) {
                backend_chess.GAME_STATUS = backend_chess.WON;
                String str = backend_chess.lookup_colour(start_piece);
                str += "WINS!!!";
                backend_chess.game.printToConsole(str);
            }
            board.set_Board(start, new backend_piece(backend_pieceType.None,backend_pieceColour.None));
            board.set_Board(stop, start_piece);
            activity_game.board = board;
        } else {
            message = backend_chess.lookup_piece(start_piece);
            message = message.concat(" fails to take ");
            message = message.concat(backend_chess.lookup_piece(stop_piece));
            backend_chess.game.printToConsole(message);
        }
    }

    public static boolean check(backend_board board, backend_pieceColour colour) {
        boolean status = false;
        backend_square stop = new backend_square(0,0);
        backend_piece stop_piece;
        backend_pieceType stop_type;
        backend_pieceColour stop_colour;
        int[][] buffer = new int[8][8];
        int x, y, total;
        for (stop.Y = 0; stop.Y < 8; stop.Y++) {
            for (stop.X = 0; stop.X < 8; stop.X++) {
                status = false;
                stop_piece = board.get_Board(stop);
                if (stop_piece.get_pieceType() != backend_pieceType.None) {
                    stop_type = stop_piece.get_pieceType();
                    stop_colour = stop_piece.get_pieceColour();
                    if (stop_type == backend_pieceType.King && stop_colour == colour) {
                        buffer = getThreats(board, stop);
                    }
                }
            }
        }
        total = 0;
        for (x = 0; x < 8; x++) {
            for (y = 0; y < 8; y++) {
                total += buffer[x][y];
            }
        }
        if (total >= 1) {
            status = true;
        } else {
            status = false;
        }
        return status;
    }

    public static int[][] getOptions(backend_board board, backend_square start) {
        boolean status;
        backend_square stop = new backend_square(0,0);
        backend_piece start_piece = board.get_Board(start);
        backend_piece stop_piece;
        backend_pieceColour start_colour = start_piece.get_pieceColour();
        backend_pieceColour stop_colour;

        int[][] buffer = new int[8][8];
        if (start_piece.get_pieceType() != backend_pieceType.None) {
            for (stop.Y = 0; stop.Y < 8; stop.Y++) {
                for (stop.X = 0; stop.X < 8; stop.X++) {
                    status = false;
                    if (start_piece.get_pieceType() == backend_pieceType.Pawn) {
                        status = movePawn(board, start_piece, start, stop);
                    } else if (start_piece.get_pieceType() == backend_pieceType.Rook) {
                        status = moveRook(board, start_piece, start, stop);
                    } else if (start_piece.get_pieceType() == backend_pieceType.Knight) {
                        status = moveKnight(board, start_piece, start, stop);
                    } else if (start_piece.get_pieceType() == backend_pieceType.Bishop) {
                        status = moveBishop(board, start_piece, start, stop);
                    } else if (start_piece.get_pieceType() == backend_pieceType.Queen) {
                        status = moveQueen(board, start_piece, start, stop);
                    } else if (start_piece.get_pieceType() == backend_pieceType.King) {
                        status = moveKing(board, start_piece, start, stop);
                    }
                    if (status && (start.X != stop.X || start.Y != stop.Y)) {
                        stop_piece = board.get_Board(stop);
                        if (stop_piece.get_pieceType() != backend_pieceType.None) {
                            stop_colour = stop_piece.get_pieceColour();
                        } else {
                            stop_colour = start_colour;
                        }
                        if (stop_piece.get_pieceType() != backend_pieceType.None) {
                            buffer[stop.X][stop.Y] = 1;
                        } else if (stop_colour != start_colour) {
                            buffer[stop.X][stop.Y] = 2;
                        } else {
                            buffer[stop.X][stop.Y] = 0;
                        }
                    }
                }
            }
        }
        return buffer;
    }

    public static int[][] getThreats(backend_board board, backend_square stop) {
        boolean status;
        backend_square start = new backend_square(0,0);
        backend_piece start_piece;
        backend_piece stop_piece = board.get_Board(stop);
        backend_pieceColour start_colour;
        backend_pieceColour stop_colour = stop_piece.get_pieceColour();

        int[][] buffer = new int[8][8];
        if (stop_piece.get_pieceType() != backend_pieceType.None) {
            for (start.Y = 0; start.Y < 8; start.Y++) {
                for (start.X = 0; start.X < 8; start.X++) {
                    start_piece = board.get_Board(start);
                    if (start_piece.get_pieceType() != backend_pieceType.None) {
                        status = false;
                        if (start_piece.get_pieceType() == backend_pieceType.Pawn) {
                            status = movePawn(board, start_piece, start, stop);
                        } else if (start_piece.get_pieceType() == backend_pieceType.Rook) {
                            status = moveRook(board, start_piece, start, stop);
                        } else if (start_piece.get_pieceType() == backend_pieceType.Knight) {
                            status = moveKnight(board, start_piece, start, stop);
                        } else if (start_piece.get_pieceType() == backend_pieceType.Bishop) {
                            status = moveBishop(board, start_piece, start, stop);
                        } else if (start_piece.get_pieceType() == backend_pieceType.Queen) {
                            status = moveQueen(board, start_piece, start, stop);
                        } else if (start_piece.get_pieceType() == backend_pieceType.King) {
                            status = moveKing(board, start_piece, start, stop);
                        }
                        if (status && (start.X != stop.X || start.Y != stop.Y)) {
                            start_colour = start_piece.get_pieceColour();
                            if (stop_colour != start_colour) {
                                buffer[stop.X][stop.Y] = 1;
                            } else {
                                buffer[stop.X][stop.Y] = 0;
                            }
                        }
                    }
                }
            }
        }
        return buffer;
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
}
