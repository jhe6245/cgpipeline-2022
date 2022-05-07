package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;

import java.util.stream.Collectors;

public abstract class PerFaceFilter extends Filter<Model, Model> {

    public PerFaceFilter(Pipe<Model> input) {
        super(input);
    }

    @Override
    public Model pull() {
        return new Model(input.pull().getFaces().stream().map(this::processFace).collect(Collectors.toList()));
    }

    abstract protected Face processFace(Face face);
}
