package at.fhv.sysarch.lab3.pipeline.push;

public interface Sink<T> {
    void push(T item);

    /**
     * signals end of stream
     */
    void flush();

    default void processAll(Iterable<T> items) {
        items.forEach(this::push);
        flush();
    }
}
