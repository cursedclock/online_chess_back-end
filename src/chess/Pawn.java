package chess;

public class Pawn extends Piece{
    public Pawn(int row, int col, Colour colour){
        super(row, col, colour);
    }

    @Override
    public void move(int r, int c) {
        if (r!=super.row+1 || !(c==col+1 || c==col-1)){
            throw new IllegalMoveException("Pawn can only move one block forwards and one block left or right.");
        }
        super.move(r, c);
    }
}
