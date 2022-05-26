package at.fhv.sysarch.lab3.pipeline.push.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Util;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.push.Pipe;
import at.fhv.sysarch.lab3.pipeline.push.TransformFilter;
import com.hackoeur.jglm.Mat4;
import javafx.scene.paint.Color;

public class FaceColorPairTransformFilter extends TransformFilter<Pair<Face, Color>, Pair<Face, Color>> {
    public FaceColorPairTransformFilter(Mat4 transform) {
        super(transform);
    }

    @Override
    public void push(Pair<Face, Color> item) {
        output.push(new Pair<>(Util.apply(item.fst(), getTransform()), item.snd()));
    }
}
