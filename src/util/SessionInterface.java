package util;

public interface SessionInterface {
    // pre-login
    void login(String username, String password);
    void signUp(String username, String password, String securityQuestion, String securityAnswer);
    void forgotPass(String username);
    void resetPass(String username, String securityAnswer, String newPass);
    // pre-game
    void sendRequest(String username);
    void declineRequest(String username);
    void cancelRequest();
    void searchFor(String searchTerm);
    // in-game
    void move(int c1, int r1, int c2, int r2);
    void leaveGame();
    void sendMessage(String message);
}
