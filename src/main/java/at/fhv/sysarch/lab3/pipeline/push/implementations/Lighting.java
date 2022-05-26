package at.fhv.sysarch.lab3.pipeline.push.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.push.Filter;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

public class Lighting extends Filter<Pair<Face, Color>, Pair<Face, Color>> {

    private final Vec3 lightSourcePosition;

    public Lighting(Vec3 lightSourcePosition) {
        this.lightSourcePosition = lightSourcePosition;
    }

    @Override
    public void push(Pair<Face, Color> item) {
        var f = item.fst();
        var c = item.snd();
        var alpha = f.getN1().toVec3().getUnitVector().dot(lightSourcePosition.getUnitVector());
        alpha = Math.max(0, alpha);
        output.push(new Pair<>(f, Color.BLACK.interpolate(c, alpha)));
    }
}
