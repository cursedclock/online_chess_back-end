package chess;

public class King extends Piece{
    public King(int row, int col, Colour colour){
        super(row, col, colour);
    }

    @Override
    public void move(int r, int c) {
        int rowDiff = Math.abs(row-r);
        int colDIff = Math.abs(col-c);
        if (colDIff>1 || rowDiff>1){
            throw new IllegalMoveException("The king can only move one square in any direction.");
        }
        super.move(r, c);
    }
}
