package chess;

public class Knight extends Piece{
    public Knight(int row, int col, Colour colour){
        super(row, col, colour);
    }

    @Override
    public void move(int r, int c) {
        int rowDiff = Math.abs(row-r);
        int colDIff = Math.abs(col-c);
        if (!((rowDiff==2 && colDIff == 3)||(rowDiff==3 && colDIff==2))){
            throw new IllegalMoveException("A knight can only move betwenn opposing edges of a 3x2 rectangle.");
        }
        super.move(r, c);
    }
}
