package at.fhv.sysarch.lab3.pipeline.push;

public abstract class Filter<Tin, Tout> implements Sink<Tin> {

    protected Pipe<Tout> output;

    public void setOutput(Pipe<Tout> output) {
        this.output = output;
    }

    // most filters don't do anything themselves here
    @Override
    public void flush() {
        output.flush();
    }
}
