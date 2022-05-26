package at.fhv.sysarch.lab3.pipeline.push.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Util;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.push.Filter;
import javafx.scene.paint.Color;

public class PerspectiveDivision extends Filter<Pair<Face, Color>, Pair<Face, Color>> {

    @Override
    public void push(Pair<Face, Color> item) {
        output.push(new Pair<>(Util.perspectiveDivision(item.fst()), item.snd()));
    }
}
