package chess.pieces;

import chess.IllegalMoveException;
import chess.Position;
import java.util.Map;

public class Bishop extends Piece{
    public Bishop(Colour colour){
        super(colour);
    }

    @Override
    public void verifyMove(Map<Position, Piece> board, Position p1, Position p2) {
        int rowDiff = Math.abs(p1.getRow()-p2.getRow());
        int colDiff = Math.abs(p1.getCol()-p2.getCol());
        if (colDiff != rowDiff){
            throw new IllegalMoveException("Bishops can only move in straight diagonal lines.");
        }
    }
}
