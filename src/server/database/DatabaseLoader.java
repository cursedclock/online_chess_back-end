package server.database;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class DatabaseLoader {
    public static <T extends Serializable> ObjectWrapper<T> loadDatabase(String path, AtomicBoolean serverState){
        ObjectWrapper<T> outp = new ObjectWrapper<>();

        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(path))){
            outp.object = (T) is.readObject();
        } catch (Exception ignore){}

        Thread writerThread = new Thread(new DatabaseMgr<T>(outp, path, serverState));
        writerThread.setDaemon(true);
        writerThread.start();
        return outp;
    }
}
