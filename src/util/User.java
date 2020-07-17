package util;

import chess.GameController;
import chess.GameResult;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class User implements Serializable, Comparable<User> {
    private final String username;
    private final long passwordHash;
    private final String securityQuestion;
    private final long securityAnswerHash;
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

    public void acceptRequest(){
        validateWaitingState();
        // TODO start game
    }

    public void denyRequest(User user){
        user.cancelRequest();
    }

    public void sendRequest(User user){
        validateWaitingState();
        user.addRequest(this);
        outgoingRequest = user;
    }

    public void cancelRequest(){
        outgoingRequest.removeRequest(this);
        outgoingRequest = null;
    }

    public void addRequest(User user){
        incomingRequests.add(user);
    }

    public void removeRequest(User user){
        incomingRequests.remove(user);
    }

    private void validateWaitingState() {
        if (outgoingRequest != null){
            throw new IllegalArgumentException("You are still waiting on your last request.");
        } else if (currentGame != null){
            throw new IllegalArgumentException("You are currently in a game.");
        }
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
}
