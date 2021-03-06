package chess.pieces;

import chess.Position;

import java.io.Serializable;
import java.util.Map;

public abstract class Piece implements Serializable {
    protected final Colour colour;

    public Piece(Colour colour){
        this.colour = colour;
    }

    public abstract void verifyMove(Map<Position, Piece> board, Position p1, Position p2);

    public boolean isColour(Colour colour){
        return  this.colour == colour;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

