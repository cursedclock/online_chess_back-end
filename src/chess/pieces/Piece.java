package chess.pieces;

import chess.Position;

public abstract class Piece {
    protected final Colour colour;

    public Piece(Colour colour){
        this.colour = colour;
    }

    public abstract void verifyMove(Position p1, Position p2);
}

enum Colour{
    BLACK, WHITE
}