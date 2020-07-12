package chess;

public abstract class Piece {
    protected final Colour colour;
    protected int row;
    protected int col;

    public Piece(int row, int col, Colour colour){
        this.row = row;
        this.col = col;
        this.colour = colour;
    }

    public void move(int r, int c){
        row = r;
        col = c;
    }
}

enum Colour{
    BLACK, WHITE
}