package at.fhv.sysarch.lab3.pipeline.push;

public abstract class Filter<I, O> implements Sink<I> {

    protected Pipe<O> output;

    public void setOutput(Pipe<O> output) {
        this.output = output;
    }

    // most filters don't do anything themselves here
    @Override
    public void flush() {
        output.flush();
    }
}
