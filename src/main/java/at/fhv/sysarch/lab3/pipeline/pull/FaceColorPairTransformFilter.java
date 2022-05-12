package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import javafx.scene.paint.Color;

public class FaceColorPairTransformFilter extends TransformFilter<Pair<Face, Color>, Pair<Face, Color>> {
    public FaceColorPairTransformFilter(Pipe<Pair<Face, Color>> input, Mat4 transform) {
        super(input, transform);
    }

    @Override
    public Pair<Face, Color> next() {
        var n = input.next();
        return new Pair<>(transformFace(n.fst()), n.snd());
    }
}
