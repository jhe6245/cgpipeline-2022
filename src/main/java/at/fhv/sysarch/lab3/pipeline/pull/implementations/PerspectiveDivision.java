package at.fhv.sysarch.lab3.pipeline.pull.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Util;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pull.Filter;
import at.fhv.sysarch.lab3.pipeline.pull.Pipe;
import javafx.scene.paint.Color;

public class PerspectiveDivision extends Filter<Pair<Face, Color>, Pair<Face, Color>> {
    public PerspectiveDivision(Pipe<Pair<Face, Color>> input) {
        super(input);
    }

    @Override
    public Pair<Face, Color> next() {
        var n = input.next();
        return new Pair<>(Util.perspectiveDivision(n.fst()), n.snd());
    }
}
