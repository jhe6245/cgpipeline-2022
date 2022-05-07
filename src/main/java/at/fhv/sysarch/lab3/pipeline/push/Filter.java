package at.fhv.sysarch.lab3.pipeline.push;

public abstract class Filter<Tin, Tout> implements Sink<Tin> {

    protected final Pipe<Tout> output;

    protected Filter(Pipe<Tout> output) {

        this.output = output;
    }
}
