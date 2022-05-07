package at.fhv.sysarch.lab3.pipeline.pull;

public class Pipe<T> implements Source<T> {
    private final Source<T> input;

    public Pipe(Source<T> input) {

        this.input = input;
    }

    @Override
    public T pull() {
        return input.pull();
    }
}
