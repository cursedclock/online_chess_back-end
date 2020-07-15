package chess.pieces;

import chess.IllegalMoveException;
import chess.Position;

import java.util.Map;

public class King extends Piece{
    public King(Colour colour){
        super(colour);
    }

    @Override
    public void verifyMove(Map<Position, Piece> board, Position p1, Position p2) {
        int rowDiff = Math.abs(p1.getRow()-p2.getRow());
        int colDIff = Math.abs(p1.getRow()-p2.getRow());
        if (colDIff>1 || rowDiff>1){
            throw new IllegalMoveException("The king can only move one square in any direction.");
        }
    }
}
