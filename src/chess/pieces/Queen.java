package chess.pieces;

import chess.Position;

import java.util.Map;

public class Queen extends Piece{
    public Queen(Colour colour){
        super(colour);
    }

    @Override
    public void verifyMove(Map<Position, Piece> board, Position p1, Position p2) {

    }
}
