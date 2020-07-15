package server;

import server.database.ObjectWrapper;
import util.User;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class Session implements Runnable{

    public Session(Socket socket, ObjectWrapper<ConcurrentHashMap<String, User>> registeredUsers, ConcurrentSkipListSet<User> onlineUsers, AtomicBoolean serverState){
        // TODO session initialisation
    }

    @Override
    public void run() {
        // TODO session stuff
    }
}
