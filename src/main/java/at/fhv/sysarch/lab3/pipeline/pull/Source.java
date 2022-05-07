package at.fhv.sysarch.lab3.pipeline.pull;

public interface Source<T> {
    T pull();
}
