package chess.pieces;

import chess.IllegalMoveException;
import chess.Position;

public class Knight extends Piece{
    public Knight(Colour colour){
        super(colour);
    }

    @Override
    public void verifyMove(Position p1, Position p2) {
        int rowDiff = Math.abs(p1.getRow()-p2.getRow());
        int colDIff = Math.abs(p1.getCol()-p2.getCol());
        if (!((rowDiff==2 && colDIff == 3)||(rowDiff==3 && colDIff==2))){
            throw new IllegalMoveException("A knight can only move between opposing edges of a 3x2 rectangle.");
        }
    }
}
