package at.fhv.sysarch.lab3.pipeline.pull.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pull.Filter;
import at.fhv.sysarch.lab3.pipeline.pull.Pipe;
import javafx.scene.paint.Color;

public class AddColor extends Filter<Face, Pair<Face, Color>> {
    private final Color color;

    public AddColor(Pipe<Face> input, Color color) {
        super(input);
        this.color = color;
    }

    @Override
    public Pair<Face, Color> next() {
        return new Pair<>(input.next(), color);
    }
}
