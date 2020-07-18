package server;

import chess.Position;
import server.database.ObjectWrapper;
import util.*;
import util.requests.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Session implements Runnable, SessionInterface {
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private Vector<User> onlineUsers;
    private ObjectWrapper<? extends ConcurrentMap<String, User>> registeredUsers;
    private AtomicBoolean serverState;
    private AtomicBoolean isRunning;
    private String message;
    private User user;


    public Session(Socket socket, ObjectWrapper<? extends ConcurrentMap<String, User>> registeredUsers, Vector<User> onlineUsers, AtomicBoolean serverState){
        try {
            os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e){
            isRunning.set(false);
        }
        this.onlineUsers = onlineUsers;
        this.registeredUsers = registeredUsers;
        isRunning = new AtomicBoolean(true);
        message = "";
        this.serverState = serverState;
    }

    @Override
    public void run() {
        if (isRunning.get()){
            initConnection();
        }
        while (isRunning.get()){
            checkServerState();
            if (isRunning.get()){
                break;
            }
            try {
                os.writeObject(getState());
                while (is.available()>0){
                    ((Request)is.readObject()).execute(this);
                    os.writeObject(getState());
                }
                Thread.sleep(100);
            } catch (Exception e){
                kill();
            }
        }
    }

    private void initConnection(){
        try {
            Request request = (Request) is.readObject();
            request.execute(this);
            if (request instanceof ForgotPassRequest){
                request = (Request) is.readObject();
                request.execute(this);
            }
        } catch (Exception e){
            kill();
        }
    }

    private void checkServerState(){
        if (!serverState.get()){
            kill();
        }
    }

    private ServerState getState(){
        User[] onlineUsrs = (User[])onlineUsers.toArray();
        User[] leaderboard = (User[]) registeredUsers.object.values().toArray();
        Arrays.sort(leaderboard);
        return new ServerState(user, onlineUsrs, leaderboard, message);
    }

    private void kill(){
        isRunning.set(false);
        try {
            is.close();
            os.close();
        } catch (Exception ignore){}
    }

    // TODO SessionInterface methods
    @Override
    public void login(String username, String password) {
        String message;
        if (!registeredUsers.object.containsKey(username)){
            message = "!User not found.";
        } else {
            User user = registeredUsers.object.get(username);
            if (user.checkPass(password)){
                this.user = user;
                message = "Login successful";
            } else{
                message = "!Wrong password";
            }
        }
        try {
            os.writeUTF(message);
            if (message.startsWith("!")){
                kill();
            }
        } catch (Exception e){
            kill();
        }
    }

    @Override
    public void signUp(String username, String password, String securityQuestion, String securityAnswer) {
        String message;
        if (registeredUsers.object.containsKey(username)){
            message = "!User already exists.";
        } else {
            try {
                User newUser = new User(username, password, securityQuestion, securityAnswer);
                registeredUsers.object.put(username, newUser);
                enterAs(newUser);
                message = "Signed up successfully.";
            } catch (Exception e){
                message = "!"+e.getMessage();
            }
        }
        try {
            os.writeUTF(message);
            if (message.startsWith("!")){
                kill();
            }
        } catch (Exception e){
            kill();
        }
    }

    @Override
    public void forgotPass(String username) {
        String message;
        if (!registeredUsers.object.containsKey(username)){
            message = "!User not found.";
            kill();
        } else {
            message = registeredUsers.object.get(username).getSecurityQuestion();
        }
        try {
            os.writeUTF(message);
            if (message.startsWith("!")){
                kill();
            }
        } catch (Exception e){
            kill();
        }
    }

    @Override
    public void resetPass(String username, String securityAnswer, String newPass) {
        String message;
        if (!registeredUsers.object.containsKey(username)){
            message = "!User not found.";
            kill();
        } else {
            User user = registeredUsers.object.get(username);
            try {

                if (user.resetPass(securityAnswer, newPass)){
                    message = "Password reset Successful.";
                    enterAs(user);
                } else{
                    message = "!Wrong answer.";
                }
            } catch (Exception e){
                message = "!"+e.getMessage();
            }
        }
        try {
            os.writeUTF(message);
            if (message.startsWith("!")){
                kill();
            }
        } catch (Exception e){
            kill();
        }
    }

    private void enterAs(User user){
        this.user = user;
    }

    private User getUser(String username){
        if (! registeredUsers.object.containsKey(username)){
            throw new IllegalArgumentException("User not found");
        }
        return registeredUsers.object.get(username);
    }

    @Override
    public void sendRequest(String username) {
        try {
            user.sendRequest(getUser(username));
            message = "Request sent successfully";
        } catch (Exception e){
            message = "!"+e.getMessage();
        }
    }

    @Override
    public void declineRequest(String username) {
        try {
            user.declineRequest(getUser(username));
            message = "Request declined successfully.";
        } catch (Exception e){
            message = "!"+e.getMessage();
        }
    }

    @Override
    public void cancelRequest() {
        user.cancelRequest();
    }

    @Override
    public void move(int c1, int r1, int c2, int r2) {
        try {
            user.move(c1, r1, c2, r2);
            message = String.format("Moved piece from %s to %s", new Position(c1, r1), new Position(c2, r2));
        } catch (Exception e){
            message = "!"+e.getMessage();
        }
    }

    @Override
    public void leaveGame() {
        try {
            user.leaveGame();
            message = "Left game successfully";
        } catch (Exception e){
            message = "!"+e.getMessage();
        }
    }

    @Override
    public void sendMessage(String message){

    }
}
