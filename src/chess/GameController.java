package chess;

import java.io.Serializable;

public class GameController implements Serializable {
    private final Game game;
    private final String player;

    public GameController(Game game, String player){
        this.game = game;
        this.player = player;
    }

    public void move(Position p1, Position p2){
        game.move(player, p1, p2);
    }

    public void leave(){
        game.leave(player);
    }

    public boolean isTurn(){
        return game.isTurn(player);
    }

    public GameResult getResults(){
        return  game.getResult(player);
    }
}
