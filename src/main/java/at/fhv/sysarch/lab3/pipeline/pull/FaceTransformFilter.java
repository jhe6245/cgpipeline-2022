package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;

public class FaceTransformFilter extends TransformFilter<Face, Face> {
    public FaceTransformFilter(Pipe<Face> input, Mat4 transform) {
        super(input, transform);
    }

    @Override
    public Face next() {
        return transformFace(input.next());
    }
}
