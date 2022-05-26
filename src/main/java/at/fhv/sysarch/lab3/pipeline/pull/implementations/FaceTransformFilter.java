package at.fhv.sysarch.lab3.pipeline.pull.implementations;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Util;
import at.fhv.sysarch.lab3.pipeline.pull.Pipe;
import at.fhv.sysarch.lab3.pipeline.pull.TransformFilter;
import com.hackoeur.jglm.Mat4;

public class FaceTransformFilter extends TransformFilter<Face, Face> {
    public FaceTransformFilter(Pipe<Face> input, Mat4 transform) {
        super(input, transform);
    }

    @Override
    public Face next() {
        return Util.apply(input.next(), getTransform());
    }
}
