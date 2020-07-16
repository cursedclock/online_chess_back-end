package chess;

import java.util.Date;

public class GameResult {
    public final Date time;
    public final boolean won;
    public final String opponent;
    public final int turns;
    public final int piecesCaptured;
    public final int piecesLost;

    public GameResult(boolean won, Date time, String opponent, int turns, int piecesCaptured, int piecesLost){
        this.won = won;
        this.time = time;
        this.opponent = opponent;
        this.turns = turns;
        this.piecesCaptured = piecesCaptured;
        this.piecesLost = piecesLost;
    }
}
