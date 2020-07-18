package util;

import chess.Game;
import chess.GameController;
import chess.GameResult;
import chess.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class User implements Serializable, Comparable<User> {
    private final String username;
    private long passwordHash;
    private String securityQuestion;
    private long securityAnswerHash;
    private long score;
    private GameController currentGame;
    private User outgoingRequest;
    private List<GameResult> history;
    private Set<User> incomingRequests;

    public User(String username, String password, String securityQuestion, String securityAnswer){
        validatePassword(password);
        this.username = username;
        passwordHash = password.hashCode();
        this.securityQuestion = securityQuestion;
        securityAnswerHash = securityAnswer.hashCode();
        score = 0;
        history = new ArrayList<>();
        incomingRequests = new ConcurrentSkipListSet<>();
    }

    private static void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password too short.");
        } else if (! password.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=.,?`~<>|/\\\\])")){
            throw new IllegalArgumentException("Password must contain at least one lowercase and uppercase character, digit, and non word character.");
        }
    }

    // requests
    public void acceptRequest(User user){
        validateWaitingState();
        Game newGame = new Game(username, user.username);
        currentGame = new GameController(newGame, username);
        user.enterGame(newGame);
        incomingRequests.remove(user);
    }

    public void sendRequest(User user){
        validateWaitingState();
        user.addRequest(this);
        outgoingRequest = user;
    }

    public void declineRequest(User user){
        user.cancelRequest();
    }

    public void cancelRequest(){
        outgoingRequest.removeRequest(this);
        outgoingRequest = null;
    }

    private void enterGame(Game game){
        currentGame = new GameController(game, username);
        outgoingRequest = null;
    }

    private void addRequest(User user){
        incomingRequests.add(user);
    }

    private void removeRequest(User user){
        incomingRequests.remove(user);
    }

    // in game
    public void move(int c1, int r1, int c2, int r2){
        validateInGame();
        if (checkEndGame()){
            return;
        }
        currentGame.move(new Position(c1, r1), new Position(c2, r2));
    }

    public void move(String c1, int r1, String c2, int r2){
        validateInGame();
        if (checkEndGame()){
            return;
        }
        currentGame.move(new Position(c1, r1), new Position(c2, r2));
    }

    public void leaveGame(){
        validateInGame();
        if (checkEndGame()){
            return;
        }
        currentGame.leave();
    }

    public void sendMessage(String message){
        validateInGame();
        // TODO send message
    }

    private boolean checkEndGame(){
        try {
            GameResult reslt = currentGame.getResults();
            history.add(reslt);
            currentGame = null;
            return true;
        }catch (Exception e){
            return false;
        }

    }


    // exception checker
    private void validateWaitingState() {
        if (outgoingRequest != null){
            throw new IllegalArgumentException("You are still waiting on your last request.");
        } else if (currentGame != null){
            throw new IllegalArgumentException("You are currently in a game.");
        }
    }

    private void validateInGame(){
        if (currentGame==null) {
            throw new IllegalArgumentException("You are not in a game.");
        }
    }

    // getters
    public String getUsername() {
        return username;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public long getScore() {
        return score;
    }

    // etc
    public boolean resetPass(String securityAnswer, String newPass){
        if (securityAnswerHash==securityAnswer.hashCode()){
            validatePassword(newPass);
            passwordHash = newPass.hashCode();
            return true;
        }
        return false;
    }

    public boolean checkPass(String password){
        return  password.hashCode()==passwordHash;
    }

    @Override
    public int compareTo(User user) {
        return Long.compare(user.score, score);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==this){
            return true;
        } else if(! (obj instanceof User)) {
            return false;
        }
        return username.equals(((User) obj).username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return username;
    }
}
