package util;

public class ServerState {
    public final User user;
    public final User[] onlineUsers;
    public final User[] leaderboard;
    public final String message;

    public ServerState(User user, User[] onlineUsers, User[] leaderboard, String message){
        this.user = user;
        this.onlineUsers = onlineUsers;
        this.leaderboard = leaderboard;
        this.message = message;
    }
}
