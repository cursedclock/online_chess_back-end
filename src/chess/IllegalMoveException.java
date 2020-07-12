package chess;

public class IllegalMoveException extends IllegalArgumentException {
    public IllegalMoveException(String msg){
        super(msg);
    }
}
