package server;

import server.database.*;
import util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server implements Runnable {
    private int port;
    private String databasePath;
    private AtomicBoolean isRunning;
    private ServerSocket serverSocket;
    private ObjectWrapper<ConcurrentHashMap<String, User>> registeredUsers;
    private ConcurrentSkipListSet<User> onlineUsers;

    public Server(int port, String databasePath){
        this.port = port;
        this.databasePath = databasePath;
        isRunning = new AtomicBoolean(false);
        onlineUsers = new ConcurrentSkipListSet<>();
    }

    @Override
    public void run() {
        init();
        loadDatabase();

        while (isRunning.get()) {
            try {
                Socket newSocket = serverSocket.accept();
                new Thread(new Session(newSocket, registeredUsers, onlineUsers, isRunning)).start();
            } catch (Exception ignore){}
        }
    }

    private  void init(){
        try {
            serverSocket = new ServerSocket(port);
            isRunning.set(true);
        } catch (Exception e){
            throw new RuntimeException("Initialisation error");
        }
    }

    private void loadDatabase(){
        registeredUsers = DatabaseLoader.loadDatabase(databasePath, isRunning);
        if (registeredUsers.object==null){
            registeredUsers.object = new ConcurrentHashMap<>();
        }
    }
}
