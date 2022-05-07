package at.fhv.sysarch.lab3.pipeline.push;

public class Pipe<T> implements Sink<T> {

    private final Sink<T> output;

    public Pipe(Sink<T> output) {

        this.output = output;
    }

    @Override
    public void push(T item) {
        output.push(item);
    }
}
