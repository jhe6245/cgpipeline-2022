package at.fhv.sysarch.lab3.pipeline.push;

public interface Sink<T> {
    void push(T item);
}
