package chess;

public class Rook extends Piece{
    public Rook(int row, int col, Colour colour){
        super(row, col, colour);
    }

    @Override
    public void move(int r, int c) {
        if (r!=row && c!= col){
            throw new IllegalMoveException("Rooks can only move in horizontal or vertical lines.");
        }
        super.move(r, c);
    }
}
