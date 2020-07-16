package server.database;

import java.io.Serializable;

public class ObjectWrapper<T> {
    public T object;

    public ObjectWrapper(T object){
        this.object = object;
    }

    public ObjectWrapper(){
        object = null;
    }
}
