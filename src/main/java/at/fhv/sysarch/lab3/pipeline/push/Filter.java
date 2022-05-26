package at.fhv.sysarch.lab3.pipeline.push;

public abstract class Filter<I, O> implements Sink<I> {

    protected Pipe<O> output;

    public void setOutput(Pipe<O> output) {
        this.output = output;
    }

    @Override
    public void flush() {
        output.flush();
    }
}
