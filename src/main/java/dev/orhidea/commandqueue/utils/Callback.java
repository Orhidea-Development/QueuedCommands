package dev.orhidea.commandqueue.utils;

public interface Callback<T> {

    void call(T returnObject);

}
