package server.database;

import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DatabaseMgr<T extends Serializable> implements Runnable {
    private final ObjectWrapper<?> database;
    private final String path;
    private final AtomicBoolean isRunning;

    public DatabaseMgr(ObjectWrapper<?> database, String path, AtomicBoolean serverState){
        this.database = database;
        this.path = path;
        isRunning = serverState;
    }

    @Override
    public void run() {
        while (isRunning.get()) {
            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(path, false))) {
                os.writeObject(database.object);
                Thread.sleep(100);
            } catch (Exception e) {
                kill();
            }
        }
    }

    private void kill(){
        isRunning.set(false);
        database.object = null;
    }
}
