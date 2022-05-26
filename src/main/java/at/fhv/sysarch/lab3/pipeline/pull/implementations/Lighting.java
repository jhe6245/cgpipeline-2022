package at.fhv.sysarch.lab3.pipeline.pull.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pull.Filter;
import at.fhv.sysarch.lab3.pipeline.pull.Pipe;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

public class Lighting extends Filter<Pair<Face, Color>, Pair<Face, Color>> {
    private final Vec3 lightPos;

    public Lighting(Pipe<Pair<Face, Color>> input, Vec3 lightPos) {
        super(input);
        this.lightPos = lightPos;
    }

    @Override
    public Pair<Face, Color> next() {
        var i = input.next();
        var f = i.fst();
        var c = i.snd();
        var alpha = f.getN1().toVec3().getUnitVector().dot(lightPos.getUnitVector());
        alpha = Math.max(0, alpha);
        return new Pair<>(f, Color.BLACK.interpolate(c, alpha));
    }
}
