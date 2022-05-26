package at.fhv.sysarch.lab3.pipeline.pull;

public abstract class Filter<I, O> implements Source<O> {

    protected final Pipe<I> input;

    public Filter(Pipe<I> input) {
        this.input = input;
    }

    @Override
    public boolean hasNext() {
        return input.hasNext();
    }
}
