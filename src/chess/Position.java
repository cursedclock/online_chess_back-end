package chess;

public class Position {
    public static final Position HIT = new Position();

    private Integer col;
    private Integer row;

    private Position(){
        set(-1,-1);
    }

    public Position(int c, int r){
        verify(c, r);
        set(c, r);
    }

    public Position(Col c, int r){
        this(c.ordinal(), r);
    }

    public Position(String c, int r){
        this(Col.valueOf(c), r);
    }

    public void move(int c, int r){
        verify(c, r);
        set(c, r);
    }

    public void move(Col c, int r){
        move(c.ordinal(), r);
    }

    public void move(String c, int r){
        move(Col.valueOf(c), r);
    }

    public int getCol() {
        return col;
    }

    public Col getColumn(){
        return Col.values()[col];
    }

    public int getRow() {
        return row;
    }

    private void set(int c, int r){
        col = c;
        row = r;
    }

    private void verify(int c, int r){
        if (c<0 || c>7 || r<0 || r>7){
            throw new IllegalArgumentException("Invalid position.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)){
            return false;
        } else if (this==obj){
            return true;
        }
        return row.equals(((Position) obj).row) && col.equals(((Position) obj).col);
    }

    @Override
    public int hashCode() {
        return (row.hashCode()+31)*(col.hashCode()+13);
    }

    @Override
    public String toString() {
        return col+"-"+row;
    }
}

enum Col{
    A,B,C,D,E,F,G,H
}
