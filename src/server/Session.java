package server;

import server.database.ObjectWrapper;
import util.User;

import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Session implements Runnable{

    public Session(Socket socket, ObjectWrapper<? extends ConcurrentMap<String, User>> registeredUsers, Vector<User> onlineUsers, AtomicBoolean serverState){
        // TODO session initialisation
    }

    @Override
    public void run() {
        // TODO session stuff
    }
}
