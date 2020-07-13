package chess.pieces;

import chess.IllegalMoveException;
import chess.Position;

public class Rook extends Piece{
    public Rook(Colour colour){
        super(colour);
    }

    @Override
    public void verifyMove(Position p1, Position p2) {
            if (p1.getCol()!=p2.getCol() && p1.getRow()!=p2.getRow()){
                throw new IllegalMoveException("Rooks can only move in horizontal or vertical lines.");
            }
    }
}
