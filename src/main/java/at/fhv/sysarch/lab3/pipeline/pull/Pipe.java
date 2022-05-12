package at.fhv.sysarch.lab3.pipeline.pull;

import java.util.function.Function;

public class Pipe<T> implements Source<T> {
    private final Source<T> input;

    public Pipe(Source<T> input) {

        this.input = input;
    }

    @Override
    public boolean hasNext() {
        return input.hasNext();
    }

    @Override
    public T next() {
        return input.next();
    }

    public <TNext> Pipe<TNext> addStage(Function<Pipe<T>, Filter<T, TNext>> filter) {
        return new Pipe<>(filter.apply(this));
    }
}
