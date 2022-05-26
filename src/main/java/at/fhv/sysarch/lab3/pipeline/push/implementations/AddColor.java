package at.fhv.sysarch.lab3.pipeline.push.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.push.Filter;
import javafx.scene.paint.Color;

public class AddColor extends Filter<Face, Pair<Face, Color>> {

    private final Color c;

    public AddColor(Color c) {
        this.c = c;
    }

    @Override
    public void push(Face item) {
        output.push(new Pair<>(item, c));
    }

}
