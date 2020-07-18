package chess;

import chess.pieces.Piece;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GameResult implements Serializable {
    public final Date time;
    public final boolean won;
    public final String opponent;
    public final int turns;
    public final List<Piece> piecesCaptured;
    public final List<Piece> piecesLost;

    public GameResult(boolean won, Date time, String opponent, int turns, List<Piece> piecesCaptured, List<Piece> piecesLost){
        this.won = won;
        this.time = time;
        this.opponent = opponent;
        this.turns = turns;
        this.piecesCaptured = piecesCaptured;
        this.piecesLost = piecesLost;
    }

    public int piecesCapturedCount(){
        return piecesCaptured.size();
    }

    public int piecesLostCount(){
        return piecesLost.size();
    }
}
