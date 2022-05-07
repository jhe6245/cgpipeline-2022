package at.fhv.sysarch.lab3.pipeline.pull;

public abstract class Filter<Tin, Tout> implements Source<Tout> {

    protected final Pipe<Tin> input;

    public Filter(Pipe<Tin> input) {
        this.input = input;
    }
}
