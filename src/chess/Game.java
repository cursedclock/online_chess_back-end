package chess;

import chess.pieces.*;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Game {
    private ConcurrentMap<Position, Piece> board;
    private List<Piece> whiteCaptured;
    private List<Piece> blackCaptured;
    private int turns;
    private String whitePlayer;
    private String blackPlayer;
    private String winner;
    private Colour turn;
    Date time;


    public Game(String p1, String p2){
        board = new ConcurrentHashMap<>();
        whitePlayer = p1;
        blackPlayer = p2;
        whiteCaptured = new Vector<>();
        blackCaptured = new Vector<>();
        turns = 0;
        turn = Colour.WHITE;
        time = new Date();
    }

    {
        //TODO initial chess piece placement
        for (int i=0; i<8; i++){
            board.put(new Position(i, 1), new Pawn(Colour.WHITE));
            board.put(new Position(i, 6), new Pawn(Colour.BLACK));
        }
    }

    public void move(String player, Position p1, Position p2){
        validateGameState();
        validateTurn(player);
        validatePositions(p1, p2);
        board.get(p1).verifyMove(board, p1, p2);

        if (turn==Colour.WHITE){
            turns++;
        }

        if (board.containsKey(p2)) {
            if (turn == Colour.BLACK) {
                whiteCaptured.add(board.get(p2));
            } else if (turn == Colour.WHITE) {
                blackCaptured.add(board.get(p2));
            }
        }
        board.put(p2, board.get(p1));
        board.remove(p1);
    }

    public boolean isTurn(String player){
        if (winner != null){
            return false;
        }
        if (player.equals(whitePlayer) && turn == Colour.WHITE
            || player.equals(blackPlayer) && turn == Colour.BLACK){
            return true;
        }
        return false;
    }

    private void validateTurn(String player){
        if (! isTurn(player)){
            throw new IllegalMoveException("You must wait for your turn.");
        }
    }

    private void validatePositions(Position p1, Position p2){
        boolean errorFlag = false;
        String errorMessage;
        if (!board.containsKey(p1) || !board.get(p1).isColour(turn)){
            throw new IllegalMoveException("You must select one of your own pieces to move.");
        } else if (board.containsKey(p2) && board.get(p2).isColour(turn)){
            throw new IllegalMoveException("You secondary position is taken by one of your own pieced.");
        }
    }

    public void leave(String player){
        if (player.equals(whitePlayer)){
            winner = blackPlayer;
        } else{
            winner = whitePlayer;
        }
    }

    public GameResult getResult(String player){
        if (winner == null){
            throw new IllegalCallerException("Game has not ended yet");
        }
        if (player.equals(whitePlayer)){
            return getWhiteResult();
        } else{
            return getBlackResult();
        }
    }

    public GameResult getWhiteResult(){
        return new GameResult(winner.equals(whitePlayer), time, blackPlayer, turns, blackCaptured.size(), whiteCaptured.size());
    }

    public GameResult getBlackResult(){
        return new GameResult(winner.equals(blackPlayer), time, whitePlayer, turns, whiteCaptured.size(), blackCaptured.size());
    }

    private void validateGameState(){
        if (winner != null){
            throw new IllegalCallerException("The game has ended.");
        }
    }
}
