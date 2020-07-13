package chess.pieces;

import chess.IllegalMoveException;
import chess.Position;

public class Pawn extends Piece{
    public Pawn(Colour colour){
        super(colour);
    }

    @Override
    public void verifyMove(Position p1, Position p2) {
        if (p1.getRow()+1!=p2.getRow() || !(p1.getCol()+1==p2.getCol() || p1.getCol()-1==p2.getCol())){
            throw new IllegalMoveException("Pawn can only move one block forwards and one block left or right.");
        }
    }
}
