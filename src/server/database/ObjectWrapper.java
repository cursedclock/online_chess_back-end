package server.database;

import java.io.Serializable;

public class ObjectWrapper<T> {
    T object;

    public ObjectWrapper(T object){
        this.object = object;
    }

    public ObjectWrapper(){
        object = null;
    }

    public T get(){
        return object;
    }
}
