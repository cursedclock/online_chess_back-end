package chess;

import chess.pieces.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Game implements Serializable {
    private final ConcurrentMap<Position, Piece> board;
    private final List<Piece> whiteCaptured;
    private final List<Piece> blackCaptured;
    private final String whitePlayer;
    private final String blackPlayer;
    private final Date time;
    private int turns;
    private String winner;
    private Colour turn;


    public Game(String p1, String p2){
        whitePlayer = p1;
        blackPlayer = p2;
        whiteCaptured = new Vector<>();
        blackCaptured = new Vector<>();
        turns = 0;
        turn = Colour.WHITE;
        time = new Date();
    }

    {
        board = new ConcurrentHashMap<>();
        for (int i=0; i<8; i++){
            board.put(new Position(i, 1), new Pawn(Colour.WHITE));
            board.put(new Position(i, 6), new Pawn(Colour.BLACK));
        }
        for (int i=0; i<8; i+=7){
            Colour c = (i==0? Colour.WHITE:Colour.BLACK);
            board.put(new Position(0,i), new Rook(c));
            board.put(new Position(1,i), new Bishop(c));
            board.put(new Position(2,i), new Knight(c));
            board.put(new Position(3,i), (i==0? new King(c):new Queen(c)));
            board.put(new Position(4,i), (i==7? new King(c):new Queen(c)));
            board.put(new Position(5,i), new Knight(c));
            board.put(new Position(6,i), new Bishop(c));
            board.put(new Position(7,i), new Rook(c));
        }
    }

    public synchronized void move(String player, Position p1, Position p2){
        validateGameState();
        validateTurn(player);
        validatePositions(p1, p2);
        board.get(p1).verifyMove(board, p1, p2);

        if (turn==Colour.WHITE){
            turns++;
        }
        iterateTurn();

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
        return player.equals(whitePlayer) && turn == Colour.WHITE || player.equals(blackPlayer) && turn == Colour.BLACK;
    }

    private void iterateTurn(){
        if (turn==Colour.WHITE){
            turn = Colour.BLACK;
        } else{
            turn = Colour.WHITE;
        }
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

    public synchronized void leave(String player){
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
